package com.clever.boot.controller;


import com.clever.boot.dto.HouseCreateRequestDto;
import com.clever.boot.dto.HouseResponseDto;
import com.clever.boot.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/houses")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;
    private final HouseHistoryService houseHistoryService;


    @GetMapping("/{uuid}")
    public ResponseEntity<HouseResponseDto> findById(@PathVariable("uuid") UUID uuid) {
        HouseResponseDto houseResponseDto = houseService.getById(uuid);
        return ResponseEntity.ok(houseResponseDto);
    }

    @GetMapping()
    public ResponseEntity<Page<HouseResponseDto>> findAll(@PageableDefault(2) Pageable pageable) {

        Page<HouseResponseDto> allHouses = houseService.getAll(pageable);
        return ResponseEntity.ok(allHouses);
    }

    @GetMapping("/tenants/{uuid}")
    public ResponseEntity<List<HouseResponseDto>> findTenantsByPersonId(@PathVariable("uuid") UUID uuid) {
        List<HouseResponseDto> houses = houseHistoryService.getHousesByUuid(uuid, HouseHistoryEntity.Type.TENANT);
        return ResponseEntity.ok(houses);
    }

    @GetMapping("/owners/{uuid}")
    public ResponseEntity<List<HouseResponseDto>> findOwnersByPersonId(@PathVariable("uuid") UUID uuid) {
        List<HouseResponseDto> houses = houseHistoryService.getHousesByUuid(uuid, HouseHistoryEntity.Type.OWNER);
        return ResponseEntity.ok(houses);
    }

    @PostMapping()
    public ResponseEntity<HouseResponseDto> create(@RequestBody HouseCreateRequestDto createRequestDto) {
        HouseResponseDto createdHouse = houseService.create(createRequestDto);
        return ResponseEntity.ok(createdHouse);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<HouseResponseDto> update(@PathVariable("uuid") UUID uuid, @RequestBody HouseCreateRequestDto updateRequestDto) {
        return ResponseEntity.ok(houseService.update(uuid, updateRequestDto));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        houseService.delete(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
