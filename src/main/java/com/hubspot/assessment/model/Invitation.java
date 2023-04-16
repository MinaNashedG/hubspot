package com.hubspot.assessment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invitation {

	private int attendeeCount;
	private List<String> attendees;
	private String name;
	private String startDate;

}
