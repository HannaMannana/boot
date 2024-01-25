package com.clever.boot.service.impl;

import com.clever.boot.dto.HouseCreateRequestDto;
import com.clever.boot.dto.HouseResponseDto;
import com.clever.boot.entity.HouseEntity;
import com.clever.boot.exeption.AppException;
import com.clever.boot.mapper.HouseMapper;
import com.clever.boot.repository.HouseRepository;
import com.clever.boot.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;
    private final ModelMapper mapper;


    @Override
    public HouseResponseDto getById(UUID uuid) {

        try {
            return houseRepository.findByUuid(uuid)
                    .map(houseMapper::toDto)
                    .orElseThrow(() -> new AppException("None person with id" + uuid));
        } catch (AppException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<HouseResponseDto> getAll(Pageable pageable) {

        Page<HouseResponseDto> houseResponseDtos = houseRepository.findAll(pageable).map(houseMapper::toDto);
        return houseResponseDtos;
    }

    @Override
    public HouseResponseDto create(HouseCreateRequestDto createDto) {
        HouseEntity house = houseRepository.save(houseMapper.createDtoToEntity(createDto));
        return houseMapper.toDto(house);
    }

    @Override
    public HouseResponseDto update(UUID uuid, HouseCreateRequestDto dto) {

        HouseEntity toUpdate = houseMapper.updateDtoToEntity(dto);
        HouseEntity house = houseRepository.findByUuid(uuid).orElseThrow();

        toUpdate.setId(house.getId());
        toUpdate.setUuid(uuid);
        toUpdate.setCountry(house.getCountry());
        toUpdate.setCity(house.getCity());
        toUpdate.setCreateDate(house.getCreateDate());

        HouseEntity saved = houseRepository.save(toUpdate);
        return houseMapper.toDto(saved);
    }

    @Override
    public void delete(UUID uuid) {

        HouseEntity house = houseRepository.findByUuid(uuid)
                .orElseThrow(NullPointerException::new);
        houseRepository.delete(house);

    }

}
