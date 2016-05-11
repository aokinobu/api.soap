package org.glytoucan.api.soap;

import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.service.impl.GlycanProcedureConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import( {GlycanProcedureConfig.class, VirtSesameTransactionConfig.class} )
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}