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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public MemberDTO getCurrentMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null; // 로그인 안 된 상태면 null 반환
        }
        String userId = auth.getName(); // 로그인한 사용자 아이디 가져오기
        return membermapper.findMemberById(userId);
    }

    public void updateProfileImage(String userId, String profileImage) {
        membermapper.updateProfileImage(userId, profileImage);
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
}