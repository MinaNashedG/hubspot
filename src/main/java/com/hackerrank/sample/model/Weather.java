package com.hackerrank.sample.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
public class Weather {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    @NotNull
    private LocalDate date;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
}
