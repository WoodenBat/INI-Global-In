package com.ini.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	//게시글 작성 페이지 조회
	@GetMapping("/write")
	public String writeBoard(Model model) {
		BoardDTO board = new BoardDTO();
		
		
		board.setUser_id("testUser");
		model.addAttribute("board",board);
		
		model.addAttribute("categoryList", boardCategoryService.getCategories());
		model.addAttribute("tagList", boardTagService.getTags());
		System.out.println("카테고리 갯수: " + boardCategoryService.getCategories().size());
		
		return "board/BoardWrite";
	}
	
	//게시글 작성 제출
	@PostMapping("/write")
	public String writeSubmit(@ModelAttribute BoardDTO board) {
		boardService.saveBoard(board);
		return "redirect:/board/list";
	}
	//게시글 작성 후 리다이렉트
	@GetMapping("/board/list")
	public String boardList(Model model) {
		return "board/BoardList";
	}
}
