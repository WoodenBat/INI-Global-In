package com.ini.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ini.board.vo.BoardReplyDTO;
import com.ini.board.vo.BoardReplyReportDTO;
import com.ini.board.vo.BoardReplyWithUserDTO;

@Mapper
public interface BoardReplyMapper {
	
	public List<BoardReplyWithUserDTO> findBoardReplyByBoardId(String board_id);
	
	public void insertBoardReplyByBoardId(BoardReplyDTO boardReplyDTO);
	
	public void insertBoardReReplyByBoardId(BoardReplyDTO boardReplyDTO);
	
	public void deleteBoardReplyByReplyId(BoardReplyDTO boardReplyDTO);
	
	public void deleteBoardRereplyByReplyId(BoardReplyDTO boardReplyDTO);
	
	public List<BoardReplyDTO> findBoardReplyByReplyId(String reply_id);
	
	public void updateBoardReplyByReplyId(BoardReplyDTO boardReplyDTO);
	
	public boolean insertBoardReplyReport(BoardReplyReportDTO boardReplyReportDTO);
}
