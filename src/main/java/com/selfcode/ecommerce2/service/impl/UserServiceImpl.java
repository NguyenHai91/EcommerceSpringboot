package com.selfcode.ecommerce2.service.impl;


import com.selfcode.ecommerce2.dto.UserDto;
import com.selfcode.ecommerce2.model.User;
import com.selfcode.ecommerce2.repository.RoleRepository;
import com.selfcode.ecommerce2.repository.UserRepository;
import com.selfcode.ecommerce2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public User save(UserDto userDto) {
    User user = new User();
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setUsername(userDto.getUsername());
    user.setPassword(userDto.getPassword());
    user.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
    return userRepository.save(user);
  }

  @Override
  public User saveAdmin(UserDto userDto) {
    User user = new User();
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setUsername(userDto.getUsername());
    user.setPassword(userDto.getPassword());
    user.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
    return userRepository.save(user);
  }

  @Override
  public User saveInfor(User user) {
    User updateUser = userRepository.findByUsername(user.getUsername());
    updateUser.setAddress(user.getAddress());
    updateUser.setCity(user.getCity());
    updateUser.setCountry(user.getCountry());
    updateUser.setPhoneNumber(user.getPhoneNumber());
    return userRepository.save(updateUser);
  }

  private UserDto mapperDTO(User user) {
    UserDto userDto = new UserDto();
    userDto.setFirstName(user.getFirstName());
    userDto.setLastName(user.getLastName());
    userDto.setUsername(user.getUsername());
    userDto.setPassword(user.getPassword());
    return userDto;
  }
}
