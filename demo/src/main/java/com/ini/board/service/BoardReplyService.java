package com.ini.board.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ini.board.mapper.BoardReplyMapper;
import com.ini.board.vo.BoardReplyDTO;
import com.ini.board.vo.BoardReplyWithUserDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardReplyService {

	private BoardReplyMapper boardReplyMapper;

	public List<BoardReplyWithUserDTO> findBoardReplyByBoardId(String board_id) {

		List<BoardReplyWithUserDTO> boardReplyWithUserDTO = boardReplyMapper.findBoardReplyByBoardId(board_id);

		for (BoardReplyWithUserDTO dto : boardReplyWithUserDTO) {
			dto.setReply_date_differ(getReplyTime(dto.getReply_write_date()));
		}

		return boardReplyWithUserDTO;

	}

	public void insertBoardReplyByBoardId(BoardReplyDTO boardReplyDTO) {

		String reply_status = "reply";
		boardReplyDTO.setReply_status(reply_status);

		boardReplyMapper.insertBoardReplyByBoardId(boardReplyDTO);

	}

	public void insertBoardReReplyByBoardId(BoardReplyDTO boardReplyDTO) {

		List<BoardReplyDTO> boardReplyDTOList = findBoardReplyBtReplyId(String.valueOf(boardReplyDTO.getReply_id()));

		if (boardReplyDTOList.size() == 1) {
			boardReplyDTO.setReply_status("rereply1");
			boardReplyMapper.insertBoardReReplyByBoardId(boardReplyDTO);

		} else {
			int lastReReply = Integer.parseInt(
					boardReplyDTOList.get(boardReplyDTOList.size() - 1).getReply_status().replace("rereply", "")) + 1;
			boardReplyDTO.setReply_status("rereply" + String.valueOf(lastReReply));
			boardReplyMapper.insertBoardReReplyByBoardId(boardReplyDTO);
		}

	}

	public void deleteBoardReplyByReplyId(BoardReplyDTO boardReplyDTO) {

		if (boardReplyDTO.getReply_status().equals("reply")) {
			boardReplyMapper.deleteBoardReplyByReplyId(boardReplyDTO);

		} else {

			boardReplyMapper.deleteBoardRereplyByReplyId(boardReplyDTO);
		}

	}

	public List<BoardReplyDTO> findBoardReplyBtReplyId(String reply_id) {

		return boardReplyMapper.findBoardReplyBtReplyId(reply_id);

	}

	public void updateBoardReplyByReplyId(BoardReplyDTO boardReplyDTO) {

		boardReplyMapper.updateBoardReplyByReplyId(boardReplyDTO);

	}

	public Long getReplyTime(Date reply_date) {

		LocalDateTime currentTime = LocalDateTime.now();

		Long differTime = ChronoUnit.MINUTES
				.between(reply_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), currentTime);

		return differTime;

	}
}
