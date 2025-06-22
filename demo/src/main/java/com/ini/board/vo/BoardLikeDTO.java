package com.ini.board.vo;

import lombok.Data;

@Data
public class BoardLikeDTO {

	private int board_id;
	private String user_id;
	private int like_status;
	
	public BoardLikeDTO(int boardId, String userId) {
        this.board_id = boardId;
        this.user_id = userId;
    }
	
}
