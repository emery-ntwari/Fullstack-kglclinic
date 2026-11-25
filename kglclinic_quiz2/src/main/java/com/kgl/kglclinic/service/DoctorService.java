package com.kgl.kglclinic.service;

import com.kgl.kglclinic.domain.Doctor;
import com.kgl.kglclinic.domain.Office;
import com.kgl.kglclinic.domain.Specialization;
import com.kgl.kglclinic.repository.DoctorRepository;
import com.kgl.kglclinic.repository.OfficeRepository;
import com.kgl.kglclinic.repository.SpecializationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final OfficeRepository officeRepository;
    private final SpecializationRepository specializationRepository;

    public DoctorService(DoctorRepository doctorRepository,
                         OfficeRepository officeRepository,
                         SpecializationRepository specializationRepository) {
        this.doctorRepository = doctorRepository;
        this.officeRepository = officeRepository;
        this.specializationRepository = specializationRepository;
    }

    @Transactional
    public Doctor saveDoctor(Doctor doctor) {
        Office office = officeRepository.findById(doctor.getOffice().getId()).orElse(null);
        if (office == null) return null;
        List<Specialization> specializations = specializationRepository
                .findAllById(doctor.getSpecializations().stream().map(Specialization::getId).toList());
        doctor.setOffice(office);
        doctor.setSpecializations(specializations);
        Doctor saved = doctorRepository.save(doctor);
        return doctorRepository.findAllWithOfficesAndSpecializations()
                .stream().filter(d -> d.getId().equals(saved.getId())).findFirst().orElse(saved);
    }

    public List<Doctor> fetchAllDoctors() {
        return doctorRepository.findAllWithOfficesAndSpecializations();
    }

    // A1
    @Transactional
    public String addSpecialization(Long doctorId, Long specializationId) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) return "The doctor with that id does not exist";

        Optional<Specialization> specOpt = specializationRepository.findById(specializationId);
        if (specOpt.isEmpty()) return "That specialization does not exist";

        Doctor doctor = doctorOpt.get();
        Specialization specialization = specOpt.get();

        boolean alreadyHas = doctor.getSpecializations().stream()
                .anyMatch(s -> s.getId().equals(specializationId));
        if (alreadyHas) return "The doctor already has that specialization";

        doctor.getSpecializations().add(specialization);
        doctorRepository.save(doctor);
        return "Specialization added to the doctor";
    }

    // A2
    @Transactional
    public String removeSpecialization(Long doctorId, Long specializationId) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) return "The doctor with that id does not exist";

        Optional<Specialization> specOpt = specializationRepository.findById(specializationId);
        if (specOpt.isEmpty()) return "That specialization does not exist";

        Doctor doctor = doctorOpt.get();

        boolean hasIt = doctor.getSpecializations().stream()
                .anyMatch(s -> s.getId().equals(specializationId));
        if (!hasIt) return "The doctor does not have that specialization";

        doctor.getSpecializations().removeIf(s -> s.getId().equals(specializationId));
        doctorRepository.save(doctor);
        return "Specialization removed from the doctor";
    }

    // A4
    @Transactional
    public String changeOffice(Long doctorId, Integer officeNumber) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) return "The doctor with that id does not exist";

        Optional<Office> officeOpt = officeRepository.findByOfficeNumber(officeNumber);
        if (officeOpt.isEmpty()) return "The office with that office number does not exist";

        Office newOffice = officeOpt.get();
        boolean isTaken = doctorRepository.findAllWithOfficesAndSpecializations().stream()
                .anyMatch(d -> d.getOffice() != null
                        && d.getOffice().getId().equals(newOffice.getId())
                        && !d.getId().equals(doctorId));
        if (isTaken) return "That office is taken by other doctor";

        Doctor doctor = doctorOpt.get();
        doctor.setOffice(newOffice);
        doctorRepository.save(doctor);
        return "Office changed successfully";
    }

    // D3
    public List<Doctor> searchByName(String name) {
        return doctorRepository.searchByName(name);
    }
}
