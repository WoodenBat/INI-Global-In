package com.ini.home.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ini.board.vo.BoardWithLikeReplyDTO;
import com.ini.home.mapper.HomeMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HomeService {

	private final HomeMapper homeMapper;
	
	public List<BoardWithLikeReplyDTO> findBoardByBoardIdRecentDESC() {
		
List<BoardWithLikeReplyDTO> hasImageIncludeList = new ArrayList<>();
		
		for(BoardWithLikeReplyDTO dto : homeMapper.findBoardByBoardIdRecentDESC()) {
			
			if(homeMapper.boardHasImage(dto.getBoard_id()) == null) {
				
				dto.setHasImage("0");
				
			} else {
				
				dto.setHasImage("1");
				
			}
			
			hasImageIncludeList.add(dto);
			
		}
		
		return hasImageIncludeList;
		
	}
	
	public List<BoardWithLikeReplyDTO> findBoardByBoardIdViewDESC() {
		
		List<BoardWithLikeReplyDTO> hasImageIncludeList = new ArrayList<>();
		
		for(BoardWithLikeReplyDTO dto : homeMapper.findBoardByBoardIdViewDESC()) {
			
			if(homeMapper.boardHasImage(dto.getBoard_id()) == null) {
				
				dto.setHasImage("0");
				
			} else {
				
				dto.setHasImage("1");
				
			}
			
			hasImageIncludeList.add(dto);
			
		}
		
		return hasImageIncludeList;
		
	}
	
}
