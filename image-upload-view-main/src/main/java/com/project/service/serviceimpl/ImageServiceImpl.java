package com.project.service.serviceimpl;


import com.project.Exception.NotFoundException;
import com.project.dto.ImageDto;
import com.project.entity.Image;
import com.project.repository.ImageRepository;
import com.project.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ModelMapper modelMapper;


    public String saveImage(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            Blob blob = new SerialBlob(bytes);

            Image image = new Image();
            image.setImage(blob);

            imageRepository.save(image);
            return "Image uploaded successfully";

        } catch (Exception e) {
            return ("Error while saving image: " + e.getMessage());
        }
    }


    public List<ImageDto> viewAll() {
        List<ImageDto> dtoList = new ArrayList<>();
        try {
            List<Image> imageList = imageRepository.findAll();

            if (imageList == null || imageList.isEmpty()) {
                System.out.println("No images found in the database.");
                return dtoList;
            }

            for (Image img : imageList) {
                byte[] imageBytes = img.getImage().getBytes(1, (int) img.getImage().length());
                ImageDto dto = new ImageDto(img.getId(), imageBytes, img.getDate());
                dtoList.add(dto);
            }

        } catch (Exception e) {
            System.out.println("Error while fetching images: " + e.getMessage());
        }

        return dtoList;
    }



    public byte[] getImageAsBytes(long id) {

        Optional<Image> optionalImage = imageRepository.findById(id);

        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();
            try {
                return image.getImage().getBytes(1, (int) image.getImage().length());
            } catch (Exception e) {
                throw new RuntimeException("Error reading image bytes: " + e.getMessage());
            }
        } else {
            throw new NotFoundException("Image not found with ID: " + id);
        }
    }



    public String updateImage(Long id, MultipartFile file) {
        Optional<Image> optionalImage = imageRepository.findById(id);

        if (!optionalImage.isPresent()) {
            throw new NotFoundException("Image not found with ID: " + id);
        }

        try {
            Image image = optionalImage.get();
            byte[] bytes = file.getBytes();
            Blob blob = new SerialBlob(bytes);
            image.setImage(blob);
            imageRepository.save(image);
            return "Image updated successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error updating image: " + e.getMessage());
        }
    }

    @Override
    public String deleteImage(Long id) {
        Optional<Image> optionalImage = imageRepository.findById(id);
        if (!optionalImage.isPresent()) {
            throw new NotFoundException("Image not found with ID: " + id);
        }

        int deletedCount = imageRepository.deleteImageById(id);
        if (deletedCount == 1) {
            return "Image deleted successfully";
        } else {
            return "Failed to delete image";
        }
    }


}
