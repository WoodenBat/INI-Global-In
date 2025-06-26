package com.ini.admin.vo;
import lombok.Data;

@Data
public class AdminUserDTO {
    private String user_id;       // 회원 고유 ID
    private String user_nickname;   // 닉네임
    private String user_email;      // 이메일
    
    private int report_count;

}

//http://localhost:8080/admin/users