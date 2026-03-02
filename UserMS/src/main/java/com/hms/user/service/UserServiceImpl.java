package com.hms.user.service;

import com.hms.user.dto.LoginDTO;
import com.hms.user.dto.UserDTO;
import com.hms.user.entity.User;
import com.hms.user.exception.HmsException;
import com.hms.user.jwt.CustomUserDetails;
import com.hms.user.jwt.JwtUtil;
import com.hms.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private APIService apiService;

    @Override
    public void registerUser(UserDTO userDTO) throws HmsException{
        Optional<User> opt = userRepository.findByEmail(userDTO.getEmail());
        if(opt.isPresent()){
            throw new HmsException("USER_ALREADY_EXISTS");
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Long profileId = apiService.addProfile(userDTO).block();
        System.out.println(profileId);
        userDTO.setProfileId(profileId);
        userRepository.save(userDTO.toEntity());
    }

//    @Override
//    public UserDTO loginUser(UserDTO userDTO) throws HmsException{
//         User user = userRepository.findByEmail(userDTO.getEmail()).orElseThrow(()->new HmsException("USER_NOT_FOUND"));
//         if(!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())){
//             throw new HmsException("INVALID_CREDENTIALS");
//         }
//         user.setPassword(null);
//         return user.toDTO();
//    }

     @Override
       public String loginUser(LoginDTO loginDTO) throws HmsException{
         // 1. Fetch user
         User user = userRepository.findByEmail(loginDTO.getEmail())
                 .orElseThrow(() -> new HmsException("USER_NOT_FOUND"));

         // 2. Validate password
         if (!passwordEncoder.matches(
                 loginDTO.getPassword(),
                 user.getPassword())) {
             throw new HmsException("INVALID_CREDENTIALS");
         }

         // 3. Build CustomUserDetails manually
         CustomUserDetails userDetails = new CustomUserDetails(
                 user.getId(),
                 user.getEmail(),          // username
                 user.getPassword(),
                 user.getRole(),
                 user.getName(),
                 user.getEmail(),
                 user.getProfileId(),
                 List.of()                 // authorities (optional)
         );

         // 4. Generate JWT
         return jwtUtil.generateToken(userDetails);
     }

    @Override
    public UserDTO getUserById(Long id) throws HmsException{
        return userRepository.findById(id).orElseThrow(()->new HmsException("USER_NOT_FOUND")).toDTO();
    }

    @Override
    public void updateUser(UserDTO userDTO) {

    }

    @Override
    public UserDTO getUser(String email) throws HmsException{
        return userRepository.findByEmail(email).orElseThrow(()->new HmsException("USER_NOT_FOUND")).toDTO();
    }
}
