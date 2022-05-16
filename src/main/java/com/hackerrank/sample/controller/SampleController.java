package com.hackerrank.sample.controller;

import com.hackerrank.sample.dto.WeatherDTO;
import com.hackerrank.sample.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/endpoint")
public class SampleController {

    private final WeatherService weatherService;

    public SampleController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping(value = "/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherDTO> addWeather(@RequestBody WeatherDTO weatherDTO) {
        return new ResponseEntity<>(weatherService.save(weatherDTO), HttpStatus.CREATED);
    }

    @GetMapping(value = "/select/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherDTO> getWeatherById(@PathVariable Long id) {
        return new ResponseEntity<>(weatherService.fetchById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/select", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WeatherDTO>> getAllWeather() {
        return new ResponseEntity<>(weatherService.fetchAll(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity removeWeatherById(@PathVariable Long id) {
        weatherService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
