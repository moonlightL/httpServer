package com.light.server;

import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 配置文件类
 * @author Light
 *
 */
public class WebApp {

	private static ServletContext context;
	
	static {
		context = new ServletContext();
		Map<String,String> servletMap = context.getServletMap();
		Map<String,String> mappingMap = context.getMappingMap();
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser sax = factory.newSAXParser();
			
			WebHandler handler = new WebHandler();
			
			sax.parse(Thread
					.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("com/light/server/web.xml"), 
					handler);
			
			// 注册 servlet
			List<ServletXml> servletXmlList = handler.getServletXmlList();
			for (ServletXml servletXml : servletXmlList) {
				servletMap.put(servletXml.getServletName(), servletXml.getServletClazz());
			}
			
			// 注册 mapping
			List<ServletMappingXml> mappingXmlList = handler.getMappingXmlList();
			for (ServletMappingXml mapping : mappingXmlList) {
				List<String> urls = mapping.getUrlPattern();
				for (String url : urls) {
					mappingMap.put(url, mapping.getServletName());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  通过请求 url 获取对应的 Servlet 对象
	 * @param url
	 * @return
	 */
	public static String getServletClazz(String url) {
		if (url == null || "".equals(url.trim())) {
			return null;
		}
		
		String servletName = context.getMappingMap().get(url);
		String servletClazz = context.getServletMap().get(servletName);
		
		return servletClazz;
	}
}
