package com.project.service;

import com.project.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    public String saveImage(MultipartFile file);
    List<ImageDto> viewAll();
    public byte[] getImageAsBytes(long id);
    String deleteImage(Long id);
    String updateImage(Long id, MultipartFile file);
}

