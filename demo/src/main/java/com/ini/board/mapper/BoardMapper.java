package com.ini.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ini.board.vo.BoardDTO;

@Mapper
public interface BoardMapper {

	List<BoardDTO> selectBoardList(@Param("keyword") String keyword,
            @Param("category") String category,
            @Param("offset") int offset,
            @Param("limit") int limit);


	int countBoards(@Param("keyword") String keyword, @Param("category") String category);

    // 게시글 상세 조회
    BoardDTO selectBoardById(@Param("boardId") int boardId);
}