package com.launcher.inflaunch.controller;

import com.launcher.inflaunch.domain.Category;
import com.launcher.inflaunch.domain.Type;
import com.launcher.inflaunch.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private TypeService typeService;

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




}
