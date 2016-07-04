package org.glytoucan.api.soap;

import org.glycoinfo.convert.GlyConvertConfig;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.service.impl.GlycanProcedureConfig;
import org.springframework.boot.SpringApplication;
//import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ GlycanProcedureConfig.class, VirtSesameTransactionConfig.class, GlyConvertConfig.class })
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.SpringBootWebSecurityConfiguration.class })
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}