package com.app.service.implementation;

import com.app.common.enums.TokenType;
import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.request.LoginRequest;
import com.app.controller.dto.request.RegisterUserRequest;
import com.app.controller.dto.request.ResetPassRequest;
import com.app.controller.dto.response.LoginResponse;
import com.app.controller.dto.response.DoctorRegistrationResponse;
import com.app.controller.dto.response.PatientRegistrationResponse;
import com.app.controller.dto.response.UserRegistrationResponse;
import com.app.exception.UserAlreadyEnabledException;
import com.app.exception.UserDoesNotExistException;
import com.app.exception.UserNotEnabledException;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
import com.app.persistence.entity.User;
import com.app.persistence.repository.UserRepository;
import com.app.service.AuthService;
import com.app.common.util.JwtUtils;
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
    public UserRegistrationResponse register(RegisterUserRequest request) {

        User user = createUserEntity(request);

        user.setEnabled(false);

        User response = userRepository.save(user);

        return createUserRegistrationResponse(response);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(UserDoesNotExistException::new);
        if(!user.isEnabled()) throw new UserNotEnabledException();

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
    public UserRegistrationResponse confirmUser(String token) {

        DecodedJWT decodedJWT = jwtUtils.validateToken(token, TokenType.CONFIRM);
        String username = jwtUtils.extractUsername(decodedJWT);

        User user = userRepository.findUserByEmail(username)
                .orElseThrow(UserDoesNotExistException::new);
        if(user.isEnabled()) throw new UserAlreadyEnabledException();

        user.setEnabled(true);
        User response = userRepository.save(user);

        return createUserRegistrationResponse(response);
    }

    @Override
    public String generateConfirmToken(String email) {
        return jwtUtils.createConfirmToken(email);
    }

    @Override
    public String generatePasswordResetToken(String email) {
        return jwtUtils.createResetPasswordToken(email);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    @Override
    public void resetPassword(ResetPassRequest request) {
        String token = request.getToken();
        String newPassword = request.getNewPassword();

        DecodedJWT decodedJWT = jwtUtils.validateToken(token, TokenType.RESET);
        String username = jwtUtils.extractUsername(decodedJWT);

        User user = userRepository.findUserByEmail(username)
                .orElseThrow(UserDoesNotExistException::new);
        if(!user.isEnabled()) throw new UserNotEnabledException();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
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

    private UserRegistrationResponse createUserRegistrationResponse(User user) {
        UserRegistrationResponse response;

        if (user instanceof Patient) {
            response = PatientRegistrationResponse.builder()
                    .address(((Patient) user).getAddress())
                    .build();
        } else if (user instanceof Doctor) {
            response = DoctorRegistrationResponse.builder()
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

    private void setCommonResponseFields(UserRegistrationResponse response, User user) {
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
