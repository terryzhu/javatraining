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
package com.ericsson;

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
public abstract class AbstractCentralServer implements Runnable {
	public AbstractCentralServer(int port) {
		super();
		this.port = port;
	}

	protected ServerSocket server;
	protected int port;
	protected boolean close = false;
	protected Set<AbstractSession> sessions = Collections.synchronizedSet(new HashSet<AbstractSession>());

	public Set<AbstractSession> getSessions() {
		return sessions;
	}

	protected void startServer() throws IOException {
		// java contains tcp self-connection
		server = new ServerSocket(port);
		SvrLogger.log(server.toString() + " is listening");
	}

	protected void handleConnections() throws IOException {
		while (!close) {
			// should use threadpool?
			Socket client = server.accept();
			if (!isResouceEnough()) {
				client.getOutputStream().write("system resource is not enough".getBytes());
				client.getOutputStream().flush();
				client.close();
				continue;
			}
			AbstractSession session = newSessionInstance(client);
			sessions.add(session);

			SvrLogger.log("new session added," + client + "size is " + sessions.size());
			new Thread(session).start();
		}
	}

	protected boolean isResouceEnough() {
		return true;
	}

	public abstract AbstractSession newSessionInstance(Socket session);

	protected void handleException(Exception e) {
		SvrLogger.log("handleServerException " + e.getMessage());
		e.printStackTrace(SvrLogger.getPrintStream());
		for (AbstractSession session : sessions) {
			if (session.socket != null) {
				try {
					session.socket.getOutputStream().write(
							("sever error happen," + e.getMessage() + " and will shutdown all sessions\r\n").getBytes());
					session.socket.getOutputStream().flush();
					session.socket.close();
				} catch (IOException e1) {
					e1.printStackTrace(SvrLogger.getPrintStream());
				}

			}
		}
	}

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

}
