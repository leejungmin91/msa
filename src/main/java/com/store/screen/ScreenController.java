package com.store.screen;

import com.store.common.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Controller
public class ScreenController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/login")
    public String loginScreen(HttpServletRequest request, Model model) {
        try{
            String token = jwtTokenProvider.resolveToken(request, "accessToken");
            jwtTokenProvider.validateToken(token);
            return "redirect:/";
        } catch (Exception  e) {
            return "login";
        }
    }

    @GetMapping("/signup")
    public String signupScreen(Model model) {
        return "signup";
    }

    @GetMapping("/about")
    public String aboutScreen(Model model) {
        return "about";
    }


    @GetMapping("/member")
    public String memberScreen(Model model) {
        return "member";
    }

    @GetMapping("/order")
    public String orderScreen(Model model) {
        return "order";
    }

    @GetMapping("/order/orderDetail")
    public String orderDetail(@RequestParam("orderNo") String ordNo, Model model) {
        model.addAttribute("ordNo", ordNo);
        return "orderDetail";
    }

    @GetMapping("/order/fail")
    public String orderFail(@RequestParam("orderNo") String ordNo, Model model) {
        model.addAttribute("ordNo", ordNo);
        return "orderFail";
    }

    @GetMapping("/order/orderComplete")
    public String orderComplete(@RequestParam("orderNo") String ordNo, Model model) {
        model.addAttribute("ordNo", ordNo);
        return "orderComplete";
    }

    @GetMapping("/checkout")
    public String checkoutCartScreen(Model model) {
        return "checkout";
    }

    @GetMapping("/checkout/{id}")
    public String checkoutProductScreen(@PathVariable("id") Long id, @RequestParam("quantity") int quantity,  Model model) {
        model.addAttribute("productId", id);
        model.addAttribute("quantity", quantity);
        return "checkout";
    }

    @GetMapping("/cart")
    public String cartScreen(Model model) {
        return "cart";
    }

    @GetMapping("/product/{id}")
    public String productScreen(@PathVariable("id") Long id, Model model) {
        model.addAttribute("productId", id);
        return "product";
    }

    @GetMapping("/privacy")
    public String privacyScreen(){ return "privacy";}

    @GetMapping("/terms")
    public String termsScreen(){ return "terms";}



}
