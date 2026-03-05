package com.hms.user.api;

import com.hms.user.dto.LoginDTO;
import com.hms.user.dto.ResponseDTO;
import com.hms.user.dto.UserDTO;
import com.hms.user.exception.HmsException;
import com.hms.user.jwt.JwtUtil;
import com.hms.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
@CrossOrigin
public class UserAPI {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO) throws HmsException{
        userService.registerUser(userDTO);
        return new ResponseEntity<>(new ResponseDTO("Account created successfully."), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) throws HmsException{
//        System.out.println("Plain password from login: " + loginDTO.getPassword());
//        try{
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
//            );
//        }
//        catch(AuthenticationException e){
//            System.out.println("Throwing from here only");
//            throw new HmsException("INVALID_CREDENTIALS");
//        }
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
//        System.out.println("Password from UserDetails: " + userDetails.getPassword());
//        final String jwt = jwtUtil.generateToken(userDetails);
//        return new ResponseEntity<>(jwt,HttpStatus.OK);
        String jwt = userService.loginUser(loginDTO);
        return ResponseEntity.ok(jwt);
    }
}
