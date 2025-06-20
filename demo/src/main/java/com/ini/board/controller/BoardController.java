package com.ini.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ini.board.service.BoardService;
import com.ini.board.vo.BoardDTO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {

	private final BoardService boardService;
	
	@GetMapping("test")
	public String test() {
		
		return "test";
	}
	
	@GetMapping("/list")
	public String boardList(@RequestParam(defaultValue = "1") int page,
	                        @RequestParam(required = false) String keyword,
	                        @RequestParam(required = false) String category,
	                        Model model) {

	    int pageSize = 10;
	    int totalCount = boardService.getBoardCount(keyword, category);
	    int totalPage = (int) Math.ceil((double) totalCount / pageSize);

	    List<BoardDTO> list = boardService.getBoardList(keyword, category, page, pageSize);

	    model.addAttribute("boardList", list);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPage", totalPage);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("category", category);

	    return "member/boardList";
	}
	
}