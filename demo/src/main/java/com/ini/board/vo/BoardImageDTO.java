package com.ini.board.vo;

import lombok.Data;
import java.util.Date;

@Data
public class BoardImageDTO {
    private int id;
    private int board_id;
    private String image_path;
    private String original_name;
    private Date upload_date;
}
