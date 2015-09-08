package kr.co.d2net.search.config;

public class NodeAddr {
	private String host;
	private int port;
	
	public NodeAddr() {

	}

	public NodeAddr(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	
}
