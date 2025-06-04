package com.store.screen;

import com.store.common.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Controller
public class ErrorScreenController {

    @GetMapping("/403")
    public String forbidden() {
        return "error/403"; // templates/error/403.html
    }

    @GetMapping("/404")
    public String notfound() {
        return "error/404"; // templates/error/404.html
    }

    @GetMapping("/500")
    public String externalError() {
        return "error/500"; // templates/error/500.html
    }

}
