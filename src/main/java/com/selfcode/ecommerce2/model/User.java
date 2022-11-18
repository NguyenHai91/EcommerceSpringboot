package com.selfcode.ecommerce2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  private String email;

  private String password;

  @Lob
  @Column(columnDefinition = "MEDIUMBLOB")
  private Blob icon;

  private String country;

  @Column(name = "phone_number")
  private String phoneNumber;

  private String state;

  private String city;

  private String ward;

  private String address;

  @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
  private Cart cart;

  @OneToMany(mappedBy = "user")
  private List<Order> orders;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable( name = "user_role",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Collection<Role> roles;
}
