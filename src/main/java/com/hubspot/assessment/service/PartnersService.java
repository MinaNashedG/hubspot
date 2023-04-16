package com.hubspot.assessment.service;

import com.hubspot.assessment.httpclient.RestHttpClient;
import com.hubspot.assessment.model.PartnerResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PartnersService {

	private final RestHttpClient restHttpClient;
	private String url;

	public PartnersService(RestHttpClient restHttpClient, @Value("${partners.endpoint}") String url) {
		this.restHttpClient = restHttpClient;
		this.url = url;
	}

	public PartnerResponse getPartnerResponse() {
		return restHttpClient.getResponse(url);
	}
}
