package com.code1024.oneconf;

/**
 * 内部使用的常量
 * @author ccor
 *
 */
class Constants {
	
	/**
	 * 配置中远端的配置服务的地址 
	 */
	public static final String CONFSERVER_URL = "confserver.url";
	/**
	 * 配置服务启动绑定ip
	 */
	public static final String CONFSERVER_IP = "confserver.ip";
	/**
	 * 配置服务启动绑定端口
	 */
	public static final String CONFSERVER_PORT = "confserver.port";
	/**
	 * 配置服务连接超时设置
	 */
	public static final int CONFSERVER_CONNECT_TIMEOUT = 10000;
	/**
	 * 配置服务读超时配置
	 */
	public static final int CONFSERVER_READ_TIMEOUT = 5000;

	/**
	 * 配置的默认位置（基于类路径）
	 */
	public static final String CONF_DEFAULT_LOCTION = "/conf.properties";
	/**
	 * 配置分组的前缀
	 */
	public static final String CONF_GROUP_PREFIX = "conf.";
}
