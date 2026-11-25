package com.kgl.kglclinic.controller;

import com.kgl.kglclinic.domain.Prescription;
import com.kgl.kglclinic.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    // C1
    @PostMapping("/save")
    public ResponseEntity<?> savePrescription(@RequestParam Long appointmentId,
                                              @RequestBody Prescription prescription) {
        Object result = prescriptionService.savePrescription(appointmentId, prescription);
        if (result instanceof String msg) {
            if (msg.equals("The appointment with that id does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // C2
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getByAppointment(@PathVariable Long appointmentId) {
        Object result = prescriptionService.fetchByAppointment(appointmentId);
        if (result instanceof String msg) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        return ResponseEntity.ok(result);
    }

    // C3
    @GetMapping("/patient/{ssNumber}")
    public ResponseEntity<List<Prescription>> getByPatient(@PathVariable String ssNumber) {
        return ResponseEntity.ok(prescriptionService.fetchByPatientSsNumber(ssNumber));
    }

    // C4 update
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePrescription(@PathVariable UUID id,
                                                     @RequestBody Prescription updates) {
        String result = prescriptionService.updatePrescription(id, updates);
        if (result.equals("Prescription updated successfully")) return ResponseEntity.ok(result);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    // C4 delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePrescription(@PathVariable UUID id) {
        String result = prescriptionService.deletePrescription(id);
        if (result.equals("Prescription deleted successfully")) return ResponseEntity.ok(result);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }
}
