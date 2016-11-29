package org.glytoucan.api.soap.endpoint;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.api.soap.AliasRegisterRequest;
import org.glytoucan.api.soap.AliasRegisterResponse;
import org.glytoucan.api.soap.ResponseMessage;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebIntegrationTest(randomPort = true)
public class AliasEndpointTest {
  
  private static final Log logger = LogFactory.getLog(AliasEndpointTest.class);
  
  private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
  
  @Before
  public void init() throws Exception {
    marshaller.setPackagesToScan(ClassUtils.getPackageName(AliasRegisterRequest.class));
    marshaller.afterPropertiesSet();
  }
  
  @Value("${local.server.port}")
  private int port = 0;
//  @Test
   public void testSendAndReceiveG97036DW() {

    AliasRegisterRequest request = new AliasRegisterRequest();
    ResponseMessage rm = new ResponseMessage();
    
    rm.setMessage("hi");
    request.setResponseMessage(rm);
     
     Object result = new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:"
         + port + "/ws", request);
     assertNotNull(result);
     AliasRegisterResponse response = (AliasRegisterResponse)result;
     logger.debug(response);
     Assert.assertEquals("hi", response.getResponseMessage().getMessage());
   }
  
}
