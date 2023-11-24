package com.series.movies.controller;

import com.series.movies.dto.LoginBody;
import com.series.movies.dto.RegistrationBody;
import com.series.movies.dto.SuccessAndMessage;
import com.series.movies.dto.UserLoginResponse;
import com.series.movies.exception.UserAlreadyExistsException;
import com.series.movies.model.dao.AdminRepository;
import com.series.movies.model.dao.LocalUserRepository;
import com.series.movies.security.JWTGenerator;
import com.series.movies.service.UserService;
import com.series.movies.service.UserTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    UserService userService;
    @Autowired
    private UserTypeService userTypeService;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    LocalUserRepository localUserRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTGenerator jwtGenerator;
    SuccessAndMessage successAndMessage;


    @PostMapping("/registerUser")
    public ResponseEntity<SuccessAndMessage> registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            SuccessAndMessage response = new SuccessAndMessage();
            userService.registerUser(registrationBody);
            response.setMessage("User Registered Successfully");
            response.setSuccess(true);
            return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            SuccessAndMessage response = new SuccessAndMessage();
            response.setMessage("Email Already Registered");
            response.setSuccess(false);
            return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("loginUser")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody LoginBody loginBody) {
        System.out.println("userLogin");
        return userService.loginUser(loginBody);
    }
}
