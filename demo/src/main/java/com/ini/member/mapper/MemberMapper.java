package com.ini.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ini.member.vo.MemberDTO;
import com.ini.member.vo.MemberFollowDTO;

@Mapper
public interface MemberMapper {

	public MemberDTO findAllMember();
	
	public MemberFollowDTO findFollowById(String user_id);
	
	public MemberDTO findMemberById(String user_id);
	
	MemberDTO findByEmail(String email);
	
	MemberDTO findByNickname(@Param("nickname") String nickname);


	int insertMember(MemberDTO member); 
	
	
	
}
