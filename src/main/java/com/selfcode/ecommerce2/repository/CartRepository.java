package com.selfcode.ecommerce2.repository;

import com.selfcode.ecommerce2.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
  @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.id = (:id)")
  Cart findByIdAndCartItem(@Param("id") Long id);
}
