package com.hms.user.service;

import com.hms.user.dto.LoginDTO;
import com.hms.user.dto.UserDTO;
import com.hms.user.exception.HmsException;

public interface UserService {

    public void registerUser(UserDTO userDTO) throws HmsException;

    public String loginUser(LoginDTO loginDTO) throws HmsException;

    public UserDTO getUserById(Long id) throws HmsException;

    public void updateUser(UserDTO userDTO);

    public UserDTO getUser(String email) throws HmsException;

}
