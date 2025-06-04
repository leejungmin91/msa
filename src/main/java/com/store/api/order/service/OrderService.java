package com.store.api.order.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.api.member.dto.MemberResponseDto;
import com.store.api.member.dto.ShippingResponseDto;
import com.store.api.member.service.MemberService;
import com.store.api.member.service.ShippingService;
import com.store.api.order.config.KisaProperties;
import com.store.api.order.domain.*;
import com.store.api.order.dto.*;
import com.store.api.order.entity.*;
import com.store.api.order.repository.OrderRepository;
import com.store.api.product.service.ProductService;
import com.store.common.config.security.CustomUserDetails;
import com.store.common.config.security.Role;
import com.store.common.exception.ApiException;
import com.store.common.http.ApiCode;
import com.store.common.util.CryptoService;
import com.store.common.util.DataEncrypt;
import com.store.common.util.RedisService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

//import org.springframework.web.reactive.function.client.WebClient;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@lombok.extern.slf4j.Slf4j
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductService productService;
    private final MemberService memberService;
    private final ShippingService shippingService;
    private final CartService cartService;
    private final RedisService redisService;
    private final CryptoService cryptoService;
    private final KisaProperties kisaProperties;

    private final WebClient webClient;
    private static final long ORDER_TTL_SECONDS = 60 * 60 * 24 * 3; // 3일

    private String getPaymentKey(String orderNo) {
        return "order:prepare:" + orderNo;
    }

    public OrderEntity getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<OrderEntity> getByMemberId(Long id) {
        return orderRepository.findByMemberId(id);
    }

    private List<OrderItemGroupedByDateDto> findOrderItemsByMemberGroupedByDate(Long memberId, OrderSearchDomain orderSearchDomain) {
        List<OrderItemEntity> orderItems = orderRepository.findOrderItemByConditions(memberId, orderSearchDomain.period(), orderSearchDomain.status());
        List<OrderItemResponseDto> dtoList = orderItems.stream()
                .map(OrderItemResponseDto::from)
                .toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        Map<String, List<OrderItemResponseDto>> grouped =
                dtoList.stream()
                        .collect(Collectors.groupingBy(dto ->
                                dto.getOrderDate().toLocalDate().format(formatter)
                        ));

        return grouped.entrySet().stream()
                .map(entry -> OrderItemGroupedByDateDto.builder()
                        .date(entry.getKey())
                        .items(entry.getValue())
                        .build())
                .sorted(Comparator.comparing(OrderItemGroupedByDateDto::getDate).reversed()) // 최신순
                .toList();
    }

    public OrderOverviewResponseDto getOrderOverview(Long memberId, OrderSearchDomain orderSearchDomain) {
        List<OrderItemGroupedByDateDto> orders = findOrderItemsByMemberGroupedByDate(memberId, orderSearchDomain);
        Map<OrderStatus, Long> statusCount = orderRepository.countOrderItemsByStatusMap(memberId, orderSearchDomain.period());

        Arrays.stream(OrderStatus.values()).forEach(status -> statusCount.putIfAbsent(status, 0L));

        return OrderOverviewResponseDto.builder()
                .orders(orders)
                .statusCount(statusCount)
                .build();
    }

    /**
     * 결제 페이지 창 데이터 (직접결제용)
     *
     * @param memberId
     * @return
     */
    public CheckoutResponseDto getCheckoutData(Long memberId, Long productId, int quantity) {

        // 로그인 맴버 정보
        MemberResponseDto member = MemberResponseDto.from(memberService.getById(memberId), cryptoService);

        // 배송지 정보
        ShippingResponseDto shipping = ShippingResponseDto.from(shippingService.getMainShipping(memberId));

        List<CheckoutProductResponseDto> products = new ArrayList<>();
        // 구매할 상품 리스트

        CheckoutProductResponseDto product = CheckoutProductResponseDto.from(productService.getById(productId, "Y"), quantity);
        products.add(product);

        return CheckoutResponseDto.builder()
                .member(member)
                .shipping(shipping)
                .products(products)
                .build();
    }

    /**
     * 결제 페이지 창 데이터 (장바구니용)
     *
     * @param memberId
     * @return
     */
    public CheckoutResponseDto getCheckoutData(Long memberId) {

        // 로그인 맴버 정보
        MemberResponseDto member = MemberResponseDto.from(memberService.getById(memberId), cryptoService);

        // 배송지 정보
        ShippingResponseDto shipping = ShippingResponseDto.from(shippingService.getMainShipping(memberId));

        // 구매할 상품 리스트
        // redis 가져오기
        List<CheckoutProductResponseDto> products = CheckoutProductResponseDto.fromCart(cartService.getCart(memberId));

        return CheckoutResponseDto.builder()
                .member(member)
                .shipping(shipping)
                .products(products)
                .build();
    }

    /**
     * 결제 준비
     *
     * @param orderPrepareDomain
     * @return
     */

//    @Transactional(rollbackFor = Exception.class)
    public OrderPrepareDomain prepare(Long memberId, OrderPrepareDomain orderPrepareDomain) {
        OrderPrepareDomain domain = orderPrepareDomain.prepare(memberId
                , kisaProperties.getMid()
                , kisaProperties.getPayReturnUrl()
                , kisaProperties.getMerchantKey()
                , kisaProperties.getCurrencyType()
                , kisaProperties.getModel());

        redisService.save(getPaymentKey(domain.ordNo()), domain, ORDER_TTL_SECONDS);
        return domain;
    }

    @Transactional(rollbackFor = Exception.class)
    public void order(Map<String, String> resultMap) {
        OrderResultDomain orderResultDomain = OrderResultDomain.fromMap(resultMap);
        OrderPrepareDomain orderPrepareDomain = (OrderPrepareDomain) redisService.get(getPaymentKey(orderResultDomain.ordNo()));

        Long memberId = orderPrepareDomain.memberId();

        OrderEntity saveOrder = OrderEntity.create(orderPrepareDomain, orderResultDomain);
        saveOrder.validatePayment(memberId, orderPrepareDomain.goodsAmt());
        orderRepository.save(saveOrder);

        Map<Long, Integer> orderedMap = saveOrder.getOrderItems()
                .stream()
                .collect(Collectors.toMap(
                        OrderItemEntity::getProductId,
                        OrderItemEntity::getQuantity
                ));

        Map<Long, Integer> cartMap = cartService.getCart(memberId)
                .stream()
                .collect(Collectors.toMap(
                        CartItemResponseDto::getProductId,
                        CartItemResponseDto::getQuantity
                ));

        if (orderedMap.equals(cartMap)) {
            cartService.clearCart(memberId);
        }
    }

    public OrderOverviewResponseDto getOrderComplete(Long memberId, String ordNo) {
        List<OrderItemGroupedByDateDto> orders = findOrderItemsByComplete(memberId, ordNo);

        return OrderOverviewResponseDto.builder()
                .orders(orders)
                .build();
    }

    public OrderEntity findByOrderNo(String ordNo) {
        return orderRepository.findByOrderNo(ordNo).orElseThrow(EntityNotFoundException::new);
    }

    private List<OrderItemGroupedByDateDto> findOrderItemsByComplete(Long memberId, String ordNo) {
        List<OrderItemEntity> orderItems = orderRepository.findOrderComplete(memberId, ordNo);
        List<OrderItemResponseDto> dtoList = orderItems.stream()
                .map(OrderItemResponseDto::from)
                .toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        Map<String, List<OrderItemResponseDto>> grouped =
                dtoList.stream()
                        .collect(Collectors.groupingBy(dto ->
                                dto.getOrderDate().toLocalDate().format(formatter)
                        ));

        return grouped.entrySet().stream()
                .map(entry -> OrderItemGroupedByDateDto.builder()
                        .date(entry.getKey())
                        .items(entry.getValue())
                        .build())
                .toList();
    }

    @Transactional
    public OrderCancelResponseDto cancelOrder(OrderCancelRequestDto dto, CustomUserDetails user) throws Exception {
        OrderEntity order = findByOrderNo(dto.getOrdNo());
        Long memberId = user.getId();
        String role = user.getAuthorities()
                .iterator()
                .next()
                .getAuthority();
        if (!role.equals("ROLE_ADMIN") && !order.getMemberId().equals(memberId)) {
            throw new ApiException(ApiCode.PAYMENT_MEMBER_DENIED);
        } else if (!dto.getOrdNo().equals(order.getOrderNo())) {
            throw new ApiException(ApiCode.PAYMENT_CANCEL_NOT_COLLECT_NO);
        } else if (PayStatus.CANCELED.equals(order.getStatus())) {
            throw new ApiException(ApiCode.PAYMENT_CANCEL_ALREADY);
        }

        OrderPayDetailEntity pay = order.getOrderPayDetail();

        String ediDate = LocalDateTime.now().format(EDI_FORMATTER);
        String encData = DataEncrypt.encrypt(kisaProperties.getMid() + ediDate + order.getOrderPayDetail().getAmt() + kisaProperties.getMerchantKey());

        String cancelMsg = role.equals("ROLE_ADMIN") ? "관리자 취소" : "고객취소";

        Map<String, String> payload = new HashMap<>();
        payload.put("tid", pay.getTid());
        payload.put("canAmt", pay.getAmt());
        payload.put("ordNo", order.getOrderNo());
        payload.put("mid", kisaProperties.getMid());
        payload.put("canId", order.getOrderDetail().getOrderName());
        payload.put("canNm", order.getOrderDetail().getOrderName());
        payload.put("partCanFlg", "0");
        payload.put("canMsg", cancelMsg);
        payload.put("payMethod", order.getPayType().name());
        payload.put("ediDate", ediDate);
        payload.put("encData", encData);
        payload.put("returnUrl", kisaProperties.getCancelReturnUrl());

        String result = webClient.post()
                .uri(kisaProperties.getApiUrl() + "/v2/cancelTrans.do")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        ObjectMapper mapper = new ObjectMapper();
        OrderCancelResponseDto orderCancelResponseDto = mapper.readValue(result, OrderCancelResponseDto.class);

        String resultCd = orderCancelResponseDto.getResultCd();
        if ("2001".equals(resultCd)) {
            order.cancel(order, orderCancelResponseDto);
            orderCancelResponseDto.setResultMsg("취소가 완료되었습니다.");
        } else {
            orderCancelResponseDto.setResultMsg("이미 취소되었거나 관리자에게 문의부탁드립니다.");
        }
        return orderCancelResponseDto;
    }

    private static final DateTimeFormatter EDI_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public void clearCartIfMatchesOrder(String ordNo) {
        OrderEntity order = findByOrderNo(ordNo);

        Long memberId = order.getMemberId();
        List<OrderItemEntity> orderItems = order.getOrderItems();

        Map<Long, Integer> orderedMap = orderItems.stream()
                .collect(Collectors.toMap(
                        OrderItemEntity::getProductId,
                        OrderItemEntity::getQuantity
                ));

        List<CartItemResponseDto> cartItems = cartService.getCart(memberId);
        if (cartItems.isEmpty()) return;

        Map<Long, Integer> cartMap = cartItems.stream()
                .collect(Collectors.toMap(
                        CartItemResponseDto::getProductId,
                        CartItemResponseDto::getQuantity
                ));

        if (orderedMap.equals(cartMap)) {
            cartService.clearCart(memberId);
        }
    }

    public Page<OrderEntity> getOrdersByRoleToPaging(int page, int size, Role role) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return orderRepository.findAll(pageable);
    }

    @Transactional
    public OrderResponseDto setInvoice(OrderInvoiceRequestDto dto) {
        String ordNo = dto.getOrdNo();
        OrderEntity order = findByOrderNo(ordNo);

        OrderDetailEntity detailEntity = order.getOrderDetail();
        detailEntity.setInvoice(order,dto);
        return OrderResponseDto.from(order);
    }
}