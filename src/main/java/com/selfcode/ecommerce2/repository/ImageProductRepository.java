package com.selfcode.ecommerce2.repository;


import com.selfcode.ecommerce2.model.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageProductRepository extends JpaRepository<ImageProduct, Long> {
  @Query("select i from ImageProduct i where i.product.id=:id")
  List<ImageProduct> findByProductId(@Param("id") Long id);
}
