/* 
 * Created : 2013-5-29 
 * 
 * Copyright (c) 2012 Ericsson AB, Sweden. 
 * All rights reserved. 
 * The Copyright to the computer program(s) herein is the property of Ericsson AB, Sweden. 
 * The program(s) may be used and/or copied with the written permission from Ericsson AB 
 * or in accordance with the terms and conditions stipulated in the agreement/contract 
 * under which the program(s) have been supplied. 
 */
package com.ericsson.config;

/**
 * @author ZJ
 * 
 */
public class ServerConfig {

	private ServerConfig() {
	}

	private static ServerConfig configuration = null;

	private int port = 8888;
	private int timeout = 10 * 1000;
	private int max_session = 6;

	public static ServerConfig getInstance() {
		// ^_^
		if (configuration == null) {
			synchronized (ServerConfig.class) {
				if (configuration == null) {
					configuration = new ServerConfig();
				}
			}
		}
		return configuration;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getMax_session() {
		return max_session;
	}

	public void setMax_session(int max_session) {
		this.max_session = max_session;
	}

}
