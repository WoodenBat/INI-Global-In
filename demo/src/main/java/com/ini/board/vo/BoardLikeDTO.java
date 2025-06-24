package com.ini.board.vo;

import lombok.Data;

@Data
public class BoardLikeDTO {

	private int board_id;
	private String user_id;
	private int like_status;
	
	public BoardLikeDTO(int board_id, String user_id) {
        this.board_id = board_id;
        this.user_id = user_id;
    }
	
}
