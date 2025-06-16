package com.ini.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ini.board.mapper.BoardMapper;
import com.ini.board.vo.BoardCategoryVO;
import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardDetailDTO;
import com.ini.board.vo.BoardLikeDTO;
import com.ini.board.vo.BoardListDTO;
import com.ini.board.vo.BoardTagVO;

@Service
public class BoardService {

    @Autowired
    private BoardMapper boardMapper;

    // 게시글 저장 - Oracle에서 board_id 자동 생성
    public void savePost(BoardDTO boardDTO) {
        boardMapper.insertPost(boardDTO);
    }

    // 말머리 리스트 조회 (이름만 반환)
    public List<String> getHeadList() {
        return boardMapper.selectAllBoardTags()
                .stream()
                .map(BoardTagVO::getTagName)
                .toList();
    }

    // 카테고리 리스트 조회 (이름만 반환)
    public List<String> getCategoryList() {
        return boardMapper.selectAllBoardCategories()
                .stream()
                .map(BoardCategoryVO::getCategoryName)
                .toList();
    }

    // 전체 말머리 VO 반환
    public List<BoardTagVO> getAllTags() {
        return boardMapper.selectAllBoardTags();
    }

    // 전체 카테고리 VO 반환
    public List<BoardCategoryVO> getAllCategories() {
        return boardMapper.selectAllBoardCategories();
    }

    // 게시글 목록 조회 (테스트용 - 조건 없이 10개 반환)
    public List<BoardListDTO> getPostList() {
        String keyword = null;
        String category = null;
        String tag = null;
        int offset = 0;
        int limit = 10;

        return boardMapper.getPostList(keyword, category, tag, offset, limit);
    }

    // 게시글 상세 조회
    public BoardDetailDTO getPostById(int id) {
        return boardMapper.selectPostById(id);
    }

    // 게시글 수정
    public void updatePost(BoardDTO dto) {
        boardMapper.updatePost(dto);
    }
    
    public BoardDTO getBoardById(int boardId) {
        return boardMapper.selectBoardById(boardId);
    }

    public void incrementViews(int boardId) {
        boardMapper.updateViewCount(boardId);
    }
    
//    public List<String> getImageList(int boardId) {
//        return boardMapper.selectImageList(boardId);
//    }

    public void deletePost(int boardId) {
        // 순서: 좋아요 → 댓글 → 이미지 → 게시글
        boardMapper.deleteLikesByBoardId(boardId);
        boardMapper.deleteCommentsByBoardId(boardId);
        boardMapper.deleteImagesByBoardId(boardId);
        boardMapper.deletePost(boardId);
    }

    public int toggleLike(int boardId, String userId) {
        boolean hasLiked = hasUserLiked(boardId, userId); // 수정
        if (hasLiked) {
            boardMapper.deleteLike(boardId, userId);
        } else {
            BoardLikeDTO like = new BoardLikeDTO(boardId, userId);
            boardMapper.insertLike(like);
        }
        return boardMapper.countLikes(boardId);
    }

    public int getLikeCount(int boardId) {
        return boardMapper.countLikes(boardId);
    }

    // ✅ 시그니처를 (int, String)으로 통일
    public boolean hasUserLiked(int boardId, String userId) {
        Integer count = boardMapper.hasUserLiked(boardId, userId);
        return count != null && count > 0;
    }

}