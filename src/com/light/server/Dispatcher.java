package com.light.server;

import java.io.IOException;
import java.net.Socket;

public class Dispatcher implements Runnable {
	// socket 客户端
	private Socket socket;
	// 请求对象
	private Request request;
	// 响应对象
	private Response response;
	// 响应码
	private int code = 200;
	
	public Dispatcher(Socket socket) {
		this.socket = socket;
		try {
			this.request = new Request(socket.getInputStream());
			this.response = new Response(socket.getOutputStream());
		} catch (IOException e) {
			code = 500;
			return;
		}
		
	}

	@Override
	public void run() {
		try {
			// 创建控制器
			String servletClazz = WebApp.getServletClazz(this.request.getUrl());
			Servlet servlet = (Servlet) Class.forName(servletClazz).newInstance();
			// 处理请求
			servlet.service(request, response);
			this.response.pushToClient(code);
			this.socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
