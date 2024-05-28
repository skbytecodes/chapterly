package com.chapterly.security;

import com.chapterly.dto.AuthenticationRequest;
import com.chapterly.dto.AuthenticationResponse;
import com.chapterly.dto.ChangePassword;
import com.chapterly.dto.RegisterRequest;
import com.chapterly.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000/")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }


    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePassword changePasswordRequest, Principal principal){
       try {
            boolean isPasswordChanged = service.changePassword(changePasswordRequest, principal);
           return new ResponseEntity<>(isPasswordChanged, HttpStatus.OK);
       }catch (IllegalStateException e){
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }catch (Exception e) {
           return new ResponseEntity<>("SOMETHING WENT WRONG", HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

}
