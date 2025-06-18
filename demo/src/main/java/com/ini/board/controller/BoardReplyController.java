package com.ini.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ini.board.service.BoardReplyService;
import com.ini.board.vo.BoardReplyDTO;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("board/reply/*")
public class BoardReplyController {

	private BoardReplyService boardReplyService;

	@GetMapping("boardReply")
	@ResponseBody
	public List<BoardReplyDTO> boardReplyList(@RequestParam("board_id") String board_id) {

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
		
		
	}

}
