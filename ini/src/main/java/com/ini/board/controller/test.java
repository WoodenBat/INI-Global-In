package com.ini.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board/*")
public class test {

	@GetMapping("test")
	public String test() {
		
		return "test";
	}
	
}
