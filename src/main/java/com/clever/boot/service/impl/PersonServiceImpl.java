package com.clever.boot.service.impl;

import com.clever.boot.dto.PersonCreateRequestDto;
import com.clever.boot.dto.PersonResponseDto;
import com.clever.boot.entity.HouseEntity;
import com.clever.boot.entity.HouseHistoryEntity;
import com.clever.boot.entity.PersonEntity;
import com.clever.boot.exeption.AppException;
import com.clever.boot.exeption.ApplicationException;
import com.clever.boot.mapper.PersonMapper;
import com.clever.boot.repository.HouseRepository;
import com.clever.boot.repository.PersonRepository;
import com.clever.boot.service.HouseHistoryService;
import com.clever.boot.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final HouseRepository houseRepository;
    private final HouseHistoryService houseHistoryService;
    private final PersonMapper personMapper;


    @Override
    public PersonResponseDto getById(UUID uuid) {
        try {
            return personRepository.findByUuid(uuid)
                    .map(personMapper::toDto)
                    .orElseThrow(() -> new AppException("None person with id" + uuid));
        } catch (AppException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<PersonResponseDto> getAll(Pageable pageable) {
        Page<PersonResponseDto> personResponseDtos = personRepository.findAll(pageable).map(personMapper::toDto);
        return personResponseDtos;
    }

    @Override
    public PersonResponseDto create(PersonCreateRequestDto dto) throws ApplicationException {
        Optional<HouseEntity> optionalHouse = houseRepository.findByUuid(dto.getHouseId());
        Optional<HouseEntity> optionalOwnerHouse = houseRepository.findByUuid(dto.getOwnerHouseId());
        HouseEntity house = optionalHouse.orElseThrow(() -> new ApplicationException("House not found", HttpStatus.NOT_FOUND));

        PersonEntity entityToCreate = personMapper.createDtoToEntity(dto);
        entityToCreate.setHouse(house);

        if (optionalOwnerHouse.isPresent()) {
            List<HouseEntity> ownershipHouses = new ArrayList<>();
            ownershipHouses.add(optionalOwnerHouse.get());

            entityToCreate.setOwnershipHouses(ownershipHouses);
        }

        PersonEntity person = personRepository.save(entityToCreate);
        return personMapper.toDto(person);
    }

    @Override
    @Transactional
    public PersonResponseDto update(UUID uuid, PersonCreateRequestDto dto) throws ApplicationException {

        Optional<HouseEntity> optionalHouse = houseRepository.findByUuid(dto.getHouseId());
        HouseEntity house = optionalHouse.orElseThrow(() -> new ApplicationException("House not found", HttpStatus.NOT_FOUND));

        PersonEntity toUpdate = personMapper.updateDtoToEntity(dto);

        PersonEntity person = personRepository.findByUuid(uuid).orElseThrow();

        toUpdate.setId(person.getId());
        toUpdate.setUuid(uuid);
        toUpdate.setHouse(house);
        toUpdate.setCreateDate(person.getCreateDate());

        if (person.getHouse().equals(toUpdate.getHouse())) {
            houseHistoryService.createHistory(person, HouseHistoryEntity.Type.TENANT);
        }

        if (person.getOwnershipHouses().equals(toUpdate.getOwnershipHouses())) {
            houseHistoryService.createHistory(person, HouseHistoryEntity.Type.OWNER);
        }

        PersonEntity saved = personRepository.save(toUpdate);
        return personMapper.toDto(saved);
    }

    @Override
    public void delete(UUID uuid) {

        PersonEntity person = personRepository.findByUuid(uuid)
                .orElseThrow(() -> new ApplicationException("Person not found", HttpStatus.NOT_FOUND));
        personRepository.delete(person);
    }
}
