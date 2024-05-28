package com.chapterly.controller;

import com.chapterly.dto.AuthenticationResponse;
import com.chapterly.dto.UserDto;
import com.chapterly.mapper.UserMapper;
import com.chapterly.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestParam(value = "file",required = false)MultipartFile file, @RequestParam("data") @Valid String data){
        try {
            AuthenticationResponse response = userService.createUser(file, data);
            if(response == null)
                return new ResponseEntity<>("NOT FOUND", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (JsonMappingException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (JsonParseException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long userId){
        if(userId != null){
            UserDto userById = null;
            try {
                userById = userService.getUserById(userId);
            }catch (Exception e){
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(userById != null)
                return new ResponseEntity<>(userById, HttpStatus.OK);
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/user/personalDetails")
    public ResponseEntity<?> personalDetails(){
            UserDto user = null;
            try {
                user = userService.getPersonalDetails();
            }catch (Exception e){
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(user != null)
                return new ResponseEntity<>(user, HttpStatus.OK);
            else
                return new ResponseEntity<>("NOT FOUND", HttpStatus.NOT_FOUND);
    }


    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable("id") String userId, UserDto userRequest){
        if(userId != null){
            UserDto user = null;
            try {
                user = userService.updateProfileById(userId, userRequest);
            }catch (Exception e){
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(user != null)
                return new ResponseEntity<>(user, HttpStatus.OK);
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PutMapping("/user/updatePersonalSetting/{username}")
    public ResponseEntity<?> updatePersonalSettings(@PathVariable("username") String username, @RequestBody UserDto userRequest){
        if(username != null && userRequest != null){
            boolean response = false;
            try {
                response = userService.updatePersonalDetails(username, userRequest);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("BAD REQUEST", HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long userId){
        if(userId != null){
            boolean res = false;
            try {
                res = userService.deleteUserById(userId);
            }catch (Exception e){
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(res)
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable("username") String username){
        if(username != null && username.length() > 0){
            UserDto user = null;
            try {
                user = userService.findByUsername(username);
            }catch (Exception e){
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(user != null)
                return new ResponseEntity<>(user, HttpStatus.OK);
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/user/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable("email") String email){
        if(email != null && email.length() > 0){
            UserDto user = null;
            try {
                user = userService.findByEmail(email);
            }catch (Exception e){
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(user != null)
                return new ResponseEntity<>(user, HttpStatus.OK);
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserDto> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/isAccountActive/{id}")
    public ResponseEntity<Object> isAccountActive(@PathVariable("id") Long userId){
        if(userId != null){
            boolean res = false;
            try {
                res = userService.isAccountActive(userId);
                return new ResponseEntity<>(res, HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/deActivateAccount/{id}")
    public ResponseEntity<Object> deactivateAccount(@PathVariable("id") Long userId){
        if(userId != null){
            boolean res = false;
            try {
                res = userService.deactivateAccount(userId);
            }catch (Exception e){
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(res)
                return new ResponseEntity<>("DEACTIVATED", HttpStatus.OK);
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/activateAccount/{id}")
    public ResponseEntity<Object> activateAccount(@PathVariable("id") Long userId){
        if(userId != null){
            boolean res = false;
            try {
                res = userService.activateAccount(userId);
            }catch (Exception e){
                return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(res)
                return new ResponseEntity<>("ACTIVATED", HttpStatus.OK);
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/activeUsers")
    public ResponseEntity<?> getAllActiveAccounts() {
        try {
            List<UserDto> users = userService.findActiveUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
