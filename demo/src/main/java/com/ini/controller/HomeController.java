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

    // 루트 경로('/') 요청을 '/home' 으로 리다이렉트
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    // 로그인 성공 후 보여줄 홈 화면
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            String userId = userDetails.getUsername(); // 실제 userId
            MemberDTO member = memberService.findMemberById(userId);
            if (member != null) {
                model.addAttribute("nickname", member.getUser_nickname());
            }
        }
        return "member/home";  // templates/member/home.html 뷰를 리턴
    }
}
