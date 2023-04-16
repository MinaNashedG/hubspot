package com.hubspot.assessment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.assessment.comparator.PartnerComparator;
import com.hubspot.assessment.httpclient.RestHttpClient;
import com.hubspot.assessment.model.InvitationRequest;
import com.hubspot.assessment.model.PartnerResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
class InvitationServiceTest {

	public static final String URL = "http://localhost:8080";
	private ObjectMapper objectMapper = new ObjectMapper();

	private InvitationService invitationService;

	@Captor
	private ArgumentCaptor<InvitationRequest> captor;
	@Mock
	private RestHttpClient restHttpClient;
	@Mock
	private PartnersService partnersService;

	@BeforeEach
	void setUp() throws IOException {
		invitationService = new InvitationService(restHttpClient, partnersService, new PartnerComparator(),
				URL);


	}

	@Test
	void should_return_invitation_list() throws IOException {
		//Given
		final InvitationRequest invitationRequest =
				objectMapper.readValue(new ClassPathResource("expected.json").getFile(), InvitationRequest.class);

		final PartnerResponse partnerResponse =
				objectMapper.readValue(new ClassPathResource("partners.json").getFile(),
						PartnerResponse.class);
		Mockito.when(partnersService.getPartnerResponse()).thenReturn(partnerResponse);
		//When
		invitationService.sendInvitations();

		//Then

		Mockito.verify(restHttpClient).sendRequest(captor.capture(), ArgumentMatchers.anyString());

		Assertions.assertThat(
						captor.getValue().getCountries())
				.containsExactlyInAnyOrderElementsOf(invitationRequest.getCountries());
	}
}