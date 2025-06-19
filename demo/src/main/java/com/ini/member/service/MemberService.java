package com.ini.member.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.ini.member.mapper.MemberMapper;
import com.ini.member.vo.MemberDTO;
import com.ini.member.vo.MemberFollowDTO;
import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardLikeDTO;
import com.ini.board.vo.BoardReplyDTO;
import com.ini.member.vo.MemberFavoriteDTO;

@Service
public class MemberService {

    private final MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public MemberDTO findMemberById(String userId) {
        return memberMapper.findMemberById(userId);
    }

    // 기존 메소드: findBoardById, findLikeById 등 유지
    public List<BoardDTO> findBoardById(String userId) {
        return memberMapper.findBoardById(userId);
    }
    public List<BoardLikeDTO> findLikeById(String userId) {
        return memberMapper.findLikeById(userId);
    }
    public List<BoardReplyDTO> findReplyById(String userId) {
        return memberMapper.findReplyById(userId);
    }
    public List<MemberFollowDTO> findFollowById(String userId) {
        return memberMapper.findFollowById(userId);
    }
    public List<MemberFavoriteDTO> findFavoriteById(String userId) {
        return memberMapper.findFavoriteById(userId);
    }

    // **alias 메소드 추가**: 컨트롤러 호출명에 맞춤
    public List<BoardDTO> findBoardsByUserId(String userId) {
        return findBoardById(userId);
    }
    public List<BoardLikeDTO> findLikesByUserId(String userId) {
        return findLikeById(userId);
    }
    public List<BoardReplyDTO> findRepliesByUserId(String userId) {
        return findReplyById(userId);
    }
    public List<MemberFollowDTO> findFollowsByUserId(String userId) {
        return findFollowById(userId);
    }
    public List<MemberFavoriteDTO> findFavoritesByUserId(String userId) {
        return findFavoriteById(userId);
    }

    public void updateProfileImage(String userId, String profileImage) {
        memberMapper.updateProfileImage(userId, profileImage);
    }
}