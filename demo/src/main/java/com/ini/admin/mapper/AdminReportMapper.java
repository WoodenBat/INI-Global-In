package com.ini.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;

import com.ini.admin.vo.AdminReportDTO;

@Mapper
public interface AdminReportMapper {

	List<AdminReportDTO> selectAllReports();

	void deleteReportByBoardId(@Param("board_id") String board_id);

	@Select("SELECT COUNT(*) FROM BOARD_REPORT")
	int countReports();
}