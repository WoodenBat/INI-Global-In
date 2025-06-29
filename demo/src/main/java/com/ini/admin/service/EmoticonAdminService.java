package com.ini.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ini.admin.mapper.EmoticonAdminMapper;
import com.ini.board.vo.BoardEmoticonDTO;

@Service
public class EmoticonAdminService {

    @Autowired
    private EmoticonAdminMapper mapper;

    public List<BoardEmoticonDTO> getByStatus(String status) {
        return mapper.selectByStatus(status);
    }

    public BoardEmoticonDTO getByName(String name) {
        return mapper.selectByName(name);
    }

    @Transactional
    public void updateStatus(String name, String status) {
        mapper.updateStatus(name, status);
    }

    @Transactional
    public void delete(String name) {
        mapper.delete(name);
    }
}
