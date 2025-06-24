package com.ini.translate.service;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ini.translate.vo.GptResponse;


@Service
public class TranslateService {

	@Value("${openai.api.key}")
	private String apiKey;
	
	public String translateText(String input) {
		
		String prompt = "다음을 한글이면 일본어, 일본어면 한글로 번역해주되 번역한 텍스트만 줘:\n" + input;
		
		//gpt서버에 HTTP요청보내야함 헤더 생성 및 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);//JSON보낼거고
		headers.setBearerAuth(apiKey);//인증 bearer
		
		//body에 담을 내용
		Map<String,Object> requestBody = new HashMap<>();
		requestBody.put("model","gpt-3.5-turbo");
		requestBody.put("messages",List.of(Map.of("role","user","content",prompt)));
		
		//헤더 바디 담는 실질적인 데이터 컨테이너 (무엇을 보낼지)
		HttpEntity<Map<String,Object>> entity = new HttpEntity<>(requestBody, headers);
		
		//실제로 보내기 위한 데이터타입
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://api.openai.com/v1/chat/completions";
		
		try {
			
			  //요청하고 응답까지받음 
			ResponseEntity<GptResponse> response = restTemplate.postForEntity(url, entity, GptResponse.class); //파싱 
			String translated = response.getBody().getChoices().get(0).getMessage().getContent().trim();
			  
			return translated;
			 
			
		}catch (Exception e) {
			 e.printStackTrace(); // 로그 출력
			 return "번역 실패 (예외 발생): " + e.getMessage();
		}
	}
}
