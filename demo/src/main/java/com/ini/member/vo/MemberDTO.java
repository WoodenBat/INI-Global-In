package com.ini.member.vo;

import lombok.Data;

@Data
public class MemberDTO {

	private String user_id;
	private String user_password;
	private String user_nickname;
	private String user_intro; // 상태메시지

	private String user_email;
	private String user_phone_number;
	private String user_profile_img;
	
}
