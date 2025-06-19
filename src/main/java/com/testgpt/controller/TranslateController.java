package com.testgpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.testgpt.dto.TranslateRequest;
import com.testgpt.service.TranslateService;



@Controller
public class TranslateController {

	private final TranslateService translateService;
	
	@Autowired
	public TranslateController(TranslateService translateService) {
		this.translateService = translateService;
	}
	@PostMapping("/api/translate")
	@ResponseBody
	public ResponseEntity<String> translate(@RequestBody TranslateRequest request) {
		System.out.println("번역 요청 받음: " + request.getInput());
		String result = translateService.translateText(request.getInput());
		return ResponseEntity.ok(result);
	}
	
}
