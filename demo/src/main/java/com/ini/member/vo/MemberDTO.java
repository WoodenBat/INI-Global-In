package com.ini.member.vo;

<<<<<<< HEAD
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberDTO {

    @NotBlank(message = "아이디를 입력하세요")
    private String user_id;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String user_password;

    @NotBlank(message = "닉네임을 입력하세요")
    private String user_nickname;

    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "유효한 이메일 형식이 아닙니다")
    private String user_email;

    @NotBlank(message = "전화번호를 입력하세요")
    @Pattern(regexp = "^\\d{2,3}-?\\d{3,4}-?\\d{4}$", message = "유효한 전화번호 형식이 아닙니다")
    private String user_phone_number;

    // 실제 업로드 파일 받기용
    private MultipartFile user_profile_img;

    // DB에 저장할 이미지 경로 (문자열)
    private String user_profile_img_path;
=======
import lombok.Data;

@Data
public class MemberDTO {

	private String user_id;
	private String user_password;
	private String user_nickname;
	private String user_email;
	private String user_phone_number;
	private String user_profile_img;
	
>>>>>>> refs/remotes/origin/hyeokjoon
}
