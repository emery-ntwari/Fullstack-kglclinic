package com.kgl.kglclinic.controller;

import com.kgl.kglclinic.domain.Office;
import com.kgl.kglclinic.service.OfficeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offices")
@CrossOrigin ("*")
public class OfficeController {

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping("/free")
    public ResponseEntity<List<Office>> getFreeOffices() {
        return ResponseEntity.ok(officeService.fetchFreeOffices());
    }

    @PostMapping("/save")
    public ResponseEntity<Office> saveOffice(@RequestBody Office office) {
        return ResponseEntity.status(HttpStatus.OK).body(officeService.saveOffice(office));
    }

    @DeleteMapping("/delete-by-number/{officeNumber}")
    public ResponseEntity<String> deleteOffice(@PathVariable Integer officeNumber) {
        officeService.deleteOfficeByNumber(officeNumber);
        return ResponseEntity.ok("Office deleted successfully");
    }
}
