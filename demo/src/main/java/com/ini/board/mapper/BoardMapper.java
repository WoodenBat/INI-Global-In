package com.ini.board.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ini.admin.vo.AdminReportDTO;
import com.ini.board.vo.BoardCategoryVO;
import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardDetailDTO;
import com.ini.board.vo.BoardImageDTO;
import com.ini.board.vo.BoardLikeDTO;
import com.ini.board.vo.BoardListDTO;
import com.ini.board.vo.BoardReplyDTO;
import com.ini.board.vo.BoardReportDTO;
import com.ini.board.vo.BoardTagVO;

@Mapper
public interface BoardMapper {

	List<BoardDTO> selectBoardList(@Param("keyword") String keyword, @Param("category") String category,
			@Param("startRow") int startRow, @Param("pageSize") int pageSize);

	int countBoardList(@Param("keyword") String keyword, @Param("category") String category);

	// 한국어용
	List<BoardCategoryVO> getAllBoardCategoriesKr();

	List<BoardTagVO> getAllBoardTagsKr();

	// 일본어용
	List<BoardCategoryVO> getAllBoardCategoriesJp();

	List<BoardTagVO> getAllBoardTagsJp();

	// 작성게시글저장
	void insertBoard(BoardDTO board);

	// ID에맞춰 게시글가져오기
	BoardDTO getBoardById(int board_id);

	// 댓글 가져오기
	List<BoardReplyDTO> getRepliesByBoardId(int board_id);

	// 댓글 저장하기
	void insertReply(BoardReplyDTO reply);

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
	List<BoardListDTO> getBoardList(@Param("keyword") String keyword, @Param("category") String category,
			@Param("tag") String tag, @Param("offset") int offset, @Param("limit") int limit);

	int countAllPosts(); // 전체 게시글 수 조회용

	// 검색 조건별 게시글 수 조회용 (선택)
	int getPostCount(@Param("keyword") String keyword, @Param("category") String category, @Param("tag") String tag);

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

	List<BoardDTO> selectBoardList2(@Param("keyword") String keyword, @Param("category") String category,
			@Param("offset") int offset, @Param("limit") int limit);

	void deleteBoardByUserId(@Param("id") String user_id);

	int countBoards(@Param("keyword") String keyword, @Param("category") String category);

	// 게시글 삭제......admin
	void deleteBoardById(@Param("board_id") String board_id);

	List<AdminReportDTO> selectReportedBoards();

}
