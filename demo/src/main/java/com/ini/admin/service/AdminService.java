package com.ini.admin.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ini.admin.mapper.AdminUserMapper;
import com.ini.admin.mapper.AdminReportMapper;
import com.ini.board.mapper.BoardMapper;
import com.ini.admin.vo.AdminUserDTO;
import com.ini.admin.vo.AdminReportDTO;
import com.ini.board.vo.BoardDTO;
import com.ini.member.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final MemberMapper memberMapper;

	private final AdminUserMapper userMapper;
	private final AdminReportMapper reportMapper;
	private final BoardMapper boardMapper;

	public int countUsers() {
		return userMapper.countUsers();
	}

	public int countReports() {
		return reportMapper.countReports();
	}

	// ── 회원 관리 ───────────────────────

	// 전체 회원 조회
	public List<AdminUserDTO> getAllUsers() {
		return userMapper.selectAllUsers();
	}

	// 회원 삭제
	public void deleteUserById(String user_id) {
		boardMapper.deleteBoardByUserId(user_id); // ① 유저 글 먼저 삭제
		userMapper.deleteUserById(user_id); // ② 유저 정보 삭제
	}

	// ── 신고 관리 ───────────────────────

	// 신고 게시글 리스트 조회
	public List<AdminReportDTO> getAllReports() {
		return boardMapper.selectReportedBoards(); // ✅ 조인 결과 반환
	}

	// 게시글 삭제 + 해당 게시글 관련 신고도 함께 삭제
	public void deleteReportedBoardById(String board_id) {
		reportMapper.deleteReportByBoardId(board_id); // 1. 신고 먼저 삭제
		boardMapper.deleteBoardById(board_id); // 2. 그 다음 게시글 삭제
	}

	@Transactional
	public void reportUser(String user_id) {
		memberMapper.increaseReportCount(user_id);

		int count = memberMapper.getReportCount(user_id);
		if (count >= 5) {
			memberMapper.banUser(user_id);
		}

	}

	// ── 신고당한 유저 조회 ───────────────────────
	public List<AdminUserDTO> getReportedUsers() {
		return memberMapper.selectReportedUsers();
	}

}