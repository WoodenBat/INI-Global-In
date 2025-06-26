package com.ini.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ini.admin.mapper.AdminReportMapper;
import com.ini.admin.service.AdminService;
import com.ini.admin.vo.AdminUserDTO;
import com.ini.board.mapper.BoardMapper;
import com.ini.admin.vo.AdminReportDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private AdminReportMapper reportMapper;

    
    // 대시보드
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("reportList", adminService.getAllReports());
        model.addAttribute("userCount", adminService.countUsers());
        model.addAttribute("reportCount", adminService.countReports());
        return "admin/admin_dashboard";
    }
    
    // 회원 관리
    @GetMapping("/users")
    public String getUsersPage(Model model) {
        List<AdminUserDTO> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/admin_users";
    }

    @GetMapping("/users/list")
    @ResponseBody
    public List<AdminUserDTO> getUsersList() {
        return adminService.getAllUsers();
    }

    // 회원 삭제 관리
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        adminService.deleteUserById(id);
        return ResponseEntity.ok("회원 삭제 완료");
    }
    
    // 🔍 신고 게시글 페이지 렌더링
    @GetMapping("/reports")
    public String getReportsPage() {
        return "admin/admin_report";
    }

    // 📦 신고 게시글 데이터 응답
    @GetMapping("/reports/list")
    @ResponseBody
    public List<AdminReportDTO> getReportsData() {
        return boardMapper.selectReportedBoards();
    }

    // 🗑️ 게시글 삭제 + 관련 신고 함께 삭제
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable("id") String board_id) {
        reportMapper.deleteReportByBoardId(board_id);   // 1️⃣ 신고 먼저 삭제
        boardMapper.deleteBoardById(board_id);          // 2️⃣ 게시글 삭제

        return ResponseEntity.ok("게시글 및 신고 삭제 완료");
    }
}
