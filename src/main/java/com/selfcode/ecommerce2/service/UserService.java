package com.selfcode.ecommerce2.service;

import com.selfcode.ecommerce2.dto.UserDto;
import com.selfcode.ecommerce2.model.User;

import java.util.List;

public interface UserService {
  List<User> getAll();
  User findByUsername(String username);
  User getById(Long id);
  User save(UserDto userDto);
  User saveAdmin(UserDto userDto);
  User saveInfor(User user);
  void activeUser(Long id, boolean actived);
  void delete(Long id);
}
