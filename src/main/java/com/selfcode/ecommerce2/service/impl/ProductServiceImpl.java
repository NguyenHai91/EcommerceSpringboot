package com.selfcode.ecommerce2.service.impl;

import com.selfcode.ecommerce2.dto.ProductDto;
import com.selfcode.ecommerce2.model.Category;
import com.selfcode.ecommerce2.model.ImageProduct;
import com.selfcode.ecommerce2.model.Product;
import com.selfcode.ecommerce2.repository.ImageProductRepository;
import com.selfcode.ecommerce2.repository.ProductRepository;
import com.selfcode.ecommerce2.service.CategoryService;
import com.selfcode.ecommerce2.service.ProductService;
import com.selfcode.ecommerce2.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;



@Service
public class ProductServiceImpl implements ProductService {
  @Autowired
  ProductRepository productRepository;

  @Autowired
  CategoryService categoryService;

  @Autowired
  ImageProductRepository imageProductRepository;

  @Autowired
  ImageUpload imageUpload;

  private final String resourcesPath = Paths.get("src/main/resources/static").toString();
  private final String UPLOAD_PATH_PRODUCT = resourcesPath + "/customer/images";

  @Override
  public List<ProductDto> findAll() {
    List<Product> products = productRepository.findAll();
    List<ProductDto> listProductsDto = new ArrayList<>();
    for (Product product : products) {
      ProductDto productDto = new ProductDto();
      productDto.setId(product.getId());
      productDto.setName(product.getName());
      productDto.setDescription(product.getDescription());
      productDto.setCostPrice(product.getCostPrice());
      productDto.setSalePrice(product.getSalePrice());
      productDto.setCurrentQuantity(product.getCurrentQuantity());
      productDto.setCategory(product.getCategory());
      productDto.setImage(product.getImage());
      productDto.setActived(product.is_actived());
      productDto.setDeleted(product.is_deleted());
      listProductsDto.add(productDto);
    }
    return listProductsDto;
  }

  @Override
  public Product save(MultipartFile imageProduct, MultipartFile[] listImages, ProductDto productDto) {
    Product newProduct = new Product();

    try {
      if (imageProduct == null) {
        newProduct.setImage(null);
      } else {
        if (imageUpload.uploadImage(UPLOAD_PATH_PRODUCT, imageProduct)) {
          System.out.println("Upload image success");
        }
        newProduct.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
      }

      newProduct.setName(productDto.getName());
      newProduct.setCategory(productDto.getCategory());
      newProduct.setDescription(productDto.getDescription());
      newProduct.setCurrentQuantity(productDto.getCurrentQuantity());
      newProduct.setQuantity(productDto.getCurrentQuantity());
      newProduct.setViews(productDto.getViews());
      newProduct.setTax(productDto.getTax());
      newProduct.set_actived(productDto.isActived());
      newProduct.set_deleted(productDto.isDeleted());
      newProduct.setCostPrice(productDto.getCostPrice());
      double salePrice = productDto.getCostPrice();
      salePrice += (productDto.getCostPrice() * productDto.getTax()) / 100;
      salePrice = Math.round(salePrice);
      newProduct.setSalePrice(salePrice);
      newProduct = productRepository.save(newProduct);

      if (listImages == null) {
        newProduct.setListImages(null);
      } else {
        for (MultipartFile image: listImages) {
          if (imageUpload.uploadImage(UPLOAD_PATH_PRODUCT, image)) {
            System.out.println("Upload image success");
          }
          ImageProduct img = new ImageProduct();
          img.setName(Base64.getEncoder().encodeToString(image.getBytes()));
          img.setProduct(newProduct);
          imageProductRepository.save(img);
        }
      }
      return newProduct;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public ProductDto getById(Long id) {
    Product product = productRepository.getById(id);
    ProductDto productDto = new ProductDto();
    productDto.setId(product.getId());
    productDto.setName(product.getName());
    productDto.setCategory(product.getCategory());
    productDto.setDescription(product.getDescription());
    productDto.setCostPrice(product.getCostPrice());
    productDto.setSalePrice(product.getSalePrice());
    productDto.setCurrentQuantity(product.getCurrentQuantity());
    productDto.setQuantity(product.getCurrentQuantity());
    productDto.setViews(product.getViews());
    productDto.setTax(product.getTax());
    productDto.setActived(product.is_actived());
    productDto.setDeleted(productDto.isDeleted());
    productDto.setImage(product.getImage());

    List<String> listImages = new ArrayList<>();
    for (ImageProduct image: product.getListImages()) {
      listImages.add(image.getName());
    }
    productDto.setListImages(listImages);
    return productDto;
  }

  @Override
  public Optional<Product> getProductById(Long id) {
    return productRepository.findById(id);
  }

  @Override
  public Product update(ProductDto productDto, MultipartFile imageProduct, MultipartFile[] extraImages) {
    Product product = productRepository.getById(productDto.getId());
    try {
      if (imageProduct == null || imageProduct.isEmpty()) {
        product.setImage(product.getImage());
      } else {
        if (imageUpload.checkExisted(UPLOAD_PATH_PRODUCT, imageProduct) == false) {
          imageUpload.uploadImage(UPLOAD_PATH_PRODUCT, imageProduct);
          System.out.println("Upload image success");
        }
        product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
      }

      if (extraImages != null) {
        for (MultipartFile image: extraImages) {
          if (imageUpload.uploadImage(UPLOAD_PATH_PRODUCT, image)) {
            System.out.println("Upload image success");
          }
          ImageProduct img = new ImageProduct();
          img.setName(Base64.getEncoder().encodeToString(image.getBytes()));
          img.setProduct(product);
          imageProductRepository.save(img);
        }
      }

      product.setName(productDto.getName());
      product.setCategory(productDto.getCategory());
      product.setDescription(productDto.getDescription());
      product.setCostPrice(productDto.getCostPrice());
      double salePrice = productDto.getCostPrice();
      salePrice += (productDto.getCostPrice() * productDto.getTax()) / 100;
      salePrice = Math.round(salePrice);
      product.setSalePrice(salePrice);
      product.setCurrentQuantity(productDto.getCurrentQuantity());
      product.setQuantity(productDto.getQuantity());
      product.setViews(productDto.getViews());
      product.setTax(productDto.getTax());
      product.set_actived(productDto.isActived());
      product.set_deleted(productDto.isDeleted());
      return productRepository.save(product);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void increaseViewsProduct(Long id) {
    Product product = productRepository.getById(id);
    try {
      product.setViews(product.getViews() + 1);
      productRepository.save(product);
    } catch (Exception e) {
    }
  }

  @Override
  public void disableById(Long id) {
    Product product = productRepository.getById(id);
    product.set_deleted(true);
    product.set_actived(false);
    productRepository.save(product);
  }

  @Override
  public void enableById(Long id) {
    Product product = productRepository.getById(id);
    product.set_actived(true);
    product.set_deleted(false);
    productRepository.save(product);
  }

  @Override
  public void deleteById(Long id) {
    productRepository.deleteById(id);
  }

  @Override
  public Page<ProductDto> pageProducts(int pageNo) {
    Pageable pageable = PageRequest.of(pageNo, 5);
    List<ProductDto> productDtoList = transfer(productRepository.findAll());
    Page<ProductDto> productPages = toPage(productDtoList, pageable);
    return productPages;
  }

  @Override
  public List<Product> searchProducts(String keyword) {
    List<Product> products = productRepository.searchProducts(keyword);
    return products;
  }

  @Override
  public Page<ProductDto> searchProducts(int pageNo, String keyword) {
    Pageable pageable = PageRequest.of(pageNo, 5);
    List<ProductDto> productDtoList = transfer(productRepository.searchProducts(keyword));
    Page<ProductDto> productsPage = toPage(productDtoList, pageable);
    return productsPage;
  }

  @Override
  public List<Product> getAllProducts() {
    return productRepository.getAllProducts();
  }

  @Override
  public List<ProductDto> getProductsWithMainCategories() {
    List<Category> mainCategories = categoryService.getMainCategories();
    List<Product> productsWithMainCategories = new ArrayList<>();
    for (Category category : mainCategories) {
      List<Product> products = productRepository.getProductsByCategoryId(category.getId());
      int numOfProducts = 4;
      if (products.size() < 4) {
        numOfProducts = products.size();
      }
      for (int i = 0; i < numOfProducts; i++) {
        productsWithMainCategories.add(products.get(i));
      }
    }
    List<ProductDto> productsDto = transfer(productsWithMainCategories);
    return productsDto;
  }

  @Override
  public List<Product> listViewProducts() {
    return productRepository.listViewProducts();
  }

  @Override
  public List<Product> getRelatedProducts(Long categoryId) {
    return productRepository.getRelatedProducts(categoryId);
  }

  @Override
  public List<Product> getProductsByCategoryId(Long categoryId) {
    return productRepository.getProductsByCategoryId(categoryId);
  }

  @Override
  public List<Product> sortProductHighPrice() {
    return productRepository.sortProductHighPrice();
  }

  @Override
  public List<Product> sortProductLowPrice() {
    return productRepository.sortProductLowPrice();
  }

  private Page toPage(List<ProductDto> list, Pageable pageable) {
    if (pageable.getOffset() >= list.size()) {
      return Page.empty();
    }
    int startIndex = (int) pageable.getOffset();
    int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
        ? list.size()
        : (int) (pageable.getOffset() + pageable.getPageSize());
    List subList = list.subList(startIndex, endIndex);
    return new PageImpl(subList, pageable, list.size());
  }

  private List<ProductDto> transfer(List<Product> products) {
    List<ProductDto> productDtoList = new ArrayList<>();
    for (Product product : products) {
      ProductDto productDto = new ProductDto();
      productDto.setId(product.getId());
      productDto.setName(product.getName());
      productDto.setDescription(product.getDescription());
      productDto.setCurrentQuantity(product.getCurrentQuantity());
      productDto.setCategory(product.getCategory());
      productDto.setSalePrice(product.getSalePrice());
      productDto.setCostPrice(product.getCostPrice());
      productDto.setImage(product.getImage());
      productDto.setViews(product.getViews());
      productDto.setTax(product.getTax());
      productDto.setDeleted(product.is_deleted());
      productDto.setActived(product.is_actived());
      productDtoList.add(productDto);
    }
    return productDtoList;
  }

  private Product toProduct(ProductDto productDto, Product product) {
    product.setId(productDto.getId());
    product.setName(productDto.getName());
    product.setCategory(productDto.getCategory());
    product.setDescription(productDto.getDescription());
    product.setCostPrice(productDto.getCostPrice());
    product.setSalePrice(productDto.getSalePrice());
    product.setCurrentQuantity(productDto.getCurrentQuantity());
    product.setQuantity(productDto.getQuantity());
    product.setViews(productDto.getViews());
    product.setTax(productDto.getTax());
    product.set_actived(productDto.isActived());
    product.set_deleted(productDto.isDeleted());
    return product;
  }
}
