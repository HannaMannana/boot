package com.clever.boot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class HouseCreateRequestDto {

    private UUID uuid;

    private Double area;

    private String country;

    private String city;

    private String street;

    private Long number;
}
