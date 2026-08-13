package com.nightluxe.core.service;

import com.nightluxe.core.dto.request.UpdateProfileRequestDTO;
import com.nightluxe.core.dto.response.UserResponseDTO;
import com.nightluxe.core.entity.User;
import com.nightluxe.core.exceptions.BadRequestException;
import com.nightluxe.core.mapper.UserMapper;
import com.nightluxe.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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

        if (requestDTO.password() == null || !passwordEncoder.matches(requestDTO.currentPassword(), user.getPasswordHash())){
            throw new BadRequestException("Current password doesn't match");
        }
        //validation for duplicate phone number
        if (requestDTO.phoneNumber() !=null && !requestDTO.phoneNumber().equals(user.getPhoneNumber())){
            if (userRepository.findByPhoneNumber(requestDTO.phoneNumber()).isPresent()){
                throw new IllegalArgumentException("This phone number is already in use by another user.");
            }
            user.setPhoneNumber(requestDTO.phoneNumber());
        }
        if (requestDTO.password() != null && !requestDTO.password().trim().isEmpty()){
            String encryptPassword = passwordEncoder.encode(requestDTO.password());
            user.setPasswordHash(encryptPassword);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDTO(updatedUser);
    }
}
