package com.clever.boot.service;


import com.clever.boot.dto.HouseResponseDto;
import com.clever.boot.dto.PersonResponseDto;
import com.clever.boot.entity.HouseHistoryEntity;
import com.clever.boot.entity.PersonEntity;

import java.util.List;
import java.util.UUID;

public interface HouseHistoryService {

    HouseHistoryEntity createHistory(PersonEntity person, HouseHistoryEntity.Type type);

    List<PersonResponseDto> getPersonsByUuid (UUID uuid, HouseHistoryEntity.Type type);

    List<HouseResponseDto> getHousesByUuid(UUID uuid, HouseHistoryEntity.Type type);
}
