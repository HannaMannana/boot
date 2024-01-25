package com.clever.boot.repository;


import com.clever.boot.entity.HouseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface HouseRepository extends JpaRepository<HouseEntity, UUID> {

    Optional<HouseEntity> findByUuid(UUID uuid);

}
