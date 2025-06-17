package com.ini.board.controller;

import com.ini.board.mapper.BoardMapper;
import com.ini.board.service.BoardService;
import com.ini.board.vo.*;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

        String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
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
                    String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                    File destFile = new File(uploadPath + fileName);
                    imageFile.transferTo(destFile);

                    BoardImageDTO imageDTO = new BoardImageDTO();
                    imageDTO.setBoard_id(dto.getBoard_id());
                    imageDTO.setImage_path("/uploads/" + fileName);
                    imageDTO.setOriginal_name(imageFile.getOriginalFilename());
                    imageDTO.setUpload_date(new Date());

                    imageList.add(imageDTO);
                }
            }

            // 이미지 정보 저장
            boardService.saveImages(imageList);
        }

        return "redirect:/board/list";
    }

    // 게시글 목록
    @GetMapping("/list")
    public String postList(Model model) {
        model.addAttribute("postList", boardService.getPostList());
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
        board.setImageList(images); // DTO에 포함

     // 로그인 사용자가 없을 경우 'test'로 가정
        String userId = "test";
        if (session.getAttribute("loginUser") != null) {
            userId = ((BoardDTO) session.getAttribute("loginUser")).getUser_id();
        }
        
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
            String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
            File destFile = new File(uploadPath + fileName);
            destFile.getParentFile().mkdirs();
            file.transferTo(destFile);

            BoardImageDTO image = new BoardImageDTO();
            image.setBoard_id(dto.getBoard_id());
            image.setImage_path("/uploads/" + fileName);
            image.setOriginal_name(file.getOriginalFilename());
            image.setUpload_date(new Date());

            boardService.saveImages(List.of(image));
        }

        boardService.updatePost(dto);
        return "redirect:/board/view/" + boardId;
    }

    // 삭제
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") int boardId, HttpSession session) {
        String userId = (String) session.getAttribute("loginUser");
        BoardDTO post = boardService.getBoardById(boardId);
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

    // 좋아요 상태 포함한 상세 보기
    @GetMapping("/view-with-like/{id}")
    public String viewWithLike(@PathVariable Long id, Model model, HttpSession session) {
        int boardId = id.intValue();
        BoardDTO board = boardService.getBoardById(boardId);
        if (board == null) return "redirect:/board/list";

        String userId = (String) session.getAttribute("loginUser");
        if (userId == null) {
            userId = "test";
            session.setAttribute("loginUser", userId);
        }

        boolean liked = boardService.hasUserLiked(boardId, userId);
        int likeCount = boardService.getLikeCount(boardId);
        List<BoardImageDTO> imageList = boardService.getBoardImages(boardId);

        model.addAttribute("board", board);
        model.addAttribute("liked", liked);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("imageList", imageList);
        return "board/view";
    }
}
