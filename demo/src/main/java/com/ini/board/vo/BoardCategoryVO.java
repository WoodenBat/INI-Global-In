package com.ini.board.vo;

import java.util.List;

import lombok.Data;

@Data
public class BoardCategoryVO {

	private final String board_category;
	private final String board_category_jp;
	
	public static List<BoardCategoryVO> defaultCategories() {
        return List.of(
            new BoardCategoryVO("java", "Java"),
            new BoardCategoryVO("db", "DB"),
            new BoardCategoryVO("jsp", "JSP"),
            new BoardCategoryVO("spring", "Spring"),
            new BoardCategoryVO("python", "Python"),
            new BoardCategoryVO("japanese", "일본어")
        );
    }

}
