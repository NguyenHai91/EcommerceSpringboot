package com.selfcode.ecommerce2.repository;

import com.selfcode.ecommerce2.model.WishItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishItemRepository extends JpaRepository<WishItem, Long> {
}
