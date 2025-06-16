package com.ini.board.controller;

import com.ini.board.mapper.BoardMapper;
import com.ini.board.service.BoardService;
import com.ini.board.vo.BoardCategoryVO;
import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardDetailDTO;
import com.ini.board.vo.BoardLikeDTO;
import com.ini.board.vo.BoardTagVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        List<BoardTagVO> tags = boardMapper.selectAllBoardTags();
        List<BoardCategoryVO> categories = boardMapper.selectAllBoardCategories();

        model.addAttribute("tags", tags);
        model.addAttribute("categories", categories);
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

        StringBuilder contentWithImages = new StringBuilder(boardContent);

        // 이미지 파일 처리
        if (imageFiles != null) {
            for (MultipartFile imageFile : imageFiles) {
                if (!imageFile.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                    File destFile = new File(uploadPath + fileName);
                    imageFile.transferTo(destFile);

                    contentWithImages.append("<br><img src='/uploads/")
                                     .append(fileName)
                                     .append("'>");
                }
            }
        }

        BoardDTO dto = new BoardDTO();
        dto.setBoard_title(boardTitle);
        dto.setBoard_content(contentWithImages.toString());
        dto.setBoard_category(boardCategory);
        dto.setBoard_tag(boardTag);
        dto.setBoard_views(0);
        dto.setBoard_write_date(new Date());
        dto.setBoard_update_date(new Date());

        String userId = (String) session.getAttribute("loginUser");
        dto.setUser_id(userId != null ? userId : "test");

        boardService.savePost(dto);
        return "redirect:/board/list";
    }

    // 게시글 목록
    @GetMapping("/list")
    public String postList(Model model) {
        model.addAttribute("postList", boardService.getPostList());
        return "board/list";
    }

    // 게시글 상세 보기
    @GetMapping("/view/{id}")
    public String viewBoard(@PathVariable("id") int boardId, Model model) {
        boardService.incrementViews(boardId);
        BoardDetailDTO board = boardService.getPostById(boardId);

        if (board == null) {
            return "redirect:/board/list"; // 게시글이 없을 경우
        }

        model.addAttribute("board", board);

        // TODO: 게시글 이미지 테이블(board_image)이 생성되면 아래 로직을 활성화할 것
        // List<String> imageList = boardService.getImageList(boardId);
        // model.addAttribute("imageList", imageList);

        return "board/view";
    }

    // 수정 폼 조회
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        BoardDetailDTO post = boardService.getPostById(id);
        List<BoardTagVO> tags = boardMapper.selectAllBoardTags();
        List<BoardCategoryVO> categories = boardMapper.selectAllBoardCategories();

        model.addAttribute("post", post);
        model.addAttribute("tags", tags);
        model.addAttribute("categories", categories);
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
        dto.setBoard_update_date(new Date());
        dto.setUser_id((String) session.getAttribute("loginUser"));

        String content = boardContent;
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
            File destFile = new File(uploadPath + fileName);
            destFile.getParentFile().mkdirs();
            file.transferTo(destFile);
            content += "<br><img src='/uploads/" + fileName + "'>";
        }
        dto.setBoard_content(content);

        boardService.updatePost(dto);
        return "redirect:/board/view/" + boardId;
    }
 // 게시글 삭제
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") int boardId, HttpSession session) {
        String userId = (String) session.getAttribute("loginUser");
        BoardDTO post = boardService.getBoardById(boardId);

        // 권한 체크 (예: 본인이 작성한 글만 삭제 가능)
        if (post == null || (userId != null && !userId.equals(post.getUser_id()))) {
            return "redirect:/board/list";
        }

        boardService.deletePost(boardId);
        return "redirect:/board/list";
    }
    
//    @PostMapping("/like/{boardId}")
//    @ResponseBody
//    public ResponseEntity<?> toggleLike(@PathVariable("boardId") int boardId, HttpSession session) {
//        String userId = (String) session.getAttribute("loginUser");
//        if (userId == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
//        }
//
//        int likeCount = boardService.toggleLike(boardId, userId);
//        boolean liked = boardService.hasUserLiked(boardId, userId);
//
//        return ResponseEntity.ok().body(Map.of(
//            "likeCount", likeCount,
//            "liked", liked
//        ));
//    }
    
    @PostMapping("/like/{boardId}")
    @ResponseBody
    public ResponseEntity<?> toggleLike(@PathVariable("boardId") int boardId, HttpSession session) {
        String userId = (String) session.getAttribute("loginUser");

        // 로그인하지 않은 경우 임시 계정으로 처리
        if (userId == null) {
            userId = "test";  // 임시 계정
            session.setAttribute("loginUser", userId);  // 저장해두면 이후에도 null 방지
        }

        int likeCount = boardService.toggleLike(boardId, userId);
        boolean liked = boardService.hasUserLiked(boardId, userId);

        return ResponseEntity.ok().body(Map.of(
            "likeCount", likeCount,
            "liked", liked
        ));
    }
    
    @GetMapping("/view-with-like/{id}")
    public String viewWithLike(@PathVariable Long id, Model model, HttpSession session) {
        // boardId는 int로 변환
        int boardId = id.intValue();

        // 게시글 조회
        BoardDTO board = boardService.getBoardById(boardId);
        if (board == null) {
            return "redirect:/board/list";
        }

        // 로그인 사용자 확인 (없으면 test 계정)
        String userId = (String) session.getAttribute("loginUser");
        if (userId == null) {
            userId = "test";
            session.setAttribute("loginUser", userId);
        }

        // 좋아요 상태 및 개수 조회
        boolean liked = boardService.hasUserLiked(boardId, userId);
        int likeCount = boardService.getLikeCount(boardId);

        // 모델에 담기
        model.addAttribute("board", board);
        model.addAttribute("liked", liked);
        model.addAttribute("likeCount", likeCount);

        return "board/view";
    }
}
