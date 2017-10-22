package com.light.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

	private ServerSocket server;
	
	private boolean isShutdown = false;
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}

	/**
	 * 启动服务器
	 */
	public void start() {
		try {
			server = new ServerSocket(8080);
			// 接收数据
			this.receiveData();
		} catch (IOException e) {
			this.stop();
		}
	}
	
	/**
	 * 接收数据
	 */
	private void receiveData() {
		try {
			while(!isShutdown) {
				new Thread(new Dispatcher(this.server.accept())).start();
			}
		} catch (IOException e) {
			this.stop();
		}
	}

	/**
	 * 关闭服务器
	 */
	public void stop() {
		isShutdown = true;
		try {
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
