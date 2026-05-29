package com.nightluxe.core.service;

import com.nightluxe.core.dto.request.UpdateProfileRequestDTO;
import com.nightluxe.core.dto.response.UserResponseDTO;
import com.nightluxe.core.entity.User;
import com.nightluxe.core.mapper.UserMapper;
import com.nightluxe.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponseDTO getProfileByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with this email"));

        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateProfile(String email, UpdateProfileRequestDTO requestDTO){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with this email"));

        //validation for duplicate phone number
        if (requestDTO.phoneNumber() !=null && !requestDTO.phoneNumber().equals(user.getPhoneNumber())){
            if (userRepository.findByPhoneNumber(requestDTO.phoneNumber()).isPresent()){
                throw new IllegalArgumentException("This phone number is already in use by another user.");
            }
            user.setPhoneNumber(requestDTO.phoneNumber());
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDTO(updatedUser);
    }
}
