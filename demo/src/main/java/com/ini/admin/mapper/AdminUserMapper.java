package com.ini.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ini.admin.vo.AdminUserDTO;

@Mapper
public interface AdminUserMapper {
    List<AdminUserDTO> selectAllUsers();
    void deleteUserById(@Param("id") String user_id);
    
    @Select("SELECT COUNT(*) FROM ini_user")
    int countUsers();
}