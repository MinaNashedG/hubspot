package com.hackerrank.sample.mapper;

import com.hackerrank.sample.dto.WeatherDTO;
import com.hackerrank.sample.model.Weather;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

//@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Mapper(componentModel = "spring")
public interface WeatherMapper {

    WeatherDTO transform(Weather weather);

    Weather transform(WeatherDTO weatherDTO);
}
