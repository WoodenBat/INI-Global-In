package com.ini.board.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ini.board.service.BoardReplyService;
import com.ini.board.vo.BoardReplyDTO;
import com.ini.board.vo.BoardReplyReportDTO;
import com.ini.board.vo.BoardReplyWithUserDTO;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("board/reply/*")
public class BoardReplyController {

	private BoardReplyService boardReplyService;

	@GetMapping("boardReply")
	@ResponseBody
	public List<BoardReplyWithUserDTO> boardReplyList(@RequestParam("board_id") String board_id) {

		return boardReplyService.findBoardReplyByBoardId(board_id);
	}

	@GetMapping("boardReplyTest")
	public String boardReplyTest() {

		return "board/boardReplyTest";
	}

	@PostMapping("boardReplyInsert")
	@ResponseBody
	public void boardReplyInsert(@RequestParam("board_id") String board_id,
			@RequestParam("reply_content") String reply_content, @RequestParam("reply_writer") String reply_writer) {

		BoardReplyDTO boardReplyDTO = new BoardReplyDTO();
		boardReplyDTO.setReply_writer(reply_writer);
		boardReplyDTO.setReply_content(reply_content);
		boardReplyDTO.setBoard_id(Integer.parseInt(board_id));

		boardReplyService.insertBoardReplyByBoardId(boardReplyDTO);

	}

	@PostMapping("boardRereplyInsert")
	@ResponseBody
	public void boardRereplyInsert(@RequestParam("board_id") String board_id,
			@RequestParam("reply_content") String reply_content, @RequestParam("reply_writer") String reply_writer,
			@RequestParam("reply_id") String reply_id) {

		BoardReplyDTO boardReplyDTO = new BoardReplyDTO();

		boardReplyDTO.setReply_content(reply_content);
		boardReplyDTO.setReply_writer(reply_writer);
		boardReplyDTO.setBoard_id(Integer.parseInt(board_id));
		boardReplyDTO.setReply_id(Integer.parseInt(reply_id));

		boardReplyService.insertBoardReReplyByBoardId(boardReplyDTO);
	}

	@PostMapping("boardReplyDelete")
	@ResponseBody
	public void deleteBoardReply(@RequestParam("reply_id") String reply_id,
			@RequestParam("reply_status") String reply_status) {

		BoardReplyDTO boardReplyDTO = new BoardReplyDTO();

		boardReplyDTO.setReply_id(Integer.parseInt(reply_id));
		boardReplyDTO.setReply_status(reply_status);
		
		boardReplyService.deleteBoardReplyByReplyId(boardReplyDTO);

	}

	@PostMapping("boardReplyUpdate")
	@ResponseBody
	public void updateBoardReply(@RequestParam("board_id") String board_id,
			@RequestParam("reply_content") String reply_content, @RequestParam("reply_id") String reply_id,
			@RequestParam("reply_status") String reply_status) {

		BoardReplyDTO boardReplyDTO = new BoardReplyDTO();

		boardReplyDTO.setReply_content(reply_content);
		boardReplyDTO.setReply_id(Integer.parseInt(reply_id));
		boardReplyDTO.setReply_status(reply_status);

		boardReplyService.updateBoardReplyByReplyId(boardReplyDTO);
	}
	
	@PostMapping("/report")
	public ResponseEntity<?> reportPostReply(@RequestBody BoardReplyReportDTO report, HttpSession session) {
		String user_id = (String) session.getAttribute("loginUser");
		if (user_id == null) {
			user_id = "test"; // 비로그인 시 기본값 처리
		}
		report.setReport_user(user_id);

		boolean success = boardReplyService.insertBoardReplyReport(report);
		if (success) {
			return ResponseEntity.ok().body("신고가 접수되었습니다.");
		} else {
			return ResponseEntity.badRequest().body("이미 신고한 게시글입니다.");
		}
	}
	
}
