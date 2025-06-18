package com.ini.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ini.board.service.BoardReplyService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("board/reply/*")
public class BoardReplyController {

	private BoardReplyService boardReplyService;
	
	@GetMapping("boardReply")
	public String boardReplyList(Model model, String board_id) {
		
		model.addAttribute("boardReplyList", boardReplyService.findBoardReplyByBoardId(board_id));
		
		return "board/boardReply";
	}
	
	@GetMapping("boardReplyTest")
	public String boardReplyTest() {
		
		return "board/boardReplyTest";
	}
	
}
