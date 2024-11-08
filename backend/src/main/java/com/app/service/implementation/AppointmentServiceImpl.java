package com.app.service.implementation;

import com.app.common.util.DateUtils;
import com.app.controller.dto.enums.RoleEnum;
import com.app.controller.dto.response.AppointmentResponse;
import com.app.exception.AppointmentNotFoundException;
import com.app.persistence.entity.Appointment;
import com.app.persistence.entity.Doctor;
import com.app.persistence.entity.Patient;
import com.app.persistence.entity.User;
import com.app.persistence.repository.AppointmentRepository;
import com.app.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public Set<AppointmentResponse> listAppointmentsByUserAndMonth(User user, int month, int year) {
        Set<Appointment> appointments = null;

        if (user instanceof Patient patient)
            appointments = appointmentRepository.findAppointmentsByPatientAndMonth(patient.getId(), month, year);

        if (user instanceof Doctor doctor)
            appointments = appointmentRepository.findAppointmentsByDoctorAndMonth(doctor.getId(), month, year);

        if (appointments == null)
            return Collections.emptySet();

        Class<?> participantType = user instanceof Doctor ? Patient.class : Doctor.class;

        return appointments.stream().map(appointment -> buildAppointmentResponse(appointment, participantType))
                .collect(Collectors.toSet());
    }

    @Override
    public AppointmentResponse createAppointment(Appointment appointment) {

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return buildAppointmentResponse(savedAppointment, Patient.class);
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        String.format("Appointment with ID %d does not exist.",id)
                ));
    }

    @Override
    public void deleteAppointment(Appointment appointment) {
        appointmentRepository.delete(appointment);
    }

    private AppointmentResponse buildAppointmentResponse(
            Appointment appointment,
            Class<?> participantType
    ) {
        boolean isParticipantAPatient = participantType == Patient.class;
        boolean isParticipantADoctor = participantType == Doctor.class;

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .reason(appointment.getReason())
                .address(appointment.getAddress())
                .date(DateUtils.formatDate(appointment.getDate()))
                .participant(buildParticipantInfo(appointment, isParticipantAPatient, isParticipantADoctor))
                .build();
    }

    private AppointmentResponse.ParticipantInfo buildParticipantInfo(
            Appointment appointment,
            boolean isParticipantAPatient,
            boolean isParticipantADoctor
    ) {
        if (isParticipantADoctor) {
            return AppointmentResponse.ParticipantInfo.builder()
                    .id(appointment.getDoctor().getId())
                    .role(RoleEnum.DOCTOR)
                    .fullName(
                            String.format(
                                    "%s %s",
                                    appointment.getDoctor().getFirstName(),
                                    appointment.getDoctor().getLastName()))
                    .avatar(appointment.getDoctor().getImage())
                    .build();
        }

        if (isParticipantAPatient) {
            return AppointmentResponse.ParticipantInfo.builder()
                    .id(appointment.getPatient().getId())
                    .role(RoleEnum.PATIENT)
                    .fullName(
                            String.format(
                                    "%s %s",
                                    appointment.getPatient().getFirstName(),
                                    appointment.getPatient().getLastName()))
                    .avatar(appointment.getPatient().getImage())
                    .build();
        }
        return null;
    }
}
