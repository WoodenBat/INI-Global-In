package com.ini.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ini.board.vo.BoardCategoryVO;
import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardTagVO;

@Mapper
public interface BoardMapper {
	// 한국어용
    List<BoardCategoryVO> getAllBoardCategoriesKr();
    List<BoardTagVO> getAllBoardTagsKr();

    // 일본어용
    List<BoardCategoryVO> getAllBoardCategoriesJp();
    List<BoardTagVO> getAllBoardTagsJp();
    
    //작성게시글저장
    void insertBoard(BoardDTO board);
}
