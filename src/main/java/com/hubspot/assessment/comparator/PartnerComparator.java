package com.hubspot.assessment.comparator;

import com.hubspot.assessment.model.Partner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
public class PartnerComparator implements Comparator<Map.Entry<String, List<Partner>>> {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public int compare(Map.Entry<String, List<Partner>> o1,
			Map.Entry<String, List<Partner>> o2) {
		final int partnerSizeO2 = o2.getValue().size();
		final int partnersSizeO1 = o1.getValue().size();
		if (partnersSizeO1 == partnerSizeO2) {
			return LocalDate.parse(o1.getKey(), FORMATTER)
					.compareTo(LocalDate.parse(o2.getKey(), FORMATTER));
		}
		return partnerSizeO2 - partnersSizeO1;
	}
}
