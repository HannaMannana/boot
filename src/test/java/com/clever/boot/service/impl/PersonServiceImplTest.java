package com.clever.boot.service.impl;

import com.clever.boot.dto.PersonCreateRequestDto;
import com.clever.boot.dto.PersonResponseDto;
import com.clever.boot.entity.HouseEntity;
import com.clever.boot.entity.HouseHistoryEntity;
import com.clever.boot.entity.PersonEntity;
import com.clever.boot.repository.HouseRepository;
import com.clever.boot.repository.PersonRepository;
import com.clever.boot.service.HouseHistoryService;
import com.clever.boot.util.HouseBuilder;
import com.clever.boot.util.PersonBuilder;
import com.clever.boot.util.PostgresContainerInit;
import com.github.dockerjava.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.clever.boot.util.DataHouse.HOUSE_UUID;
import static com.clever.boot.util.DataPerson.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor
class PersonServiceImplTest extends PostgresContainerInit {

    private final PersonServiceImpl personService;

    @MockBean
    private final PersonRepository personRepository;

    @MockBean
    private final HouseRepository houseRepository;

    @MockBean
    private final HouseHistoryService houseHistoryService;

    @Captor
    private ArgumentCaptor<PersonEntity> captor;

    @Nested
    class FindByUuid {

        @Test
        void shouldReturnPersonByUUID() {
            // given
            UUID personUuid = PERSON_UUID;

            PersonEntity person = PersonBuilder.builder()
                    .build()
                    .getEntity();

            PersonResponseDto expected = PersonBuilder.builder()
                    .build()
                    .getResponseDto();

            when(personRepository.findByUuid(personUuid))
                    .thenReturn(Optional.ofNullable(person));

            // when
            PersonResponseDto actual = personService.getById(personUuid);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldReturnExceptionPersonNotExistWithUUID() {
            // given
            UUID incorrectUuid = INCORRECT_UUID;

            Optional<PersonEntity> optionalPerson = Optional.empty();

            when(personRepository.findByUuid(incorrectUuid))
                    .thenReturn(optionalPerson);

            // when, then
            assertThrows(NotFoundException.class, () -> personService.getById(incorrectUuid));
        }
    }

    @Nested
    class FindAllPersons {

        @Test
        void shouldReturnListOfPersons() {
            // given
            int expectedSize = 1;
            List<PersonEntity> personList = List.of(PersonBuilder.builder()
                    .build()
                    .getEntity());
            Page<PersonEntity> page = new PageImpl<>(personList);

            when(personRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<PersonResponseDto> actual = personService.getAll(PageRequest.of(0, 2));

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
            verify(personRepository, times(1)).findAll(any(PageRequest.class));
        }

    }

    @Nested
    class Create {

        @Test
        void shouldReturnCreatedPerson() {
            // given
            PersonCreateRequestDto personRequest = PersonBuilder.builder()
                    .build()
                    .getRequestDto();

            PersonEntity expected = PersonBuilder.builder()
                    .build()
                    .getEntity();

            PersonEntity person = PersonBuilder.builder()
                    .build()
                    .getEntity();

            HouseEntity house = HouseBuilder.builder()
                    .build()
                    .getEntity();

            when(houseRepository.findByUuid(personRequest.getHouseId()))
                    .thenReturn(Optional.ofNullable(house));

            expected.setHouse(house);

            when(personRepository.save(any(PersonEntity.class)))
                    .thenReturn(person);

            // when
            personService.create(personRequest);

            // then
            verify(personRepository, times(1)).save(captor.capture());
            PersonEntity actual = captor.getValue();
            assertThat(actual.getName()).isEqualTo(expected.getName());
            assertThat(actual.getSurname()).isEqualTo(expected.getSurname());
            assertThat(actual.getHouse()).isEqualTo(expected.getHouse());
        }
    }

    @Nested
    class Update {

        @Test
        void shouldReturnUpdatedPerson() {
            // given
            UUID personUuid = PERSON_UUID;
            UUID houseUUID = HOUSE_UUID;

            PersonCreateRequestDto personRequest = PersonBuilder.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build()
                    .getRequestDto();

            PersonEntity updatedPerson = PersonBuilder.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build()
                    .getEntity();

            PersonEntity person = PersonBuilder.builder()
                    .build()
                    .getEntity();

            HouseEntity house = HouseBuilder.builder()
                    .build()
                    .getEntity();

            PersonResponseDto expected = PersonBuilder.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build()
                    .getResponseDto();

            when(houseRepository.findByUuid(houseUUID))
                    .thenReturn(Optional.ofNullable(house));
            when(personRepository.findByUuid(personUuid))
                    .thenReturn(Optional.ofNullable(person));

            updatedPerson.setId(person.getId());
            updatedPerson.setUuid(person.getUuid());
            updatedPerson.setHouse(house);
            updatedPerson.setCreateDate(person.getCreateDate());

            when(personRepository.save(any(PersonEntity.class)))
                    .thenReturn(updatedPerson);

            // when
            PersonResponseDto actual = personService.update(personUuid, personRequest);

            // then
            assertThat(actual).isEqualTo(expected);
        }

    }


    @Nested
    class Delete {

        @Test
        void shouldDeletePersonByUUID() {
            // given
            UUID personUuid = PERSON_UUID;

            PersonEntity person = PersonBuilder.builder()
                    .build()
                    .getEntity();

            when(personRepository.findByUuid(personUuid))
                    .thenReturn(Optional.ofNullable(person));

            // when
            personService.delete(personUuid);

            // then
            verify(personRepository, times(1)).findByUuid(personUuid);
        }

    }
}