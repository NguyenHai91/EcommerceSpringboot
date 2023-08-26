package com.selfcode.ecommerce2.repository;

import com.selfcode.ecommerce2.dto.CategoryDto;
import com.selfcode.ecommerce2.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  @Query("select c from Category c where c.is_actived = true and c.is_deleted = false")
  List<Category> findAllByActivated();

  @Query("select new com.selfcode.ecommerce2.dto.CategoryDto(c.id, c.name, c.parentCategory, count(p.category.id)) from Category c inner join Product p on p.category.id = c.id " +
      " where c.is_actived = true and c.is_deleted = false group by c.id")
  List<CategoryDto> getCategoryAndProduct();

  @Query("select c from Category c where c.parentCategory is null")
  List<Category> getRootCategories();
}
