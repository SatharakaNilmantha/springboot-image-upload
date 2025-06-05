package com.example.project.service.serviceIMPL;

import com.example.project.Exception.NotFoundException;
import com.example.project.dto.UserDto;
import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserService;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public String saveUser(MultipartFile photo, String username, Integer age) {
        try {
            byte[] bytes = photo.getBytes();
            Blob blob = new SerialBlob(bytes);

            User user = new User();
            user.setUsername(username);
            user.setAge(age);
            user.setPhoto(blob);

            userRepository.save(user);
            return "User saved successfully";
        } catch (Exception e) {
            return "Error while saving user: " + e.getMessage();
        }
    }

    @Override
    public List<UserDto> viewAll() {
        List<UserDto> dtoList = new ArrayList<>();
        try {
            List<User> userList = userRepository.findAll();

            if (userList == null || userList.isEmpty()) {
                System.out.println("No users found.");
                return dtoList;
            }

            for (User user : userList) {
                UserDto dto = modelMapper.map(user, UserDto.class);
                if (user.getPhoto() != null) {
                    dto.setPhoto(user.getPhoto().getBytes(1, (int) user.getPhoto().length()));
                }
                dtoList.add(dto);
            }

        } catch (Exception e) {
            System.out.println("Error while fetching users: " + e.getMessage());
        }
        return dtoList;
    }

    @Override
    public byte[] getUserPhotoAsBytes(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            try {
                return user.getPhoto().getBytes(1, (int) user.getPhoto().length());
            } catch (Exception e) {
                throw new RuntimeException("Error reading photo bytes: " + e.getMessage());
            }
        } else {
            throw new NotFoundException("User not found with ID: " + id);
        }
    }

    @Override
    public String updateUser(Long id, MultipartFile photo, String username, Integer age) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User not found with ID: " + id);
        }

        try {
            User user = optionalUser.get();
            byte[] bytes = photo.getBytes();
            Blob blob = new SerialBlob(bytes);

            user.setPhoto(blob);
            user.setUsername(username);
            user.setAge(age);

            userRepository.save(user);
            return "User updated successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage());
        }
    }

    @Override
    public String deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User not found with ID: " + id);
        }

        int deletedCount = userRepository.deleteUserById(id);
        if (deletedCount == 1) {
            return "User deleted successfully";
        } else {
            return "Failed to delete user";
        }
    }
}
