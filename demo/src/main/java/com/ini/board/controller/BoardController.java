package com.ini.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ini.board.service.BoardCategoryService;
import com.ini.board.service.BoardReplyService;
import com.ini.board.service.BoardService;
import com.ini.board.service.BoardTagService;
import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardReplyDTO;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {

	private final BoardService boardService;
	private final BoardCategoryService boardCategoryService;	
	private final BoardTagService boardTagService;
	private final BoardReplyService boardReplyService;
	
	@GetMapping("test")
	public String test() {
		
		return "test";
	}
	
	@GetMapping("/list")
	public String boardList(@RequestParam(defaultValue = "1") int page,
	                        @RequestParam(required = false) String keyword,
	                        @RequestParam(required = false) String category,
	                        Model model) {

	    int pageSize = 10;
	    int totalCount = boardService.getBoardCount(keyword, category);
	    int totalPage = (int) Math.ceil((double) totalCount / pageSize);

	    List<BoardDTO> list = boardService.getBoardList(keyword, category, page, pageSize);

	    model.addAttribute("boardList", list);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPage", totalPage);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("category", category);

	    return "boardList";
	}
	
	//게시글 작성 페이지 조회
		@GetMapping("/write")
		public String writeBoard(Model model) {
			BoardDTO board = new BoardDTO();
			
			
			board.setUser_id("testUser");  //하드코딩
			model.addAttribute("board",board);
			
			model.addAttribute("categoryList", boardCategoryService.getCategories());
			model.addAttribute("tagList", boardTagService.getTags());
			System.out.println("카테고리 갯수: " + boardCategoryService.getCategories().size());
			
			return "board/BoardWrite";
		}
		
		//게시글 작성 제출
		@PostMapping("/write")
		public String writeSubmit(@ModelAttribute BoardDTO board) {
			int boardId = boardService.saveBoard(board);
			return "redirect:/board/view/" + boardId;
		}
		//게시글 작성 후 작성한 글로 리다이렉트
		@GetMapping("/view/{board_id}")
		public String viewBoard(@PathVariable("board_id") int board_id,Model model) {
			
			BoardDTO board = boardService.getBoardById(board_id);
			BoardReplyDTO reply = new BoardReplyDTO();
			List<BoardReplyDTO> replies = boardReplyService.getRepliesByBoardId(board_id);
			reply.setBoard_id(board_id);
			reply.setReply_writer("tester");  //유저닉네임 가져오기 지금은 하드코딩
			model.addAttribute("board",board);
			model.addAttribute("reply",reply);
			model.addAttribute("replies",replies);
			return "board/BoardView";
		}
		
		@PostMapping("/reply/write")
		public String writeReply(@ModelAttribute BoardReplyDTO reply) {
			
			boardReplyService.insertReply(reply);
			
			return "redirect:/board/view/" + reply.getBoard_id();
		}
		
}
