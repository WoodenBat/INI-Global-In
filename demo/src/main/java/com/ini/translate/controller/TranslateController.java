package com.ini.translate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ini.translate.service.TranslateService;
import com.ini.translate.vo.TranslateRequest;

import lombok.AllArgsConstructor;



@Controller
@AllArgsConstructor

public class TranslateController {

	private final TranslateService translateService;
	
	@PostMapping("/api/translate")
	@ResponseBody
	public ResponseEntity<String> translate(@RequestBody TranslateRequest request) {
		System.out.println("번역 요청 받음: " + request.getInput());
		String result = translateService.translateText(request.getInput());
		System.out.println(result);
		return ResponseEntity.ok(result); //js에 보내기
	}
	
	
	
}
