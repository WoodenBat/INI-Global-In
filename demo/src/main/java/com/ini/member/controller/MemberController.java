package com.ini.member.controller;
import java.util.List;
import com.ini.member.service.MemberService;
import com.ini.member.vo.MemberDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final String uploadDir;

    public MemberController(MemberService memberService,
                             @Value("${file.upload-path}") String uploadDir) {
        this.memberService = memberService;
        this.uploadDir     = uploadDir;
    }

    // 프로필 수정 폼
    @GetMapping("/memberProfileUpdateForm")
    public String showProfileUpdateForm(Model model, Principal principal) {
        String userId = principal != null ? principal.getName() : "test";
        MemberDTO member = memberService.findMemberById(userId);
        model.addAttribute("member_info", member);
        return "member/MemberProfileUpdate";
    }

    // 마이페이지
    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal) {
        String userId = principal != null ? principal.getName() : "test";

        model.addAttribute("member_info",     memberService.findMemberById(userId));
        model.addAttribute("member_board",    memberService.findBoardById(userId));     // ← 여기
        model.addAttribute("member_like",     memberService.findLikeById(userId));      // ← 여기
        model.addAttribute("member_reply",    memberService.findReplyById(userId));     // ← 여기
        model.addAttribute("member_follow",   memberService.findFollowById(userId));    // ← 여기
        model.addAttribute("member_favorite", memberService.findFavoriteById(userId));  // ← 여기

        return "member/MemberMyPage";
    }

    @PostMapping("/memberProfileUpdate")
    public String updateProfile(@RequestParam(value = "profileImage", required = false) MultipartFile file,
                                @RequestParam("user_intro") String userIntro,
                                @RequestParam(value = "mode",defaultValue = "both")String mode,
                                Principal principal) throws IOException {
        String userId = principal != null ? principal.getName() : "test";
        System.out.println("モード：" + mode);
        System.out.println("入記しれた 自己紹介: " + userIntro);
     // 먼저 자기소개부터 저장
        MemberDTO dto = new MemberDTO();
        dto.setUser_id(userId);
        dto.setUser_intro(userIntro);
        memberService.updateMemberIntro(dto);
        
        // 프로필 이미지가 있을 경우에만 처리
        if ("both".equals(mode) && file != null && !file.isEmpty()) {
            String original = file.getOriginalFilename();
            String ext = original.substring(original.lastIndexOf('.') + 1).toLowerCase();

            if (!List.of("jpg", "jpeg", "png", "gif").contains(ext)) {
                return "redirect:/member/myPage?error=unsupportedFileType";
            }

            Path dir = Paths.get(uploadDir, "profile");
            if (!Files.exists(dir)) Files.createDirectories(dir);

            String newName = UUID.randomUUID() + "." + ext;
            file.transferTo(dir.resolve(newName).toFile());

            memberService.updateProfileImage(userId, newName);
        }

        // 3. 성공 리다이렉트
        return "redirect:/member/myPage?success=true";
    }
}





