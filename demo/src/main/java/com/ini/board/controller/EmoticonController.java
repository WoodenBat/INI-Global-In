package com.ini.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ini.board.service.EmoticonService;
import com.ini.board.vo.BoardEmoticonDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/emoticon")
public class EmoticonController {

	@Autowired
	private EmoticonService emoticonService;

	@GetMapping("/applyForm")
	public String showApplyForm() {
		return "board/emoticonApply";
	}

	@PostMapping("/apply")
	public String applyEmoticon(@RequestParam("emoticon_name") String name, @RequestParam("file") MultipartFile file,
			HttpSession session) {
		String userId = (String) session.getAttribute("user_id");
		emoticonService.applyEmoticon(name, file, userId);
		return "redirect:/emoticon/applyForm?success";
	}

	@GetMapping("/approvedList")
	@ResponseBody
	public List<BoardEmoticonDTO> getApprovedEmoticons() {
		return emoticonService.getApprovedEmoticons();
	}
}