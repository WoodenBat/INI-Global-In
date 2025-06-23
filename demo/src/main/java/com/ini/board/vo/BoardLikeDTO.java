package com.ini.board.vo;

import lombok.Data;

@Data
public class BoardLikeDTO {
    private int board_id;
    private String user_id;

    public BoardLikeDTO() {}

    public BoardLikeDTO(int boardId, String userId) {
        this.board_id = boardId;
        this.user_id = userId;
    }

    public int getBoardId() {
        return board_id;
    }

    public void setBoardId(int boardId) {
        this.board_id = boardId;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }
}
