package com.selfcode.ecommerce2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "category", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "parent_id", referencedColumnName = "id")
  private Category parentCategory;

  @OneToMany(mappedBy = "parentCategory")
  private Set<Category> childCategories;

  private boolean is_deleted;

  private boolean is_actived;

  public Category(String name) {
    this.name = name;
    this.is_actived = true;
    this.is_deleted = false;
  }
}
