package com.kgl.kglclinic.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

@Entity
@Table(name = "offices")
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("name")
    private String officeName;

    @Column(nullable = false, unique = true)
    private Integer officeNumber;

    public Office() {}

    public Office(Integer officeNumber) {
        this.officeNumber = officeNumber;
    }

    public Office(String officeName, Integer officeNumber) {
        this.officeName = officeName;
        this.officeNumber = officeNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOfficeName() { return officeName; }
    public void setOfficeName(String officeName) { this.officeName = officeName; }

    public Integer getOfficeNumber() { return officeNumber; }
    public void setOfficeNumber(Integer officeNumber) { this.officeNumber = officeNumber; }
}
