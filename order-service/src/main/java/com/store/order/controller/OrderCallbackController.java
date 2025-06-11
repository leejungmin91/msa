package com.store.order.controller;


import com.store.order.domain.OrderPrepareDomain;
import com.store.order.domain.OrderSearchDomain;
import com.store.order.dto.OrderOverviewResponseDto;
import com.store.order.dto.OrderResponseDto;
import com.store.order.entity.OrderEntity;
import com.store.order.service.OrderService;
import com.store.common.annotation.CurrentUser;
import com.store.common.annotation.MemberAuthorize;
import com.store.common.annotation.SkipAuthorize;
import com.store.common.config.security.CustomUserDetails;
import com.store.common.exception.ApiException;
import com.store.common.http.ApiCode;
import com.store.common.http.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("${prefix}/order")
public class OrderCallbackController {

    private final OrderService orderService;

    /**
     * 결제 완료 후 호출 (주문)
     * @param resultMap
     * @return
     */
    @PostMapping("/callback")
    public String callback(@RequestParam Map<String,String> resultMap, Model model, HttpServletResponse response) throws Exception {
        boolean isSuccess = true;
        // TODO : 추후 결제 Exception 구성해 refund 필요한 Exception 인지 체크
        // 성공이 아닌경우
        if(!(resultMap.get("resultCd").equals("0000")
                || resultMap.get("resultCd").equals("1600")
                || resultMap.get("resultCd").equals("3001")
                || resultMap.get("resultCd").equals("A000")
                || resultMap.get("resultCd").equals("B000")
        )) {
            // 환불 불필요
            isSuccess = false;
        }

        String ordNo = resultMap.get("ordNo");

        try {
            orderService.order(resultMap);
        } catch (ApiException e) {
            // TODO : 환불로직 필요.
            isSuccess = false;
        Writer out = response.getWriter();
        String message = URLEncoder.encode("주문에 실패하였습니다. 다시 시도해주세11111요","UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        out.write("<script type=\"text/javascript\">alert(decodeURIComponent('"+message+"'.replace(/\\+/g, '%20'))); location.href=history.go(-2);</script>");
        out.flush();
        response.flushBuffer();
        out.close();
        return null;
//            return "redirect:/order/fail?orderNo=" + ordNo;
        }

        return "redirect:/order/orderComplete?orderNo=" + ordNo;

    }

    @PostMapping("/order/cancelCallback")
    public String cancelCallback(@RequestParam Map<String,String> resultMap) {
        try {
            String resultCd = resultMap.get("resultCd");
            String resultMsg = resultMap.get("resultMsg");
            String tid = resultMap.get("tid");
            String canAmt = resultMap.get("canAmt");
            String ordNo = resultMap.get("ordNo");

            //log.info("결제 취소 콜백 수신 - resultCd: {}, msg: {}, tid: {}, ordNo: {}, amt: {}", resultCd, resultMsg, tid, ordNo, canAmt);

            // TODO: 여기서 DB 상태 변경 처리 (ex. 주문 상태를 CANCEL로 변경)

            return ResponseEntity.ok("SUCCESS").toString();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL").toString();
        }
    }


}
