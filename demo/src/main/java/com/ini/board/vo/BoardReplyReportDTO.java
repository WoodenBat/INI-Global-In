package com.ini.board.vo;

import java.util.Date;

import lombok.Data;

@Data
public class BoardReplyReportDTO {

	private String report_id;
	private String report_content;
	private String report_user;
	private Date report_date;
	private String report_category;
	private int reply_id;
	
}
