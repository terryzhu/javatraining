/* 
 * Created : 2013-4-27 
 * 
 * Copyright (c) 2012 Ericsson AB, Sweden. 
 * All rights reserved. 
 * The Copyright to the computer program(s) herein is the property of Ericsson AB, Sweden. 
 * The program(s) may be used and/or copied with the written permission from Ericsson AB 
 * or in accordance with the terms and conditions stipulated in the agreement/contract 
 * under which the program(s) have been supplied. 
 */
package com.exercise.framework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.UUID;



/**
 * @author ZJ
 * 
 */
public abstract class Session extends AbstractSession {
	public static final String EXIT = "EXIT";
	public static final String PROMPT = "> ";
	protected Socket socket = null;
	protected CentralServer server = null;
	protected UUID uuid = UUID.randomUUID();

	public Session(CentralServer server, Socket socket) {
		super();
		this.socket = socket;
		this.server = server;
	}

	@Override
	protected void handleMessage() throws IOException {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream));
			writer = new BufferedWriter(new OutputStreamWriter(outputStream));
			writer.write(PROMPT);
			writer.flush();
			String input;
			while (!EXIT.equals((input = reader.readLine()))) {
				String ouput = handleInput(input.trim());
				writer.write(ouput + "\r\n" + PROMPT);
				writer.flush();
			}
		} finally {
			boolean exceptionHappened = closeIo(socket);
			// how to close several streams?
			// exceptionHappened = closeIo(inputStream) || closeIo(outputStream)
			// || closeIo(reader) || closeIo(writer);
			if (exceptionHappened) {
				throw new IOException("IO Exception occurs in onMessage()");
			}
		}

	}

	@Override
	protected void onEstablished() {
		SvrLogger.log("session " + uuid + " established");
	}

	@Override
	protected void onDestroyed() {
		SvrLogger.log("session " + uuid + " destroyed");
	}

	@Override
	protected void release() {
		SvrLogger.log("session release");
		if (server.getSessions().contains(this)) {
			server.getSessions().remove(this);
			SvrLogger.log("session " + uuid + " is removed from session list,now sessions size is "
					+ server.getSessions().size());
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	public abstract String handleInput(String input);

	@Override
	protected void handleException(Exception e) {
		SvrLogger.log("handle session exception " + e.getMessage());
		e.printStackTrace(SvrLogger.getPrintStream());
		try {
			socket.getOutputStream().write(e.getMessage().getBytes());
		} catch (IOException e1) {
			e1.printStackTrace(SvrLogger.getPrintStream());
		}
	}

	private boolean closeIo(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			return true;
		}
		return false;
	}
}
