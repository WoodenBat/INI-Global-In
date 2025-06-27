package com.ini.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
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
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board/*")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;
	private final BoardMapper boardMapper;

	private String uploadPath = "C:/upload/board";

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
			@RequestParam("board_tag") String boardTag, HttpSession session) {

		BoardDTO dto = new BoardDTO();
		dto.setBoard_title(boardTitle);
		dto.setBoard_content(boardContent);
		dto.setBoard_category(boardCategory);
		dto.setBoard_tag(boardTag);
		dto.setBoard_views(0);
		dto.setBoard_write_date(new Date());
		dto.setBoard_update_date(new Date());

		String userId = (String) session.getAttribute("loginUser");
		dto.setUser_id(userId != null ? userId : "test");

		boardService.savePost(dto); // board_id 생성됨

		// ✅ summernote 내용에서 <img src="/uploads/xxxx"> 태그 추출
		List<BoardImageDTO> imageList = new ArrayList<>();
		Matcher matcher = Pattern.compile("<img[^>]+src=\"/uploads/([^\"]+)\"").matcher(boardContent);
		while (matcher.find()) {
			String imageFileName = matcher.group(1); // 실제 파일명만 추출
			BoardImageDTO imageDTO = new BoardImageDTO();
			imageDTO.setBoard_id(dto.getBoard_id());
			imageDTO.setImage_path(imageFileName);
			imageDTO.setOriginal_name(imageFileName); // 필요 시 파싱
			imageDTO.setUpload_date(new Date());
			imageList.add(imageDTO);
		}

		if (!imageList.isEmpty()) {
			boardService.saveImages(imageList);
		}

		return "redirect:/board/list";
	}

	// Summernote 이미지 업로드 처리
	@PostMapping("/uploadImage")
	@ResponseBody
	public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			return Map.of("url", "");
		}

		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists())
			uploadDir.mkdirs();

		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		File destFile = new File(uploadDir, fileName);
		file.transferTo(destFile);

		// 클라이언트는 이 URL을 <img src="">로 사용함
		return Map.of("url", "/uploads/board/" + fileName);
	}

	// 게시글 목록
	@GetMapping("/list")
	public String postList(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "tag", required = false) String tag, Model model) {

		int totalCount = boardService.getPostCount(keyword, category, tag);
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);
		if (page > totalPages)
			page = totalPages > 0 ? totalPages : 1;
		int offset = (page - 1) * pageSize;

		List<BoardListDTO> posts = boardService.getPostList(keyword, category, tag, offset, pageSize);

		model.addAttribute("postList", posts);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("keyword", keyword);
		model.addAttribute("category", category);
		model.addAttribute("tag", tag);
		model.addAttribute("tags", boardService.getAllTags());
		model.addAttribute("categories", boardService.getAllCategories());

		return "board/list";
	}

	// 게시글 상세 보기
	@GetMapping("/view/{id}")
	public String viewBoard(@PathVariable("id") int boardId, Model model, HttpSession session) {
		boardService.incrementViews(boardId);
		BoardDetailDTO board = boardService.getPostById(boardId);

		if (board == null)
			return "redirect:/board/list";

		List<BoardImageDTO> images = boardService.getBoardImages(boardId);

		if (board.getBoard_content() != null && !board.getBoard_content().contains("<img")) {
			StringBuilder sb = new StringBuilder(board.getBoard_content());
			for (BoardImageDTO image : images) {
				sb.append("<p><img src=\"/uploads/board/").append(image.getImage_path())
						.append("\" style=\"max-width: 100%\"></p>");
			}
			board.setBoard_content(sb.toString());
		}

		String userId = "test";
		Object loginUser = session.getAttribute("user_id");
		if (loginUser instanceof String)
			userId = (String) loginUser;

		boolean liked = boardService.hasUserLiked(boardId, userId);
		int likeCount = boardService.getLikeCount(boardId);

		model.addAttribute("board", board);
		model.addAttribute("liked", liked);
		model.addAttribute("likeCount", likeCount);

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

	// 게시글 수정 처리
	@PostMapping("/edit")
	public String updatePost(@RequestParam("board_id") Long board_id, @RequestParam("board_title") String board_title,
			@RequestParam("board_content") String board_content, @RequestParam("board_category") String board_category,
			@RequestParam("board_tag") String board_tag, HttpSession session) {

		BoardDTO dto = new BoardDTO();
		dto.setBoard_id(board_id.intValue());
		dto.setBoard_title(board_title);
		dto.setBoard_content(board_content);
		dto.setBoard_category(board_category);
		dto.setBoard_tag(board_tag);
		dto.setBoard_update_date(new Date());

		String userId = (String) session.getAttribute("loginUser");
		dto.setUser_id(userId != null ? userId : "test");

		// ✅ 1. 본문에서 <img src="/uploads/..."> 추출해서 이미지 리스트 구성
		List<BoardImageDTO> imageList = new ArrayList<>();
		Matcher matcher = Pattern.compile("<img[^>]+src=\"/uploads/([^\"]+)\"").matcher(board_content);
		Set<String> addedPaths = new HashSet<>();

		while (matcher.find()) {
			String imageFileName = matcher.group(1);
			if (addedPaths.add(imageFileName)) {
				BoardImageDTO image = new BoardImageDTO();
				image.setBoard_id(dto.getBoard_id());
				image.setImage_path(imageFileName);
				image.setOriginal_name(imageFileName);
				image.setUpload_date(new Date());

				imageList.add(image);
			}
		}

		// ✅ 2. 무조건 세팅 (본문 내 이미지가 없으면 빈 리스트 저장)
		dto.setImageList(imageList);

		// ✅ 3. 저장
		boardService.updatePost(dto);

		return "redirect:/board/view/" + board_id;
	}

	// 게시글 삭제
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

	// 좋아요
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

	// 신고
	@PostMapping("/report")
	@ResponseBody
	public ResponseEntity<String> reportBoard(@RequestBody BoardReportDTO reportDTO, HttpSession session) {

		reportDTO.setReport_user((String) session.getAttribute("user_id"));
		int count = boardMapper.countReportsByBoardAndUser(reportDTO.getBoard_id(), reportDTO.getReport_user());

		if (count > 0) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 신고한 게시글입니다.");
		}

		boardMapper.insertBoardReport(reportDTO);
		return ResponseEntity.ok("신고가 접수되었습니다.");
	}

}
