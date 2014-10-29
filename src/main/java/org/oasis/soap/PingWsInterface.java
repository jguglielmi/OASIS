package org.oasis.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface PingWsInterface {

	@WebMethod String pingTest();
	@WebMethod String generateUuidWithName(String name);
	
}
