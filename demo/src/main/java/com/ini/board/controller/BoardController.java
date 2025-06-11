package com.ini.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ini.board.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {

	private final BoardService boardservice;
	
	@GetMapping("test")
	public String test() {
		
		return "test";
	}
	
}
