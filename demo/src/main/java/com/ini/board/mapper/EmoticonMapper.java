package com.ini.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ini.board.vo.BoardEmoticonDTO;

@Mapper
public interface EmoticonMapper {
	 void insertEmoticon(BoardEmoticonDTO dto);
}
