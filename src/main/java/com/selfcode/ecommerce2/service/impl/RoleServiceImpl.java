package com.selfcode.ecommerce2.service.impl;

import com.selfcode.ecommerce2.model.Role;
import com.selfcode.ecommerce2.repository.RoleRepository;
import com.selfcode.ecommerce2.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public List<Role> getAll() {
    List<Role> roles = roleRepository.findAll();
    return roles;
  }

  @Override
  public Role createRole(Role role) {
    return roleRepository.save(role);
  }

  @Override
  public Role findByName(String name) {
    return roleRepository.findByName(name);
  }
}
