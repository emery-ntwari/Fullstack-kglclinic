package com.kgl.kglclinic.service;

import com.kgl.kglclinic.domain.Specialization;
import com.kgl.kglclinic.repository.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public Specialization addSpecialization(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    public List<Specialization> fetchAllSpecializations() {
        return specializationRepository.findAll();
    }
}
