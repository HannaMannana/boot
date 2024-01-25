package com.clever.boot.controller;

import com.clever.boot.dto.PersonCreateRequestDto;
import com.clever.boot.dto.PersonResponseDto;
import com.clever.boot.entity.HouseHistoryEntity;
import com.clever.boot.service.HouseHistoryService;
import com.clever.boot.service.PersonService;
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
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final HouseHistoryService houseHistoryService;

    @GetMapping("/{uuid}")
    public ResponseEntity<PersonResponseDto> findById(@PathVariable("uuid") UUID uuid) {
        PersonResponseDto personResponseDto = personService.getById(uuid);
        return ResponseEntity.ok(personResponseDto);
    }

    @GetMapping()
    public ResponseEntity<Page<PersonResponseDto>> findAll(@PageableDefault(2) Pageable pageable) {

        Page<PersonResponseDto> allPersons = personService.getAll(pageable);
        return ResponseEntity.ok(allPersons);
    }

    @GetMapping("/tenants/{uuid}")
    public ResponseEntity<List<PersonResponseDto>> findTenantsByHouseId(@PathVariable("uuid") UUID uuid) {
        List<PersonResponseDto> persons = houseHistoryService.getPersonsByUuid(uuid, HouseHistoryEntity.Type.TENANT);
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/owners/{uuid}")
    public ResponseEntity<List<PersonResponseDto>> findOwnersByHouseId(@PathVariable("uuid") UUID uuid) {
        List<PersonResponseDto> persons = houseHistoryService.getPersonsByUuid(uuid, HouseHistoryEntity.Type.OWNER);
        return ResponseEntity.ok(persons);
    }

    @PostMapping()
    public ResponseEntity<PersonResponseDto> create(@RequestBody PersonCreateRequestDto createRequestDto) {
        PersonResponseDto createdPerson = personService.create(createRequestDto);
        return ResponseEntity.ok(createdPerson);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<PersonResponseDto> update(@PathVariable("uuid") UUID uuid, @RequestBody PersonCreateRequestDto updateRequestDto) {
        return ResponseEntity.ok(personService.update(uuid, updateRequestDto));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        personService.delete(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


