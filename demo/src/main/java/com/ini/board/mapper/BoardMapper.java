package com.ini.board.mapper;

import com.ini.board.vo.BoardCategoryVO;
import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardDetailDTO;
import com.ini.board.vo.BoardImageDTO;
import com.ini.board.vo.BoardLikeDTO;
import com.ini.board.vo.BoardListDTO;
import com.ini.board.vo.BoardTagVO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    // 게시글
    void insertPost(BoardDTO dto);
    void updatePost(BoardDTO dto);
    void deletePost(int boardId);

    // 게시글 조회
    BoardDetailDTO selectPostById(@Param("id") int id);
    BoardDTO selectBoardById(int boardId);
    void updateViewCount(int boardId);

    // 리스트
    List<BoardListDTO> getPostList(
        @Param("keyword") String keyword,
        @Param("category") String category,
        @Param("tag") String tag,
        @Param("offset") int offset,
        @Param("limit") int limit
    );
    int getPostCount(
        @Param("keyword") String keyword,
        @Param("category") String category,
        @Param("tag") String tag
    );

    // 카테고리, 말머리
    List<BoardTagVO> selectAllBoardTags();
    List<BoardCategoryVO> selectAllBoardCategories();

    // 좋아요
    Integer hasUserLiked(@Param("boardId") int boardId, @Param("userId") String userId);
    void insertLike(BoardLikeDTO like);
    void deleteLike(@Param("boardId") int boardId, @Param("userId") String userId);
    int countLikes(@Param("boardId") int boardId);
    int getLikeCount(Long boardId);
    void deleteLikesByBoardId(int boardId);

    // 이미지
    void insertBoardImages(@Param("list") List<BoardImageDTO> imageList);
    List<BoardImageDTO> selectImageList(@Param("boardId") int boardId);
    void deleteImagesByBoardId(@Param("boardId") int boardId);

    // 댓글 삭제
    void deleteCommentsByBoardId(int boardId);
}