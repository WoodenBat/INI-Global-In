package com.ini.board.service;

import org.springframework.stereotype.Service;

import com.ini.board.mapper.BoardMapper;
import com.ini.board.vo.BoardDTO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor
public class BoardService {

	private final BoardMapper boardmapper;
	
	
	public int saveBoard(BoardDTO board) {
		boardmapper.insertBoard(board);
		
		return board.getBoard_id();
	}
	
	public BoardDTO getBoardById(int board_id) {
		
		BoardDTO board = boardmapper.getBoardById(board_id);
		
		return board;
	}
}