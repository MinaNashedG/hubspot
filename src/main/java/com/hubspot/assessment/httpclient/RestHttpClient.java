package com.hubspot.assessment.httpclient;

import com.hubspot.assessment.exception.BadRequestException;
import com.hubspot.assessment.model.InvitationRequest;
import com.hubspot.assessment.model.PartnerResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class RestHttpClient {
	private final RestTemplate restTemplate = new RestTemplate();

	public PartnerResponse getResponse(String url) {
		return restTemplate.exchange(getUrl(url), HttpMethod.GET, getHttpEntity(null), PartnerResponse.class)
				.getBody();
	}

	public int sendRequest(InvitationRequest invitationRequest, String url) {
		return restTemplate.postForEntity(getUrl(url), getHttpEntity(invitationRequest), InvitationRequest.class)
				.getStatusCodeValue();
	}

	private URI getUrl(String url) {
		try {
			return new URI(url);
		}
		catch (URISyntaxException e) {
			throw new BadRequestException("Bad Request sent");
		}
	}

	private HttpEntity getHttpEntity(Object body) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity(body, headers);
	}
}
