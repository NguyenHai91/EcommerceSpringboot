package com.selfcode.ecommerce2.service.impl;


import com.selfcode.ecommerce2.dto.UserDto;
import com.selfcode.ecommerce2.model.User;
import com.selfcode.ecommerce2.repository.RoleRepository;
import com.selfcode.ecommerce2.repository.UserRepository;
import com.selfcode.ecommerce2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public List<User> getAll() {
    return userRepository.findAll();
  }

  @Override
  public User getById(Long id) {
      return  userRepository.findById(id).get();
  }

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
    user.setEmail(userDto.getEmail());
    user.setPhoneNumber(userDto.getPhone());
    user.setAddress(userDto.getAddress());
    user.setIcon(userDto.getIcon());
    user.setPassword(userDto.getPassword());
    user.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
    user.setActived(true);
    return userRepository.save(user);
  }

  @Override
  public User saveAdmin(UserDto userDto) {
    User user = new User();
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setUsername(userDto.getUsername());
    user.setEmail(userDto.getEmail());
    user.setPhoneNumber(userDto.getPhone());
    user.setAddress(userDto.getAddress());
    user.setIcon(userDto.getIcon());
    user.setPassword(userDto.getPassword());
    user.setRoles(userDto.getRoles());
    user.setActived(true);
    return userRepository.save(user);
  }

  public void activeUser(Long id, boolean actived) {
    userRepository.activeUser(id, actived);
  }

  @Override
  public void delete(Long id) {
    User user = userRepository.getById(id);
    if (user != null)
      userRepository.delete(user);
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
