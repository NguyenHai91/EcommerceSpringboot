package com.selfcode.ecommerce2.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUpload {

  public boolean uploadImage(String uploadFolder, MultipartFile imageProduct){
    boolean isUpload = false;
    if (this.checkExisted(uploadFolder, imageProduct)) return true;
    try {
      Files.copy(imageProduct.getInputStream(),
          Paths.get( uploadFolder + File.separator, imageProduct.getOriginalFilename()),
          StandardCopyOption.REPLACE_EXISTING);
      isUpload = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return isUpload;
  }

  public boolean checkExisted(String uploadFolder, MultipartFile imageProduct) {
    boolean isExisted = false;
    try {
      File file = new File(uploadFolder + File.separator + imageProduct.getOriginalFilename());
      isExisted = file.exists();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return isExisted;
  }
}
