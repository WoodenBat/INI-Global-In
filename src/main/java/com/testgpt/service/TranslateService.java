package com.testgpt.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TranslateService {
	
	@Value("${openai.api.key}")//application.properties에서 값을 읽어와 필드에 주입
	private String apiKey;
	
	public String translateText(String input) {
		
		
		
		String prompt = "다음을 일본어로 번역해줘:\n" + input;
		
		//gpt서버에 HTTP요청보내야함 헤더생성및설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);//JSON보낼거고
		headers.setBearerAuth(apiKey);//인증 bearer
		
		
		//body에 담을내용
		Map<String,Object> requestBody = new HashMap<>();
		requestBody.put("model","gpt-3.5-turbo");
		requestBody.put("messages",List.of(Map.of("role","user","content",prompt)));
		
		//헤더 바디 담는 실질적인 데이터 컨테이너 (무엇을보낼지)
		HttpEntity<Map<String,Object>> entity = new HttpEntity<>(requestBody, headers);
		
		//실제로 보내기 위한 데이터타입
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://api.openai.com/v1/chat/completions";
		
		try {
			ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
			List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
			Map<String, Object> message = (Map<String,Object>)choices.get(0).get("message");
			return message.get("content").toString().trim();
		}catch (Exception e) {
			return "번역 중 오류 발생:" + e.getMessage();
		}
	}
}
