package com.ini.home.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ini.board.vo.BoardImageDTO;
import com.ini.board.vo.BoardWithLikeReplyDTO;

@Mapper
public interface HomeMapper {
	
	public List<BoardWithLikeReplyDTO> findBoardByBoardIdRecentDESC();
	
	public List<BoardWithLikeReplyDTO> findBoardByBoardIdViewDESC();
	
	public List<BoardImageDTO> boardHasImage(int board_id);
	
}
