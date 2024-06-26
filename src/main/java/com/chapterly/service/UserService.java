package com.chapterly.service;

import com.chapterly.dto.AuthenticationResponse;
import com.chapterly.dto.UserDto;
import com.chapterly.entity.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    public AuthenticationResponse createUser(MultipartFile file, String data) throws JsonMappingException, JsonParseException, FileUploadException;
    public UserDto getUserById(Long userId);
    public UserDto updateProfileById(String userId, UserDto userDto);
    public boolean deleteUserById(Long userId);
    public UserDto findByUsername(String username);
    public UserDto findByEmail(String email);
    public boolean changePassword(Long userid, String password);
    public List<UserDto> getAllUsers();
    public boolean activateAccount(Long userid);
    public boolean deactivateAccount(Long userId);
    public boolean isAccountActive(Long userId);
    public List<UserDto> findActiveUsers();
    User getUserBYUsername(String username);

    User userByEmail(String username);

    UserDto getPersonalDetails();

    boolean updatePersonalDetails(String username, UserDto userRequest);
}
