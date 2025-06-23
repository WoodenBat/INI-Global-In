package com.ini.board.mapper;

import com.ini.board.vo.BoardCategoryVO;
import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardDetailDTO;
import com.ini.board.vo.BoardImageDTO;
import com.ini.board.vo.BoardLikeDTO;
import com.ini.board.vo.BoardListDTO;
import com.ini.board.vo.BoardReportDTO;
import com.ini.board.vo.BoardTagVO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    // 게시글
    void insertPost(BoardDTO dto);
    void updatePost(BoardDTO dto);
    void deletePost(int board_id);
    void insertBoardImage(BoardImageDTO image);

    // 게시글 조회
    BoardDetailDTO selectPostById(@Param("board_id") int id);
    BoardDTO selectBoardById(int board_id);
    void updateViewCount(int board_id);

    // 리스트
    List<BoardListDTO> getBoardList(
        @Param("keyword") String keyword,
        @Param("category") String category,
        @Param("tag") String tag,
        @Param("offset") int offset,
        @Param("limit") int limit
    );
    
    int countAllPosts();	// 전체 게시글 수 조회용
    
    int getPostCount(
        @Param("keyword") String keyword,
        @Param("category") String category,
        @Param("tag") String tag
    );	// 검색 조건별 게시글 수 조회용 (선택)

    // 카테고리, 말머리
    List<BoardTagVO> selectAllBoardTags();
    List<BoardCategoryVO> selectAllBoardCategories();

    // 좋아요
    Integer hasUserLiked(@Param("board_id") int board_id, @Param("user_id") String user_id);
    void insertLike(BoardLikeDTO like);
    void deleteLike(@Param("board_id") int board_id, @Param("user_id") String user_id);
    int countLikes(@Param("board_id") int board_id);
    int getLikeCount(Long board_id);
    void deleteLikesByBoardId(int board_id);

    // 이미지
    void insertBoardImages(@Param("list") List<BoardImageDTO> imageList);
    List<BoardImageDTO> selectImageList(@Param("board_id") int board_id);
    void deleteImagesByBoardId(@Param("board_id") int board_id);

    // 댓글 삭제
    void deleteCommentsByBoardId(int board_id);
    
    BoardDetailDTO selectBoardDetail(int board_id);
    
 // 신고 등록
    int insertBoardReport(BoardReportDTO report);

    // 특정 게시글에 특정 유저가 이미 신고했는지 확인 (중복신고 방지용)
    int countReportsByBoardAndUser(@Param("board_id") int boardId, @Param("report_user") String report_user);

    // 특정 게시글 신고 횟수 조회
    int countReportsByBoard(int board_id);
}