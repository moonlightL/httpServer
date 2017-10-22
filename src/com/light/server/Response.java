package com.light.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

public class Response {
	// 常量
	private static final String BLANK = " ";
	private static final String RN = "\r\n";
	// 响应内容长度
	private int len;
	// 存储头信息
	private StringBuilder headerInfo;
	// 存储正文信息
	private StringBuilder contentInfo;
	// 输出流
	private BufferedWriter bw;
	
	public Response() {
		headerInfo = new StringBuilder();
		contentInfo = new StringBuilder();
		len = 0;
	}
	
	public Response(OutputStream os) {
		this();
		bw = new BufferedWriter(new OutputStreamWriter(os));
	}
	
	/**
	 * 设置头信息
	 * @param code
	 */
	private void setHeaderInfo(int code) {
		// 响应头信息
		headerInfo.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);
		
		if ("200".equals(code)) {
			headerInfo.append("OK");
			
		} else if ("404".equals(code)) {
			headerInfo.append("NOT FOUND");
			
		} else if ("500".equals(code)) {
			headerInfo.append("SERVER ERROR");
		}
		
		headerInfo.append(RN);
		headerInfo.append("Content-Length:").append(len).append(RN);
		headerInfo.append("Content-Type:text/html").append(RN);
		headerInfo.append("Date:").append(new Date()).append(RN);
		headerInfo.append("Server:nginx/1.12.1").append(RN);
		headerInfo.append(RN);
	}
	
	/**
	 * 设置正文
	 * @param content
	 * @return
	 */
	public Response print(String content) {
		contentInfo.append(content);
		len += content.getBytes().length;
		return this;
	}
	
	/**
	 * 设置正文
	 * @param content
	 * @return
	 */
	public Response println(String content) {
		contentInfo.append(content).append(RN);
		len += (content + RN).getBytes().length;
		return this;
	}
	
	/**
	 * 返回客户端
	 * @param code
	 * @throws IOException
	 */
	public void pushToClient(int code) throws IOException {
		// 设置头信息
		this.setHeaderInfo(code);
		bw.append(headerInfo.toString());
		// 设置正文
		bw.append(contentInfo.toString());
		
		bw.flush();
	}
	
	
	public void close() {
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
