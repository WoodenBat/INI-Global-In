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

    // 🔹 end 제거된 메서드
    public List<BoardDTO> getBoardList(String keyword, String category, int offset, int limit) {
        return boardMapper.selectBoardList(keyword, category, offset, limit);
    }

    // 🔸 게시글 총 개수 조회
    public int countBoards(String keyword, String category) {
        return boardMapper.countBoards(keyword, category);
    }
}
