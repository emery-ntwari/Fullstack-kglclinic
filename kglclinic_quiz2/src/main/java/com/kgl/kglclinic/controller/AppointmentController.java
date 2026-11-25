package com.kgl.kglclinic.controller;

import com.kgl.kglclinic.domain.Appointment;
import com.kgl.kglclinic.domain.AppointmentStatus;
import com.kgl.kglclinic.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // B1
    @PostMapping("/save")
    public ResponseEntity<?> saveAppointment(@RequestBody Appointment appointment) {
        Object result = appointmentService.saveAppointment(appointment);
        if (result instanceof String msg) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.fetchAllAppointments());
    }

    // B2
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id,
                                               @RequestParam AppointmentStatus status) {
        String result = appointmentService.updateStatus(id, status);
        if (result.equals("Appointment status updated successfully")) return ResponseEntity.ok(result);
        if (result.equals("The appointment with that id does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // B3
    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<String> reschedule(@PathVariable Long id,
                                             @RequestParam LocalDate newDate) {
        String result = appointmentService.reschedule(id, newDate);
        if (result.equals("Appointment rescheduled successfully")) return ResponseEntity.ok(result);
        if (result.equals("The appointment with that id does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        if (result.equals("Doctor is already booked on that date")) return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // D1
    @GetMapping("/{doctorId}/agenda")
    public ResponseEntity<?> getAgenda(@PathVariable Long doctorId,
                                       @RequestParam LocalDate date) {
        Object result = appointmentService.fetchAgenda(doctorId, date);
        if (result instanceof String msg) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        return ResponseEntity.ok(result);
    }

    // C4 delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        String result = appointmentService.removeAppointment(id);
        if (result.equals("Appointment deleted successfully")) return ResponseEntity.ok(result);
        if (result.equals("That appointment has prescriptions, delete them first")) return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }
}
