package com.app.validation;

import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.request.RegisterUserRequest;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class RegisterUserRequestValidator implements ConstraintValidator<ValidUserRequest, RegisterUserRequest> {

    @Override
    public boolean isValid(RegisterUserRequest request, ConstraintValidatorContext context) {
        boolean valid = true;

        // Validación de edad
        valid &= validateAge(request, context);

        // Validación de campos según el rol
        valid &= validateRoleFields(request, context);

        return valid;
    }

    private boolean validateRoleFields(RegisterUserRequest request, ConstraintValidatorContext context) {
        if (request.getRole() == RoleEnum.PATIENT) {
            return validatePatientFields(request, context);
        } else if (request.getRole() == RoleEnum.DOCTOR) {
            return validateDoctorFields(request, context);
        }
        return true;
    }

    private boolean validatePatientFields(RegisterUserRequest request, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(request.getAddress())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Address is required for patients")
                    .addPropertyNode("address").addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateDoctorFields(RegisterUserRequest request, ConstraintValidatorContext context) {
        boolean valid = true;

        if (StringUtils.isBlank(request.getSpeciality())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Speciality is required for doctors")
                    .addPropertyNode("speciality").addConstraintViolation();
            valid = false;
        }
        if (StringUtils.isBlank(request.getLicense())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("License is required for doctors")
                    .addPropertyNode("license").addConstraintViolation();
            valid = false;
        }
        if (StringUtils.isBlank(request.getOfficeAddress())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Office address is required for doctors")
                    .addPropertyNode("officeAddress").addConstraintViolation();
            valid = false;
        }

        return valid;
    }

    private boolean validateAge(RegisterUserRequest request, ConstraintValidatorContext context) {
        if (request.getRole() == null || !request.getRole().equals(RoleEnum.DOCTOR)) {
            return true;
        }

        LocalDate birthDate = request.getBirthDate();
        if (birthDate == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Birth date is required")
                    .addPropertyNode("birthDate").addConstraintViolation();
            return false;
        }

        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < 18) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("User must be at least 18 years old.")
                    .addPropertyNode("birthDate").addConstraintViolation();
            return false;
        }

        return true;
    }
}