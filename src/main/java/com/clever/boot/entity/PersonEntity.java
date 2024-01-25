package com.clever.boot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "person")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = false)
    private UUID uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "passport_series", unique = true, nullable = false)
    private String passportSeries;

    @Column(name = "passport_number", unique = true, nullable = false)
    private String passportNumber;

    @Column(name = "sex_type")
    @Enumerated(EnumType.STRING)
    private Sex sexType;

    @Column(name = "create_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false)
    private HouseEntity house;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Person_House",
            joinColumns = { @JoinColumn(name = "person_id") },
            inverseJoinColumns = { @JoinColumn(name = "house_id") }
    )
    private List<HouseEntity> ownershipHouses;

    public enum Sex {
        FEMALE, MALE
    }
}
