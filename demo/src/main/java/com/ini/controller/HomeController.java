package com.ini.controller;

import com.ini.member.vo.MemberDTO;
import com.ini.member.service.MemberService;
import com.ini.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            String userId = userDetails.getUsername(); // 실제로는 userId임
            MemberDTO member = memberService.findMemberById(userId);
            model.addAttribute("nickname", member.getUser_nickname());
        }
        return "member/home";  // templates/member/home.html
    }
}





