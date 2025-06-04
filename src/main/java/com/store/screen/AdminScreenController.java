package com.store.screen;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class AdminScreenController {

    @GetMapping("/admin/users")
    public String userList(Model model) {
        return "admin/users";
    }

    @GetMapping("/admin/approval")
    public String approvalList(Model model) {
        return "admin/approval";
    }

    @GetMapping("/admin/products")
    public String productList(Model model) {
        return "admin/products";
    }

    @GetMapping("/admin/product/register")
    public String productRegister(Model model) {
        return "admin/product_register";
    }

    @GetMapping("/admin/product/update/{id}")
    public String productScreen(@PathVariable("id") Long id, Model model) {
        model.addAttribute("productId", id);
        return "admin/product_update";
    }

    @GetMapping("/admin/orders")
    public String orderList(Model model) {
        return "admin/orders";
    }

    @GetMapping("/admin/orderDetail")
    public String orderDetail(@RequestParam("orderNo") String ordNo, Model model) {
        model.addAttribute("ordNo", ordNo);
        return "admin/orderDetail";
    }

}
