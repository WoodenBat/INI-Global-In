package com.ini.home.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ini.board.vo.BoardWithLikeReplyDTO;
import com.ini.home.mapper.HomeMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HomeService {

	private final HomeMapper homeMapper;

	public List<BoardWithLikeReplyDTO> findBoardByBoardIdRecentDESC() {

		List<BoardWithLikeReplyDTO> hasImageIncludeList = new ArrayList<>();

		for (BoardWithLikeReplyDTO dto : homeMapper.findBoardByBoardIdRecentDESC()) {

			if (homeMapper.boardHasImage(dto.getBoard_id()).size() == 0) {

				System.out.println(homeMapper.boardHasImage(dto.getBoard_id()));
				dto.setHas_image("0");

			} else {
				System.out.println(homeMapper.boardHasImage(dto.getBoard_id()));
				dto.setHas_image("1");

			}

			hasImageIncludeList.add(dto);

		}

		return hasImageIncludeList;

	}

	public List<BoardWithLikeReplyDTO> findBoardByBoardIdViewDESC() {

		List<BoardWithLikeReplyDTO> hasImageIncludeList = new ArrayList<>();

		for (BoardWithLikeReplyDTO dto : homeMapper.findBoardByBoardIdViewDESC()) {

			if (homeMapper.boardHasImage(dto.getBoard_id()).size() == 0) {

				dto.setHas_image("0");

			} else {

				dto.setHas_image("1");

			}

			hasImageIncludeList.add(dto);

		}

		return hasImageIncludeList;

	}

}
