package com.selfcode.ecommerce2.service.impl;

import com.selfcode.ecommerce2.model.Product;
import com.selfcode.ecommerce2.model.WishItem;
import com.selfcode.ecommerce2.model.Wishlist;
import com.selfcode.ecommerce2.repository.ProductRepository;
import com.selfcode.ecommerce2.repository.WishItemRepository;
import com.selfcode.ecommerce2.repository.WishlistRepository;
import com.selfcode.ecommerce2.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class WishlistServiceImpl implements WishlistService {
  @Autowired
  private WishlistRepository wishlistRepository;

  @Autowired
  private WishItemRepository wishItemRepository;

  @Autowired
  private ProductRepository productRepository;

  @Override
  public Wishlist getById(Long id) {
    return wishlistRepository.findById(id).get();
  }

  @Override
  public Wishlist addProductToWishlist(Long wishlistId, Long productId) {
    Wishlist wishlist;
    if (wishlistId == null) {
      wishlist = new Wishlist();
      wishlist = wishlistRepository.save(wishlist);
    } else {
      wishlist = wishlistRepository.findById(wishlistId).get();
    }
    Product product = productRepository.findById(productId).get();
    boolean isExisted = false;
    Set<WishItem> wishItems = wishlist.getWishItems();

    for (WishItem obj: wishItems) {
      if (obj.getProduct().getId() == productId) {
        isExisted = true;
        break;
      }
    }

    if (!isExisted) {
      WishItem wishItem = new WishItem();
      wishItem.setProduct(product);
      wishItem.setWishlist(wishlist);
      wishItem = wishItemRepository.save(wishItem);
      wishItems.add(wishItem);
      wishlist.setWishItems(wishItems);
    }

    return wishlist;
  }

  @Override
  public Wishlist deleteWishItem(Long wishlistId, Long itemId) {
    Wishlist wishlist = wishlistRepository.findById(wishlistId).get();
    WishItem item = wishItemRepository.findById(itemId).get();
    Set<WishItem> wishItems = wishlist.getWishItems();
    wishItems.remove(item);
    wishItemRepository.deleteById(itemId);
    wishlist.setWishItems(wishItems);
    return wishlistRepository.save(wishlist);
  }
}
