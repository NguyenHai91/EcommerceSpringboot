package com.selfcode.ecommerce2.repository;

import com.selfcode.ecommerce2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);

  User findByEmailAndActived(String email, boolean b);

  @Modifying
  @Transactional
  @Query(value = "update User u set u.actived = :actived where u.id = :id")
  void activeUser(@Param("id") Long id, @Param("actived") boolean actived);
}
