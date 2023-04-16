package com.hubspot.assessment.service;

import com.hubspot.assessment.comparator.PartnerComparator;
import com.hubspot.assessment.httpclient.RestHttpClient;
import com.hubspot.assessment.model.Invitation;
import com.hubspot.assessment.model.InvitationRequest;
import com.hubspot.assessment.model.Partner;
import com.hubspot.assessment.model.PartnerResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvitationService {

	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final long DAYS_TO_ADD = 1L;
	private final RestHttpClient restHttpClient;
	private final PartnersService partnersService;

	private final Comparator<Map.Entry<String, List<Partner>>> partnerComparator;

	private String url;

	public InvitationService(RestHttpClient restHttpClient, PartnersService partnersService,
			PartnerComparator partnerComparator, @Value("${invitation.endpoint}") String url) {
		this.restHttpClient = restHttpClient;
		this.partnersService = partnersService;
		this.partnerComparator = partnerComparator;
		this.url = url;
	}

	public int sendInvitations() {
		return Optional.ofNullable(partnersService.getPartnerResponse())
				.map(PartnerResponse::getPartners)
				.map(p -> restHttpClient.sendRequest(buildInvitationRequest(p), url))
				.orElse(0);
	}

	private InvitationRequest buildInvitationRequest(List<Partner> partners) {
		partners.forEach(this::setAvailableDates);
		final Map<String, Map.Entry<String, List<Partner>>> countryPartners = getCountryPartners(partners);
		return InvitationRequest.builder()
				.countries(invitations(countryPartners))
				.build();
	}

	private Map<String, Map.Entry<String, List<Partner>>> getCountryPartners(List<Partner> partners) {
		return partners.stream()
				.collect(Collectors.groupingBy(Partner::getCountry,
						Collectors.collectingAndThen(Collectors.toList(), this::dateByPartners)));
	}

	private Map.Entry<String, List<Partner>> dateByPartners(List<Partner> partners) {

		final Map<String, List<Partner>> partnersByDateMap = partners.stream()
				.map(Partner::getAvailableDates)
				.flatMap(List::stream)
				.distinct()
				.collect(Collectors.toMap(d -> d, date -> mapToPartnersByDate(date, partners)));

		final List<Map.Entry<String, List<Partner>>> partnersByDateList = new ArrayList(partnersByDateMap.entrySet());
		Collections.sort(partnersByDateList, partnerComparator);
		return partnersByDateList.size() > 0 ? partnersByDateList.get(0) : null;
	}

	private List<Partner> mapToPartnersByDate(String date, List<Partner> partners) {
		return partners.stream()
				.filter(p -> p.getAvailableDates().contains(date))
				.collect(Collectors.toList());
	}

	private List<Invitation> invitations(Map<String, Map.Entry<String, List<Partner>>> countryPartners) {

		return countryPartners.entrySet()
				.stream()
				.map(this::mapToInvitation)
				.collect(Collectors.toList());
	}

	private Invitation mapToInvitation(Map.Entry<String, Map.Entry<String, List<Partner>>> entry) {
		return Optional.ofNullable(entry.getValue())
				.map(partnerEntry -> Invitation.builder()
						.attendeeCount(partnerEntry.getValue().size())
						.attendees(partnerEntry.getValue().stream()
								.map(Partner::getEmail)
								.collect(Collectors.toList()))
						.name(entry.getKey())
						.startDate(partnerEntry.getKey())
						.build())
				.orElse(Invitation.builder()
						.name(entry.getKey())
						.attendees(new ArrayList<>())
						.build());
	}

	private void setAvailableDates(Partner partner) {

		final List<String> availableDates = partner.getAvailableDates();
		final List<String> possibleDates = availableDates.stream()
				.filter(startDate -> availableDates.contains(
						LocalDate.parse(startDate, FORMATTER).plusDays(DAYS_TO_ADD).format(FORMATTER)))
				.collect(Collectors.toList());

		partner.setAvailableDates(possibleDates);
	}

}
