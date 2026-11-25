package com.kgl.kglclinic.service;

import com.kgl.kglclinic.domain.Doctor;
import com.kgl.kglclinic.domain.Patient;
import com.kgl.kglclinic.repository.DoctorRepository;
import com.kgl.kglclinic.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public PatientService(PatientRepository patientRepository,
                          DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public Patient enrollPatient(Patient patient) {
        if (patient.getDoctor() != null && patient.getDoctor().getId() != null) {
            Doctor doctor = doctorRepository.findById(patient.getDoctor().getId()).orElse(null);
            if (doctor != null) patient.setDoctor(doctor);
        }
        return patientRepository.save(patient);
    }

    public List<Patient> fetchPatientsByDoctor(Long doctorId) {
        return patientRepository.findAllByDoctorId(doctorId);
    }
}
