package com.selfcode.ecommerce2.service;

import com.selfcode.ecommerce2.model.Role;

import java.util.List;

public interface RoleService {
  List<Role> getAll();
  Role createRole(Role role);
  Role findByName(String name);
}
