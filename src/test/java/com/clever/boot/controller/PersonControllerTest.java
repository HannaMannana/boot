package com.clever.boot.controller;

import com.clever.boot.dto.PersonCreateRequestDto;
import com.clever.boot.dto.PersonResponseDto;
import com.clever.boot.entity.HouseHistoryEntity;
import com.clever.boot.service.HouseHistoryService;
import com.clever.boot.service.PersonService;
import com.clever.boot.util.PersonBuilder;
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

import static com.clever.boot.util.DataHouse.HOUSE_UUID;
import static com.clever.boot.util.DataPerson.*;
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
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    @MockBean
    private HouseHistoryService houseHistoryService;

    private static final String URL = "/persons";

    @Nested
    class FindById {

        @SneakyThrows
        @Test
        void shouldReturnPersonAndStatusOk() {
            // given
            UUID personUuid = PERSON_UUID;

            PersonResponseDto response = PersonBuilder.builder()
                    .build()
                    .getResponseDto();

            when(personService.getById(personUuid))
                    .thenReturn(response);

            // when, then
            mockMvc.perform(get(URL + "/" + personUuid))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.uuid").value(personUuid.toString()));
        }

        @SneakyThrows
        @Test
        void shouldReturnNotFoundException() {
            // given
            UUID personUuid = INCORRECT_UUID;

            when(personService.getById(any()))
                    .thenThrow(new NotFoundException("Not found"));

            // when, then
            mockMvc.perform(get(URL + "/" + personUuid))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));
        }
    }

    @Nested
    class FindAll {

        @SneakyThrows
        @Test
        void shouldReturnAllPersons() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 2);

            PersonResponseDto person = PersonBuilder.builder()
                    .build()
                    .getResponseDto();

            List<PersonResponseDto> personList = List.of(person);

            Page<PersonResponseDto> page = PageableExecutionUtils.getPage(
                    personList,
                    pageRequest,
                    personList::size);

            when(personService.getAll(pageRequest))
                    .thenReturn(page);

            // when, then
            MvcResult mvcResult = mockMvc.perform(get(URL + "?page=0&size=2"))
                    .andExpect(status().isOk())
                    .andReturn();

            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(Integer.parseInt(response.getContentAsString()));
            assertThat(jsonObject.get("totalPages")).isEqualTo(1);
            assertThat(jsonObject.get("totalElements")).isEqualTo(1);
            assertThat(jsonObject.get("number")).isEqualTo(0);
            assertThat(jsonObject.get("size")).isEqualTo(2);
            assertThat(jsonObject.get("content")).isNotNull();
        }

    }

    @Nested
    class FindTenants {

        @SneakyThrows
        @Test
        void shouldReturnTenants() {
            // given
            UUID houseUuid = HOUSE_UUID;

            PersonResponseDto person = PersonBuilder.builder()
                    .build()
                    .getResponseDto();

            List<PersonResponseDto> personList = List.of(person);

            when(houseHistoryService.getPersonsByUuid(houseUuid, HouseHistoryEntity.Type.TENANT))
                    .thenReturn(personList);

            // when, then
            MvcResult mvcResult = mockMvc.perform(get(URL + "/tenants/" + houseUuid))
                    .andExpect(status().isOk())
                    .andReturn();

            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(Integer.parseInt(response.getContentAsString()));
            assertThat(jsonObject.get("content")).isNotNull();
        }

    }

    @Nested
    class FindOwners {

        @SneakyThrows
        @Test
        void shouldReturnOwners() {
            // given
            UUID houseUuid = HOUSE_UUID;

            PersonResponseDto person = PersonBuilder.builder()
                    .build()
                    .getResponseDto();

            List<PersonResponseDto> personList = List.of(person);

            when(houseHistoryService.getPersonsByUuid(houseUuid, HouseHistoryEntity.Type.OWNER))
                    .thenReturn(personList);

            // when, then
            MvcResult mvcResult = mockMvc.perform(get(URL + "/owners/" + houseUuid))
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
        void shouldReturnNewPerson() {

            // given
            PersonCreateRequestDto personRequest = PersonBuilder.builder()
                    .build().
                    getRequestDto();
            PersonResponseDto personResponse = PersonBuilder.builder()
                    .build()
                    .getResponseDto();

            String json = objectMapper.writeValueAsString(personResponse);

            when(personService.create(personRequest))
                    .thenReturn(personResponse);

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
        void shouldReturnUpdatedPerson() {
            // given
            UUID personUuid = PERSON_UUID;

            PersonCreateRequestDto personRequest = PersonBuilder.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build().
                    getRequestDto();
            PersonResponseDto personResponse = PersonBuilder.builder()
                    .withName(UPDATE_PERSON_NAME)
                    .withSurname(UPDATE_PERSON_SURNAME)
                    .build()
                    .getResponseDto();

            String json = objectMapper.writeValueAsString(personResponse);

            when(personService.update(personUuid, personRequest))
                    .thenReturn(personResponse);

            // when, then
            mockMvc.perform(put(URL + "/" + personUuid)
                            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpectAll(jsonPath("$.name").value(personResponse.getName()),
                            jsonPath("$.surname").value(personResponse.getSurname()));
        }
    }


    @Nested
    class Delete {

        @SneakyThrows
        @Test
        void shouldReturnStatusOk() {
            // given
            UUID personUuid = PERSON_UUID;

            doNothing().when(personService)
                    .delete(personUuid);

            // when, then
            mockMvc.perform(delete(URL + "/" + personUuid))
                    .andExpect(status().isNoContent());
        }
    }

}