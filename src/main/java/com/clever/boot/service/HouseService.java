package com.clever.boot.service;


import com.clever.boot.dto.HouseCreateRequestDto;
import com.clever.boot.dto.HouseResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface HouseService {

    HouseResponseDto getById(UUID uuid);

    Page<HouseResponseDto> getAll(Pageable pageable);

    HouseResponseDto create(HouseCreateRequestDto dto);

    HouseResponseDto update(UUID uuid, HouseCreateRequestDto dto);

    void delete(UUID uuid);
}
