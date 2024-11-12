package com.app.service.implementation;

import com.app.common.util.DateUtils;
import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.request.UserProfileRequest;
import com.app.controller.dto.response.DoctorProfileResponse;
import com.app.controller.dto.response.PatientProfileResponse;
import com.app.controller.dto.response.UserProfileResponse;
import com.app.exception.UserDoesNotExistException;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
import com.app.persistence.entity.User;
import com.app.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(username);
        if (user.isEmpty())
            throw new UserDoesNotExistException();
        return user.get();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserDoesNotExistException(
                        String.format("User with ID %d does not exist.", id)
                )
        );
    }

    public UserProfileResponse updateUserProfile(
            UserProfileRequest request,
            User user
    ) {
        if (user instanceof Doctor) {
            Doctor doctor = (Doctor) user;
            doctor.setSpeciality(request.getSpeciality());
            doctor.setLicense(request.getLicense());
            doctor.setOfficeAddress(request.getOfficeAddress());
        }

        if (user instanceof Patient) {
            Patient patient = (Patient) user;
            patient.setAddress(request.getAddress());
        }

        if (user == null) return null;

        setCommonUserEntityFields(request, user);

        User savedUser = userRepository.save(user);

        return createUserProfileResponse(savedUser);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    private void setCommonUserEntityFields(
            UserProfileRequest request,
            User user
    ) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBirthDate(request.getBirthDate());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setGender(request.getGender());
    }

    private void setCommonResponseFields(
            User user,
            UserProfileResponse response
    ) {
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setBirthDate(DateUtils.formatDate(user.getBirthDate()));
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setGender(user.getGender());
        response.setRole(user instanceof Patient ? RoleEnum.PATIENT : RoleEnum.DOCTOR);
        response.setImage(user.getImage());
    }

    private UserProfileResponse createUserProfileResponse(
            User user
    ) {
        UserProfileResponse response = null;

        if (user instanceof Patient)
            response = PatientProfileResponse.builder()
                    .address(((Patient) user).getAddress())
                    .build();

        if (user instanceof Doctor)
            response = DoctorProfileResponse.builder()
                    .license(((Doctor) user).getLicense())
                    .officeAddress(((Doctor) user).getOfficeAddress())
                    .speciality(((Doctor) user).getSpeciality())
                    .build();

        if (response == null) return null;

        setCommonResponseFields(user, response);
        return response;
    }

}
