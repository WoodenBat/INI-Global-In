package com.ini.board.vo;

import java.util.Date;

import lombok.Data;

@Data
public class BoardDTO {

    private int board_id;
    private String board_title;
    private String board_content;
    private String user_id;
    private int board_views;
    private String board_category;
    private String board_category_jp;    // 조인된 일본어 버전 (사용 안 하면 null)
    private Date board_write_date;
    private Date board_update_date;
    private String board_tag;
    private String board_tag_jp;
    
    private int likes;
    private int comments;
}