package kr.co.d2net.search;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import kr.co.d2net.search.config.Configure;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Clusters {
	final static Logger logger = LoggerFactory.getLogger(Clusters.class);

	private static final Map<String,Integer> seeds = new HashMap<String, Integer>();
	private static String clusterName;
	private static final int POOL_SIZE = 5;
	private static LinkedList<Client> pool = new LinkedList<Client>();
	private static int index = 0;
	private static Configure configure  = Configure.getInstance();
	
	public static synchronized Client getClient(String clusterName, String clusterNodes) {
		if(pool.isEmpty()) {
			buildClient(clusterName, clusterNodes);
		}
		
		index++;
		if(index >= pool.size()) {
			index = 0;
		}

		return pool.get(index);
	}

	public static synchronized Client getClient() {
		if(pool.isEmpty()) {
			buildClient();
		}

		index++;
		if(index >= pool.size()) {
			index = 0;
		}

		return pool.get(index);
	}

	private static void buildClient(String clusterName, String clusterNodes) {
		if(seeds.isEmpty()) {
			Clusters.clusterName = clusterName;
			String[] servers = clusterNodes.split(",");
			for(String s : servers){
				String[] hostPort = s.split(":");
				String host = hostPort[0];
				Integer port = Integer.valueOf(hostPort[1]);
				seeds.put(host,port);
			}
		}

		for(int i=0;i<POOL_SIZE;i++){
			Client client = makeClient(seeds);
			pool.add(client);
		}
	}
	
	private static void buildClient() {
		if(seeds.isEmpty()) {
			clusterName = configure.getClusterName();
			String seedStr = configure.getClusterNodes();
			String[] servers = seedStr.split(",");
			for(String s : servers){
				String[] hostPort = s.split(":");
				String host = hostPort[0];
				Integer port = Integer.valueOf(hostPort[1]);
				seeds.put(host,port);
			}
			if(logger.isInfoEnabled()) {
				logger.info(seeds.toString());
			}
		}

		for(int i=0;i<POOL_SIZE;i++){
			Client client = makeClient(seeds);
			pool.add(client);
		}
	}

	protected static Client makeClient(String host, int port){
		Builder builder = ImmutableSettings.settingsBuilder();
		if(StringUtils.isNotBlank(clusterName)){
			builder.put("cluster.name", clusterName);
		}
		Settings settings = builder.build();
		return new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(host, port));
	}

	protected static Client makeClient(Map<String,Integer> addrMap){
		Builder builder = ImmutableSettings.settingsBuilder();
		if(StringUtils.isNotBlank(clusterName)){
			builder.put("cluster.name", clusterName);
		}
		Settings settings = builder.build();
		TransportClient client =  new TransportClient(settings);
		for(Map.Entry<String,Integer> e : addrMap.entrySet()){
			client.addTransportAddress(new InetSocketTransportAddress(e.getKey(), e.getValue()));
		}
		return client;
	}
}
