package kr.co.d2net.soap;

import java.rmi.RemoteException;
import javax.jws.WebService;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import kr.co.d2net.dto.xml.WebRoot;

@WebService(endpointInterface = "kr.co.d2net.soap.Navigator")
public class ServiceNavigator implements Navigator {

	public String soapTest(String xml) throws RemoteException {
		return Boolean.valueOf("true").toString();
	}

	@Produces({MediaType.APPLICATION_XML})
	public WebRoot getRoot() throws RemoteException {
		WebRoot root = new WebRoot();
		root.setVersion("1.1");
		root.setWriter("Kang");
		return root;
	}

}
