package com.ini.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ini.board.mapper.BoardReplyMapper;
import com.ini.board.vo.BoardReplyDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardReplyService {

	private BoardReplyMapper boardReplyMapper;

	public List<BoardReplyDTO> findBoardReplyByBoardId(String board_id) {
		
		return boardReplyMapper.findBoardReplyByBoardId(board_id);
		
	}

}
