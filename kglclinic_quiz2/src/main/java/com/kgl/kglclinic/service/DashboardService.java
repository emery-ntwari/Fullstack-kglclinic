package com.kgl.kglclinic.service;

import com.kgl.kglclinic.domain.AppointmentStatus;
import com.kgl.kglclinic.repository.AppointmentRepository;
import com.kgl.kglclinic.repository.DoctorRepository;
import com.kgl.kglclinic.repository.OfficeRepository;
import com.kgl.kglclinic.repository.PatientRepository;
import com.kgl.kglclinic.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final OfficeRepository officeRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;

    public DashboardService(PatientRepository patientRepository,
                            DoctorRepository doctorRepository,
                            OfficeRepository officeRepository,
                            AppointmentRepository appointmentRepository,
                            PrescriptionRepository prescriptionRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.officeRepository = officeRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("patients", patientRepository.count());
        summary.put("doctors", doctorRepository.count());
        summary.put("freeOffices", (long) officeRepository.findFreeOffices().size());
        summary.put("appointmentsToday", appointmentRepository.countByAppointmentDate(LocalDate.now()));
        summary.put("scheduled", appointmentRepository.countByStatus(AppointmentStatus.SCHEDULED));
        summary.put("prescriptions", prescriptionRepository.count());
        return summary;
    }
}
