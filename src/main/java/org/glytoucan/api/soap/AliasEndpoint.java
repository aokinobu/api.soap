package org.glytoucan.api.soap;

import java.math.BigInteger;

import org.glytoucan.api.soap.contributor.AliasRegisterRequest;
import org.glytoucan.api.soap.contributor.AliasRegisterResponse;
import org.glytoucan.api.soap.contributor.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class AliasEndpoint {
	private static final String NAMESPACE_URI = "http://soap.api.glytoucan.org/contributor";

	private AliasRepository aliasRepository;

	@Autowired
	public AliasEndpoint(AliasRepository aliasRepository) {
		this.aliasRepository = aliasRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "aliasRegisterRequest")
	@ResponsePayload
	public AliasRegisterResponse registerAlias(@RequestPayload AliasRegisterRequest request) {
		AliasRegisterResponse response = new AliasRegisterResponse();
		ResponseMessage rm = new ResponseMessage();
		rm.setMessage("SUCCESS!" + this.aliasRepository.findTrivialName(request.getTrivialName().getName()));
		rm.setErrorCode(new BigInteger("0"));

		return response;
	}

	// @ResponsePayload
	// public AliasRegisterResponse getTrivialName(@RequestPayload
	// TrivialNameRequest request) {
	// ResponseMessage rm = new ResponseMessag()
	// response.setTrivialName(trivialNameRepository.findTrivialName(request.getSequence()));
	//
	// return response;
	// }
}