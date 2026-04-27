package com.nightluxe.core.service;


import com.nightluxe.core.Exceptions.IllegalArgument;
import com.nightluxe.core.dto.request.LoginRequestDTO;
import com.nightluxe.core.dto.response.TokenResponseDTO;
import com.nightluxe.core.entity.User;
import com.nightluxe.core.mapper.UserMapper;
import com.nightluxe.core.repository.UserRepository;
import com.nightluxe.core.dto.request.RegisterRequestDTO;
import com.nightluxe.core.dto.response.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; // need spring security configuration
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;



    public TokenResponseDTO login(LoginRequestDTO request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(()-> new UsernameNotFoundException("User nor found"));

        var jwtToken = jwtService.generateToken(user);
        return new TokenResponseDTO(jwtToken);
    }


    @Transactional
    public UserResponseDTO register(RegisterRequestDTO request) {
        //1. does email exists?
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgument("E-mail is already used by a user.");
        }

        //2. does phone number exists?
        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new IllegalArgument("Phone number is already used by a user.");
        }

        //3. 18+ verification

        if (Period.between(request.getBirthDate(), LocalDate.now()).getYears() < 18) {
            throw new IllegalArgument("You must have 18+ years to register");
        }



        // map to entity
        User user = userMapper.toEntity(request);

        // hash passowrd
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        // save to database
        User savedUser = userRepository.save(user);

        // return dto
        return userMapper.toResponseDTO(savedUser);

    }
}
