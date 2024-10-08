package com.app.service.implementation;

import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.request.LoginRequest;
import com.app.controller.dto.request.RegisterUserRequest;
import com.app.controller.dto.response.LoginResponse;
import com.app.controller.dto.response.RegisterDoctorResponse;
import com.app.controller.dto.response.RegisterPatientResponse;
import com.app.controller.dto.response.RegisterUserResponse;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
import com.app.persistence.entity.User;
import com.app.persistence.repository.UserRepository;
import com.app.service.AuthService;
import com.app.util.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public RegisterUserResponse register(RegisterUserRequest request) {

        User user = createUserEntity(request);

        user.setEnabled(false);

        User response = userRepository.save(user);

        return createRegisterUserResponse(response);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.createAccessToken(authentication);

        return LoginResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public RegisterUserResponse enableUser(String token) {

        DecodedJWT decodedJWT = jwtUtils.validateToken(token);
        String username = jwtUtils.extractUsername(decodedJWT);

        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        user.setEnabled(true);
        User response = userRepository.save(user);

        return createRegisterUserResponse(response);
    }


    /** Non-override methods */

    private User createUserEntity(RegisterUserRequest request) {
        User userEntity;

        if(request.getRole() == RoleEnum.PATIENT) {
            userEntity = Patient.builder()
                    .address(request.getAddress())
                    .build();
        } else if(request.getRole() == RoleEnum.DOCTOR) {
            userEntity = Doctor.builder()
                    .speciality(request.getSpeciality())
                    .license(request.getLicense())
                    .officeAddress(request.getOfficeAddress())
                    .build();
        } else {
            throw new RuntimeException("Invalid role provided");
        }

        setCommonUserEntityFields(userEntity, request);

        return userEntity;
    }

    private void setCommonUserEntityFields(User userEntity, RegisterUserRequest request) {
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        userEntity.setBirthDate(request.getBirthDate());
        userEntity.setGender(request.getGender());
        userEntity.setPhone(request.getPhone());
    }

    private RegisterUserResponse createRegisterUserResponse(User user) {
        RegisterUserResponse response;

        if (user instanceof Patient) {
            response = RegisterPatientResponse.builder()
                    .address(((Patient) user).getAddress())
                    .build();
        } else if (user instanceof Doctor) {
            response = RegisterDoctorResponse.builder()
                    .license(((Doctor) user).getLicense())
                    .speciality(((Doctor) user).getSpeciality())
                    .officeAddress(((Doctor) user).getOfficeAddress())
                    .build();
        } else {
            throw new RuntimeException("The user could not be created");
        }

        setCommonResponseFields(response, user);

        return response;
    }

    private void setCommonResponseFields(RegisterUserResponse response, User user) {
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPassword(user.getPassword());
        response.setBirthDate(user.getBirthDate());
        response.setGender(user.getGender());
        response.setPhone(user.getPhone());
        response.setRole(
                user instanceof Patient ? RoleEnum.PATIENT : RoleEnum.DOCTOR
        );
        response.setEnabled(user.isEnabled());
    }
}
