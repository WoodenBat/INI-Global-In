package com.ini.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ini.board.mapper.BoardMapper;
import com.ini.board.service.BoardService;
import com.ini.board.vo.BoardCategoryVO;
import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardDetailDTO;
import com.ini.board.vo.BoardImageDTO;
import com.ini.board.vo.BoardListDTO;
import com.ini.board.vo.BoardReportDTO;
import com.ini.board.vo.BoardTagVO;

import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board/*")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;
	private final BoardMapper boardMapper;
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

	private String uploadPath = "C:/upload/board";

//	@GetMapping("/list")
//	public String boardList(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String keyword,
//			@RequestParam(required = false) String category, Model model) {
//
//		int pageSize = 10;
//		int totalCount = boardService.getBoardCount(keyword, category);
//		int totalPage = (int) Math.ceil((double) totalCount / pageSize);
//
//		List<BoardDTO> list = boardService.getBoardList(keyword, category, page, pageSize);
//
//		model.addAttribute("boardList", list);
//		model.addAttribute("currentPage", page);
//		model.addAttribute("totalPage", totalPage);
//		model.addAttribute("keyword", keyword);
//		model.addAttribute("category", category);
//
//		return "boardList";
//	}
//
//	// 게시글 작성 페이지 조회
//	@GetMapping("/write")
//	public String writeBoard(Model model) {
//		BoardDTO board = new BoardDTO();
//
//		board.setUser_id("testUser"); // 하드코딩
//		model.addAttribute("board", board);
//
//		model.addAttribute("categoryList", boardCategoryService.getCategories());
//		model.addAttribute("tagList", boardTagService.getTags());
//		System.out.println("카테고리 갯수: " + boardCategoryService.getCategories().size());
//
//		return "board/BoardWrite";
//	}
//
//	// 게시글 작성 제출
//	@PostMapping("/write")
//	public String writeSubmit(@ModelAttribute BoardDTO board) {
//		int board_id = boardService.saveBoard(board);
//		return "redirect:/board/view/" + board_id;
//	}
//	
//	
//
//	// 게시글 작성 후 작성한 글로 리다이렉트
//	@GetMapping("/view/{board_id}")
//	public String viewBoard(@PathVariable("board_id") int board_id, Model model) {
//
//		BoardDTO board = boardService.getBoardById(board_id);
//		BoardReplyDTO reply = new BoardReplyDTO();
//		List<BoardReplyDTO> replies = boardReplyService.getRepliesByboard_id(board_id);
//		reply.setBoard_id(board_id);
//		reply.setReply_writer("tester"); // 유저닉네임 가져오기 지금은 하드코딩
//		model.addAttribute("board", board);
//		model.addAttribute("reply", reply);
//		model.addAttribute("replies", replies);
//		return "board/BoardView";
//	}
//
//	@PostMapping("/reply/write")
//	public String writeReply(@ModelAttribute BoardReplyDTO reply) {
//
//		boardReplyService.insertReply(reply);
//
//		return "redirect:/board/view/" + reply.getBoard_id();
//	}

	// 글 작성 폼
	@GetMapping("/write")
	public String showWriteForm(Model model) {
		model.addAttribute("tags", boardMapper.selectAllBoardTags());
		model.addAttribute("categories", boardMapper.selectAllBoardCategories());
		model.addAttribute("boardDTO", new BoardDTO());
		return "board/write";
	}

	// 글 작성 처리
	@PostMapping("/write")
	public String submitPost(@RequestParam("board_title") String boardTitle,
			@RequestParam("board_content") String boardContent, @RequestParam("board_category") String boardCategory,
			@RequestParam("board_tag") String boardTag,
			@RequestParam(value = "uploadFiles", required = false) MultipartFile[] imageFiles, HttpSession session)
			throws IOException {

		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists())
			uploadDir.mkdirs();

		BoardDTO dto = new BoardDTO();
		dto.setBoard_title(boardTitle);
		dto.setBoard_content(boardContent);
		dto.setBoard_category(boardCategory);
		dto.setBoard_tag(boardTag);
		dto.setBoard_views(0);
		dto.setBoard_write_date(new Date());
		dto.setBoard_update_date(new Date());
		
		String userId = (String) session.getAttribute("loginUser");
		dto.setUser_id(userId != null ? userId : String.valueOf(session.getAttribute("user_id")));

		// 게시글 저장 (board_id 생성됨)
		boardService.savePost(dto);

		// 이미지가 존재하면 board_image 테이블에도 저장
		if (imageFiles != null && imageFiles.length > 0) {
			List<BoardImageDTO> imageList = new ArrayList<>();

			for (MultipartFile imageFile : imageFiles) {
				if (!imageFile.isEmpty()) {
					// UUID + 원본파일명 형태로 저장 (중복방지)
					String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
					File destFile = new File(uploadPath, fileName);
					imageFile.transferTo(destFile);

					BoardImageDTO imageDTO = new BoardImageDTO();
					imageDTO.setBoard_id(dto.getBoard_id());
					// DB에는 파일명만 저장 (상대경로 X)
					imageDTO.setImage_path(fileName);
					imageDTO.setOriginal_name(imageFile.getOriginalFilename());
					imageDTO.setUpload_date(new Date());

					imageList.add(imageDTO);
				}
			}

			boardService.saveImages(imageList);
		}

		return "redirect:/board/list";
	}

	// 게시글 목록
	// BoardController.java 게시판 목록 부분 수정
	@GetMapping("/list")
	public String postList(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "tag", required = false) String tag, Model model) {

		int pageSize = 10;

		// 검색, 카테고리, 태그 조건 포함해서 전체 게시글 수 조회
		int totalCount = boardService.getPostCount(keyword, category, tag);
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);

		if (page > totalPages)
			page = totalPages > 0 ? totalPages : 1; // 페이지 범위 보정
		int offset = (page - 1) * pageSize;

		// 조건 반영해서 게시글 리스트 조회
		List<BoardListDTO> posts = boardService.getPostList(keyword, category, tag, offset, pageSize);

		model.addAttribute("postList", posts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("keyword", keyword);
		model.addAttribute("category", category);
		model.addAttribute("tag", tag);

		// 말머리, 카테고리 목록도 같이 넘겨서 필터 UI 구현 가능하도록 함
		model.addAttribute("tags", boardService.getAllTags());
		model.addAttribute("categories", boardService.getAllCategories());

		return "board/list";
	}

	// 게시글 상세 보기 (이미지 포함)
	@GetMapping("/view/{id}")
	public String viewBoard(@PathVariable("id") int boardId, Model model, HttpSession session) {
		boardService.incrementViews(boardId);
		BoardDetailDTO board = boardService.getPostById(boardId);

		if (board == null)
			return "redirect:/board/list";

		// 이미지 목록 조회
		List<BoardImageDTO> images = boardService.getBoardImages(boardId);
		logger.info("▶ 이미지 개수: {}", images.size()); // 이거 반드시 추가
		for (BoardImageDTO img : images) {
			logger.info("▶ imagePath: {}", img.getImage_path());
		}

		// 로그인 사용자가 없을 경우 'test'로 가정
		String userId = "test";
		Object loginUser = session.getAttribute("user_id");
		if (loginUser != null && loginUser instanceof String) {
			userId = (String) loginUser;
		}

		boolean liked = boardService.hasUserLiked(boardId, userId);
		int likeCount = boardService.getLikeCount(boardId);

		model.addAttribute("board", board);
		model.addAttribute("liked", liked);
		model.addAttribute("likeCount", likeCount);
		model.addAttribute("imageList", images);
		return "board/view";
	}

	// 수정 폼
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") int id, Model model) {
		BoardDetailDTO post = boardService.getPostById(id);
		if (post == null) {
			return "redirect:/board/list";
		}
		model.addAttribute("post", post);

		List<BoardTagVO> tags = boardMapper.selectAllBoardTags();
		model.addAttribute("tags", tags);

		List<BoardCategoryVO> categories = boardMapper.selectAllBoardCategories();
		model.addAttribute("categories", categories);

		return "board/edit";
	}

	// 수정 처리
	@PostMapping("/edit")
	public String updatePost(@RequestParam("board_id") Long board_id, @RequestParam("board_title") String board_title,
			@RequestParam("board_content") String board_content, @RequestParam("board_category") String board_category,
			@RequestParam("board_tag") String board_tag,
			@RequestParam(value = "upload_files", required = false) MultipartFile[] files, HttpSession session)
			throws IOException {

		BoardDTO dto = new BoardDTO();
		dto.setBoard_id(board_id.intValue());
		dto.setBoard_title(board_title);
		dto.setBoard_category(board_category);
		dto.setBoard_tag(board_tag);
		dto.setBoard_content(board_content);
		dto.setBoard_update_date(new Date());
		dto.setUser_id((String) session.getAttribute("loginUser"));

		List<BoardImageDTO> imageList = new ArrayList<>();
		// 이미지 업로드가 있을 경우 추가 저장
		if (files != null && files.length > 0) {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
					File destFile = new File(uploadPath, fileName);
					destFile.getParentFile().mkdirs();
					file.transferTo(destFile);

					BoardImageDTO image = new BoardImageDTO();
					image.setBoard_id(dto.getBoard_id());
					image.setImage_path(fileName);
					image.setOriginal_name(file.getOriginalFilename());
					image.setUpload_date(new Date());

					imageList.add(image);
				}
			}
		}

		dto.setImageList(imageList);

		boardService.updatePost(dto);

		return "redirect:/board/view/" + board_id;
	}

	// 삭제
	@PostMapping("/delete/{id}")
	public String deletePost(@PathVariable("id") int boardId, HttpSession session) {
		String userId = (String) session.getAttribute("loginUser");
		BoardDetailDTO post = boardService.getBoardById(boardId, userId);
		if (post == null || (userId != null && !userId.equals(post.getUser_id()))) {
			return "redirect:/board/list";
		}

		boardService.deletePost(boardId);
		return "redirect:/board/list";
	}

	// 좋아요 토글
	@PostMapping("/like/{boardId}")
	@ResponseBody
	public ResponseEntity<?> toggleLike(@PathVariable("boardId") int boardId, HttpSession session) {
		String userId = (String) session.getAttribute("user_id");
		if (userId == null) {
			userId = "test";
			session.setAttribute("loginUser", userId);
		}

		int likeCount = boardService.toggleLike(boardId, userId);
		boolean liked = boardService.hasUserLiked(boardId, userId);

		return ResponseEntity.ok().body(Map.of("likeCount", likeCount, "liked", liked));
	}

	@PostMapping("/report")
	public ResponseEntity<?> reportPost(@RequestBody BoardReportDTO report, HttpSession session) {
		String userId = (String) session.getAttribute("loginUser");
		if (userId == null) {
			userId = "test"; // 비로그인 시 기본값 처리
		}
		report.setReport_user(userId);

		boolean success = boardService.addReport(report);
		if (success) {
			return ResponseEntity.ok().body("신고가 접수되었습니다.");
		} else {
			return ResponseEntity.badRequest().body("이미 신고한 게시글입니다.");
		}
	}

}
