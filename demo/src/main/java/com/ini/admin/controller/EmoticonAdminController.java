package com.ini.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ini.admin.service.EmoticonAdminService;
import com.ini.board.vo.BoardEmoticonDTO;

@Controller
@RequestMapping("/admin/emoticons")
public class EmoticonAdminController {

	@Autowired
	private EmoticonAdminService service;

	@GetMapping
	public String showPage(org.springframework.ui.Model model) {
		List<BoardEmoticonDTO> pendingList = service.getByStatus("승인보류중");
		List<BoardEmoticonDTO> approvedList = service.getByStatus("승인됨");
		model.addAttribute("pendingList", pendingList);
		model.addAttribute("approvedList", approvedList);
		return "admin/admin_emoticons";
	}

	@PostMapping("/approve")
	@ResponseBody
	public String approve(@RequestBody BoardEmoticonDTO dto) {
		service.updateStatus(dto.getEmoticon_name(), "승인됨");
		return "ok";
	}

	@PostMapping("/reject")
	@ResponseBody
	public String reject(@RequestBody BoardEmoticonDTO dto) {
		service.updateStatus(dto.getEmoticon_name(), "거절됨");
		return "ok";
	}

	@PostMapping("/delete")
	@ResponseBody
	public String delete(@RequestBody BoardEmoticonDTO dto) {
		service.delete(dto.getEmoticon_name());
		return "ok";
	}

	@GetMapping("/info")
	@ResponseBody
	public BoardEmoticonDTO info(@RequestParam("name") String name) {
		return service.getByName(name);
	}
}
