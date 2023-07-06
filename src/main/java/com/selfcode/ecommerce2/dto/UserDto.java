package com.selfcode.ecommerce2.dto;


import com.selfcode.ecommerce2.model.Role;
import com.selfcode.ecommerce2.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.Size;
import java.sql.Blob;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  @Size(min = 3, max = 50, message = "first name must have 3 to 50 characters")
  private String firstName;

  @Size(min = 3, max = 50, message = "last name must have 3 to 50 character")
  private String lastName;

  @Size(min = 3, max = 20, message = "username must have 3 to 20 characters")
  private String username;

  @Size(min = 6, max = 20, message = "email must have 6 to 20 characters")
  private String email;

  @Size(min = 10, max = 18, message = "phone must have 10 to 18 number")
  private String phone;

  @Size(min = 3, max = 20, message = "password must have 3 to 20 characters")
  private String password;

  @Size(min = 3, max = 20, message = "re-password not same password")
  private String re_password;

  private List<Role> roles;

  private String icon;

  private String country;

  private String state;

  private String city;

  private String ward;

  private String address;

  private String zipcode;

  public UserDto(User user) {
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.phone = user.getPhoneNumber();
    this.icon = user.getIcon();
    this.country = user.getCountry();
    this.state = user.getState();
    this.city = user.getCity();
    this.ward = user.getWard();
    this.address = user.getAddress();
    this.roles = (List<Role>) user.getRoles();
  }

  public boolean validate() {
    if (this.getUsername() == null || this.getUsername().equals("")) return false;

    if (this.getFirstName() == null || this.getFirstName().equals("")) return false;

    if (this.getLastName() == null || this.getLastName().equals("")) return false;

    if (this.getCountry() == null || this.getCountry().equals("")) return false;

    if (this.getAddress() == null || this.getAddress().equals("")) return false;

    return true;
  }

}
