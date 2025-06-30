package com.ini.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardLikeDTO;
import com.ini.board.vo.BoardReplyDTO;
import com.ini.board.vo.BoardWithLikeReplyDTO;
import com.ini.member.mapper.MemberMapper;
import com.ini.member.vo.MemberAuthenticationDTO;
import com.ini.member.vo.MemberDTO;
import com.ini.member.vo.MemberFavoriteDTO;
import com.ini.member.vo.MemberFollowDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;

	public List<MemberDTO> findAllMember() {

		return memberMapper.findAllMember();

	}

	public List<MemberFollowDTO> findFollowById(String user_id) {

		return memberMapper.findFollowById(user_id);

	}

	public MemberDTO findMemberById(String user_id) {

		return memberMapper.findMemberById(user_id);

	}

	public List<BoardDTO> findBoardById(String user_id) {

		return memberMapper.findBoardById(user_id);

	}

	public List<BoardLikeDTO> findLikeById(String user_id) {

		return memberMapper.findLikeById(user_id);

	}

	public List<BoardReplyDTO> findReplyById(String user_id) {

		return memberMapper.findReplyById(user_id);

	}

	public List<MemberFavoriteDTO> findFavoriteById(String user_id) {

		return memberMapper.findFavoriteById(user_id);

	}

	public List<BoardWithLikeReplyDTO> findBoardLikeReplyById(String user_id) {

		return memberMapper.findBoardLikeReplyById(user_id);

	}

	public List<BoardWithLikeReplyDTO> findBoardLikeReplyByLikeId(String user_id) {

		return memberMapper.findBoardLikeReplyByLikeId(user_id);

	}

	public List<BoardWithLikeReplyDTO> findBoardLikeReplyByReplyId(String user_id) {

		return memberMapper.findBoardLikeReplyByReplyId(user_id);

	}

	public List<BoardWithLikeReplyDTO> findBoardLikeReplyByFavoriteId(String user_id) {

		return memberMapper.findBoardLikeReplyByFavoriteId(user_id);

	}

	public boolean registerMember(MemberDTO user) {
		if (memberMapper.findByEmail(user.getUser_email()) != null) {
			return false;
		}
		if (memberMapper.findByNickname(user.getUser_nickname()) != null) {
			return false;
		}
		int result = memberMapper.insertMember(user);
		return result > 0;
	}

	public MemberDTO findByEmail(String email) {
		return memberMapper.findByEmail(email);
	}

	public MemberDTO findByNickname(String nickname) {
		return memberMapper.findByNickname(nickname);
	}

	public void updateProfileImage(String userId, String profileImage) {
		
		memberMapper.updateProfileImage(userId, profileImage);
	}

	public void updateMemberIntro(MemberDTO dto) {
		System.out.println("➡️ 자기소개 업데이트 시도: user_id = " + dto.getUser_id() + ", user_intro = " + dto.getUser_intro());
		memberMapper.updateUserIntro(dto);
	}
	
	public void updateMemberProfile(MemberDTO memberDTO) {
		
		memberMapper.updateMemberProfile(memberDTO);
		
	}
	
	public MemberAuthenticationDTO findUserAuthByUserId(String user_id) {
		
		return memberMapper.findUserAuthByUserId(user_id);
		
	}
	
	public void insertIntoUserAuthentication(String user_id) {
		
		memberMapper.insertIntoUserAuthentication(user_id);
		
	}

}
