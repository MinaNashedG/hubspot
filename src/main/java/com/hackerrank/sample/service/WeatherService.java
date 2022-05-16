package com.hackerrank.sample.service;

import com.hackerrank.sample.dao.WeatherRepository;
import com.hackerrank.sample.dto.WeatherDTO;
import com.hackerrank.sample.exception.NoWeatherFoundException;
import com.hackerrank.sample.mapper.WeatherMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;

    private final WeatherMapper weatherMapper;


    public WeatherService(WeatherRepository weatherRepository, WeatherMapper weatherMapper) {
        this.weatherRepository = weatherRepository;
        this.weatherMapper = weatherMapper;
    }

    public WeatherDTO save(WeatherDTO weatherDTO) {
        return Optional.ofNullable(weatherDTO)
                .map(weatherMapper::transform)
                .map(weatherRepository::save)
                .map(weatherMapper::transform)
                .orElse(WeatherDTO.builder().build());
    }

    public WeatherDTO fetchById(Long id) throws NoWeatherFoundException {
        return weatherRepository.findById(id)
                .map(weatherMapper::transform)
                .orElseThrow(() -> new NoWeatherFoundException());
    }

    public List<WeatherDTO> fetchAll() {
        return weatherRepository.findAll(Sort.by(Sort.Order.by("id")))
                .stream()
                .map(weatherMapper::transform)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws NoWeatherFoundException {
        fetchById(id);
        weatherRepository.deleteById(id);
    }
}
