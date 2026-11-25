package com.kgl.kglclinic.repository;

import com.kgl.kglclinic.domain.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    List<Prescription> findByAppointmentId(Long appointmentId);

    boolean existsByAppointmentId(Long appointmentId);

    @Query("SELECT p FROM Prescription p WHERE p.appointment.patient.ssNumber = :ssNumber ORDER BY p.issuedDate DESC")
    List<Prescription> findByPatientSsNumber(String ssNumber);
}
