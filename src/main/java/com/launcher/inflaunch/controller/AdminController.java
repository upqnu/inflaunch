package com.launcher.inflaunch.controller;

import com.launcher.inflaunch.domain.Category;
import com.launcher.inflaunch.domain.ReportReview;
import com.launcher.inflaunch.domain.Type;
import com.launcher.inflaunch.domain.User;
import com.launcher.inflaunch.dto.ReportReviewInfoDto;
import com.launcher.inflaunch.enum_status.ReviewStatus;
import com.launcher.inflaunch.repository.ReportReviewRepository;
import com.launcher.inflaunch.repository.ReviewRepository;
import com.launcher.inflaunch.repository.UserRepository;
import com.launcher.inflaunch.service.AdminService;
import com.launcher.inflaunch.service.ReportReviewService;
import com.launcher.inflaunch.service.TypeService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private TypeService typeService;

    @Autowired
    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    PrintWriter pw = new PrintWriter(System.out);
    public AdminController() { pw.println(getClass().getName() + "()생성"); }

    @RequestMapping("/income")
    public void income() {}

    @GetMapping("/type")
    public void addtype(Model model) {

        model.addAttribute("type", typeService.typeList());
        model.addAttribute("category",typeService.categoryList());
    }

    @PostMapping("/type")
    public String typeAddOk(@RequestParam Long categoryId, Type type, Model model){
        model.addAttribute("result", typeService.addType(type, categoryId));
        return "/admin/AddOk";
    }

    @PostMapping("/typeDel")
    public String typeDeleteOk(long id, Model model){
        model.addAttribute("result", typeService.delType(id));
        return "/admin/DelOk";
    }

    @PostMapping("/category")
    public String categoryAddOk(Category category, Model model){
        model.addAttribute("result", typeService.addCategory(category));
        return "/admin/AddOk";
    }

    @PostMapping("/categoryDel")
    public String categoryDeleteOk(long id, Model model){
        model.addAttribute("result", typeService.delCategory(id));
        return "/admin/DelOk";
    }

    /* 신고된 수강평 모음 페이지로 이동 */
    @GetMapping("/report-reviews")
    public String viewReportReviews(Model model, @RequestParam(defaultValue = "0") int page) {
//        Pageable pageable = PageRequest.of(page, 5);
//        Page<ReportReviewInfoDto> reportedReviewsPage = adminService.getReportedReviews(pageable);
//
////        List<ReportReviewInfoDto> reportedReviews = adminService.getReportedReviews();
//        List<ReportReviewInfoDto> reportedReviews = reportedReviewsPage.getContent();
//
//        model.addAttribute("reportedReviews", reportedReviews);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", reportedReviewsPage.getTotalPages());
//
//        return "admin/reportreview-page";

        try {
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.desc("regDate"));
            Pageable pageable = PageRequest.of(page, 3, Sort.by(sorts));
            Page<ReportReviewInfoDto> reportedReviewsPage = adminService.getReportedReviews(pageable);

            if (reportedReviewsPage != null) {
                model.addAttribute("reportedReviews", reportedReviewsPage.getContent());
                model.addAttribute("reportedReviewsPage", reportedReviewsPage);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", reportedReviewsPage.getTotalPages());
            } else {
                model.addAttribute("errorMessage", "No reported reviews found.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while retrieving reported reviews.");
        }

        return "admin/reportreview-page";
    }

    /* 신고된 수강평을 처리 */
    @PatchMapping("/report-reviews")
    public String editReviewStatus(@RequestParam("reviewId") Long reviewId, @RequestParam("action") String action, @RequestParam("currentPage") int currentPage, HttpSession session, Model model) {
        try {
            if (action.equals("active")) {
                adminService.updateReviewStatus(reviewId, ReviewStatus.ACTIVE);
            } else if (action.equals("inactive")) {
                adminService.updateReviewStatus(reviewId, ReviewStatus.INACTIVE);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "수강평 상태 변경에 실패했습니다.");
            return "admin/reportreview-page";
        }

        // 현재 페이지 정보를 세션에 저장
        session.setAttribute("currentPage", currentPage);

        return "redirect:/admin/report-reviews?page=" + currentPage;
    }
}
