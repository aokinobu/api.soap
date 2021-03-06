package org.glytoucan.api.soap.endpoint;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.convert.error.ConvertException;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.DerivatizedMass;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.ResourceEntry;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glycoinfo.rdf.service.exception.InvalidException;
import org.glytoucan.api.soap.GlycoSequenceArchivedRequest;
import org.glytoucan.api.soap.GlycoSequenceArchivedResponse;
import org.glytoucan.api.soap.GlycoSequenceCoreDetailRequest;
import org.glytoucan.api.soap.GlycoSequenceCountRequest;
import org.glytoucan.api.soap.GlycoSequenceCountResponse;
import org.glytoucan.api.soap.GlycoSequenceDetailRequest;
import org.glytoucan.api.soap.GlycoSequenceDetailResponse;
import org.glytoucan.api.soap.GlycoSequenceSearchResponse;
import org.glytoucan.api.soap.GlycoSequenceTextSearchRequest;
import org.glytoucan.api.soap.ResponseMessage;
import org.glytoucan.api.soap.util.ResponseMessageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * 
 * @author aoki
 * 
 *         The specification for this can be found at
 *         http://code.glytoucan.org/system/api_list/.
 *
 *         This work is licensed under the Creative Commons Attribution 4.0
 *         International License. To view a copy of this license, visit
 *         http://creativecommons.org/licenses/by/4.0/.
 *
 */
@Endpoint
public class GlycoSequenceEndpoint {
  private static final Log logger = LogFactory.getLog(GlycoSequenceEndpoint.class);
  private static final String NAMESPACE_URI = "http://soap.api.glytoucan.org/";

  private GlycanProcedure glycanProcedure;

  @Autowired
  public GlycoSequenceEndpoint(GlycanProcedure glycanProcedure) {
    this.glycanProcedure = glycanProcedure;
  }

  /**
   * 
   * Query entry using accession number.
   * 
   * @param accessionNumber
   * @return
   */
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "glycoSequenceDetailRequest")
  @ResponsePayload
  public GlycoSequenceDetailResponse queryEntry(@RequestPayload GlycoSequenceDetailRequest request) {
    Assert.notNull(request);
    Assert.notNull(request.getAccessionNumber());

    SparqlEntity se = null;
    ResponseMessage rm = new ResponseMessage();
    GlycoSequenceDetailResponse gsdr = new GlycoSequenceDetailResponse();
	try {
		se = glycanProcedure.getDescription(request.getAccessionNumber());
	} catch (InvalidException e) {
		// invalid data in se, return with errorcode.
	    rm.setMessage("Invalid Accession Number");
	    rm.setErrorCode(new BigInteger("-100"));
	    gsdr.setAccessionNumber(request.getAccessionNumber());
	    gsdr.setResponseMessage(rm);
	    return gsdr;
	}

    rm.setMessage(se.getValue(GlycanProcedure.Description));
    rm.setErrorCode(new BigInteger("0"));

    gsdr.setAccessionNumber(se.getValue(ResourceEntry.Identifier));
    gsdr.setDescription(se.getValue(GlycanProcedure.Description));
    gsdr.setIupac(se.getValue(GlycoSequence.Sequence));
    gsdr.setMass(se.getValue(DerivatizedMass.MassLabel));
    gsdr.setSequence(se.getValue(GlycoSequence.Sequence));
    gsdr.setResponseMessage(rm);
    return gsdr;
  }

  
  /**
   * 
   * Query entry using core accession number.
   * 
   * @param accessionNumber
   * @return
   */
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "glycoSequenceCoreDetailRequest")
  @ResponsePayload
  public GlycoSequenceDetailResponse queryEntry(@RequestPayload GlycoSequenceCoreDetailRequest request) {
    Assert.notNull(request);
    Assert.notNull(request.getAccessionNumber());

    SparqlEntity se = null;
    ResponseMessage rm = new ResponseMessage();
    GlycoSequenceDetailResponse gsdr = new GlycoSequenceDetailResponse();
	try {
		se = glycanProcedure.getDescriptionCore(request.getAccessionNumber());
	} catch (InvalidException e) {
		// invalid data in se, return with errorcode.
	    rm.setMessage("Invalid Accession Number");
	    rm.setErrorCode(new BigInteger("-100"));
	    gsdr.setAccessionNumber(request.getAccessionNumber());
	    gsdr.setResponseMessage(rm);
	    return gsdr;
	}

    rm.setMessage(se.getValue(GlycanProcedure.Description));
    rm.setErrorCode(new BigInteger("0"));

    gsdr.setAccessionNumber(se.getValue(ResourceEntry.Identifier));
    gsdr.setDescription(se.getValue(GlycanProcedure.Description));
    gsdr.setIupac(se.getValue(GlycoSequence.Sequence));
    gsdr.setMass(se.getValue(DerivatizedMass.MassLabel));
    gsdr.setSequence(se.getValue(GlycoSequence.Sequence));
    gsdr.setResponseMessage(rm);
    return gsdr;
  }
  
  /**
   * 
   * Search for entry using sequence text.
   * 
   * @param sequence
   *          text
   * @return glycosequencesearchresponse
   * 
   */
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "glycoSequenceTextSearchRequest")
  @ResponsePayload
  public GlycoSequenceSearchResponse searchSequence(@RequestPayload GlycoSequenceTextSearchRequest request) {
    Assert.notNull(request);
    Assert.notNull(request.getSequence());
    GlycoSequenceSearchResponse gssr = new GlycoSequenceSearchResponse();

    SparqlEntity se;
    try {
      se = glycanProcedure.searchBySequence(request.getSequence());
    } catch (SparqlException | ConvertException e) {
      ResponseMessage rm = ResponseMessageGenerator.extractException(e);
      rm.setErrorCode(new BigInteger(GlycanProcedure.CouldNotConvertErrorCode));
      gssr.setResponseMessage(rm);
      return gssr;
    }

    ResponseMessage rm = new ResponseMessage();
    rm.setMessage(se.getValue(GlycanProcedure.FromSequence));
    rm.setErrorCode(new BigInteger("0"));

    gssr.setAccessionNumber(se.getValue(GlycanProcedure.AccessionNumber));
    gssr.setConvertedSequence(se.getValue(GlycanProcedure.ResultSequence));
    gssr.setSequence(se.getValue(GlycanProcedure.Sequence));
    gssr.setImage(se.getValue(GlycanProcedure.Image));
    gssr.setResponseMessage(rm);
    return gssr;
  }
  
  /**
   * 
   * Query for total count.
   * 
   * @return glycosequencecountresponse
   * 
   */
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "glycoSequenceCountRequest")
  @ResponsePayload
  public GlycoSequenceCountResponse countSequence(@RequestPayload GlycoSequenceCountRequest request) {
    Assert.notNull(request);
    GlycoSequenceCountResponse gscr = new GlycoSequenceCountResponse();

    SparqlEntity se = glycanProcedure.getCount();

    ResponseMessage rm = new ResponseMessage();
    rm.setMessage(se.getValue("total"));
    rm.setErrorCode(new BigInteger("0"));

    gscr.setCount(se.getValue("total"));
    gscr.setResponseMessage(rm);
    return gscr;
  }
  
  /**
   * 
   * Query entry using core accession number.
   * 
   * @param accessionNumber
   * @return
   */
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "glycoSequenceArchivedRequest")
  @ResponsePayload
  public GlycoSequenceArchivedResponse queryEntry(@RequestPayload GlycoSequenceArchivedRequest request) {
    Assert.notNull(request);

    List<SparqlEntity> seList = null;
    String id = null;
    ResponseMessage rm = new ResponseMessage();
    GlycoSequenceArchivedResponse response = new GlycoSequenceArchivedResponse();
	try {
		seList = glycanProcedure.getArchivedAccessionNumbers(request.getOffset(), request.getLimit());
	} catch (InvalidException e) {
		// invalid data in se, return with errorcode.
	    rm.setMessage(e.getMessage());
	    rm.setErrorCode(new BigInteger("-100"));
	    response.setResponseMessage(rm);
	    return response;
	}

    rm.setMessage("archived list");
    rm.setErrorCode(new BigInteger("0"));

    response.setResponseMessage(rm);
    for (Iterator iterator = seList.iterator(); iterator.hasNext();) {
		SparqlEntity se = (SparqlEntity) iterator.next();
		if (se.getValue("message") != null) {
			response.getArchivedList().add(se.getValue("message"));
		} else {
			id = se.getValue("archivedId");
			logger.debug("archived ID:" + id);
			response.getArchivedList().add(id);
		}
	}

    return response;
  }
}