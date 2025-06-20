package com.ini.board.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ini.board.service.BoardService;
import com.ini.board.vo.BoardDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public String boardListPage(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            Model model) {

        int offset = page * size;

        System.out.println("🟢 keyword = " + keyword);
        System.out.println("🟢 category = " + category);
        System.out.println("🟢 offset = " + offset + ", limit = " + size);

        // ✅ end 제거한 Service 호출
        List<BoardDTO> boardList = boardService.getBoardList(keyword, category, offset, size);
        int totalCount = boardService.countBoards(keyword, category);

        System.out.println("🟢 boardList size = " + boardList.size());
        for (BoardDTO dto : boardList) {
            System.out.println("🟢 게시글 제목: " + dto.getBoard_title());
        }

        List<String> categories = Arrays.asList("Java", "DB", "JSP", "Spring", "Python", "일본어");

        model.addAttribute("boards", boardList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("categories", categories);

        return "board/list";
    }

    }
