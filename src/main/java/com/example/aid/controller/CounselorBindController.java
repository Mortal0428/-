package com.example.aid.controller;

import com.example.aid.model.BindRequest;
import com.example.aid.model.User;
import com.example.aid.repository.BindRequestRepository;
import com.example.aid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/counselor/bind")
public class CounselorBindController {
    @Autowired
    private BindRequestRepository bindRequestRepository;
    @Autowired
    private UserService userService;

    @GetMapping
    public String list(@RequestParam(required=false) String status, Authentication authentication, Model model) {
        User counselor = userService.findByUsername(authentication.getName()).orElse(null);
        List<BindRequest> list;
        if (status == null || status.isEmpty() || "all".equals(status)) {
            list = bindRequestRepository.findByCounselor(counselor);
        } else {
            list = bindRequestRepository.findByCounselorAndStatus(counselor, status);
        }
        model.addAttribute("binds", list);
        model.addAttribute("status", status == null ? "all" : status);
        return "counselor/bind_list";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id) {
        bindRequestRepository.findById(id).ifPresent(b -> { b.setStatus("approved"); bindRequestRepository.save(b); });
        return "redirect:/counselor/bind?status=pending";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id) {
        bindRequestRepository.findById(id).ifPresent(b -> { b.setStatus("rejected"); bindRequestRepository.save(b); });
        return "redirect:/counselor/bind?status=pending";
    }
}


