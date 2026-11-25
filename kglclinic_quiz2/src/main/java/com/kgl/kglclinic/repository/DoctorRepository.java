package com.kgl.kglclinic.repository;

import com.kgl.kglclinic.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    boolean existsByOffice_Id(Long officeId);

    @Query("SELECT DISTINCT d FROM Doctor d JOIN FETCH d.office JOIN FETCH d.specializations")
    List<Doctor> findAllWithOfficesAndSpecializations();

    @Query("SELECT d FROM Doctor d WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY d.lastName ASC")
    List<Doctor> searchByName(String name);
}
