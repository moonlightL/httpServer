package com.light.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Request {

	// 常量（回车+换行）
	private static final String RN = "\r\n";
	private static final String GET = "get";
	private static final String POST = "post";
	private static final String CHARSET = "GBK";
	// 请求方式
	private String method = "";
	// 请求 url
	private String url = "";
	// 请求参数
	private Map<String, List<String>> parameterMap;

	private InputStream in;

	private String requestInfo = "";
	
	public Request() {
		parameterMap = new HashMap<>();
	}

	public Request(InputStream in) {
		this();
		this.in = in;
		try {
			byte[] data = new byte[10240];
			int len = in.read(data);
			requestInfo = new String(data, 0, len);
		} catch (IOException e) {
			return;
		}
		// 分析头信息
		this.analyzeHeaderInfo();
	}

	/**
	 * 分析头信息
	 */
	private void analyzeHeaderInfo() {
		if (this.requestInfo == null || "".equals(this.requestInfo.trim())) {
			return;
		}
		
		// 第一行请求数据： GET /login?username=aaa&password=bbb HTTP/1.1
		
		// 1.获取请求方式
		String firstLine = this.requestInfo.substring(0, this.requestInfo.indexOf(RN));
		int index = firstLine.indexOf("/");
		this.method = firstLine.substring(0,index).trim();
		
		String urlStr = firstLine.substring(index,firstLine.indexOf("HTTP/1.1")).trim();
		String parameters = "";
		if (GET.equalsIgnoreCase(this.method)) {
			if (urlStr.contains("?")) {
				String[] arr = urlStr.split("\\?");
				this.url = arr[0];
				parameters = arr[1];
			} else {
				this.url = urlStr;
			}
			
		} else if (POST.equalsIgnoreCase(this.method)) {
			this.url = urlStr;
			parameters = this.requestInfo.substring(this.requestInfo.lastIndexOf(RN)).trim();
		}
		
		// 2. 将参数封装到 map 中
		if ("".equals(parameters)) {
			return;
		}
		this.parseToMap(parameters);
	}

	/**
	 * 封装参数到 Map 中
	 * @param parameters
	 */
	private void parseToMap(String parameters) {
		// 请求参数格式：username=aaa&password=bbb&likes=1&likes=2
		
		StringTokenizer token = new StringTokenizer(parameters, "&");
		while(token.hasMoreTokens()) {
			// keyValue 格式：username=aaa 或 username=
			String keyValue = token.nextToken();
			String[] kv = keyValue.split("=");
			if (kv.length == 1) {
				kv = Arrays.copyOf(kv, 2);
				kv[1] = null;
			} 
			
			String key = kv[0].trim();
			String value = kv[1] == null ? null : this.decode(kv[1].trim(), CHARSET);
			
			if (!this.parameterMap.containsKey(key)) {
				this.parameterMap.put(key, new ArrayList<>());
			}
			
			this.parameterMap.get(key).add(value);
		}
	}
	
	/**
	 * 根据参数名获取多个参数值
	 * @param name
	 * @return
	 */
	public String[] getParameterValues(String name) {
		List<String> values = null;
		if ((values = this.parameterMap.get(name)) == null) {
			return null;
		}
		
		return values.toArray(new String[0]);
	}
	
	/**
	 * 根据参数名获取唯一参数值
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		String[] values = this.getParameterValues(name);
		if (values == null) {
			return null;
		}
		return values[0];
	}
	
	
	/**
	 * 解码中文
	 * @param value
	 * @param code
	 * @return
	 */
	private String decode(String value, String charset) {
		try {
			return URLDecoder.decode(value, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getUrl() {
		return url;
	}
	
 }
