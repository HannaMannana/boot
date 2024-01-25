package com.clever.boot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class HouseResponseDto {

    private UUID uuid;

    private Double area;

    private String country;

    private String city;

    private String street;

    private Long number;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
    private LocalDateTime createDate;
}
