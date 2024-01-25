package com.clever.boot.mapper;

import com.clever.boot.dto.HouseCreateRequestDto;
import com.clever.boot.dto.HouseResponseDto;
import com.clever.boot.entity.HouseEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HouseMapper {

    HouseResponseDto toDto(HouseEntity entity);

    HouseEntity toEntity(HouseResponseDto dto);

    HouseEntity createDtoToEntity(HouseCreateRequestDto createDto);

    HouseEntity updateDtoToEntity(HouseCreateRequestDto createDto);
}
