package com.chapterly.serviceImpl;

import com.chapterly.aws.AmazonClient;
import com.chapterly.dto.UserDto;
import com.chapterly.entity.User;
import com.chapterly.mapper.UserMapper;
import com.chapterly.repository.UserRepo;
import com.chapterly.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AmazonClient amazonClient;
    @Autowired
    private UserRepo userRepo;

    @Value("${s3.endpointUrl}")
    private String parentUrl;

    @Autowired
    private UserMapper userMapper;

    Logger logger = LoggerFactory.getLogger("UserServiceImpl");


    @Override
    public UserDto createUser(MultipartFile file, String data) {
        UserDto response = null;
        if (data != null && file != null) {
            User user = null;
            try {
                ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
                user = mapper.readValue(data, User.class);
            } catch (JsonMappingException e) {
                logger.error("ERROR ", e);
            } catch (JsonProcessingException e) {
                logger.error("ERROR ", e);
            }

            try {
                String fileDownloadUri = amazonClient.uploadFile(file);
                user.setImageUrl(fileDownloadUri);
                user.setImageName(file.getOriginalFilename());
            } catch (FileUploadException e) {
                logger.error("ERROR", e);
            }catch (Exception e){
                logger.error("ERROR", e);
            }
            if(user.getAddress() == null)
                return null;
            if(user.getShippingAddress() != null)
                user.getShippingAddress().setUser(user);

            user.getAddress().setUser(user);
            user.setAccountActive(true);
            userRepo.save(user);
            response = userMapper.toDto(user);
        }
        return response;
    }

    @Override
    public UserDto getUserById(Long userId) {
        if(userId != null){
            return userMapper.toDto(userRepo.findById(userId).get());
        }
        return null;
    }

    @Override
    public UserDto updateProfileById(String userId, UserDto userDto) {
        if (userId != null && userDto != null) {
            User user = userRepo.findByUserName(userId);
            user.setShippingAddress(userDto.getShippingAddress());
            user.setAddress(userDto.getAddress());
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPhone(userDto.getPhone());
            user.setPwd(userDto.getPassword());

            userMapper.toDto(userRepo.save(user));
        }
        return null;
    }

    @Override
    public boolean deleteUserById(Long userId) {
        if(userId != null){
            User user = userRepo.findById(userId).get();
            if(user != null){
                userRepo.deleteById(userId);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public UserDto findByUsername(String username) {
        if(username != null && username.length() > 0){
           User user = userRepo.findByUserName(username);
           if(user != null)
               return userMapper.toDto(user);
           return null;
        }
        return null;
    }

    @Override
    public UserDto findByEmail(String email) {
        if(email != null && email.length() > 0){
            User user = userRepo.findByEmail(email).get();
            if(user != null)
                return userMapper.toDto(user);
            return null;
        }
        return null;
    }

    @Override
    public boolean changePassword(Long userId,String password) {
        if(password != null && password.length() > 0){
            User user = userRepo.findById(userId).get();
            user.setPwd(password);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> allUsers = userRepo.findAll();
        List<UserDto> users = allUsers.stream().map(userObj -> {
            UserDto user = userMapper.toDto(userObj);
            user.setImageUrl(parentUrl+"/"+userObj.getImageUrl());
            return user;
        }).collect(Collectors.toList());
        if(allUsers.size() > 0) {
            return users;
        }
        return new ArrayList<>();
    }

    @Override
    public boolean activateAccount(Long userId) {
        if(userId != null){
            User user = userRepo.findById(userId).get();
            if (user != null){
                user.setAccountActive(true);
                userRepo.save(user);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deactivateAccount(Long userId) {
        if(userId != null){
            User user = userRepo.findById(userId).get();
            if (user != null){
                user.setAccountActive(false);
                userRepo.save(user);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isAccountActive(Long userId) {
        if(userId != null){
            User user = userRepo.findById(userId).get();
            if(user.isAccountActive())
                return true;
            return false;
        }
        return false;
    }

    @Override
    public List<UserDto> findActiveUsers() {
        List<User> allUsers = userRepo.findAllActiveUsers();
        List<UserDto> users = allUsers.stream().map(user -> {
            return userMapper.toDto(user);
        }).collect(Collectors.toList());
        if(allUsers.size() > 0)
            return users;
        return new ArrayList<>();
    }

    @Override
    public User getUserBYUsername(String username) {
        if(username != null && username.length() > 0){
            return userRepo.findByUserName(username);
        }
        return null;
    }

    @Override
    public User userByEmail(String email) {
        if(email != null && email.length() > 3){
            return userRepo.findByEmail(email).get();
        }
        return null;
    }

    @Override
    public UserDto getPersonalDetails() {
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            username = authentication.getName();
        }

        if(username != null && username.length() > 0){
            Optional<User> user = userRepo.findByEmail(username);
            if(user.isPresent()){
                UserDto userDetails = new UserDto();
                userDetails.setFirstName(user.get().getFirstName());
                userDetails.setLastName(user.get().getLastName());
                userDetails.setEmail(user.get().getEmail());
                userDetails.setPhone(user.get().getPhone());
                return userDetails;
            }
            return null;
        }
        return null;
    }

    @Override
    public boolean updatePersonalDetails(String username, UserDto userRequest) {
        try {
            Optional<User> byEmail = userRepo.findByEmail(username);
            boolean present = byEmail.isPresent();
            User user = null;
            if(present){
                user = byEmail.get();
                user.setFirstName(userRequest.getFirstName());
                user.setLastName(userRequest.getLastName());
                user.setEmail(userRequest.getEmail());
                user.setPhone(userRequest.getPhone());
                userRepo.save(user);
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
}
