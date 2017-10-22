package com.light.server;

import java.util.HashMap;
import java.util.Map;

/**
 *  web 容器上下文
 * @author Light
 *
 */
public class ServletContext {

	// 存储处理不同请求的 servlet
	// servletName -> servlet 子类的全名
	private Map<String,String> servletMap;
	
	// 存储请求 url 与 servlet 的对应关系
	// 请求 url -> servletName
	private Map<String,String> mappingMap;
	
	public ServletContext() {
		this.servletMap = new HashMap<String, String>();
		this.mappingMap = new HashMap<String, String>();
	}

	public Map<String, String> getServletMap() {
		return servletMap;
	}

	public void setServletMap(Map<String, String> servletMap) {
		this.servletMap = servletMap;
	}

	public Map<String, String> getMappingMap() {
		return mappingMap;
	}

	public void setMappingMap(Map<String, String> mappingMap) {
		this.mappingMap = mappingMap;
	}
	
}
