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

	protected void startServer() throws IOException {
		server = new ServerSocket(port);
		SvrLogger.log(server.toString() + " is listening");
	}

	protected void handleConnections() throws IOException {
		while (!close) {
			Socket client = server.accept();
			AbstractSession session = newSessionInstance(client);
			new Thread(session).start();
		}
	}

	public abstract AbstractSession newSessionInstance(Socket session);

	protected void handleServerException(IOException e) {
		SvrLogger.log("handleServerException " + e.getMessage());
		e.printStackTrace(SvrLogger.getPrintStream());
	}

	protected void release() {
		SvrLogger.log("release the resource");
	}

	@Override
	public void run() {
		try {
			startServer();
			handleConnections();
			release();
		} catch (IOException e) {
			handleServerException(e);
		}
	}

}
