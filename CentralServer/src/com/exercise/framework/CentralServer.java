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
package com.exercise.framework;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;



/**
 * @author eprtuxy
 * 
 */
public abstract class CentralServer extends AbstractCentralServer {
	public CentralServer(int port) {
		super();
		this.port = port;
	}

	protected ServerSocket server;
	protected int port;
	protected boolean close = false;
	protected boolean isResourceEnough = true;
	protected Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

	public Set<Session> getSessions() {
		return sessions;
	}

	@Override
	protected void startServer() throws IOException {
		server = new ServerSocket(port);
		SvrLogger.log(server.toString() + " is listening");
	}

	@Override
	protected void handleConnections() throws IOException {
		while (!close) {
			Socket client = server.accept();
			if (!isResouceEnough()) {
				client.getOutputStream().write("system resource is not enough".getBytes());
				client.getOutputStream().flush();
				client.close();
				continue;
			}
			Session session = newSessionInstance(client);
			sessions.add(session);

			SvrLogger.log("new session added," + client + "size is " + sessions.size());
			new Thread(session).start();
		}
	}

	protected boolean isResouceEnough() {
		return isResourceEnough;
	}

	public abstract Session newSessionInstance(Socket session);

	@Override
	protected void handleException(Exception e) {
		SvrLogger.log("handleServerException " + e.getMessage());
		e.printStackTrace(SvrLogger.getPrintStream());
		for (Session session : sessions) {
			if (session.socket != null) {
				try {
					session.socket.getOutputStream()
							.write(("sever error happen," + e.getMessage() + " and will shutdown all sessions\r\n")
									.getBytes());
					session.socket.getOutputStream().flush();
					session.socket.close();
				} catch (IOException e1) {
					e1.printStackTrace(SvrLogger.getPrintStream());
				}

			}
		}
	}

	@Override
	protected void release() {
		SvrLogger.log("release the resource");
		if (server != null) {
			try {
				// is it ok like this?
				server.close();
			} catch (IOException e) {
			}
		}
	}

	public void close() throws IOException {
		if (server != null) {
			server.close();
		}
	}

}
