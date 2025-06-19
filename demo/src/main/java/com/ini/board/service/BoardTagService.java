package com.ini.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ini.board.mapper.BoardMapper;
import com.ini.board.vo.BoardTagVO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardTagService {
	
	private final BoardMapper boardmapper;
	
	public List<BoardTagVO> getTags(){
		return boardmapper.getAllBoardTagsKr();
	}

}