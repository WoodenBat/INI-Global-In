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
}
