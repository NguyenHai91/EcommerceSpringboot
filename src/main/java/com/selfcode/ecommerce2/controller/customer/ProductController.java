package com.selfcode.ecommerce2.controller.customer;

import com.selfcode.ecommerce2.dto.CategoryDto;
import com.selfcode.ecommerce2.dto.ProductDto;
import com.selfcode.ecommerce2.model.Category;
import com.selfcode.ecommerce2.model.Product;
import com.selfcode.ecommerce2.service.CategoryService;
import com.selfcode.ecommerce2.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {
  @Autowired
  ProductService productService;

  @Autowired
  CategoryService categoryService;

  @RequestMapping(value = "/products", method = RequestMethod.GET)
  public String products(Model model, @RequestParam(value = "search", defaultValue = "") String keyword) {
    List<Product> products;
    if (keyword != null && !keyword.equals("")) {
      products = productService.searchProducts(keyword);
    } else {
      products = productService.getAllProducts();
    }
    List<Category> categories = categoryService.getMainCategories();
    model.addAttribute("categories", categories);
    model.addAttribute("products", products);
    return "customer/products";
  }

  @GetMapping("/features")
  public String featuresProducts(Model model) {
    List<Product> featuresProducts = productService.getFeatureProducts();
    List<Category> categories = categoryService.getMainCategories();
    model.addAttribute("categories", categories);
    model.addAttribute("products", featuresProducts);
    return "customer/products";
  }

  @GetMapping("/product/detail/{id}")
  public String findProductById(@PathVariable("id") Long id, Model model) {
    ProductDto product = productService.getById(id);
    Long categoryId = product.getCategory().getId();
    List<Product> products = productService.getRelatedProducts(categoryId);
    model.addAttribute("product", product);
    model.addAttribute("products", products);
    productService.increaseViewsProduct(product.getId());
    return "customer/product-detail";
  }

  @GetMapping("/products/category={id}")
  public String getProductsInCategory(@PathVariable("id") Long categoryId, Model model) {
    List<Product> products = productService.getProductsByCategoryId(categoryId);
    Category category = categoryService.getById(categoryId);
    List<Category> categories = categoryService.findAllByActivated();
    model.addAttribute("products", products);
    model.addAttribute("category", category);
    model.addAttribute("categories", categories);
    return "customer/products_category";
  }

  @GetMapping("/products/high-price")
  public String sortProductsHighPrice(Model model) {
    List<Product> products = productService.sortProductHighPrice();
    List<Category> categories = categoryService.findAllByActivated();
    model.addAttribute("products", products);
    model.addAttribute("categories", categories);
    return "customer/products";
  }

  @GetMapping("/products/low-price")
  public String sortProductsLowPrice(Model model) {
    List<Product> products = productService.sortProductLowPrice();
    List<Category> categories = categoryService.findAllByActivated();
    model.addAttribute("products", products);
    model.addAttribute("categories", categories);
    return "customer/products";
  }
}
