package com.ini.member.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ini.admin.vo.AdminUserDTO;
import com.ini.board.vo.BoardDTO;
import com.ini.board.vo.BoardLikeDTO;
import com.ini.board.vo.BoardReplyDTO;
import com.ini.board.vo.BoardWithLikeReplyDTO;
import com.ini.member.vo.MemberAuthenticationDTO;
import com.ini.member.vo.MemberDTO;
import com.ini.member.vo.MemberFavoriteDTO;
import com.ini.member.vo.MemberFollowDTO;

@Mapper
public interface MemberMapper {

	public List<MemberDTO> findAllMember();

	public MemberDTO findMemberById(String user_id);

	public List<MemberFollowDTO> findFollowById(String user_id);

	public List<BoardDTO> findBoardById(String user_id);

	public List<BoardLikeDTO> findLikeById(String user_id);

	public List<BoardReplyDTO> findReplyById(String user_id);

	public List<MemberFavoriteDTO> findFavoriteById(String user_id);

	public List<BoardWithLikeReplyDTO> findBoardLikeReplyById(String user_id);

	public List<BoardWithLikeReplyDTO> findBoardLikeReplyByLikeId(String user_id);

	public List<BoardWithLikeReplyDTO> findBoardLikeReplyByReplyId(String user_id);

	public List<BoardWithLikeReplyDTO> findBoardLikeReplyByFavoriteId(String user_id);

	public MemberDTO findByEmail(String email);

	public MemberDTO findByNickname(@Param("nickname") String nickname);

	public int insertMember(MemberDTO member);

	public int insertOAuthUser(MemberDTO memberDTO);

	void updateProfileImage(@Param("userId") String userId, @Param("profileImage") String profileImage);

	void updateUserIntro(MemberDTO dto);

	void updateMemberProfile(MemberDTO memberDTO);

	void increaseReportCount(@Param("user_id") String user_id);

	int getReportCount(@Param("user_id") String user_id);

	void banUser(@Param("user_id") String user_id);

	List<AdminUserDTO> selectReportedUsers();
	
	public MemberAuthenticationDTO findUserAuthByUserId(String user_id);
	
	public void insertIntoUserAuthentication(String user_id);
}
