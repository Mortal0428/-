package com.example.aid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class LogoutTestController {
    
    @GetMapping("/test-logout")
    @ResponseBody
    public String testLogout(HttpServletRequest request, Principal principal) {
        if (principal != null) {
            return "当前用户: " + principal.getName() + "<br>" +
                   "会话ID: " + request.getSession().getId() + "<br>" +
                   "注销链接: <a href='/logout'>点击注销</a><br>" +
                   "POST注销表单: <form action='/logout' method='post'><button type='submit'>POST注销</button></form>";
        } else {
            return "用户未登录";
        }
    }
}
