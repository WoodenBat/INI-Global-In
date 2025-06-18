package com.ini.member.controller;

import com.ini.member.service.MemberService;
import com.ini.member.vo.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class LoginController {

    private final MemberService memberService;

    /** 로그인 폼 */
    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

    /** 로그아웃 */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 전체 삭제
        return "redirect:/home"; // 홈 화면으로 리다이렉트
    }

    /** 비밀번호 찾기 메인 폼 */
    @GetMapping("/finding-password")
    public String findingPasswordForm() {
        return "member/finding-password";
    }

    /** 아이디로 찾기 폼 */
    @GetMapping("/find-by-id")
    public String showFindByIdForm() {
        return "member/find-by-id";
    }

    /** 이메일 찾기 폼으로 리다이렉트 */
    @GetMapping("/finding-password/by-email")
    public String redirectToFindByEmailForm() {
        return "redirect:/find-by-email";
    }

    /** 이메일 입력 폼 */
    @GetMapping("/find-by-email")
    public String showFindByEmailForm() {
        return "member/find-by-email";
    }

    /** 아이디로 비밀번호 찾기 */
    @PostMapping("/finding-password/by-id")
    public String findPasswordById(@RequestParam("user_id") String user_id, Model model) {
        MemberDTO member = memberService.findMemberById(user_id);
        if (member != null) {
            model.addAttribute("password", member.getUser_password());
            return "member/show-password";
        } else {
            model.addAttribute("error", "아이디가 존재하지 않습니다.");
            return "member/find-by-id";
        }
    }

    /** 이메일로 비밀번호 찾기 */
    @PostMapping("/finding-password/by-email")
    public String findPasswordByEmail(@RequestParam("user_email") String user_email, Model model) {
        MemberDTO member = memberService.findByEmail(user_email);
        if (member != null) {
            // 실제 이메일 전송 로직은 생략
            model.addAttribute("message", "이메일로 비밀번호를 보냈습니다.");
            return "member/email-sent";
        } else {
            model.addAttribute("error", "이메일이 존재하지 않습니다.");
            return "member/find-by-email";
        }
    }
}
