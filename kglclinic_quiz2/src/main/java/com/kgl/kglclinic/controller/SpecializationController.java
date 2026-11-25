package com.kgl.kglclinic.controller;

import com.kgl.kglclinic.domain.Specialization;
import com.kgl.kglclinic.service.SpecializationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    private final SpecializationService specializationService;

    public SpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @PostMapping
    public ResponseEntity<Specialization> createSpecialization(@RequestBody Specialization specialization) {
        return ResponseEntity.status(HttpStatus.CREATED).body(specializationService.addSpecialization(specialization));
    }

    @GetMapping
    public ResponseEntity<List<Specialization>> getAllSpecializations() {
        return ResponseEntity.ok(specializationService.fetchAllSpecializations());
    }
}
