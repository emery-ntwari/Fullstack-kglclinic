package com.kgl.kglclinic.config;

import com.kgl.kglclinic.domain.*;
import com.kgl.kglclinic.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final OfficeRepository officeRepository;
    private final SpecializationRepository specializationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public DataSeeder(OfficeRepository officeRepository,
                      SpecializationRepository specializationRepository,
                      DoctorRepository doctorRepository,
                      PatientRepository patientRepository,
                      AppointmentRepository appointmentRepository) {
        this.officeRepository = officeRepository;
        this.specializationRepository = specializationRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) {
        if (officeRepository.count() > 0) {
            ensureTwelveFreeOffices();
            return;
        }

        // Offices
        Office o101 = officeRepository.save(new Office(101));
        Office o102 = officeRepository.save(new Office(102));
        Office o103 = officeRepository.save(new Office(103));
        officeRepository.save(new Office("Office 104", 104)); // no doctor assigned

        // Specializations
        Specialization cardiology   = specializationRepository.save(new Specialization("Cardiology",  "Heart and cardiovascular system"));
        Specialization pediatrics   = specializationRepository.save(new Specialization("Pediatrics",  "Medical care for infants and children"));
        Specialization neurology    = specializationRepository.save(new Specialization("Neurology",   "Nervous system disorders"));

        // Doctors (doctor3 has only 1 specialization)
        Doctor doctor1 = doctorRepository.save(new Doctor("Alice",  "Mugisha", "alice.mugisha@kgl.com",  o101, List.of(cardiology, pediatrics)));
        Doctor doctor2 = doctorRepository.save(new Doctor("Bob",    "Nkurunziza", "bob.nkurunziza@kgl.com", o102, List.of(neurology, cardiology)));
        Doctor doctor3 = doctorRepository.save(new Doctor("Claire", "Uwase",  "claire.uwase@kgl.com",  o103, List.of(pediatrics)));

        // Patients
        Patient p1 = patientRepository.save(new Patient("1199880011", "Jean Habimana",    "0781000001"));
        Patient p2 = patientRepository.save(new Patient("2200110022", "Marie Uwimana",    "0781000002"));
        Patient p3 = patientRepository.save(new Patient("3300220033", "Eric Niyonzima",   "0781000003"));
        Patient p4 = patientRepository.save(new Patient("4400330044", "Grace Mukamana",   "0781000004"));
        Patient p5 = patientRepository.save(new Patient("5500440055", "Paul Bizimungu",   "0781000005"));

        LocalDate today = LocalDate.now();

        // 3 upcoming appointments for p1 (ssNumber "1199880011")
        appointmentRepository.save(new Appointment(today,          AppointmentStatus.SCHEDULED,  p1, doctor1));
        appointmentRepository.save(new Appointment(today.plusDays(3),  AppointmentStatus.CONFIRMED,  p1, doctor2));
        appointmentRepository.save(new Appointment(today.plusDays(7),  AppointmentStatus.SCHEDULED,  p1, doctor3));

        // 1 CANCELLED appointment today for same doctor (doctor1) and patient (p1)
        appointmentRepository.save(new Appointment(today,          AppointmentStatus.CANCELLED,  p1, doctor1));

        // 2 COMPLETED appointments in the past
        appointmentRepository.save(new Appointment(today.minusDays(10), AppointmentStatus.COMPLETED, p2, doctor1));
        appointmentRepository.save(new Appointment(today.minusDays(5),  AppointmentStatus.COMPLETED, p3, doctor2));

        ensureTwelveFreeOffices();
    }

    private void ensureTwelveFreeOffices() {
        List<Office> freeOffices = officeRepository.findFreeOffices();
        int freeOfficeCount = freeOffices.size();

        for (Office office : freeOffices) {
            if (office.getOfficeName() == null || office.getOfficeName().isBlank()) {
                office.setOfficeName("Office " + office.getOfficeNumber());
                officeRepository.save(office);
            }
        }

        int officeNumber = 104;
        while (freeOfficeCount < 12) {
            if (!officeRepository.existsByOfficeNumber(officeNumber)) {
                officeRepository.save(new Office("Office " + officeNumber, officeNumber));
                freeOfficeCount++;
            }
            officeNumber++;
        }
    }
}
