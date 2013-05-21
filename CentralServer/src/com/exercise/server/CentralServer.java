/* 
 * Created : Apr 26, 2013 
 * 
 * Copyright (c) 2012 Ericsson AB, Sweden. 
 * All rights reserved. 
 * The Copyright to the computer program(s) herein is the property of Ericsson AB, Sweden. 
 * The program(s) may be used and/or copied with the written permission from Ericsson AB 
 * or in accordance with the terms and conditions stipulated in the agreement/contract 
 * under which the program(s) have been supplied. 
 */
package com.exercise.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.exercise.session.Session;
import com.exercise.session.SessionFactory;
import com.exercise.util.SvrLogger;

/**
 * @author eprtuxy
 * 
 */
public class CentralServer implements Runnable {
	public static final int SO_TIMEOUT = 10*1000;
	protected ServerSocket server;
	protected int port;
	protected boolean close = false;
	
	ServerData data = new ServerData();
	SessionManager manager = new SessionManager();

	public CentralServer(int port) {
		super();
		this.port = port;
	}

	@Override
	public void run() {
		try {
			startServer();
			handleConnections();
		} catch (Exception e) {
			handleException(e);
		} finally {
			release();
		}
	}

	protected void startServer() throws IOException {
		server = new ServerSocket(port);
		SvrLogger.log(server.toString() + " is listening");
	}

	protected void handleConnections() throws IOException {
		while (!close) {
			Socket socket = server.accept();
			socket.setSoTimeout(SO_TIMEOUT);
			Session session = SessionFactory.createSession(SessionFactory.MYSESSION, socket, data);
			if (session != null) {
				manager.executeSession(session);
			}
		}
	}

	protected void handleException(Exception e) {
		SvrLogger.log("handleServerException " + e.getMessage());
		e.printStackTrace(SvrLogger.getPrintStream());
		manager.handleServerException(e);
	}

	protected void release() {
		SvrLogger.log("server release");
		if (server != null) {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace(SvrLogger.getPrintStream());
			}
		}
	}

	public void close() throws IOException {
		if (server != null) {
			server.close();
		}
	}
}
