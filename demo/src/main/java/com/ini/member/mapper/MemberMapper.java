package com.ini.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ini.member.vo.MemberDTO;
import com.ini.member.vo.MemberFollowDTO;

@Mapper
public interface MemberMapper {

	public MemberDTO findAllMember();
	
	public MemberFollowDTO findFollowById(String user_id);
	
	public MemberDTO findMemberById(String user_id);
	
}
