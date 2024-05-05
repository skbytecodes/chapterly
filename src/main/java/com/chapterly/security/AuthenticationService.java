package com.chapterly.security;

import com.chapterly.dto.AuthenticationRequest;
import com.chapterly.dto.AuthenticationResponse;
import com.chapterly.dto.ChangePassword;
import com.chapterly.dto.RegisterRequest;
import com.chapterly.entity.User;
import com.chapterly.repository.UserRepo;
import com.chapterly.service.UserService;
import com.chapterly.util.UniqueUsernameGenerator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepo repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = new User();
        UniqueUsernameGenerator usernameGenerator = new UniqueUsernameGenerator();
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPwd(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setAccount_creation_date(LocalDateTime.now());
        user.setUserName(usernameGenerator.generateUniqueUsername(request.getFirstname(), request.getLastname()));
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var AuthenticationResponse = new AuthenticationResponse();
        AuthenticationResponse.setAccessToken(jwtToken);
        return AuthenticationResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            var AuthenticationResponse = new AuthenticationResponse();
            AuthenticationResponse.setAccessToken(jwtToken);
            return AuthenticationResponse;
        }catch (AuthenticationException e){
            throw new IllegalStateException("AUTHENTICATION FAILED: " + e.getMessage());
        } catch (Exception e){
            throw new RuntimeException("SOMETHING WENT WRONG");
        }
    }

    public boolean changePassword(ChangePassword changePasswordRequest, Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken)principal).getPrincipal();
        if(user != null){
            if(passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPwd())){
                boolean equals = changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword());
                if(equals){
                   user.setPwd(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                   repository.save(user);
                   return true;
                }else {
                    throw new IllegalStateException("PASSWORDS DON'T MATCH");
                }
            }else{
                throw new IllegalStateException("WRONG PASSWORD");
            }
        }
        return false;
    }
}
