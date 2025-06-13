package com.ini.member.controller;

import java.util.List;
import java.util.Arrays;
import java.util.UUID;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ini.member.service.MemberService;
import com.ini.member.vo.MemberDTO;

@Controller
@RequestMapping("/member/")
public class MemberController {

    private final MemberService memberService;
    private final String uploadDir;

    public MemberController(MemberService memberService, @Value("${file.upload-path}") String uploadDir) {
        this.memberService = memberService;
        this.uploadDir = uploadDir;
    }

    // URL: /member/profile/update
    @GetMapping("/memberProfileUpdateForm")
    public String showProfileUpdateForm(Model model) {
        MemberDTO member = memberService.findMemberById("test");
//        if (member == null) {
//            // 로그인 페이지로 리다이렉트하거나 에러 처리
//            return "redirect:/login";
//        }
        model.addAttribute("member_info", member);
        return "member/MemberProfileUpdate";  // 뷰 이름
    }

// URL: /member/myPage
    @GetMapping("myPage")
    public String myPageTest(Model model, Principal principal) {
//        if (principal == null) {
//            return "redirect:/login";
//        }
//        String userId = principal.getName();

        model.addAttribute("member_info", memberService.findMemberById("test"));
        model.addAttribute("member_follow", memberService.findFollowById("test"));
        model.addAttribute("member_board", memberService.findBoardById("test"));
        model.addAttribute("member_like", memberService.findLikeById("test"));
        model.addAttribute("member_reply", memberService.findReplyById("test"));
        model.addAttribute("member_favorite", memberService.findFavoriteById("test"));

        return "member/MemberMyPage";
    }

    // URL: /member/profile-image (POST)
    @PostMapping("memberProfileUpdate")
    public String uploadProfileImage(@RequestParam("profileImage") MultipartFile file, Principal principal)
        throws IOException {
        String userId = "test";
        
        if (file.isEmpty()) return "redirect:/member/myPage?error=emptyFile";

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains("."))
            return "redirect:/member/myPage?error=invalidFileName";

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        List<String> allowedExt = Arrays.asList("jpg", "jpeg", "png", "gif");
        if (!allowedExt.contains(extension.toLowerCase()))
            return "redirect:/member/myPage?error=unsupportedFileType";

        String newFileName = UUID.randomUUID().toString() + "." + extension;
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(newFileName);
        file.transferTo(filePath.toFile());

        memberService.updateProfileImage(userId, newFileName);
        return "redirect:/member/myPage";
        
        
    }
}