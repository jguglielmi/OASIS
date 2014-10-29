package org.oasis.soap;

import java.util.UUID;
import javax.jws.WebService;

@WebService(endpointInterface = "org.oasis.soap.PingWsInterface")
public class PingWsImpl implements PingWsInterface {

	@Override
	public String pingTest() {
		return "pong";
	}

	@Override
	public String generateUuidWithName(String name) {
		return name + "-" + UUID.randomUUID();
	}


}
