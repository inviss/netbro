package kr.co.d2net.soap;

import java.rmi.RemoteException;

import javax.jws.WebService;

import kr.co.d2net.dto.xml.WebRoot;

@WebService
public interface Navigator {
	public String soapTest(String xml) throws RemoteException;
	public WebRoot getRoot() throws RemoteException;
}
