package com.selfcode.ecommerce2.controller.admin;

import com.selfcode.ecommerce2.dto.ProductDto;
import com.selfcode.ecommerce2.model.ImageProduct;
import com.selfcode.ecommerce2.model.Product;
import com.selfcode.ecommerce2.service.ImageService;
import com.selfcode.ecommerce2.service.impl.ProductServiceImpl;
import com.selfcode.ecommerce2.utils.ImageUpload;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Controller
public class ImageAdminController {
  @Autowired
  ProductServiceImpl productService;

  @Autowired
  ImageService imageService;

  @Autowired
  ImageUpload imageUpload;

  private final String resourcesPath = Paths.get("src/main/resources/static").toString();
  private final String UPLOAD_PATH_PRODUCT = resourcesPath + "/customer/images";

  @GetMapping("/admin/product/{id}/images")
  public String imagesProduct(Model model, @PathVariable("id") Long id, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    List<ImageProduct> imagesProduct = imageService.getListImagesByProductId(id);
    model.addAttribute("title", "Images Product");
    model.addAttribute("images", imagesProduct);
    model.addAttribute("size", imagesProduct.size());
    return "admin/images";
  }

  @GetMapping("/admin/product/add-image")
  public String addImagesProduct(Model model, @PathVariable("id") Long id, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    Product product = productService.getProductById(id).get();
    model.addAttribute("title", "Add Images Product");
    model.addAttribute("product", product);
    return "admin/add_image";
  }

  @PostMapping("/admin/product/{id}/add-image")
  public String addImagesProduct(Model model,
                                 @RequestParam("product") Product product,
                                 @RequestParam("extra_images") MultipartFile[] listImages,
                                 Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    if (listImages != null & listImages.length > 0) {
      try {
        for (MultipartFile image: listImages) {
          if (imageUpload.uploadImage(UPLOAD_PATH_PRODUCT, image)) {
            System.out.println("Upload image success");
          }
          ImageProduct img = new ImageProduct();
          img.setName(image.getOriginalFilename());
          img.setProduct(product);
          imageService.save(img);
        }

        return "redirect:/admin/product/images/" + product.getId();
      } catch (Exception ex) {
        ex.printStackTrace();
        return null;
      }
    }
    model.addAttribute("title", "Add Images Product");
    model.addAttribute("product", product);
    model.addAttribute("error", "Add images for product failed");
    return "admin/add_image";
  }

  @GetMapping("/admin/image/delete/{id}")
  public String deleteImage(Model model, @PathVariable("id") Long id, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    imageService.deleteImage(id);
    return "redirect:/admin/products";
  }
}
