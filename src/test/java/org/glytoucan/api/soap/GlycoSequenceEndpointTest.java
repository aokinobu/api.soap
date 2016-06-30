/**
 * 
 */
package org.glytoucan.api.soap;

import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
}