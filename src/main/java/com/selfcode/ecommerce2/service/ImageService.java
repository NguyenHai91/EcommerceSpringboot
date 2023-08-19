package com.selfcode.ecommerce2.service;

import com.selfcode.ecommerce2.model.ImageProduct;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ImageService {
  ImageProduct save(ImageProduct image);
  List<ImageProduct> listImageProduct();
  ImageProduct getImageById(Long id);
  List<ImageProduct> getListImagesByProductId(Long id);
  void deleteImage(Long id);
}
