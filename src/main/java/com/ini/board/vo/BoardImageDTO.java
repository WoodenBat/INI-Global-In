package com.ini.board.vo;

import lombok.Data;
import java.util.Date;

@Data
public class BoardImageDTO {
    private int id;
    private int boardId;           // ✅ 수정
    private String imagePath;      // ✅ 수정
    private String originalName;   // ✅ 수정
    private Date uploadDate;       // ✅ 수정
}