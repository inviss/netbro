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
	private final List<NodeAddr> nodes = new ArrayList<NodeAddr>();
	private String clusterName;
	private String clusterNodes;
	private String numberOfShards;
	private String numberOfReplicas;
	private String refreshInterval;
	private String analyzer;
	private String storeType;
	private String searchColumns;
	private String routingPath;
	
	private static Configure instance;
	
	public static synchronized Configure getInstance(){
		if(instance == null){
			instance = new Configure();
		}
		return instance;
	}
	
	
	private  Configure(){
		Properties prop = PropertiesUtils.load(CONF_FILE, Configure.class);
		clusterNodes = prop.getProperty("clusterNodes");
		clusterName = prop.getProperty("clusterName");
		numberOfShards = prop.getProperty("numberOfShards");
		numberOfReplicas = prop.getProperty("numberOfReplicas");
		refreshInterval = prop.getProperty("refreshInterval");
		analyzer = prop.getProperty("analyzer");
		storeType = prop.getProperty("storeType");
		searchColumns = prop.getProperty("searchColumns");
		routingPath = prop.getProperty("routingPath");
		
		if(logger.isInfoEnabled()) {
			logger.info("[cluster.name]:" + clusterName);
			logger.info("[cluster.nodes]:" + clusterNodes);
		}
		String[] servers = clusterNodes.split(",");
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
	public String getClusterNodes() {
		return clusterNodes;
	}
	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}
	public String getNumberOfShards() {
		return numberOfShards;
	}
	public void setNumberOfShards(String numberOfShards) {
		this.numberOfShards = numberOfShards;
	}
	public String getNumberOfReplicas() {
		return numberOfReplicas;
	}
	public void setNumberOfReplicas(String numberOfReplicas) {
		this.numberOfReplicas = numberOfReplicas;
	}
	public String getRefreshInterval() {
		return refreshInterval;
	}
	public void setRefreshInterval(String refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
	public String getAnalyzer() {
		return analyzer;
	}
	public void setAnalyzer(String analyzer) {
		this.analyzer = analyzer;
	}
	public String getStoreType() {
		return storeType;
	}
	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}
	public String getSearchColumns() {
		return searchColumns;
	}
	public void setSearchColumns(String searchColumns) {
		this.searchColumns = searchColumns;
	}
	public String getRoutingPath() {
		return routingPath;
	}
	public void setRoutingPath(String routingPath) {
		this.routingPath = routingPath;
	}
	public List<NodeAddr> getNodes() {
		return nodes;
	}
	
}
