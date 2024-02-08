package com.clever.boot.service.impl;

import com.clever.boot.dto.HouseResponseDto;
import com.clever.boot.dto.PersonResponseDto;
import com.clever.boot.entity.HouseHistoryEntity;
import com.clever.boot.entity.PersonEntity;
import com.clever.boot.mapper.HouseMapper;
import com.clever.boot.mapper.PersonMapper;
import com.clever.boot.repository.HouseHistoryRepository;
import com.clever.boot.service.HouseHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HouseHistoryServiceImpl implements HouseHistoryService {

    private final HouseHistoryRepository houseHistoryRepository;
    private final PersonMapper personMapper;
    private final HouseMapper houseMapper;


    @Override
    public HouseHistoryEntity createHistory(PersonEntity person, HouseHistoryEntity.Type type) {
        HouseHistoryEntity houseHistoryEntity = new HouseHistoryEntity();
        houseHistoryEntity.setPerson(person);
        houseHistoryEntity.setHouse(person.getHouse());
        houseHistoryEntity.setType(type);
        houseHistoryRepository.save(houseHistoryEntity);
        return houseHistoryEntity;
    }

    @Override
    public List<PersonResponseDto> getPersonsByUuid(UUID uuid, HouseHistoryEntity.Type type) {

        List<HouseHistoryEntity> houseHistoryEntityList = houseHistoryRepository.findPersonsByHouseUuidAndType(uuid, type);


        return houseHistoryEntityList.stream()
                .map(HouseHistoryEntity::getPerson)
                .map(personMapper::toDto)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<HouseResponseDto> getHousesByUuid(UUID uuid, HouseHistoryEntity.Type type) {

        List<HouseHistoryEntity> houseHistoryEntityList = houseHistoryRepository.findHousesByPersonUuidAndType(uuid, type);

        return houseHistoryEntityList.stream()
                .map(HouseHistoryEntity::getHouse)
                .map(houseMapper::toDto)
                .distinct()
                .collect(Collectors.toList());
    }
}