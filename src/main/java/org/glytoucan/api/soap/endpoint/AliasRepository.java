package org.glytoucan.api.soap.endpoint;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.glytoucan.api.soap.NotationSchema;
import org.glytoucan.api.soap.TrivialName;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class AliasRepository {
	private static final List<TrivialName> names = new ArrayList<TrivialName>();

	@PostConstruct
	public void initData() {
		TrivialName bcsdb = new TrivialName();
		bcsdb.setName("aD3,7dgalNonp5N-ulosonic");
		bcsdb.setNotationSchema(NotationSchema.BCSDB);
		names.add(bcsdb);

		TrivialName msdb = new TrivialName();
		msdb.setName("a-dgal-NON-2:6|1:a|2:keto|3:d|7:d||(5d:1)n-acetyl");
		msdb.setNotationSchema(NotationSchema.MSDB);
		names.add(msdb);

		TrivialName carbbank = new TrivialName();
		carbbank.setName("a-D-3,7-deoxy-galNon2ulop5NAc-onic");
		carbbank.setNotationSchema(NotationSchema.CARBBANK);
		names.add(bcsdb);
	}

	public TrivialName findTrivialName(String name) {
		Assert.notNull(name);

		TrivialName result = null;

		for (TrivialName n : names) {
			if (name.equals(n.getName())) {
				result = n;
			}
		}

		return result;
	}
}
