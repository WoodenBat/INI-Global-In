package com.ini.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ini.board.mapper.BoardMapper;
import com.ini.board.vo.BoardDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardMapper boardMapper;

	public List<BoardDTO> getBoardList(String keyword, String category, int page, int pageSize) {
		int startRow = (page - 1) * pageSize;
		return boardMapper.selectBoardList(keyword, category, startRow, pageSize);
	}

	public int getBoardCount(String keyword, String category) {
		return boardMapper.countBoardList(keyword, category);
	}

	public int saveBoard(BoardDTO board) {
		boardMapper.insertBoard(board);

		return board.getBoard_id();
	}

	public BoardDTO getBoardById(int board_id) {

		BoardDTO board = boardMapper.getBoardById(board_id);

		return board;
	}
}
