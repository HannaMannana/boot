package com.clever.boot.repository;

import com.clever.boot.entity.HouseHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HouseHistoryRepository extends JpaRepository<HouseHistoryEntity, Long> {

    List<HouseHistoryEntity> findPersonsByHouseUuidAndType(UUID uuid, HouseHistoryEntity.Type type);

    List<HouseHistoryEntity> findHousesByPersonUuidAndType(UUID uuid, HouseHistoryEntity.Type type);
}
