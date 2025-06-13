package com.ini.board.service;

import org.springframework.stereotype.Service;

import com.ini.board.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Service
@AllArgsConstructor
public class BoardService {

	private final BoardMapper boardmapper;
	
}
