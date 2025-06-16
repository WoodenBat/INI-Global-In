package com.ini.board.mapper;

import com.ini.board.vo.BoardCategoryVO;
import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardDetailDTO;
import com.ini.board.vo.BoardLikeDTO;
import com.ini.board.vo.BoardListDTO;
import com.ini.board.vo.BoardTagVO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {

    // 게시글 등록
    void insertPost(BoardDTO dto);
    void updatePost(BoardDTO dto);
    void deletePost(int boardId);
    void deleteImagesByBoardId(int boardId);
    void deleteCommentsByBoardId(int boardId);
    void deleteLikesByBoardId(int boardId);


    // 카테고리, 말머리 목록 조회
    List<BoardTagVO> selectAllBoardTags();
    List<BoardCategoryVO> selectAllBoardCategories();

    // 게시글 목록 (검색 + 필터 + 페이징)
    List<BoardListDTO> getPostList(
        @Param("keyword") String keyword,
        @Param("category") String category,
        @Param("tag") String tag,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    // 게시글 전체 개수 (페이징을 위해)
    int getPostCount(
        @Param("keyword") String keyword,
        @Param("category") String category,
        @Param("tag") String tag
    );
    
    //List<BoardTagVO> selectAllBoardTags();
   // List<BoardCategoryVO> selectAllBoardCategories();
    BoardDetailDTO selectPostById(@Param("id") int id
    );
    
    BoardDTO selectBoardById(int boardId);
    void updateViewCount(int boardId);
    //List<String> selectImageList(int boardId);
    
    Integer hasUserLiked(@Param("boardId") int boardId, @Param("userId") String userId);
    void insertLike(BoardLikeDTO like);
    void deleteLike(@Param("boardId") int boardId, @Param("userId") String userId);
    int countLikes(@Param("boardId") int boardId);
    int getLikeCount(Long boardId);
}
