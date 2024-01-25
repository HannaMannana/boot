package com.clever.boot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "house")
public class HouseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = false)
    private UUID uuid;

    @Column(name = "area", nullable = false)
    private Double area;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "number", nullable = false)
    private Long number;

    @Column(name = "create_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "house")
    private List<PersonEntity> tenants = new ArrayList<>();

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "ownershipHouses")
    private List<PersonEntity> owners = new ArrayList<>();
}
