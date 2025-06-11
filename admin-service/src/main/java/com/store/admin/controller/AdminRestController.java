package com.store.api.admin.controller;

import com.store.api.admin.annotation.AdminAuthorize;
import com.store.api.admin.service.AdminService;
import com.store.api.member.dto.MemberResponseDto;
import com.store.api.member.entity.MemberEntity;
import com.store.api.member.service.MemberService;
import com.store.api.order.dto.OrderInvoiceRequestDto;
import com.store.api.order.dto.OrderResponseDto;
import com.store.api.order.entity.OrderEntity;
import com.store.api.order.service.OrderService;
import com.store.api.product.domain.ProductCreateDomain;
import com.store.api.product.domain.ProductUpdateDomain;
import com.store.api.product.dto.ProductResponseDto;
import com.store.api.product.entity.ProductEntity;
import com.store.api.product.service.ProductService;
import com.store.common.dto.PageResponse;
import com.store.common.http.ApiResponse;
import com.store.common.util.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AdminAuthorize
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin")
public class AdminRestController {

    private final AdminService adminService;
    private final MemberService memberService;
    private final ProductService productService;
    private final CryptoService cryptoService;
    private final OrderService orderService;

    /**
     * 회원 전체 조회
     *
     * @return
     */
    @GetMapping("/members")
    public ResponseEntity<ApiResponse> getMembers(@RequestParam int page,
                                                  @RequestParam int size) {
        Page<MemberEntity> members = adminService.getMembers(page, size);
        Page<MemberResponseDto> dtoPage = members.map(m -> MemberResponseDto.from(m, cryptoService));
        return ResponseEntity.ok()
                .body(ApiResponse.success(new PageResponse<>(dtoPage)));
    }

    /**
     * 회원조회
     *
     * @param id
     * @return
     */
    @GetMapping("/member/{id}")
    public ResponseEntity<ApiResponse> getMember(@PathVariable Long id) {
        MemberEntity member = memberService.getById(id);
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        MemberResponseDto.from(member, cryptoService))
                );
    }

    @GetMapping("/approval")
    public ResponseEntity<ApiResponse> getPendingMembers(@RequestParam int page,
                                                  @RequestParam int size) {
        Page<MemberEntity> members = adminService.getPendingMembers(page, size);
        Page<MemberResponseDto> dtoPage = members.map(m -> MemberResponseDto.from(m, cryptoService));
        return ResponseEntity.ok()
                .body(ApiResponse.success(new PageResponse<>(dtoPage)));
    }

    /**
     * 회원 승인
     * @param id
     * @return
     */
    @PatchMapping("/member/{id}")
    public ResponseEntity<ApiResponse> approvalMember(@PathVariable Long id, @RequestParam String approvalYn) {
        adminService.approvalMember(id, approvalYn);
        return ResponseEntity.ok()
                .body(ApiResponse.success());
    }

    /**
     * 비밀번호 초기화 (0000 으로)
     * @param id
     * @return
     */
    @PatchMapping("/member/init-password/{id}")
    public ResponseEntity<ApiResponse> updatePassword(@PathVariable Long id) {
        adminService.initPassword(id);
        return ResponseEntity.ok()
                .body(ApiResponse.success());
    }

    /**
     * 상품 조회
     *
     * @param
     * @return
     */
    @GetMapping("/products")
    public ResponseEntity<ApiResponse> getProducts(@RequestParam int page,
                                                   @RequestParam int size) {
        Page<ProductEntity> products = adminService.getProducts(page, size);
        Page<ProductResponseDto> dtoPage = products.map(ProductResponseDto::from);
        return ResponseEntity.ok()
                .body(ApiResponse.success(new PageResponse<>(dtoPage)));
    }

    /**
     * 상품 단일 조회
     *
     * @param
     * @return
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse> getProducts(@PathVariable Long id) {
        ProductEntity product = productService.getByIdContaining(id);
        return ResponseEntity.ok()
                .body(ApiResponse.success(ProductResponseDto.from(product)));
    }

    /**
     * 상품 등록
     *
     * @param
     * @return
     */
    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> registerProduct(ProductCreateDomain productCreateDomain) {
        ProductEntity product = productService.register(productCreateDomain);
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        ProductResponseDto.from(product)
                ));
    }

    /**
     * 상품 수정
     *
     * @param id
     * @return
     */
    @PatchMapping(value = "/product/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, ProductUpdateDomain productUpdateDomain) {
        ProductEntity product = productService.update(id, productUpdateDomain);
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        ProductResponseDto.from(product)
                ));
    }

    /**
     * 상품 삭제
     *
     * @param id
     * @return
     */
    @DeleteMapping("/product/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok()
                .body(ApiResponse.success());
    }

    /**
     * 주문 전체 조회
     *
     * @return
     */
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse> getOrders(@RequestParam int page,
                                                  @RequestParam int size) {
        Page<OrderEntity> orders = adminService.getOrders(page, size);
        Page<OrderResponseDto> dtoPage = orders.map(OrderResponseDto::from);
        return ResponseEntity.ok()
                .body(ApiResponse.success(new PageResponse<>(dtoPage)));
    }

    @PatchMapping("/setInvoice")
    public ResponseEntity<ApiResponse> setVoice(@RequestBody OrderInvoiceRequestDto dto) {
        OrderResponseDto orderResponseDto =  orderService.setInvoice(dto);
        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        orderResponseDto
                ));
    }
}
