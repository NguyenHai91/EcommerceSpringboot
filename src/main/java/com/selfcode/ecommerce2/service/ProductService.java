package com.selfcode.ecommerce2.service;

import com.selfcode.ecommerce2.dto.ProductDto;
import com.selfcode.ecommerce2.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {
  List<ProductDto> findAll();

  ProductDto getById(Long id);

  Optional<Product> getProductById(Long id);

  Product save(MultipartFile imageProduct, MultipartFile[] listImages, ProductDto productDto);

  Product update(ProductDto productDto, MultipartFile imageProduct, MultipartFile[] extraImages);

  void updateQuantity(Product product, int num);

  void increaseViewsProduct(Long id);

  void deleteById(Long id);

  void enableById(Long id);

  void disableById(Long id);

  Page<ProductDto> pageProducts(int pageNo);

  Page<ProductDto> searchProducts(int pageNo, String keyword);

  List<Product> searchProducts(String keyword);

  List<Product> getAllProducts();

  List<ProductDto> getProductsWithMainCategories();

  List<Product> listViewProducts();

  List<Product> getFeatureProducts();

  List<Product> getRelatedProducts(Long categoryId);

  List<Product> getProductsByCategoryId(Long categoryId);

  List<Product> getProductsByCategoryId(Long categoryId, int limit, int offset);

  List<Product> sortProductHighPrice();

  List<Product> sortProductLowPrice();
}
