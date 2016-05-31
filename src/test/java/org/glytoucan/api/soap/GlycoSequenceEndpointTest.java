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
		Assert.assertTrue(response.getDescription().contains("Galb1-4GlcNAcb1-R"));
	}
}