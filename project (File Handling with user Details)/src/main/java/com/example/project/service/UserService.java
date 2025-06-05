package com.example.project.service;

import com.example.project.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;




public interface UserService {

   String saveUser(MultipartFile photo, String username, Integer age);

   List<UserDto> viewAll();

   byte[] getUserPhotoAsBytes(long id);

   String updateUser(Long id, MultipartFile photo, String username, Integer age);

   String deleteUser(Long id);
}