package com.ini.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ini.member.vo.MemberDTO;

@Mapper
public interface MemberMapper {

	public MemberDTO findAllMember();
	
}
