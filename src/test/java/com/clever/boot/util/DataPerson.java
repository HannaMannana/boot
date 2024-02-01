package com.clever.boot.util;

import com.clever.boot.dto.PersonCreateRequestDto;
import com.clever.boot.entity.PersonEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DataPerson {

    public static final Long PERSON_ID = 1L;
    public static final UUID PERSON_UUID = UUID.fromString("1d649540-aca1-4a4f-99f2-7f38ea37cd2e");
    public static final String PERSON_NAME = "Nill";
    public static final String PERSON_SURNAME = "Aple";
    public static final PersonEntity.Sex PERSON_SEX = PersonEntity.Sex.MALE;
    public static final String PERSON_PSERIES = "PK";

    public static final String PERSON_PNUMBER = "1245786";
    public static final LocalDateTime PERSON_CREATE_DATE = LocalDateTime.of(2024, 1, 14, 15, 0, 0);
    public static final LocalDateTime PERSON_UPDATE_DATE = LocalDateTime.of(2024, 1, 15, 12, 0, 0);
    public static final String UPDATE_PERSON_NAME = "Mila";
    public static final String UPDATE_PERSON_SURNAME = "Yogvich";
    public static final UUID INCORRECT_UUID = UUID.fromString("a54ec398-2cab-40e1-9882-53b8736bbe9f");
    public static final UUID PERSON_UUID_ONE = UUID.fromString("4e741af3-3ebe-49f0-b4b7-65c4fc19571a");
    public static final UUID PERSON_UUID_TWO = UUID.fromString("da9d2556-64f7-4013-9e66-a1ca0c19666f");


    public static PersonCreateRequestDto getNewPerson() {
        return PersonBuilder.builder()
                .withName("Tomy")
                .withSurname("Fin")
                .withSexType(PersonEntity.Sex.MALE)
                .withPassportNumber("1025763")
                .withPassportSeries("BV")
                .withHouse(DataHouse.addHouseOne())
                .withOwnershipHouses(List.of(DataHouse.addHouseOne()))
                .build()
                .getRequestDto();
    }

    public static PersonCreateRequestDto getPersonForUpdate() {
        return PersonBuilder.builder()
                .withName("TomyM")
                .withSurname("Fin-Bin")
                .withSexType(PersonEntity.Sex.MALE)
                .withPassportNumber("1025763")
                .withPassportSeries("BV")
                .withHouse(DataHouse.addHouseOne())
                .withOwnershipHouses(List.of(DataHouse.addHouseOne()))
                .build()
                .getRequestDto();
    }

    public static PersonEntity getPerson() {
        return PersonBuilder.builder()
                .withId(7L)
                .withUuid(UUID.fromString("6397a398-678f-4831-8336-3cc97d692c6e"))
                .withName("Nila")
                .withSurname("Milka")
                .withSexType(PersonEntity.Sex.FEMALE)
                .withPassportNumber("2369570")
                .withPassportSeries("BM")
                .withCreateDate(LocalDateTime.of(2024, 1, 14, 15, 15, 20))
                .withUpdateDate(LocalDateTime.of(2024, 1, 14, 15, 15, 20))
                .withHouse(DataHouse.addHouseOne())
                .withOwnershipHouses(List.of(DataHouse.addHouseOne()))
                .build()
                .getEntity();
    }
}
