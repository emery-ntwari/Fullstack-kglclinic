package com.kgl.kglclinic.service;

import com.kgl.kglclinic.domain.Appointment;
import com.kgl.kglclinic.domain.AppointmentStatus;
import com.kgl.kglclinic.domain.Prescription;
import com.kgl.kglclinic.repository.AppointmentRepository;
import com.kgl.kglclinic.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                               AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // C1
    @Transactional
    public Object savePrescription(Long appointmentId, Prescription prescription) {
        Optional<Appointment> opt = appointmentRepository.findById(appointmentId);
        if (opt.isEmpty()) return "The appointment with that id does not exist";

        Appointment appointment = opt.get();
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            return "Prescriptions are only for COMPLETED appointments";
        }

        prescription.setIssuedDate(LocalDate.now());
        prescription.setAppointment(appointment);
        prescriptionRepository.save(prescription);
        return "Prescription saved successfully";
    }

    // C2
    public Object fetchByAppointment(Long appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            return "The appointment with that id does not exist";
        }
        return prescriptionRepository.findByAppointmentId(appointmentId);
    }

    // C3
    public List<Prescription> fetchByPatientSsNumber(String ssNumber) {
        return prescriptionRepository.findByPatientSsNumber(ssNumber);
    }

    // C4 update
    @Transactional
    public String updatePrescription(UUID id, Prescription updates) {
        Optional<Prescription> opt = prescriptionRepository.findById(id);
        if (opt.isEmpty()) return "The prescription with that id does not exist";

        Prescription existing = opt.get();
        existing.setMedicineName(updates.getMedicineName());
        existing.setDosage(updates.getDosage());
        existing.setDurationDays(updates.getDurationDays());
        prescriptionRepository.save(existing);
        return "Prescription updated successfully";
    }

    // C4 delete
    public String deletePrescription(UUID id) {
        if (!prescriptionRepository.existsById(id)) {
            return "The prescription with that id does not exist";
        }
        prescriptionRepository.deleteById(id);
        return "Prescription deleted successfully";
    }
}
