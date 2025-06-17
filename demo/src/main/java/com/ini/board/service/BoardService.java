package com.ini.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ini.board.mapper.BoardMapper;
import com.ini.board.vo.BoardCategoryVO;
import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardDetailDTO;
import com.ini.board.vo.BoardImageDTO;
import com.ini.board.vo.BoardLikeDTO;
import com.ini.board.vo.BoardListDTO;
import com.ini.board.vo.BoardTagVO;

@Service
public class BoardService {

    @Autowired
    private BoardMapper boardMapper;

    /**
     * 게시글 저장 (board_id 자동 생성됨)
     * 이미지 목록이 포함되어 있으면 이미지도 함께 저장
     */
    public void savePost(BoardDTO boardDTO) {
        boardMapper.insertPost(boardDTO);
        if (boardDTO.getImageList() != null && !boardDTO.getImageList().isEmpty()) {
            boardMapper.insertBoardImages(boardDTO.getImageList());
        }
    }

    // 말머리 이름 목록 반환
    public List<String> getHeadList() {
        return boardMapper.selectAllBoardTags()
                .stream()
                .map(BoardTagVO::getTagName)
                .toList();
    }

    // 카테고리 이름 목록 반환
    public List<String> getCategoryList() {
        return boardMapper.selectAllBoardCategories()
                .stream()
                .map(BoardCategoryVO::getCategoryName)
                .toList();
    }

    // 전체 말머리 VO 객체 목록 반환
    public List<BoardTagVO> getAllTags() {
        return boardMapper.selectAllBoardTags();
    }

    // 전체 카테고리 VO 객체 목록 반환
    public List<BoardCategoryVO> getAllCategories() {
        return boardMapper.selectAllBoardCategories();
    }

    /**
     * 게시글 목록 조회
     * 조건 없이 최근 10개 가져오는 테스트용 쿼리
     */
    public List<BoardListDTO> getPostList() {
        String keyword = null;
        String category = null;
        String tag = null;
        int offset = 0;
        int limit = 10;

        return boardMapper.getPostList(keyword, category, tag, offset, limit);
    }

    // 게시글 상세 조회 (본문 + 유저정보 등)
    public BoardDetailDTO getPostById(int id) {
        return boardMapper.selectPostById(id);
    }

    // 단순 게시글 DTO만 조회
    public BoardDTO getBoardById(int boardId) {
        return boardMapper.selectBoardById(boardId);
    }

    // 게시글 수정 처리
    public void updatePost(BoardDTO dto) {
        boardMapper.updatePost(dto);
        boardMapper.deleteImagesByBoardId(dto.getBoard_id());
        if (dto.getImageList() != null && !dto.getImageList().isEmpty()) {
            boardMapper.insertBoardImages(dto.getImageList());
        }
    }

    // 게시글 삭제 처리 (좋아요, 댓글, 이미지 포함)
    public void deletePost(int boardId) {
        boardMapper.deleteLikesByBoardId(boardId);
        boardMapper.deleteCommentsByBoardId(boardId);
        boardMapper.deleteImagesByBoardId(boardId);
        boardMapper.deletePost(boardId);
    }

    // 조회수 증가
    public void incrementViews(int boardId) {
        boardMapper.updateViewCount(boardId);
    }

    /**
     * 좋아요 토글 기능
     * 이미 눌렀으면 취소, 아니면 좋아요 등록
     * 결과적으로 현재 좋아요 수 반환
     */
    public int toggleLike(int boardId, String userId) {
        boolean hasLiked = hasUserLiked(boardId, userId);
        if (hasLiked) {
            boardMapper.deleteLike(boardId, userId);
        } else {
            BoardLikeDTO like = new BoardLikeDTO(boardId, userId);
            boardMapper.insertLike(like);
        }
        return boardMapper.countLikes(boardId);
    }

    // 좋아요 수 반환
    public int getLikeCount(int boardId) {
        return boardMapper.countLikes(boardId);
    }

    // 특정 유저가 특정 게시글에 좋아요 눌렀는지 확인
    public boolean hasUserLiked(int boardId, String userId) {
        Integer count = boardMapper.hasUserLiked(boardId, userId);
        return count != null && count > 0;
    }

    // 특정 게시글의 이미지 목록 조회
    public List<BoardImageDTO> getBoardImages(int boardId) {
        return boardMapper.selectImageList(boardId);
    }

    // ✅ 단독 이미지 저장용 메서드 (예: 작성 후 저장 or 수정 시 추가 저장)
    public void saveImages(List<BoardImageDTO> imageList) {
        if (imageList != null && !imageList.isEmpty()) {
            boardMapper.insertBoardImages(imageList);
        }
    }
}