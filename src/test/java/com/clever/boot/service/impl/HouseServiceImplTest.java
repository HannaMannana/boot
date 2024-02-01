package com.clever.boot.service.impl;

import com.clever.boot.dto.HouseCreateRequestDto;
import com.clever.boot.dto.HouseResponseDto;
import com.clever.boot.entity.HouseEntity;
import com.clever.boot.mapper.HouseMapper;
import com.clever.boot.repository.HouseRepository;
import com.clever.boot.service.HouseHistoryService;
import com.clever.boot.service.HouseService;
import com.clever.boot.util.HouseBuilder;
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

import static com.clever.boot.util.DataHouse.*;
import static com.clever.boot.util.DataPerson.PERSON_UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;


@RequiredArgsConstructor
class HouseServiceImplTest extends PostgresContainerInit {

    private final HouseService houseService;

    @MockBean
    private HouseRepository houseRepository;

    @MockBean
    private HouseMapper houseMapper;

    @Captor
    private ArgumentCaptor<HouseEntity> captor;

    @Nested
    class FindByUuid {

        @Test
        void shouldReturnHouse() {
            // given
            UUID houseUuid = HOUSE_UUID;

            HouseEntity house = HouseBuilder.builder()
                    .withUuid(HOUSE_UUID)
                    .build()
                    .getEntity();

            HouseResponseDto expected = HouseBuilder.builder()
                    .withUuid(HOUSE_UUID)
                    .build()
                    .getResponseDto();

            when(houseRepository.findByUuid(houseUuid))
                    .thenReturn(Optional.ofNullable(house));

            // when
            HouseResponseDto actual = houseService.getById(houseUuid);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldReturnExceptionHouseNotExistWithUUID() {
            // given
            UUID incorrectUuid = INCORRECT_UUID;

            Optional<HouseEntity> house = Optional.empty();

            when(houseRepository.findByUuid(incorrectUuid))
                    .thenReturn(house);

            // when, then
            assertThrows(NotFoundException.class, () -> houseService.getById(incorrectUuid));
        }
    }

    @Nested
    class FindHouses {

        @Test
        void shouldReturnListOfPersons() {
            // given
            int expectedSize = 1;
            List<HouseEntity> houseList = List.of(HouseBuilder.builder()
                    .build()
                    .getEntity());
            Page<HouseEntity> page = new PageImpl<>(houseList);

            when(houseRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<HouseResponseDto> actual = houseService.getAll(PageRequest.of(0, 2));

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
            verify(houseRepository, times(1)).findAll(any(PageRequest.class));
        }


        @Nested
        class Create {

            @Test
            void shouldReturnCreatedHouse() {
                // given
                HouseCreateRequestDto houseRequest = HouseBuilder.builder()
                        .build()
                        .getRequestDto();

                HouseEntity expected = HouseBuilder.builder()
                        .build()
                        .getEntity();

                HouseEntity house = HouseBuilder.builder()
                        .build()
                        .getEntity();

                when(houseRepository.save(any(HouseEntity.class)))
                        .thenReturn(house);

                // when
                houseService.create(houseRequest);

                // then
                verify(houseRepository, times(1)).save(captor.capture());
                HouseEntity actual = captor.getValue();
                assertThat(actual.getCity()).isEqualTo(expected.getCity());
            }
        }

        @Nested
        class Update {

            @Test
            void shouldReturnUpdatedHouse() {
                // given
                UUID houseUuid = HOUSE_UUID;

                HouseCreateRequestDto requestDto = HouseBuilder.builder()
                        .withStreet(HOUSE_STREET)
                        .withNumber(HOUSE_NUMBER)
                        .build()
                        .getRequestDto();

                HouseEntity updatedHouse = HouseBuilder.builder()
                        .withStreet(HOUSE_STREET)
                        .withNumber(HOUSE_NUMBER)
                        .build()
                        .getEntity();

                HouseEntity house = HouseBuilder.builder()
                        .build()
                        .getEntity();

                HouseResponseDto expected = HouseBuilder.builder()
                        .withStreet(HOUSE_STREET)
                        .withNumber(HOUSE_NUMBER)
                        .build()
                        .getResponseDto();

                when(houseRepository.findByUuid(houseUuid))
                        .thenReturn(Optional.ofNullable(house));

                updatedHouse.setId(house.getId());
                updatedHouse.setUuid(house.getUuid());
                updatedHouse.setCreateDate(house.getCreateDate());

                when(houseRepository.save(any(HouseEntity.class)))
                        .thenReturn(updatedHouse);

                // when
                HouseResponseDto actual = houseService.update(houseUuid, requestDto);

                // then
                assertThat(actual).isEqualTo(expected);
            }

        }

        @Nested
        class Delete {

            @Test
            void shouldDeleteHouseByUUID() {
                // given
                UUID houseUuid = HOUSE_UUID;

                HouseEntity house = HouseBuilder.builder()
                        .build()
                        .getEntity();


                when(houseRepository.findByUuid(houseUuid))
                        .thenReturn(Optional.of(house));

                // when
                houseService.delete(houseUuid);

                // then
                verify(houseRepository, times(1)).findByUuid(houseUuid);

            }


        }
    }
}