package com.banking.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.dto.UserRequestDto;
import com.banking.entity.User;
import com.banking.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(UserRequestDto user) {
    	User userName = new User();
    	userName.setName(user.getName());
    	userName.setPassword(user.getPassword());
        return userRepository.save(userName);
    }
   
}
