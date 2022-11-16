package com.selfcode.ecommerce2.service;

import com.selfcode.ecommerce2.model.Wishlist;

public interface WishlistService {

  Wishlist getById(Long id);

  Wishlist addProductToWishlist(Long wishlistId, Long productId);

  Wishlist deleteWishItem(Long wishlistId, Long itemId);

}
