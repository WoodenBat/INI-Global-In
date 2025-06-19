package com.ini.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ini.board.mapper.BoardMapper;
import com.ini.board.vo.BoardCategoryVO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardCategoryService {

	private final BoardMapper boardmapper;
	
	public List<BoardCategoryVO> getCategories() {
		return boardmapper.getAllBoardCategoriesKr();
	}
}