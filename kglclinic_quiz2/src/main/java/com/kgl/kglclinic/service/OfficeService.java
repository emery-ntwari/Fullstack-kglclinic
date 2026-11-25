package com.kgl.kglclinic.service;

import com.kgl.kglclinic.domain.Office;
import com.kgl.kglclinic.repository.DoctorRepository;
import com.kgl.kglclinic.repository.OfficeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final DoctorRepository doctorRepository;

    public OfficeService(OfficeRepository officeRepository, DoctorRepository doctorRepository) {
        this.officeRepository = officeRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<Office> fetchFreeOffices() {
        return officeRepository.findFreeOffices();
    }

    public Office saveOffice(Office office) {
        if (office == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Office details are required");
        }
        if (office.getOfficeName() == null || office.getOfficeName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Office name is required");
        }
        if (office.getOfficeNumber() == null || office.getOfficeNumber() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A positive office number is required");
        }
        if (officeRepository.existsByOfficeNumber(office.getOfficeNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "That office number already exists");
        }

        office.setId(null);
        office.setOfficeName(office.getOfficeName().trim());
        return officeRepository.save(office);
    }

    public void deleteOfficeByNumber(Integer officeNumber) {
        Office office = officeRepository.findByOfficeNumber(officeNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Office not found"));
        if (doctorRepository.existsByOffice_Id(office.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This office is assigned to a doctor and cannot be deleted"
            );
        }

        officeRepository.delete(office);
    }
}
