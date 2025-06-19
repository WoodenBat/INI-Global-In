package com.ini.board.vo;

import java.util.Date;

import lombok.Data;

@Data
public class BoardReplyWithUserDTO {

	private int reply_id;
	private String reply_content;
	private String reply_writer;
	private Date reply_write_date;
	private Date reply_update_date;
	private String reply_status;
	private int board_id;
	private String user_nickname;
	private String user_profile_img;
	private Long reply_date_differ;
	
}
