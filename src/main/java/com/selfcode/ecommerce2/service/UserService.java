package com.selfcode.ecommerce2.service;

import com.selfcode.ecommerce2.dto.UserDto;
import com.selfcode.ecommerce2.model.User;

public interface UserService {
  User findByUsername(String username);
  User save(UserDto userDto);
  User saveAdmin(UserDto userDto);
  User saveInfor(User user);
}
