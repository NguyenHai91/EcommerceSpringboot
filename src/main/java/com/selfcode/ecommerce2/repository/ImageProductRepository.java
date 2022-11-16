package com.selfcode.ecommerce2.repository;


import com.selfcode.ecommerce2.model.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageProductRepository extends JpaRepository<ImageProduct, Long> {
}
