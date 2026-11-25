package com.kgl.kglclinic.controller;

import com.kgl.kglclinic.domain.Patient;
import com.kgl.kglclinic.service.AppointmentService;
import com.kgl.kglclinic.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    public PatientController(PatientService patientService,
                             AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<Patient> registerPatient(@RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.enrollPatient(patient));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Patient>> getPatientsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(patientService.fetchPatientsByDoctor(doctorId));
    }

    // D2
    @GetMapping("/{ssNumber}/upcoming")
    public ResponseEntity<?> getUpcoming(@PathVariable String ssNumber) {
        Object result = appointmentService.fetchUpcoming(ssNumber);
        if (result instanceof String msg) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        return ResponseEntity.ok(result);
    }
}
