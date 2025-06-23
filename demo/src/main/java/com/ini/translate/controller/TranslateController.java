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
	    String title = request.getTitle();   // ✅ null 아니게 제대로 받기
	    System.out.println("번역 요청 제목: " + title);

	    String result = translateService.translateText(title); // 이 title이 실제 제목임
	    System.out.println("번역 결과: " + result);

	    return ResponseEntity.ok(result);
	}
	
	
	
}