package com.ini.board.vo;
import lombok.Data;
import java.util.Date;
@Data
public class BoardListDTO {
    private int board_id;
    private String board_title;
    private String user_id;
    private Date board_write_date;
}
