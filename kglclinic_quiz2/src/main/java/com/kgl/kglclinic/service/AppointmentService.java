package com.kgl.kglclinic.service;

import com.kgl.kglclinic.domain.Appointment;
import com.kgl.kglclinic.domain.AppointmentStatus;
import com.kgl.kglclinic.domain.Doctor;
import com.kgl.kglclinic.domain.Patient;
import com.kgl.kglclinic.repository.AppointmentRepository;
import com.kgl.kglclinic.repository.DoctorRepository;
import com.kgl.kglclinic.repository.PatientRepository;
import com.kgl.kglclinic.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              PatientRepository patientRepository,
                              PrescriptionRepository prescriptionRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    // B1
    @Transactional
    public Object saveAppointment(Appointment appointment) {
        if (appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            return "Appointment date cannot be in the past";
        }
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId()).orElse(null);
        if (doctor == null) return "The doctor with that id does not exist";

        Patient patient = patientRepository.findById(appointment.getPatient().getId()).orElse(null);
        if (patient == null) return "The patient with that id does not exist";

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> fetchAllAppointments() {
        return appointmentRepository.findAllWithDoctorAndPatient();
    }

    // B2
    @Transactional
    public String updateStatus(Long id, AppointmentStatus newStatus) {
        Optional<Appointment> opt = appointmentRepository.findById(id);
        if (opt.isEmpty()) return "The appointment with that id does not exist";

        Appointment appointment = opt.get();
        AppointmentStatus current = appointment.getStatus();

        boolean allowed = (current == AppointmentStatus.SCHEDULED
                && (newStatus == AppointmentStatus.CONFIRMED || newStatus == AppointmentStatus.CANCELLED))
                || (current == AppointmentStatus.CONFIRMED
                && (newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.CANCELLED));

        if (!allowed) return "Cannot change status from " + current + " to " + newStatus;

        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
        return "Appointment status updated successfully";
    }

    // B3
    @Transactional
    public String reschedule(Long id, LocalDate newDate) {
        Optional<Appointment> opt = appointmentRepository.findById(id);
        if (opt.isEmpty()) return "The appointment with that id does not exist";

        Appointment appointment = opt.get();

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            return "A COMPLETED appointment cannot be rescheduled";
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            return "A CANCELLED appointment cannot be rescheduled";
        }
        if (newDate.isBefore(LocalDate.now())) {
            return "Appointment date cannot be in the past";
        }

        boolean doctorBusy = appointmentRepository.existsByDoctorAndAppointmentDateAndStatusNot(
                appointment.getDoctor(), newDate, AppointmentStatus.CANCELLED);
        if (doctorBusy) return "Doctor is already booked on that date";

        appointment.setAppointmentDate(newDate);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);
        return "Appointment rescheduled successfully";
    }

    // D1
    public Object fetchAgenda(Long doctorId, LocalDate date) {
        if (!doctorRepository.existsById(doctorId)) {
            return "The doctor with that id does not exist";
        }
        return appointmentRepository.findByDoctorIdAndAppointmentDateAndStatusNot(
                doctorId, date, AppointmentStatus.CANCELLED);
    }

    // D2
    public Object fetchUpcoming(String ssNumber) {
        boolean exists = patientRepository.findBySsNumber(ssNumber).isPresent();
        if (!exists) return "That patient with that ssnumber does not exist";
        return appointmentRepository
                .findByPatientSsNumberAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
                        ssNumber, LocalDate.now(),
                        List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED));
    }

    // C4 delete protection
    public String removeAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            return "The appointment with that id does not exist";
        }
        if (prescriptionRepository.existsByAppointmentId(id)) {
            return "That appointment has prescriptions, delete them first";
        }
        appointmentRepository.deleteById(id);
        return "Appointment deleted successfully";
    }
}
