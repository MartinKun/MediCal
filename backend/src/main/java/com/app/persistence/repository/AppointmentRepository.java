package com.app.persistence.repository;

import com.app.persistence.entity.Appointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE MONTH(a.date) = :month AND YEAR(a.date) = :year AND a.doctor.id = :doctorId")
    Set<Appointment> findAppointmentsByDoctorAndMonth(@Param("doctorId") Long doctorId,
                                                      @Param("month") int month,
                                                      @Param("year") int year);

    @Query("SELECT a FROM Appointment a WHERE MONTH(a.date) = :month AND YEAR(a.date) = :year AND a.patient.id = :patientId")
    Set<Appointment> findAppointmentsByPatientAndMonth(@Param("patientId") Long patientId,
                                                       @Param("month") int month,
                                                       @Param("year") int year);
}
