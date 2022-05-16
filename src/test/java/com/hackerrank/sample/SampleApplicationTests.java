package com.hackerrank.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.dao.WeatherRepository;
import com.hackerrank.sample.dto.WeatherDTO;
import com.hackerrank.sample.exception.NoWeatherFoundException;
import com.hackerrank.sample.service.WeatherService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.WriterOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.input.WriterCharAppender;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.Writer;
import java.nio.charset.Charset;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class SampleApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("classpath:/addWeatherResponse.json")
    private Resource addWeatherResponse;

    @Autowired
    private WeatherService weatherService;

    @Before
    public void setup() {
    }

    @Test
    public void testInsert() throws Exception {
        mockMvc.perform(post("/endpoint/insert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        getWeatherDTO()))
                )
                .andDo(print())
                .andExpect(content().json(IOUtils.toString(addWeatherResponse.getURI(), Charset.defaultCharset())))
                .andExpect(status().isCreated());
    }

    private WeatherDTO getWeatherDTO() {
        return WeatherDTO.builder()
                .date(LocalDate.of(1985, 1, 1))
                .firstName("Foo")
                .lastName("Bar")
                .phoneNumber(3876542098L)
                .build();
    }

    @Test
    public void testGetWeatherById() throws Exception {
        weatherService.save(getWeatherDTO());
        mockMvc.perform(get("/endpoint/select/1"))
                .andDo(print())
                .andExpect(content().json(IOUtils.toString(addWeatherResponse.getURI(), Charset.defaultCharset())))
                .andExpect(
                        status().isOk());
    }

    @Test
    public void testGetAllWeather() throws Exception {
        weatherService.save(getWeatherDTO());
        mockMvc.perform(get("/endpoint/select"))
                .andDo(print())
                .andExpect(content().json("["+IOUtils.toString(addWeatherResponse.getURI(), Charset.defaultCharset())+"]"))
                .andExpect(
                        status().isOk());
    }

    @Test
    public void testDeleteWeatherById() throws Exception {
        weatherService.save(getWeatherDTO());
        mockMvc.perform(delete("/endpoint/delete/1"))
                .andDo(print())
                .andExpect(
                        status().isOk());
    }

    @Test
    public void testDeleteWeatherByIdWithoutId() throws Exception {
        mockMvc.perform(delete("/endpoint/delete/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoWeatherFoundException))
                .andExpect(result -> assertEquals("Weather ID is not found!!", result.getResolvedException().getMessage()));
    }
}
