package com.project.controller;

import com.project.Exception.NotFoundException;
import com.project.dto.ImageDto;
import com.project.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images") // clearer API base path
public class ClientController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/ping")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello World!");
    }


    @PostMapping("save")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            String respons = imageService.saveImage(file) ;
            return ResponseEntity.ok(respons) ; // returns ResponseEntity
        }catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {
        try {
            byte[] image = imageService.getImageAsBytes(id);

            // Set Content-Type to image/jpeg (or image/png depending on your data)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // or MediaType.IMAGE_PNG

            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/getAllImages")
    public ResponseEntity<List<ImageDto>> getAllImages() {
        List<ImageDto> images = imageService.viewAll();
        return ResponseEntity.ok(images);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {
            String response = imageService.updateImage(id, file);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        try {
            String response = imageService.deleteImage(id);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
