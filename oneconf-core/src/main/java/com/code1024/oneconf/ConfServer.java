package com.code1024.oneconf;

import static com.code1024.oneconf.Constants.CONFSERVER_IP;
import static com.code1024.oneconf.Constants.CONFSERVER_PORT;
import static com.code1024.oneconf.Constants.CONF_DEFAULT_LOCTION;
import static com.code1024.oneconf.Constants.CONF_GROUP_PREFIX;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * 配置参数的集中发布服务的简单实现
 * @author ccor
 */
public class ConfServer {
	
	@SuppressWarnings("restriction")
	public static void main(String[] args) throws IOException {

		Properties conf = new OrderProperties();
		conf.load(ConfServer.class.getResourceAsStream(CONF_DEFAULT_LOCTION));
		String hostname = conf.getProperty(CONFSERVER_IP);
		int port = Integer.parseInt(conf.getProperty(CONFSERVER_PORT)); 
		
		InetSocketAddress addr = new InetSocketAddress(hostname, port);
		com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(addr, 0);
		server.createContext("/", (com.sun.net.httpserver.HttpExchange hx) -> {
			String uri = hx.getRequestURI().toString();
			String key = uri.substring(1);
			String groupName = CONF_GROUP_PREFIX + key;
			Properties groupConf = new OrderProperties();
			if(conf.containsKey(groupName)){
				String[] prefixs = conf.getProperty(groupName).split(",");
				for(Object k : conf.keySet()){
					String kstr = (String) k;
					int pos = kstr.indexOf(".");
					if(pos > 0){
						String prefix = kstr.substring(0, pos);
						if(inArr(prefixs, prefix)){
							groupConf.put(k, conf.get(k));
						}
					}
				}
			}
			
			com.sun.net.httpserver.Headers responseHeaders = hx.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/plain;charset=utf-8");
            hx.sendResponseHeaders(200, 0);
            OutputStream out = hx.getResponseBody();
            groupConf.store(out, null);
            out.close();
		});
		server.start();
		System.out.println("ConfServer start @ "+hostname+":"+port);
	}
	
	private static boolean inArr(String[] arr, String val){
		for(String s : arr){
			if(s.equals(val)){
				return true;
			}
		}
		return false;
	}

}
