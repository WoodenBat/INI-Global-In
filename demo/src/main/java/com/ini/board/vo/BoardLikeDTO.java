package com.ini.board.vo;

import lombok.Data;

@Data
public class BoardLikeDTO {
    private int boardId;
    private String userId;

    public BoardLikeDTO() {}

    public BoardLikeDTO(int boardId, String userId) {
        this.boardId = boardId;
        this.userId = userId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
