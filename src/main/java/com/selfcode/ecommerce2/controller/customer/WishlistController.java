package com.selfcode.ecommerce2.controller.customer;

import com.selfcode.ecommerce2.model.Product;
import com.selfcode.ecommerce2.model.User;
import com.selfcode.ecommerce2.model.WishItem;
import com.selfcode.ecommerce2.model.Wishlist;
import com.selfcode.ecommerce2.service.ProductService;
import com.selfcode.ecommerce2.service.UserService;
import com.selfcode.ecommerce2.service.WishlistService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.*;

@Controller
public class WishlistController {
  @Autowired
  private UserService userService;

  @Autowired
  private WishlistService wishlistService;

  @Autowired
  private ProductService productService;

  @GetMapping("/wishlist")
  public String getWishlist(Model model, Principal principal, HttpSession session) {
    if (session.getAttribute("wishlist") == null) {
      model.addAttribute("check", "Not found item in wishlist");
    } else {
      Long wishId = (Long) session.getAttribute("wishlist");
      Wishlist wishlist = wishlistService.getById(wishId);
      if (wishlist == null || wishlist.getTotalItems() == 0) {
        model.addAttribute("check", "Not found item in wishlist");
      }
      model.addAttribute("wishlist", wishlist);
    }

    return "customer/wishlist";
  }

  @GetMapping("/add-to-wishlist/{id}")
  public ResponseEntity<Object> addToWishlist(@PathVariable("id") Long id, Model model, Principal principal, HttpSession session) {
    Wishlist wishlist;
    Long wishId;
    if (session.getAttribute("wishlist") == null) {
      wishId = null;
    } else {
      wishId = (Long) session.getAttribute("wishlist");
    }
    wishlist = wishlistService.addProductToWishlist(wishId, id);
    Set<WishItem> wishItems = wishlist.getWishItems();
    List<Long> listIdProducts = new ArrayList<>();
    for (WishItem item: wishItems) {
      listIdProducts.add(item.getProduct().getId());
    }
    Map<String, Object> data = new HashMap<>();
    data.put("listIdProducts", listIdProducts);
    data.put("wishItems", wishlist.getTotalItems());
    session.setAttribute("wishlist", wishlist.getId());
    session.setAttribute("wishItems", wishlist.getTotalItems());
    session.setAttribute("listIdProducts", listIdProducts);
    return ResponseEntity.status(HttpStatus.OK).body(data);
  }

  @GetMapping("/delete-wishitem/{id}")
  public String deleteWishItem(@PathVariable("id") Long id, Model model, Principal principal, HttpSession session) {
    Wishlist wishlist;
    if (session.getAttribute("wishlist") != null) {
      Long wishId = (Long) session.getAttribute("wishlist");
      wishlist = wishlistService.deleteWishItem(wishId, id);

      Set<WishItem> wishItems = wishlist.getWishItems();
      List<Long> listIdProducts = new ArrayList<>();
      for (WishItem item: wishItems) {
        listIdProducts.add(item.getProduct().getId());
      }
      session.setAttribute("wishlist", wishlist.getId());
      session.setAttribute("wishItems", wishlist.getTotalItems());
      session.setAttribute("listIdProducts", listIdProducts);
      return "redirect:/wishlist";
    }
    return null;
  }
}
