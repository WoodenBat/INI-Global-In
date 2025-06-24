package com.ini.member.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ini.member.service.MemberService;
import com.ini.member.mapper.MemberMapper;
import com.ini.member.vo.MemberDTO;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/member/*")
@AllArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final String uploadDir = "C:/upload/profile";

	@Autowired
	private MemberMapper memberMapper;

	@GetMapping("myPage")
	public String myPageTest(Model model, HttpSession session) {

		System.out.println("sessionid ==========================================" + session.getAttribute("user_id"));
		String session_id = String.valueOf(session.getAttribute("user_id"));
		model.addAttribute("member_info", memberService.findMemberById(session_id));
		model.addAttribute("member_follow", memberService.findFollowById(session_id));
		model.addAttribute("member_board", memberService.findBoardLikeReplyById(session_id));
		model.addAttribute("member_like", memberService.findBoardLikeReplyByLikeId(session_id));
		model.addAttribute("member_reply", memberService.findBoardLikeReplyByReplyId(session_id));
		model.addAttribute("member_favorite", memberService.findBoardLikeReplyByFavoriteId(session_id));

		return "/member/MemberMyPage";
	}

	@GetMapping("signup")
	public String signupForm(Model model) {
		model.addAttribute("user", new MemberDTO());
		return "/member/signup";
	}

	@PostMapping("signup")
	public String signup(@Valid @ModelAttribute("user") MemberDTO user, BindingResult bindingResult,
			@RequestParam(value = "user_profile_img", required = false) MultipartFile file, Model model) {

		if (bindingResult.hasErrors()) {
			return "/member/signup";
		}

		// ID 중복 검사
		if (memberMapper.findMemberById(user.getUser_id()) != null) {
			model.addAttribute("errorMessage", "이미 사용 중인 아이디입니다. 중복 확인을 해주세요!");
			return "/member/signup";
		}

		// 이메일 중복 검사
		if (memberMapper.findByEmail(user.getUser_email()) != null) {
			model.addAttribute("errorMessage", "이미 사용 중인 이메일입니다. 중복 확인을 해주세요!");
			return "/member/signup";
		}

		// 닉네임 중복 검사
		if (memberMapper.findByNickname(user.getUser_nickname()) != null) {
			model.addAttribute("errorMessage", "이미 사용 중인 닉네임입니다. 중복 확인을 해주세요!");
			return "/member/signup";
		}

		// 프로필 이미지 필수 체크
		if (file == null || file.isEmpty()) {
			model.addAttribute("errorMessage", "프로필 이미지는 꼭 첨부해 주세요!");
			return "/member/signup";
		}

		try {
			// 비밀번호 암호화
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			user.setUser_password(encoder.encode(user.getUser_password()));

			// 이미지 업로드 처리
			String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			Path path = Paths.get(uploadDir, fileName);
			Files.createDirectories(path.getParent());
			file.transferTo(path.toFile());
			user.setUser_profile_img_path("/images/profile/" + fileName);

			// DB 저장
			int result = memberMapper.insertMember(user);
			if (result <= 0) {
				model.addAttribute("errorMessage", "회원가입에 실패했습니다.");
				return "/member/signup";
			}

			return "redirect:/member/signupsuccess";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "서버 오류가 발생했습니다.");
			return "/member/signup";
		}
	}

	@GetMapping("signupsuccess")
	public String signupSuccessPage() {
		return "/member/signupsuccess";
	}

	// 아이디 중복 확인
	@GetMapping("checkId")
	@ResponseBody
	public Map<String, Boolean> checkUserId(@RequestParam("user_id") String userId) {
		MemberDTO found = memberMapper.findMemberById(userId);
		return Collections.singletonMap("exists", found != null);
	}

	// 이메일 중복 확인
	@GetMapping("checkEmail")
	@ResponseBody
	public Map<String, Boolean> checkEmail(@RequestParam("user_email") String email) {
		MemberDTO found = memberMapper.findByEmail(email);
		return Collections.singletonMap("exists", found != null);
	}

	// 닉네임 중복 확인
	@GetMapping("checkNickname")
	@ResponseBody
	public Map<String, Boolean> checkNickname(@RequestParam("user_nickname") String nickname) {
		MemberDTO found = memberMapper.findByNickname(nickname);
		return Collections.singletonMap("exists", found != null);
	}

}
