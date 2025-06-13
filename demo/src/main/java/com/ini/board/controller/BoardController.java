package com.ini.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ini.board.service.BoardCategoryService;
import com.ini.board.service.BoardService;
import com.ini.board.service.BoardTagService;
import com.ini.board.vo.BoardDTO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {

	private final BoardService boardService;
	private final BoardCategoryService boardCategoryService;	
	private final BoardTagService boardTagService;
	
	
	@GetMapping("/write")
	public String writeBoard(Model model) {
		BoardDTO board = new BoardDTO();
		board.setUser_id("testUser");
		model.addAttribute("board",board);
		return "board/BoardWrite";
	}
	
}
