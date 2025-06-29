package com.ini.admin.mapper;

import com.ini.board.vo.BoardEmoticonDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmoticonAdminMapper {
	List<BoardEmoticonDTO> selectByStatus(String status);

	void updateStatus(@Param("name") String name, @Param("status") String status);

	void delete(String name);

	BoardEmoticonDTO selectByName(String name);

}
