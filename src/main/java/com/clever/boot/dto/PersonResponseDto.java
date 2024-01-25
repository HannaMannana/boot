package com.clever.boot.dto;

import com.clever.boot.entity.PersonEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PersonResponseDto {

    private UUID uuid;

    private String name;

    private String surname;

    private String passportSeries;

    private String passportNumber;

    private PersonEntity.Sex sexType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
    private LocalDateTime updateDate;
}
