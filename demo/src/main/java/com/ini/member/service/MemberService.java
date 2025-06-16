package com.ini.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardLikeDTO;
import com.ini.board.vo.BoardReplyDTO;
import com.ini.board.vo.BoardWithLikeReplyDTO;
import com.ini.member.mapper.MemberMapper;
import com.ini.member.vo.MemberDTO;
import com.ini.member.vo.MemberFavoriteDTO;
import com.ini.member.vo.MemberFollowDTO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor
public class MemberService {

	private final MemberMapper membermapper;

	public MemberDTO findAllMember() {

		return membermapper.findAllMember();

	}

	public List<MemberFollowDTO> findFollowById(String user_id) {

		return membermapper.findFollowById(user_id);

	}

	public MemberDTO findMemberById(String user_id) {

		return membermapper.findMemberById(user_id);

	}

	public List<BoardDTO> findBoardById(String user_id) {

		return membermapper.findBoardById(user_id);

	}

	public List<BoardLikeDTO> findLikeById(String user_id) {

		return membermapper.findLikeById(user_id);

	}

	public List<BoardReplyDTO> findReplyById(String user_id) {

		return membermapper.findReplyById(user_id);

	}

	public List<MemberFavoriteDTO> findFavoriteById(String user_id) {

		return membermapper.findFavoriteById(user_id);

	}

	public List<BoardWithLikeReplyDTO> findBoardLikeReplyById(String user_id) {

		return membermapper.findBoardLikeReplyById(user_id);

	}

	public List<BoardWithLikeReplyDTO> findBoardLikeReplyByLikeId(String user_id) {

		return membermapper.findBoardLikeReplyByLikeId(user_id);

	}

	public List<BoardWithLikeReplyDTO> findBoardLikeReplyByReplyId(String user_id) {

		return membermapper.findBoardLikeReplyByLikeId(user_id);

	}

	public List<BoardWithLikeReplyDTO> findBoardLikeReplyByFavoriteId(String user_id) {

		return membermapper.findBoardLikeReplyByLikeId(user_id);

	}

	public boolean registerMember(MemberDTO user) {
		if (membermapper.findByEmail(user.getUser_email()) != null) {
			return false;
		}
		if (membermapper.findByNickname(user.getUser_nickname()) != null) {
			return false;
		}
		int result = membermapper.insertMember(user);
		return result > 0;
	}

	public MemberDTO findByEmail(String email) {
		return membermapper.findByEmail(email);
	}

	public MemberDTO findByNickname(String nickname) {
		return membermapper.findByNickname(nickname);
	}

	public int insertOAuthUser(MemberDTO memberDTO) {
        return memberMapper.insertOAuthUser(memberDTO);
    }
	
}
