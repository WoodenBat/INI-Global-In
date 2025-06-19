package com.ini.board.controller;

import com.ini.board.mapper.BoardMapper;
import com.ini.board.service.BoardService;
import com.ini.board.vo.*;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private final BoardService boardService;
    private final BoardMapper boardMapper;
    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
    
    @Value("${file.upload-path}")
    private String uploadPath;

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
                             @RequestParam("board_content") String boardContent,
                             @RequestParam("board_category") String boardCategory,
                             @RequestParam("board_tag") String boardTag,
                             @RequestParam(value = "uploadFiles", required = false) MultipartFile[] imageFiles,
                             HttpSession session) throws IOException {

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

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
                    imageDTO.setBoardId(dto.getBoard_id());
                    // DB에는 파일명만 저장 (상대경로 X)
                    imageDTO.setImagePath(fileName);
                    imageDTO.setOriginalName(imageFile.getOriginalFilename());
                    imageDTO.setUploadDate(new Date());

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
    public String postList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "tag", required = false) String tag,
            Model model) {

        int pageSize = 10;

        // 검색, 카테고리, 태그 조건 포함해서 전체 게시글 수 조회
        int totalCount = boardService.getPostCount(keyword, category, tag);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        if (page > totalPages) page = totalPages > 0 ? totalPages : 1; // 페이지 범위 보정
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

        if (board == null) return "redirect:/board/list";

        // 이미지 목록 조회
        List<BoardImageDTO> images = boardService.getBoardImages(boardId);
        logger.info("▶ 이미지 개수: {}", images.size());  // 이거 반드시 추가
        for (BoardImageDTO img : images) {
            logger.info("▶ imagePath: {}", img.getImagePath());
        }

        // 로그인 사용자가 없을 경우 'test'로 가정
        String userId = "test";
        Object loginUser = session.getAttribute("loginUser");
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
        model.addAttribute("post", boardService.getPostById(id));
        model.addAttribute("tags", boardMapper.selectAllBoardTags());
        model.addAttribute("categories", boardMapper.selectAllBoardCategories());
        return "board/edit";
    }

    // 수정 처리
    @PostMapping("/edit")
    public String updatePost(@RequestParam("board_id") Long boardId,
                             @RequestParam("board_title") String boardTitle,
                             @RequestParam("board_content") String boardContent,
                             @RequestParam("board_category") String boardCategory,
                             @RequestParam("board_tag") String boardTag,
                             @RequestParam(value = "uploadFile", required = false) MultipartFile file,
                             HttpSession session) throws IOException {

        BoardDTO dto = new BoardDTO();
        dto.setBoard_id(boardId.intValue());
        dto.setBoard_title(boardTitle);
        dto.setBoard_category(boardCategory);
        dto.setBoard_tag(boardTag);
        dto.setBoard_content(boardContent);
        dto.setBoard_update_date(new Date());
        dto.setUser_id((String) session.getAttribute("loginUser"));
        
        // 이미지 업로드가 있을 경우 추가 저장
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destFile = new File(uploadPath, fileName);
            destFile.getParentFile().mkdirs();
            file.transferTo(destFile);

            BoardImageDTO image = new BoardImageDTO();
            image.setBoardId(dto.getBoard_id());
            image.setImagePath(fileName);
            image.setOriginalName(file.getOriginalFilename());
            image.setUploadDate(new Date());

            dto.setImageList(List.of(image));  // 이미지 리스트 세팅
        } else {
            dto.setImageList(Collections.emptyList());
        }

        boardService.updatePost(dto);
        return "redirect:/board/view/" + boardId;
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
        String userId = (String) session.getAttribute("loginUser");
        if (userId == null) {
            userId = "test";
            session.setAttribute("loginUser", userId);
        }

        int likeCount = boardService.toggleLike(boardId, userId);
        boolean liked = boardService.hasUserLiked(boardId, userId);

        return ResponseEntity.ok().body(Map.of(
            "likeCount", likeCount,
            "liked", liked
        ));
    }
}
