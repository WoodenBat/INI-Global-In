package com.ini.member.service;

import org.springframework.stereotype.Service;

import com.ini.member.mapper.MemberMapper;
import com.ini.member.vo.MemberDTO;
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
	
	public MemberFollowDTO findFollowById(String user_id) {
		
		return membermapper.findFollowById(user_id);
		
	}
	
	public MemberDTO findMemberById(String user_id) {
		
		return membermapper.findMemberById(user_id);
		
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

}
