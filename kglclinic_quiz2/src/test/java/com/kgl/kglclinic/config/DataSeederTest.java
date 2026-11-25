package com.kgl.kglclinic.config;

import com.kgl.kglclinic.domain.Office;
import com.kgl.kglclinic.repository.AppointmentRepository;
import com.kgl.kglclinic.repository.DoctorRepository;
import com.kgl.kglclinic.repository.OfficeRepository;
import com.kgl.kglclinic.repository.PatientRepository;
import com.kgl.kglclinic.repository.SpecializationRepository;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DataSeederTest {

    @Test
    void addsFreeOfficesUntilTwelveAreAvailableForExistingData() {
        OfficeRepository officeRepository = mock(OfficeRepository.class);
        Set<Integer> officeNumbers = new HashSet<>(Set.of(101, 102, 103, 104));

        when(officeRepository.count()).thenReturn(1L);
        when(officeRepository.findFreeOffices()).thenReturn(List.of(new Office(104)));
        when(officeRepository.existsByOfficeNumber(anyInt()))
                .thenAnswer(invocation -> officeNumbers.contains(invocation.getArgument(0)));
        doAnswer(invocation -> {
            Office office = invocation.getArgument(0);
            officeNumbers.add(office.getOfficeNumber());
            return office;
        }).when(officeRepository).save(any(Office.class));

        DataSeeder seeder = new DataSeeder(
                officeRepository,
                mock(SpecializationRepository.class),
                mock(DoctorRepository.class),
                mock(PatientRepository.class),
                mock(AppointmentRepository.class)
        );

        seeder.run();

        assertEquals(12, officeNumbers.stream().filter(number -> number >= 104).count());
        verify(officeRepository, times(12)).save(any(Office.class));
    }
}
