package com.kgl.kglclinic.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @OneToOne
    @JoinColumn(name = "office_id")
    private Office office;

    @ManyToMany
    @JoinTable(
        name = "doctor_specialization",
        joinColumns = @JoinColumn(name = "doctor_id"),
        inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private List<Specialization> specializations;

    public Doctor() {}

    public Doctor(String firstName, String lastName, String email, Office office, List<Specialization> specializations) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.office = office;
        this.specializations = specializations;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Office getOffice() { return office; }
    public void setOffice(Office office) { this.office = office; }

    public List<Specialization> getSpecializations() { return specializations; }
    public void setSpecializations(List<Specialization> specializations) { this.specializations = specializations; }
}
