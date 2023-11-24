package com.series.movies.service;

import com.series.movies.dto.*;
import com.series.movies.dto.RegistrationBody;
import com.series.movies.dto.UserLoginResponse;
import com.series.movies.exception.UserAlreadyExistsException;
import com.series.movies.model.LocalUser;
import com.series.movies.model.UserType;
import com.series.movies.model.dao.LocalUserRepository;
import com.series.movies.security.JWTGenerator;
import com.series.movies.security.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Autowired
    private LocalUserRepository localUserRepository;
    @Autowired
    WebSecurityConfig webSecurityConfig;
    @Autowired
    private UserTypeService userTypeService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {
        if (localUserRepository.findByEmail(registrationBody.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setUserName(registrationBody.getUserName());
        user.setPassword(webSecurityConfig.passwordEncoder().encode(registrationBody.getPassword()));
        user = localUserRepository.save(user);
        return localUserRepository.save(user);
    }

    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody LoginBody loginBody) {
        System.out.println("userLogin");
        userTypeService.setUserType(UserType.LOCALUSER);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginBody.getEmail(), loginBody.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication, UserType.LOCALUSER.toString());
        UserLoginResponse response= new UserLoginResponse();
        LocalUser user = localUserRepository.findByEmail(loginBody.getEmail()).orElseThrow();
        String encodedPassword = user.getPassword();
        String passedPassword = loginBody.getPassword();
        boolean passwordsMatch = passwordEncoder.matches(passedPassword, encodedPassword);
        if(passwordsMatch){
            response.setMessage("login successful");
            response.setToken(token);
            response.setUser(user.getUserName(), user.getEmail(), user.getId());
            return new ResponseEntity<UserLoginResponse>(response, HttpStatus.OK);
        }
        response.setMessage("Incorrect PhoneNumber or password");
        return new ResponseEntity<UserLoginResponse>(response, HttpStatus.UNAUTHORIZED);
    }
}
