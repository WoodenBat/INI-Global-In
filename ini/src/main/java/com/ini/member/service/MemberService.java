package com.ini.member.service;

import org.springframework.stereotype.Service;

import com.ini.member.mapper.MemberMapper;
import com.ini.member.vo.MemberDTO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@AllArgsConstructor
public class MemberService {

	private final MemberMapper membermapper;
	
	public MemberDTO findAllMember() {
		
		return membermapper.findAllMember();
		
	}
	
}
