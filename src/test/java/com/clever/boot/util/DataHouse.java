package com.clever.boot.util;

import com.clever.boot.entity.HouseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class DataHouse {


    public static final Long HOUSE_ID = 2L;
    public static final UUID HOUSE_UUID = UUID.fromString("65e7e1b5-ab23-4df2-880f-a73879014fa4");
    public static final Double HOUSE_AREA = 32.2;
    public static final String HOUSE_COUNTRY = "Belarus";
    public static final String HOUSE_CITY = "Grodno";
    public static final String HOUSE_STREET = "Kolasa";
    public static final Long HOUSE_NUMBER = 82L;
    public static final LocalDateTime HOUSE_CREATE_DATE = LocalDateTime.of(2024, 1, 14, 15, 15, 20);
    public static final int UPDATE_HOUSE_NUMBER = 132;
    public static final UUID INCORRECT_UUID = UUID.fromString("a54ec398-2cab-40e1-9882-53b8736bbe9f");
    public static final UUID HOUSE_UUID_ONE = UUID.fromString("65e7e1b5-ab23-4df2-880f-a73879014fa4");
    public static final UUID HOUSE_UUID_TWO = UUID.fromString("1fe3a64c-ca05-4346-b3f3-e53972a6b497");


    public static HouseEntity addHouseOne() {
        return HouseBuilder.builder()
                .withId(2L)
                .withUuid(UUID.fromString("65e7e1b5-ab23-4df2-880f-a73879014fa4"))
                .withArea(32.2)
                .withCountry("Belarus")
                .withCity("Grodno")
                .withStreet("Kolasa")
                .withNumber(82L)
                .withCreateDate(LocalDateTime.of(2024, 1, 14, 15, 15, 20))
                .build()
                .getEntity();
    }

    public static HouseEntity addHouseTwo() {
        return HouseBuilder.builder()
                .withUuid(UUID.fromString("1fe3a64c-ca05-4346-b3f3-e53972a6b497"))
                .withArea(135.9)
                .withCountry("Belarus")
                .withCity("Grodno")
                .withStreet("Respublici")
                .withNumber(197L)
                .build()
                .getEntity();
    }


}
