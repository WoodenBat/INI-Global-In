package com.ini.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ini.member.service.MemberService;
import com.ini.member.vo.MemberDTO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/member/*")
@AllArgsConstructor
public class MemberController {

	private final MemberService memberservice;
	
	@GetMapping("memberTest")
	public String memberTest(Model model) {
		
		MemberDTO memberVO = memberservice.findAllMember();
		
		model.addAttribute("members" ,memberVO);
		
		return "/member/test";
		
	}
	
}
