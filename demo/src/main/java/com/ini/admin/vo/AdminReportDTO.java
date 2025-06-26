package com.ini.admin.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminReportDTO {
	 	private String report_id; //신고 번호
	 	private String report_content; //신고 내용
	 	private String report_user; //신고자
	 	private LocalDateTime report_date; //신고일자
	 	private String report_category; //신고 종류
	    private String board_id; //게시글 번호
}
