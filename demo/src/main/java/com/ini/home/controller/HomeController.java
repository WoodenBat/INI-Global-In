package com.ini.home.controller;

import com.ini.member.vo.MemberDTO;
import com.ini.home.service.HomeService;
import com.ini.member.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {

	private MemberService memberService;
	private HomeService homeService;

	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
		if (session.getAttribute("user_id") != null) {

			String userId = (String) session.getAttribute("user_id");
			MemberDTO member = memberService.findMemberById(userId);

			model.addAttribute("nickname", member.getUser_nickname());
			model.addAttribute("board_views", homeService.findBoardByBoardIdViewDESC());
			model.addAttribute("board_recent", homeService.findBoardByBoardIdRecentDESC());
			
		} else {

			model.addAttribute("board_views", homeService.findBoardByBoardIdViewDESC());
			model.addAttribute("board_recent", homeService.findBoardByBoardIdRecentDESC());
			
		}
		return "home"; // templates/member/home.html
	}
}