package kr.co.d2net.search.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kr.co.d2net.utils.PropertiesUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configure {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String CONF_FILE = "elastics.properties";
	private  final List<NodeAddr> nodes = new ArrayList<NodeAddr>();
	private  String clusterName;
	
	private static Configure instance;
	
	public static synchronized Configure getInstance(){
		if(instance == null){
			instance = new Configure();
		}
		return instance;
	}
	
	
	private  Configure(){
		Properties prop = PropertiesUtils.load(CONF_FILE, Configure.class);
		String seedStr = prop.getProperty("cluster.nodes");
		clusterName = prop.getProperty("cluster.name");
		
		if(logger.isInfoEnabled()) {
			logger.info("[cluster.name]:" + clusterName);
			logger.info("[cluster.nodes]:" + seedStr);
		}
		String[] servers = seedStr.split(",");
		for(String s : servers){
			String[] hostPort = s.split(":");
			String host = hostPort[0];
			Integer port = Integer.valueOf(hostPort[1]);
			nodes.add(new NodeAddr(host,port));
		}
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public List<NodeAddr> getNodes() {
		return nodes;
	}
	
}
