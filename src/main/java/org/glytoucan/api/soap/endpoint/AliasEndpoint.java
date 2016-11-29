package org.glytoucan.api.soap.endpoint;

import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.api.soap.AliasRegisterRequest;
import org.glytoucan.api.soap.AliasRegisterResponse;
import org.glytoucan.api.soap.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class AliasEndpoint {
  
  private static final Log log = LogFactory.getLog(AliasEndpoint.class);

	private static final String NAMESPACE_URI = "http://soap.api.glytoucan.org/";

	private AliasRepository aliasRepository;

	@Autowired
	public AliasEndpoint(AliasRepository aliasRepository) {
		this.aliasRepository = aliasRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "aliasRegisterRequest")
	@ResponsePayload
	public AliasRegisterResponse registerAlias(@RequestPayload AliasRegisterRequest request) {
	  Assert.notNull(request);
	  Assert.notNull(request.getResponseMessage());
	  Assert.notNull(request.getResponseMessage().getMessage());

	  Assert.notNull(request.getResponseMessage().getMessage());
		AliasRegisterResponse response = new AliasRegisterResponse();
		ResponseMessage rm = new ResponseMessage();
		rm.setMessage("SUCCESS!" + this.aliasRepository.findTrivialName(request.getTrivialName().getName()) + "response message:>" + request.getResponseMessage().getMessage());
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