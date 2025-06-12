package com.ini.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardLikeDTO;
import com.ini.board.vo.BoardReplyDTO;
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

    private final MemberMapper memberMapper;

    public MemberDTO findByEmail(String email) {
        return memberMapper.findByEmail(email);
    }

    public int insertOAuthUser(MemberDTO memberDTO) {
        return memberMapper.insertOAuthUser(memberDTO);
    }

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

}

