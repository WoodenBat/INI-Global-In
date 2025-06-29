package com.ini.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ini.board.vo.BoardEmoticonDTO;

@Mapper
public interface EmoticonMapper {
	void insertEmoticon(BoardEmoticonDTO dto);

	List<BoardEmoticonDTO> selectAll();
}
