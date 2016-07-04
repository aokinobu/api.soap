package org.glytoucan.api.soap.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.convert.error.ConvertException;
import org.glycoinfo.rdf.SparqlException;
import org.glytoucan.api.soap.ResponseMessage;

public class ResponseMessageGenerator {
  
  private static final Log logger = LogFactory.getLog(ResponseMessageGenerator.class);
  
  public static ResponseMessage extractException(Exception e) {
    ResponseMessage rm = new ResponseMessage();
    logger.debug("error message>" + e.getMessage() + "<");
    if (StringUtils.isNotBlank(e.getMessage()))
      rm.setMessage(e.getMessage());
    else if (null != e.getCause() && StringUtils.isNotBlank(e.getCause().getMessage()))
      rm.setMessage(e.getCause().getMessage());
    
    if (StringUtils.isBlank(rm.getMessage())) {
      if (e instanceof ConvertException)
        rm.setMessage("Conversion Exception cause:" + e.getCause());
      if (e instanceof SparqlException)
        rm.setMessage("Sparql Exception cause:" + e.getCause());
    }
    logger.debug(rm.getMessage());
    
    return rm;    
  }
}
