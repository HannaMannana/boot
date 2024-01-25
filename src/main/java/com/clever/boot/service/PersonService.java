package com.clever.boot.service;


import com.clever.boot.dto.PersonCreateRequestDto;
import com.clever.boot.dto.PersonResponseDto;
import com.clever.boot.exeption.ApplicationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PersonService {

    PersonResponseDto getById(UUID uuid);

    Page<PersonResponseDto> getAll(Pageable pageable);

    PersonResponseDto create(PersonCreateRequestDto dto) throws ApplicationException;

    PersonResponseDto update(UUID uuid,PersonCreateRequestDto dto);

    void delete(UUID uuid);
}
