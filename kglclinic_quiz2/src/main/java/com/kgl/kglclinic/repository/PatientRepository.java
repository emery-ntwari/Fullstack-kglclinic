package com.kgl.kglclinic.repository;

import com.kgl.kglclinic.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findBySsNumber(String ssNumber);

    List<Patient> findAllByDoctorId(Long doctorId);
}
