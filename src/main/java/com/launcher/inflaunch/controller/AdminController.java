package com.launcher.inflaunch.controller;

import com.launcher.inflaunch.domain.Category;
import com.launcher.inflaunch.domain.ReportReview;
import com.launcher.inflaunch.domain.Type;
import com.launcher.inflaunch.domain.User;
import com.launcher.inflaunch.repository.ReportReviewRepository;
import com.launcher.inflaunch.repository.ReviewRepository;
import com.launcher.inflaunch.repository.UserRepository;
import com.launcher.inflaunch.service.ReportReviewService;
import com.launcher.inflaunch.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private TypeService typeService;
    private ReviewRepository reviewRepository;
    private ReportReviewRepository reportReviewRepository;
    private ReportReviewService reportReviewService;
    private UserRepository userRepository;

    @Autowired
    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
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
    public String viewReportReviews(Model model, RedirectAttributes redirectAttributes, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        if (!reportReviewService.hasAdminAuthority(user)) {
            redirectAttributes.addFlashAttribute("error message", "신고된 수강평을 살펴볼 권한이 없습니다.");
            return "redirect:/courses";
        }

        // Retrieve the reported review
        List<ReportReview> reportReviews = reportReviewRepository.findAll();

        // Pass the information to the view
        model.addAttribute("reportedReview", reportReviews);

        return "admin/reportreview-page";
    }


}
