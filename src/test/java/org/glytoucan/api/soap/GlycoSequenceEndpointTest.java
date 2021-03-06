/**
 * 
 */
package org.glytoucan.api.soap;

import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glycoinfo.rdf.service.exception.InvalidException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * @author developer
 *
 *         This work is licensed under the Creative Commons Attribution 4.0
 *         International License. To view a copy of this license, visit
 *         http://creativecommons.org/licenses/by/4.0/.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
public class GlycoSequenceEndpointTest {
	
	private static final Log logger = LogFactory.getLog(GlycoSequenceEndpointTest.class);

	private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

	@Value("${local.server.port}")
	private int port = 0;

	@Autowired
	GlycanProcedure glycanProcedure;
	
	@Before
	public void init() throws Exception {
		marshaller.setPackagesToScan(ClassUtils.getPackageName(AliasRegisterRequest.class));
		marshaller.afterPropertiesSet();
	}

	@Test
	public void testSendAndReceive() {
		GlycoSequenceDetailRequest request = new GlycoSequenceDetailRequest();
		request.setAccessionNumber("G00055MO");
		
		Object result = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
				+ port + "/ws", request);
		assertNotNull(result);
		GlycoSequenceDetailResponse response = (GlycoSequenceDetailResponse)result;
		logger.debug(response);
		logger.debug(response.getDescription());
		Assert.assertEquals(new BigInteger("0"),response.getResponseMessage().getErrorCode());
		Assert.assertEquals("G00055MO", response.getAccessionNumber());
		Assert.assertTrue(response.getDescription().contains("Gal(b1-4)GlcNAc(b1-"));
	}
	
	 @Test
	  public void testSendAndReceiveG97036DW() {
	    GlycoSequenceDetailRequest request = new GlycoSequenceDetailRequest();
	    request.setAccessionNumber("G97036DW");
	    
	    Object result = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
	        + port + "/ws", request);
	    assertNotNull(result);
	    GlycoSequenceDetailResponse response = (GlycoSequenceDetailResponse)result;
	    logger.debug(response);
	    logger.debug(response.getDescription());
	    Assert.assertEquals(new BigInteger("0"),response.getResponseMessage().getErrorCode());
	    Assert.assertEquals("G97036DW", response.getAccessionNumber());
	    Assert.assertTrue(response.getDescription().contains("Error+in+GlycoCT+validation"));
	  }
	 
   @Test
   public void testSendAndReceiveInvalid() {
     GlycoSequenceDetailRequest request = new GlycoSequenceDetailRequest();
     request.setAccessionNumber("G97036DWASDF");
     
     Object result = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
         + port + "/ws", request);
     assertNotNull(result);
     GlycoSequenceDetailResponse response = (GlycoSequenceDetailResponse)result;
     logger.debug(response);
     logger.debug(response.getDescription());
     Assert.assertEquals(new BigInteger("-100"),response.getResponseMessage().getErrorCode());
     Assert.assertEquals("G97036DWASDF", response.getAccessionNumber());
//     Assert.assertTrue(response.getDescription().contains("Error+in+GlycoCT+validation"));
   }
   
   @Test
   public void testSendAndReceiveArchived() {
     GlycoSequenceCoreDetailRequest request = new GlycoSequenceCoreDetailRequest();
     request.setAccessionNumber("G68335WN");
     
     Object result = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
         + port + "/ws", request);
     assertNotNull(result);
     GlycoSequenceDetailResponse response = (GlycoSequenceDetailResponse)result;
     logger.debug(response);
     logger.debug(response.getDescription());
     Assert.assertEquals(new BigInteger("-100"),response.getResponseMessage().getErrorCode());
     Assert.assertEquals("G68335WN", response.getAccessionNumber());
   }
	 
//   @Test
//   @Transactional
//   public void testSendAndReceiveNewWithNoIupac() {
//     // new accession number with no iupac...
//     // need -da command line param for java
//     GlycoSequenceDetailRequest request = new GlycoSequenceDetailRequest();
//     request.setAccessionNumber("G86383BI");
//     
//     Object result = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
//         + port + "/ws", request);
//     assertNotNull(result);
//     GlycoSequenceDetailResponse response = (GlycoSequenceDetailResponse)result;
//     logger.debug(response);
//     logger.debug(response.getDescription());
//     Assert.assertEquals(new BigInteger("0"),response.getResponseMessage().getErrorCode());
//     Assert.assertEquals("G86383BI", response.getAccessionNumber());
//     Assert.assertTrue(response.getDescription().contains("G86383BI"));
//   }
	
   @Test
   public void testSendAndReceiveTextSearchG94473FP() {
     GlycoSequenceTextSearchRequest request = new GlycoSequenceTextSearchRequest();
     request.setSequence("RES\n" + 
         "1b:b-dgal-HEX-1:5\n" + 
         "2b:a-dglc-HEX-1:5\n" + 
         "3s:n-acetyl\n" + 
         "LIN\n" + 
         "1:1o(4+1)2d\n" + 
         "2:2d(2+1)3n");
     
     Object result = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
         + port + "/ws", request);
     assertNotNull(result);
     GlycoSequenceSearchResponse response = (GlycoSequenceSearchResponse)result;
     logger.debug(response);
     Assert.assertEquals(new BigInteger("0"),response.getResponseMessage().getErrorCode());
     Assert.assertEquals("G94473FP", response.getAccessionNumber());
   }	
   
   @Test
   public void testSendAndReceiveTextSearchNotRegisteredSpaceCheck() {
     GlycoSequenceTextSearchRequest request = new GlycoSequenceTextSearchRequest();
     request.setSequence(" RES\n" + 
         "1b:b-dgal-HEX-1:5\n" + 
         "2b:a-dglc-HEX-1:5\n" + 
         "3s:n-acetyl\n" + 
         "LIN\n" + 
         "1:1o(4+1)2d\n" + 
         "2:2d(2+1)3n  \n");
     
     
     Object result = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
         + port + "/ws", request);
     assertNotNull(result);
     GlycoSequenceSearchResponse response = (GlycoSequenceSearchResponse)result;
     logger.debug(response);
     Assert.assertEquals(new BigInteger("0"),response.getResponseMessage().getErrorCode());
     Assert.assertEquals("G94473FP", response.getAccessionNumber());
   }  
   
	 @Test
   public void testSendAndReceiveTextSearchNotRegistered() {
     GlycoSequenceTextSearchRequest request = new GlycoSequenceTextSearchRequest();
     request.setSequence("RES\n" + 
         "1b:x-dglc-HEX-1:5\n" + 
         "2b:x-dman-HEX-1:5\n" + 
         "3b:x-lgal-HEX-1:5|6:d\n" + 
         "4b:x-dgal-HEX-1:5\n" + 
         "LIN\n" + 
         "1:1o(-1+1)2d\n" + 
         "2:1o(-1+1)3d\n" + 
         "3:3o(-1+1)4d");
     
     Object result = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
         + port + "/ws", request);
     assertNotNull(result);
     GlycoSequenceSearchResponse response = (GlycoSequenceSearchResponse)result;
     logger.debug(response);
     Assert.assertEquals(new BigInteger("0"),response.getResponseMessage().getErrorCode());
     Assert.assertEquals("not registered", response.getAccessionNumber());
   }  
	 
   @Test
   public void testSendAndReceiveTextSearchInvalid() {
     GlycoSequenceTextSearchRequest request = new GlycoSequenceTextSearchRequest();
     request.setSequence(" RES"
         + "1b:b-dgal-HEX-1:5\n" + 
         "2b:a-dglc-HEX-1:5\n" + 
         "3s:n-acetyl" + 
         "LIN\n" + 
         "1:1o(4+1)2d\n" + 
         "2:2d(2+1)3n  \n");
     
     
     Object result = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
         + port + "/ws", request);
     assertNotNull(result);
     GlycoSequenceSearchResponse response = (GlycoSequenceSearchResponse)result;
     logger.debug(response);
     Assert.assertTrue(response.getResponseMessage().getMessage().startsWith("Conversion Exception "));
     Assert.assertEquals(new BigInteger("-100"),response.getResponseMessage().getErrorCode());
   }  
   
//   @Test
   @Transactional
   public void testSendAndReceiveNewWithNoIupac() throws SparqlException {
     GlycoSequenceTextSearchRequest request = new GlycoSequenceTextSearchRequest();
     String sec = "RES\n" + 
         "1b:b-dglc-HEX-1:5\n" + 
         "2s:n-acetyl\n" + 
         "3b:b-dglc-HEX-1:5\n" + 
         "4s:n-acetyl\n" + 
         "5b:b-dman-HEX-1:5\n" + 
         "6b:a-dman-HEX-1:5\n" + 
         "7b:a-dman-HEX-1:5\n" + 
         "8b:x-llyx-PEN-1:5\n" + 
         "9b:x-dgal-HEX-1:5\n" + 
         "10b:x-lgal-HEX-1:5|6:d\n" + 
         "LIN\n" + 
         "1:1d(2+1)2n\n" + 
         "2:1o(4+1)3d\n" + 
         "3:3d(2+1)4n\n" + 
         "4:3o(4+1)5d\n" + 
         "5:5o(3+1)6d\n" + 
         "6:5o(6+1)7d\n" + 
         "7:7o(-1+1)8d\n" + 
         "8:8o(-1+1)9d\n" + 
         "9:9o(-1+1)10d";
     String acc = glycanProcedure.register(sec, "14e1d868cf50557143032041eef95cc7271b8c3a0bdc5a52fb849cdf29ef4aff");
     request.setSequence(sec);
     
     Object result = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
         + port + "/ws", request);
     assertNotNull(result);
     GlycoSequenceSearchResponse response = (GlycoSequenceSearchResponse)result;
     logger.debug(response);
     Assert.assertEquals(new BigInteger("0"),response.getResponseMessage().getErrorCode());
     Assert.assertEquals(acc, response.getAccessionNumber());
   }
   
   @Test
   public void testCount() {
     GlycoSequenceCountRequest request = new GlycoSequenceCountRequest();
     request.setType(ClassType.GLYCOSEQUENCE_WURCS);
     
     Object result = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
         + port + "/ws", request);
     assertNotNull(result);
     GlycoSequenceCountResponse response = (GlycoSequenceCountResponse)result;
     logger.debug(response);
     Assert.assertEquals(new BigInteger("0"),response.getResponseMessage().getErrorCode());
     Assert.assertEquals("59747",response.getCount());
   }
   
   @Test
   public void testSendAndReceiveArchivedList() {
     GlycoSequenceArchivedRequest request = new GlycoSequenceArchivedRequest();
     request.setLimit("100");
     request.setOffset("10");
     
     Object result = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
         + port + "/ws", request);
     assertNotNull(result);
     GlycoSequenceArchivedResponse response = (GlycoSequenceArchivedResponse)result;
     logger.debug(response);
     logger.debug(response.getArchivedList());
     
     for (Iterator iterator = response.getArchivedList().iterator(); iterator.hasNext();) {
		String id = (String) iterator.next();
		logger.debug("archived ID:" + id);		
     }
     
     Assert.assertEquals(new BigInteger("0"),response.getResponseMessage().getErrorCode());
     Assert.assertEquals(100, response.getArchivedList().size());
   }
}