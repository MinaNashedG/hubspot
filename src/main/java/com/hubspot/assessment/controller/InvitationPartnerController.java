package com.hubspot.assessment.controller;

import com.hubspot.assessment.service.InvitationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class InvitationPartnerController {

	private final InvitationService invitationService;

	public InvitationPartnerController(InvitationService invitationService) {
		this.invitationService = invitationService;
	}

	@GetMapping(value = "/invitations", produces = MediaType.APPLICATION_JSON_VALUE)
	public int sendInvitations() {
		return invitationService.sendInvitations();
	}

}
