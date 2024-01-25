package com.clever.boot.dto;

import com.clever.boot.entity.PersonEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PersonCreateRequestDto {

    private UUID uuid;

    private String name;

    private String surname;

    private String passportSeries;

    private String passportNumber;

    private PersonEntity.Sex sexType;

    private UUID houseId;

    private UUID ownerHouseId;
}
