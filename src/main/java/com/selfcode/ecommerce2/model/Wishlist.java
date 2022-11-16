package com.selfcode.ecommerce2.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="wishlist")
public class Wishlist {

  @GeneratedValue(strategy= GenerationType.IDENTITY)
  @Id
  private Long id;

  private Date createdDate;

  private Date updatedDate;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy="wishlist")
  private Set<WishItem> wishItems = new HashSet<>();

  public int getTotalItems() {
    return wishItems.size();
  }
}
