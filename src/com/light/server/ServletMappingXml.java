package com.light.server;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装 web.xml 中 <servlet-mapping> 配置信息
 * @author Light
 *
 */
public class ServletMappingXml {

	private String servletName;
	
	private List<String> urlPattern = new ArrayList<>();

	public String getServletName() {
		return servletName;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	public List<String> getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(List<String> urlPattern) {
		this.urlPattern = urlPattern;
	}
	
}
