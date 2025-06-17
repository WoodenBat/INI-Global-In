package com.ini.board.vo;

import lombok.Data;

@Data
public class BoardWithLikeReplyDTO {

	private int board_id;
	private String board_title;
	private String user_id;
	private int board_views;
	private String board_category;
	private String board_tag;
	private String board_like_cnt;
	private String board_reply_cnt;

}
