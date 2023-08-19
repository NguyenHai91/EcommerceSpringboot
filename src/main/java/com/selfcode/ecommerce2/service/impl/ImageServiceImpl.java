package com.selfcode.ecommerce2.service.impl;

import com.selfcode.ecommerce2.model.ImageProduct;
import com.selfcode.ecommerce2.repository.ImageProductRepository;
import com.selfcode.ecommerce2.repository.ProductRepository;
import com.selfcode.ecommerce2.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
  @Autowired
  private ImageProductRepository imageProductRepository;

  @Autowired
  private ProductRepository productRepository;

  @Override
  public ImageProduct save(ImageProduct image) {
    return imageProductRepository.save(image);
  }

  @Override
  public List<ImageProduct> listImageProduct() {
    return imageProductRepository.findAll();
  }

  @Override
  public ImageProduct getImageById(Long id) {
    return imageProductRepository.findById(id).get();
  }

  @Override
  public List<ImageProduct> getListImagesByProductId(Long id) {
    return imageProductRepository.findByProductId(id);
  }

  @Override
  public void deleteImage(Long id) {
    imageProductRepository.deleteById(id);
  }
}
