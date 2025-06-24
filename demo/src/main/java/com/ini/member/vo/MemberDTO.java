package com.ini.member.vo;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberDTO {

    @NotBlank(message = "아이디를 입력하세요")
    @Size(min = 4, max = 20, message = "아이디는 4~20자 사이여야 합니다")
    private String user_id;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String user_password;

    @NotBlank(message = "닉네임을 입력하세요")
    @Size(max = 16, message = "닉네임은 최대 16자까지 가능합니다")
    private String user_nickname;
    
    @Email(message = "이메일을 입력하세요")
    private String user_email;

    @NotBlank(message = "전화번호를 입력하세요")
    @Pattern(regexp = "^\\d{2,3}-?\\d{3,4}-?\\d{4}$", message = "유효한 전화번호 형식이 아닙니다")
    private String user_phone_number;

    // 실제 업로드 파일 받기용
    private MultipartFile user_profile_img;

    // DB에 저장할 이미지 경로 (문자열)
    private String user_profile_img_path;
    
    private String user_intro;
}
