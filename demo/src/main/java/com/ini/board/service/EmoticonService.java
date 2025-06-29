package com.ini.board.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ini.board.mapper.EmoticonMapper;
import com.ini.board.vo.BoardEmoticonDTO;

@Service
public class EmoticonService {

	@Autowired
	private EmoticonMapper emoticonMapper;

	@Value("${file.upload-path}")
	private String uploadPath;
	

	public void applyEmoticon(String name, MultipartFile file, String userId) {
		if (file.isEmpty())
			return;

		String originalFileName = file.getOriginalFilename();
		String savedFileName = UUID.randomUUID() + "_" + originalFileName;
		Path savePath = Paths.get(uploadPath, "emoticon", savedFileName);

		try {
			Files.copy(file.getInputStream(), savePath);
		} catch (IOException e) {
			throw new RuntimeException("파일 저장 실패", e);
		}

		BoardEmoticonDTO dto = new BoardEmoticonDTO();
		dto.setEmoticon_name(name);
		dto.setEmoticon_file_name(savedFileName);
		dto.setEmoticon_creator(userId);
		dto.setEmoticon_create_date(new Date());
		dto.setEmoticon_status("승인보류중");

		emoticonMapper.insertEmoticon(dto);
	}
}