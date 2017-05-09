package com.code1024.oneconf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 配置可以使用远程的的配置覆盖本地的.
 * 远程的地址配置键为：confserver.url=http://xxxx
 * @author ccor
 * 
 */
public class ConfPropertiesFactoryBean implements FactoryBean<Properties>, InitializingBean {
	
	private static final String BR = "\n";

	private static final String HTTP_METHOD_GET = "GET";

	private static final Charset UTF8 = Charset.forName("UTF-8");
	
	private String location = Constants.CONF_DEFAULT_LOCTION;
	private boolean singleton = true;
	private Properties singletonInstance;

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public Properties getObject() throws Exception {
		if (this.singleton) {
			return this.singletonInstance;
		} else {
			return createProperties();
		}
	}

	@Override
	public Class<?> getObjectType() {
		return Properties.class;
	}

	@Override
	public boolean isSingleton() {
		return this.singleton;
	}

	protected Properties createProperties() throws IOException {
		Properties p = new Properties();
		InputStream in = ConfPropertiesFactoryBean.class.getResourceAsStream(location);
		p.load(in);
		in.close();
		
		String confserverUrl = p.getProperty(Constants.CONFSERVER_URL);
		if(confserverUrl == null || confserverUrl.isEmpty()){
			return p;
		}
		
		URL requrl = new URL(confserverUrl);
		HttpURLConnection conn = (HttpURLConnection) requrl.openConnection();

		conn.setConnectTimeout(Constants.CONFSERVER_CONNECT_TIMEOUT);
		conn.setReadTimeout(Constants.CONFSERVER_READ_TIMEOUT);
		conn.setRequestMethod(HTTP_METHOD_GET);
		int statusCode = conn.getResponseCode();
		if (statusCode != 200){
			throw new RuntimeException("Get remote properties fail.responseCode="
					+ statusCode + ",confserverUrl=" + confserverUrl);
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF8));
		StringBuilder result = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			result.append(line).append(BR);
		}
		
		p.load(new StringReader(result.toString()));
		return p;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.singleton) {
			this.singletonInstance = createProperties();
		}		
	}

}
