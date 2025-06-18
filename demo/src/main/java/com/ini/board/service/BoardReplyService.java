package com.ini.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ini.board.mapper.BoardMapper;
import com.ini.board.vo.BoardReplyDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardReplyService {
	private final BoardMapper boardmapper;
	
	public List<BoardReplyDTO> getRepliesByBoardId(int board_id) {
		
		return boardmapper.getRepliesByBoardId(board_id);
	}
	
	public void insertReply(BoardReplyDTO reply) {
		boardmapper.insertReply(reply);
	}
	
}
