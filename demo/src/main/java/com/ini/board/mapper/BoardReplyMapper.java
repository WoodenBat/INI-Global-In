package com.ini.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ini.board.vo.BoardReplyDTO;

@Mapper
public interface BoardReplyMapper {
	
	public List<BoardReplyDTO> findBoardReplyByBoardId(String board_id);
	
}
