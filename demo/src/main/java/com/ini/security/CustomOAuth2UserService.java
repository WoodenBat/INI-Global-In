package com.ini.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession; // jakarta or javax.servlet.http.HttpSession 사용 (환경에 따라)

import java.util.Map;
import java.util.Collections;

import com.ini.member.vo.MemberDTO;
import com.ini.member.mapper.MemberMapper;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final MemberMapper memberMapper;
	private final HttpSession session; // 세션 주입

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) {
		// 구글로부터 사용자 정보 받아오기
		OAuth2User oAuth2User = super.loadUser(userRequest);
		Map<String, Object> attributes = oAuth2User.getAttributes();

		// 구글에서 제공하는 이메일과 이름
		String email = (String) attributes.get("email");
		String nickname = (String) attributes.get("name");

		// DB에 사용자 존재 여부 확인
		MemberDTO member = memberMapper.findMemberById(email);

		// 신규 회원이면 DB에 저장
		if (member == null) {
			member = new MemberDTO();
			member.setUser_id(email);
			member.setUser_email(email);
			member.setUser_nickname(nickname);
			member.setUser_password("GOOGLE_USER"); // OAuth 유저는 비밀번호 임시로 지정

			memberMapper.insertOAuthUser(member); // 반드시 매퍼 XML에 insertOAuthUser 쿼리 구현 필요
		}

		// ✅ 세션에 닉네임 저장
		session.invalidate();
		session.setAttribute("nickname", member.getUser_nickname());

		// ROLE_USER 권한으로 사용자 반환 (식별자는 email)
		return new DefaultOAuth2User(Collections.singleton(() -> "ROLE_USER"), attributes, "email" // 사용자 식별 키
		);
	}
}