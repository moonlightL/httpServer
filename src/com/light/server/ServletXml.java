package com.light.server;

/**
 * 封装 web.xml 中 <servlet> 配置信息
 * @author Light
 *
 */
public class ServletXml {

	private String servletName;
	
	private String servletClazz;

	public String getServletName() {
		return servletName;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	public String getServletClazz() {
		return servletClazz;
	}

	public void setServletClazz(String servletClazz) {
		this.servletClazz = servletClazz;
	}

}
