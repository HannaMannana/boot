package com.clever.boot.controller;

import com.clever.boot.dto.HouseCreateRequestDto;
import com.clever.boot.dto.HouseResponseDto;
import com.clever.boot.entity.HouseHistoryEntity;
import com.clever.boot.service.HouseHistoryService;
import com.clever.boot.service.HouseService;
import com.clever.boot.util.HouseBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.exception.NotFoundException;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static com.clever.boot.util.DataHouse.*;
import static com.clever.boot.util.DataPerson.PERSON_UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class HouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HouseService houseService;

    @MockBean
    private HouseHistoryService houseHistoryService;

    private static final String URL = "/houses";

    @Nested
    class FindById {

        @SneakyThrows
        @Test
        void shouldReturnHouseAndStatusOk() {
            // given
            UUID houseUuid = HOUSE_UUID;

            HouseResponseDto response = HouseBuilder.builder()
                    .build()
                    .getResponseDto();

            when(houseService.getById(houseUuid))
                    .thenReturn(response);

            // when, then
            mockMvc.perform(get(URL + "/" + houseUuid))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.uuid").value(houseUuid.toString()));
        }

        @SneakyThrows
        @Test
        void shouldReturnNotFoundException() {
            // given
            UUID houseUuid = INCORRECT_UUID;

            when(houseService.getById(any()))
                    .thenThrow(new NotFoundException("Not found"));

            // when, then
            mockMvc.perform(get(URL + "/" + houseUuid))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
        }
    }

    @Nested
    class FindAll {

        @SneakyThrows
        @Test
        void shouldReturnAllHouses() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 2);

            HouseResponseDto houseResponseDto = HouseBuilder.builder()
                    .build()
                    .getResponseDto();

            List<HouseResponseDto> houseList = List.of(houseResponseDto);

            Page<HouseResponseDto> page = PageableExecutionUtils.getPage(
                    houseList,
                    pageRequest,
                    houseList::size);

            when(houseService.getAll(pageRequest))
                    .thenReturn(page);

            // when, then
            MvcResult mvcResult = mockMvc.perform(get(URL + "?page=0&size=2"))
                    .andExpect(status().isOk())
                    .andReturn();

            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(Integer.parseInt(response.getContentAsString()));
            assertThat(jsonObject.get("pages")).isEqualTo(1);
            assertThat(jsonObject.get("count")).isEqualTo(1);
            assertThat(jsonObject.get("number")).isEqualTo(0);
            assertThat(jsonObject.get("size")).isEqualTo(2);
            assertThat(jsonObject.get("content")).isNotNull();
        }


        @Nested
        class FindHousesWithTenants {

            @SneakyThrows
            @Test
            void shouldReturnHousesWithTenants() {
                // given
                UUID personUuid = PERSON_UUID;

                HouseResponseDto house = HouseBuilder.builder()
                        .build()
                        .getResponseDto();

                List<HouseResponseDto> houseList = List.of(house);

                when(houseHistoryService.getHousesByUuid(personUuid, HouseHistoryEntity.Type.TENANT))
                        .thenReturn(houseList);

                // when, then
                MvcResult mvcResult = mockMvc.perform(get(URL + "/tenants/" + personUuid))
                        .andExpect(status().isOk())
                        .andReturn();

                MockHttpServletResponse response = mvcResult.getResponse();
                JSONObject jsonObject = new JSONObject(Integer.parseInt(response.getContentAsString()));
                assertThat(jsonObject.get("content")).isNotNull();
            }
        }

        @Nested
        class FindHousesWithOwners {

            @SneakyThrows
            @Test
            void shouldReturnWithOwners() {
                // given
                UUID personUuid = PERSON_UUID;

                HouseResponseDto house = HouseBuilder.builder()
                        .build()
                        .getResponseDto();

                List<HouseResponseDto> houseList = List.of(house);

                when(houseHistoryService.getHousesByUuid(personUuid, HouseHistoryEntity.Type.OWNER))
                        .thenReturn(houseList);

                // when, then
                MvcResult mvcResult = mockMvc.perform(get(URL + "/owners/" + personUuid))
                        .andExpect(status().isOk())
                        .andReturn();

                MockHttpServletResponse response = mvcResult.getResponse();
                JSONObject jsonObject = new JSONObject(Integer.parseInt(response.getContentAsString()));
                assertThat(jsonObject.get("content")).isNotNull();
            }
        }


        @Nested
        class Create {

            @SneakyThrows
            @Test
            void shouldReturnNewHouse() {

                // given
                HouseCreateRequestDto houseRequest = HouseBuilder.builder()
                        .build().
                        getRequestDto();
                HouseResponseDto houseResponse = HouseBuilder.builder()
                        .build()
                        .getResponseDto();

                String json = objectMapper.writeValueAsString(houseResponse);

                when(houseService.create(houseRequest))
                        .thenReturn(houseResponse);

                // when, then
                mockMvc.perform(post(URL)
                                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .content(json))
                        .andExpect(status().isCreated());
            }
        }

        @Nested
        class Update {

            @SneakyThrows
            @Test
            void shouldReturnUpdatedHouse() {
                // given
                UUID houseUuid = HOUSE_UUID;

                HouseCreateRequestDto houseRequest = HouseBuilder.builder()
                        .withCity(String.valueOf(UPDATE_HOUSE_NUMBER))
                        .build().
                        getRequestDto();
                HouseResponseDto houseResponse = HouseBuilder.builder()
                        .withCity(String.valueOf(UPDATE_HOUSE_NUMBER))
                        .build()
                        .getResponseDto();
                String json = objectMapper.writeValueAsString(houseResponse);

                when(houseService.update(houseUuid, houseRequest))
                        .thenReturn(houseResponse);

                // when, then
                mockMvc.perform(put(URL + "/" + houseUuid)
                                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .content(json))
                        .andExpect(status().isOk())
                        .andExpectAll(jsonPath("$.number").value(houseResponse.getNumber()));
            }
        }


        @Nested
        class Delete {

            @SneakyThrows
            @Test
            void shouldReturnStatusOk() {
                // given
                UUID houseUuid = HOUSE_UUID;

                doNothing().when(houseService)
                        .delete(houseUuid);

                // when, then
                mockMvc.perform(delete(URL + "/" + houseUuid))
                        .andExpect(status().isNoContent());
            }
        }

    }
}