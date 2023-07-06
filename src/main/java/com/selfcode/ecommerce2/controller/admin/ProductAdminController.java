package com.selfcode.ecommerce2.controller.admin;


import com.selfcode.ecommerce2.dto.ProductDto;
import com.selfcode.ecommerce2.model.Category;
import com.selfcode.ecommerce2.service.impl.CategoryServiceImpl;
import com.selfcode.ecommerce2.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductAdminController {
  @Autowired
  ProductServiceImpl productService;

  @Autowired
  CategoryServiceImpl categoryService;

  @GetMapping("/admin/products")
  public String products(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    List<ProductDto> productDtoList = productService.findAll();
    model.addAttribute("title", "Products");
    model.addAttribute("products", productDtoList);
    model.addAttribute("size", productDtoList.size());
    return "admin/products";
  }

  @GetMapping("/admin/add-product")
  public String addProduct(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    List<Category> categoryList = categoryService.findAll();
    model.addAttribute("title", "Add Product");
    model.addAttribute("newProduct", new ProductDto());
    model.addAttribute("categories", categoryList);
    return "admin/add-product";
  }

  @PostMapping("/admin/add-product")
  public String addProduct(@ModelAttribute("newProduct") ProductDto productDto,
                           @RequestParam("imageProduct") MultipartFile imageProduct,
                           @RequestParam("extra_images") MultipartFile[] listImages,
                           RedirectAttributes redirect)
  {
    try {
      productService.save(imageProduct, listImages, productDto);
      redirect.addFlashAttribute("success", "Product added success");
    } catch (Exception e) {
      e.printStackTrace();
      redirect.addFlashAttribute("error", "Add product failed");
    }
    return "redirect:/admin/add-product";
  }

  @GetMapping("/admin/update-product/{id}")
  public String updateProduct(@PathVariable("id") Long id, Model model) {
    ProductDto updateProduct = productService.getById(id);
    List<Category> categories = categoryService.findAll();
    model.addAttribute("title", "Update Product");
    model.addAttribute("productDto", updateProduct);
    model.addAttribute("categories", categories);
    return "admin/update-product";
  }

  @PostMapping("/admin/update-product/{id}")
  public String editProduct(@PathVariable("id") Long id,
                            @ModelAttribute("productDto") ProductDto productDto,
                            @RequestParam("imageProduct") MultipartFile imageProduct,
                            @RequestParam(value = "extraImages") MultipartFile[] extraImages,
                            RedirectAttributes redirect) {
    try {
      productService.update(productDto, imageProduct, extraImages);
      redirect.addFlashAttribute("success", "Product updated success");
    } catch (Exception e) {
      e.printStackTrace();
      redirect.addFlashAttribute("error", "Update product failed");
    }
    return "redirect:/admin/products";
  }

  @RequestMapping(value="/admin/enable-product", method={RequestMethod.PUT, RequestMethod.GET})
  public String enableProduct(@PathVariable("id") Long id, RedirectAttributes redirect){
    try {
      productService.enableById(id);
      redirect.addFlashAttribute("success", "Product is enabled");
    } catch (Exception e) {
      e.printStackTrace();
      redirect.addFlashAttribute("error", "Enable product failed");
    }
    return "redirect:/admin/products";
  }

  @RequestMapping(value = "/admin/delete-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
  public String deletedProduct(@PathVariable("id") Long id, RedirectAttributes redirect){
    try {
      productService.deleteById(id);
      redirect.addFlashAttribute("success", "Deleted success");
    }catch (Exception e){
      e.printStackTrace();
      redirect.addFlashAttribute("error", "Failed to deleted");
    }
    return "redirect:/admin/products";
  }

  @GetMapping("/admin/products/{pageNo}")
  public String productsPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    Page<ProductDto> products = productService.pageProducts(pageNo);
    model.addAttribute("title", "Products");
    model.addAttribute("size", products.getSize());
    model.addAttribute("totalPages", products.getTotalPages());
    model.addAttribute("currentPage", pageNo);
    model.addAttribute("products", products);
    return "admin/products";
  }

  @GetMapping("/admin/search-result/{pageNo}")
  public String searchProducts(@PathVariable("pageNo") int pageNo, @RequestParam("keyword") String keyword, Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    Page<ProductDto> products = productService.searchProducts(pageNo, keyword);
    model.addAttribute("title", "Products");
    model.addAttribute("size", products.getSize());
    model.addAttribute("totalPages", products.getTotalPages());
    model.addAttribute("currentPage", pageNo);
    model.addAttribute("products", products);
    return "admin/result-products";
  }
}
