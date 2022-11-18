package com.selfcode.ecommerce2.repository;

import com.selfcode.ecommerce2.dto.ProductDto;
import com.selfcode.ecommerce2.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query("select p from Product p")
  Page<ProductDto> pageProduct(Pageable pageable);

  @Query("select p from Product p where p.description like %?1% or p.name like %?1%")
  List<Product> searchProducts(String keyword);

  @Query("select p from Product p where p.is_actived = true and p.is_deleted = false")
  List<Product> getAllProducts();

  @Query(value = "select * from products p where p.is_actived = true and p.is_deleted = false order by rand() asc limit 6", nativeQuery = true)
  List<Product> listViewProducts();

  @Query(value = "select * from products p inner join category c on c.id = p.category_id where p.category_id = ?1 order by p.id desc limit 12", nativeQuery = true)
  List<Product> getRelatedProducts(Long categoryId);

  @Query("select p from Product p where p.category.id = ?1")
  List<Product> getProductsByCategoryId(Long categoryId);

  @Query("select p from Product p where p.is_actived = true and p.is_deleted = false order by p.costPrice desc")
  List<Product> sortProductHighPrice();

  @Query("select p from Product p where p.is_actived = true and p.is_deleted = false order by p.costPrice asc")
  List<Product> sortProductLowPrice();
}
