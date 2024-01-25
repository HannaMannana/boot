package com.clever.boot.mapper;

import com.clever.boot.dto.PersonCreateRequestDto;
import com.clever.boot.dto.PersonResponseDto;
import com.clever.boot.entity.PersonEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonMapper {

    PersonResponseDto toDto(PersonEntity entity);

    PersonEntity toEntity(PersonResponseDto dto);

    PersonEntity createDtoToEntity(PersonCreateRequestDto dto);

    PersonEntity updateDtoToEntity(PersonCreateRequestDto dto);
}
