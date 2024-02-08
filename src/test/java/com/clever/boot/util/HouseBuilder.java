package com.clever.boot.util;

import com.clever.boot.dto.HouseCreateRequestDto;
import com.clever.boot.dto.HouseResponseDto;
import com.clever.boot.entity.HouseEntity;
import com.clever.boot.entity.PersonEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.clever.boot.util.DataHouse.*;

@Data
@Builder(setterPrefix = "with")
public class HouseBuilder {


    @Builder.Default
    private Long id = HOUSE_ID;

    @Builder.Default
    private UUID uuid = HOUSE_UUID;

    @Builder.Default
    private Double area = HOUSE_AREA;

    @Builder.Default
    private String country = HOUSE_COUNTRY;

    @Builder.Default
    private String city = HOUSE_CITY;

    @Builder.Default
    private String street = HOUSE_STREET;

    @Builder.Default
    private Long number = Long.valueOf(HOUSE_NUMBER);

    @Builder.Default
    private LocalDateTime createDate = HOUSE_CREATE_DATE;

    @Builder.Default
    private List<PersonEntity> tenants = new ArrayList<>();

    @Builder.Default
    private List<PersonEntity> owners = new ArrayList<>();

    public HouseEntity getEntity() {
        return new HouseEntity(id, uuid, area, country, city, street, number, createDate, tenants, owners);
    }

    public HouseCreateRequestDto getRequestDto() {
        return new HouseCreateRequestDto();

    }

       public HouseResponseDto getResponseDto() {
        return new HouseResponseDto();

    }

}
