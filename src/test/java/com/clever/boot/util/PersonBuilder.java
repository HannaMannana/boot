package com.clever.boot.util;

import com.clever.boot.dto.PersonCreateRequestDto;
import com.clever.boot.dto.PersonResponseDto;
import com.clever.boot.entity.HouseEntity;
import com.clever.boot.entity.PersonEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.clever.boot.util.DataPerson.*;

@Data
@Builder(setterPrefix = "with")
public class PersonBuilder {

    @Builder.Default
    private Long id = PERSON_ID;

    @Builder.Default
    private UUID uuid = PERSON_UUID;

    @Builder.Default
    private String name = PERSON_NAME;

    @Builder.Default
    private String surname = PERSON_SURNAME;

    @Builder.Default
    private PersonEntity.Sex sexType = PERSON_SEX;

    @Builder.Default
    private String passportSeries = PERSON_PSERIES;

    @Builder.Default
    private String passportNumber = PERSON_PNUMBER;

    @Builder.Default
    private LocalDateTime createDate = PERSON_CREATE_DATE;

    @Builder.Default
    private LocalDateTime updateDate = PERSON_UPDATE_DATE;

    @Builder.Default
    private HouseEntity house = HouseBuilder.builder()
            .build()
            .getEntity();

    @Builder.Default
    private List<HouseEntity> ownershipHouses;

    public PersonEntity getEntity() {
        return new PersonEntity(id, uuid, name, surname, sexType, passportSeries, passportNumber, createDate, updateDate, house, ownershipHouses);
    }

    public PersonResponseDto getResponseDto() {
        return new PersonResponseDto();
    }

    public PersonCreateRequestDto getRequestDto() {
        return new PersonCreateRequestDto();
    }
}
