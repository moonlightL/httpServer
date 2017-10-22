package com.light.server;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * xmlÎÄ¼þ½âÎöÆ÷
 * @author Light
 *
 */
public class WebHandler extends DefaultHandler{
	
	private List<ServletXml> servletXmlList;
	private List<ServletMappingXml> mappingXmlList;
	
	private ServletXml servletXml;
	private ServletMappingXml servletMappingXml;
	
	private String beginTag;
	private boolean isMapping;
	
	

	@Override
	public void startDocument() throws SAXException {
		servletXmlList = new ArrayList<>();
		mappingXmlList = new ArrayList<>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName != null) {
			beginTag = qName;
			
			if ("servlet".equals(qName)) {
				isMapping = false;
				servletXml = new ServletXml();
			} else if ("servlet-mapping".equals(qName)){
				isMapping = true;
				servletMappingXml = new ServletMappingXml();
			}
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (this.beginTag != null) {
			String str = new String(ch,start,length);
			
			if(isMapping) {
				if("servlet-name".equals(beginTag)) {
					servletMappingXml.setServletName(str);
				} else if ("url-pattern".equals(beginTag)) {
					servletMappingXml.getUrlPattern().add(str);
				}
				
			} else {
				if("servlet-name".equals(beginTag)) {
					servletXml.setServletName(str);
				} else if ("servlet-class".equals(beginTag)) {
					servletXml.setServletClazz(str);
				}
				
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName != null) {
			
			if ("servlet".equals(qName)) {
				this.servletXmlList.add(this.servletXml);
			} else if ("servlet-mapping".equals(qName)){
				this.mappingXmlList.add(this.servletMappingXml);
			}
		}
		this.beginTag = null;
	}
	
	public List<ServletXml> getServletXmlList() {
		return servletXmlList;
	}

	public List<ServletMappingXml> getMappingXmlList() {
		return mappingXmlList;
	}

}
