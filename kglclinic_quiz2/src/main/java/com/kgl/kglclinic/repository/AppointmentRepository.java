package com.kgl.kglclinic.repository;

import com.kgl.kglclinic.domain.Appointment;
import com.kgl.kglclinic.domain.AppointmentStatus;
import com.kgl.kglclinic.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a JOIN FETCH a.doctor JOIN FETCH a.patient")
    List<Appointment> findAllWithDoctorAndPatient();

    boolean existsByDoctorAndAppointmentDateAndStatusNot(Doctor doctor, LocalDate date, AppointmentStatus status);

    List<Appointment> findByDoctorIdAndAppointmentDateAndStatusNot(Long doctorId, LocalDate date, AppointmentStatus status);

    List<Appointment> findByPatientSsNumberAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(String ssNumber, LocalDate date, List<AppointmentStatus> statuses);

    long countByAppointmentDate(LocalDate date);

    long countByStatus(AppointmentStatus status);
}
