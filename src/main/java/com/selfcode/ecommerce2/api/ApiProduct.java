package com.selfcode.ecommerce2.api;

import com.selfcode.ecommerce2.dto.ProductDto;
import com.selfcode.ecommerce2.model.Product;
import com.selfcode.ecommerce2.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiProduct {

  @Autowired
  private ProductService productService;

  @RequestMapping("/products")
  public ResponseEntity getProducts() {
    List<ProductDto> products = productService.getProductsWithMainCategories();
    return ResponseEntity.ok(products);
  }

  @RequestMapping("/products/category/{id}")
  public ResponseEntity getProducts(@PathVariable("id") Long idCategory) {
    List<Product> products = productService.getProductsByCategoryId(idCategory);
    return ResponseEntity.ok(products);
  }
}
