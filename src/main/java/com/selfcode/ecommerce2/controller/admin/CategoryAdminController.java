package com.selfcode.ecommerce2.controller.admin;


import com.selfcode.ecommerce2.model.Category;
import com.selfcode.ecommerce2.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CategoryAdminController {
  @Autowired
  CategoryServiceImpl categoryService;

  @GetMapping("/admin/categories")
  public String categories(Model model) {
    List<Category> categories = categoryService.findAll();
    model.addAttribute("title", "Categories");
    model.addAttribute("categories", categories);
    model.addAttribute("size", categories.size());
    return "admin/categories";
  }

  @GetMapping("/admin/add-category")
  public String addCategoryForm(Model model) {
    List<Category> categories = categoryService.findAll();
    model.addAttribute("title", "Add Category");
    model.addAttribute("newCategory", new Category());
    model.addAttribute("categories", categories);
    return "admin/add_category";
  }

  @PostMapping("/admin/add-category")
  public String addCategory(@ModelAttribute("newCategory") Category newCategory, RedirectAttributes redirect, BindingResult result, Model model) {
    try {
      categoryService.save(newCategory);
      redirect.addFlashAttribute("success", "Category created success");
    } catch (Exception e) {
      e.printStackTrace();
      redirect.addFlashAttribute("error", "Create category failded, please try again.");
    }
    return "redirect:/admin/categories";
  }

  @GetMapping("/admin/edit-category/{id}")
  public String updateCategoryForm(Model model, @PathVariable("id") Long id) {
    Category editCategory = categoryService.getById(id);
    List<Category> categories = categoryService.findAll();
    model.addAttribute("title", "Update category");
    model.addAttribute("editCategory", editCategory);
    model.addAttribute("categories", categories);
    return "admin/edit_category";
  }

  @PostMapping("/admin/edit-category/{id}")
  public String updateCategory(@ModelAttribute("editCategory") Category editCategory, Model model, BindingResult result, RedirectAttributes redirect) {
    try {
      categoryService.update(editCategory);
      redirect.addFlashAttribute("success", "Category edited success");
    } catch (Exception e) {
      e.printStackTrace();
      redirect.addFlashAttribute("error", "Edit category have error");
    }
    return "redirect:/admin/categories";
  }

  @DeleteMapping("/admin/delete-category/{id}")
  public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes redirect) {
    try {
      categoryService.deleteById(id);
      redirect.addFlashAttribute("success", "Category deleted success");
    } catch (Exception e) {
      e.printStackTrace();
      redirect.addFlashAttribute("error", "Delete category failed, try again");
    }
    return "redirect:/admin/categories";
  }

  @RequestMapping(value="/admin/enable-category", method={RequestMethod.PUT, RequestMethod.GET})
  public String enableCategory(Long id, RedirectAttributes redirect) {
    try {
      categoryService.enableById(id);
      redirect.addFlashAttribute("success", "Category is enabled");
    } catch (Exception e) {
      redirect.addFlashAttribute("error", "Can not enable category");
    }
    return "redirect:/admin/categories";
  }
}
