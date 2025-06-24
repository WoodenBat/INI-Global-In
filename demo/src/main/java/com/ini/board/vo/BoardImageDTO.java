package com.ini.board.vo;

import lombok.Data;
import java.util.Date;

@Data
public class BoardImageDTO {
	private int id;
    private int board_id;           // ✅ 수정
    private String image_path;      // ✅ 수정
    private String original_name;   // ✅ 수정
    private Date upload_date;    // ✅ 수정
}