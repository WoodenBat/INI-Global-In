package com.ini.home.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LanguageController {

	@GetMapping("/lang/set")
	public ResponseEntity<Void> setLang(@RequestParam("lang") String lang, HttpSession session) {
		
		System.out.println("▶ 선택된 언어: " + lang); // 확인용
		session.setAttribute("lang", lang);
		
		return ResponseEntity.ok().build();
	}

}
