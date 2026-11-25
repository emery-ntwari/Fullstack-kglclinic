package com.kgl.kglclinic.controller;

import com.kgl.kglclinic.domain.Doctor;
import com.kgl.kglclinic.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<?> registerDoctor(@RequestBody Doctor doctor) {
        Doctor saved = doctorService.saveDoctor(doctor);
        if (saved == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Office or specialization not found");
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.fetchAllDoctors());
    }

    // A1
    @PutMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<String> addSpecialization(@PathVariable Long doctorId,
                                                    @PathVariable Long specializationId) {
        String result = doctorService.addSpecialization(doctorId, specializationId);
        if (result.equals("Specialization added to the doctor")) return ResponseEntity.ok(result);
        if (result.equals("The doctor already has that specialization")) return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    // A2
    @DeleteMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<String> removeSpecialization(@PathVariable Long doctorId,
                                                       @PathVariable Long specializationId) {
        String result = doctorService.removeSpecialization(doctorId, specializationId);
        if (result.equals("Specialization removed from the doctor")) return ResponseEntity.ok(result);
        if (result.equals("The doctor does not have that specialization")) return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    // A4
    @PatchMapping("/{doctorId}/office")
    public ResponseEntity<String> changeOffice(@PathVariable Long doctorId,
                                               @RequestParam Integer officeNumber) {
        String result = doctorService.changeOffice(doctorId, officeNumber);
        if (result.equals("Office changed successfully")) return ResponseEntity.ok(result);
        if (result.equals("That office is taken by other doctor")) return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    // D3
    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.searchByName(name));
    }
}
