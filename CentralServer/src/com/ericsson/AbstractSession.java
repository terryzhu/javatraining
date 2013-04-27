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
package com.ericsson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author ZJ
 * 
 */
public abstract class AbstractSession implements Runnable {
	public static final String EXIT = "EXIT";
	public static final String PROMPT = "> ";
	Socket socket;

	public AbstractSession(Socket socket) {
		super();
		this.socket = socket;
	}

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
				String ouput = handleData(input);
				writer.write(ouput + "\r\n" + PROMPT);
				writer.flush();
			}
		} finally {
			boolean exceptionHappened = false;
			exceptionHappened = closeIo(inputStream, exceptionHappened);
			exceptionHappened = closeIo(outputStream, exceptionHappened);
			exceptionHappened = closeIo(reader, exceptionHappened);
			exceptionHappened = closeIo(writer, exceptionHappened);
			if (exceptionHappened) {
				throw new IOException("IO Exception occurs when handleMessage()");
			}

		}

	}

	protected void sessionEstablished() {
		SvrLogger.log("session established");
	}

	protected void sessionDestroyed() {
		SvrLogger.log("session destroyed");
	}

	public abstract String handleData(String input);

	protected void handleSessionException(Exception e) {
		SvrLogger.log("handle session exception " + e.getMessage());
		e.printStackTrace(SvrLogger.getPrintStream());
	}

	@Override
	public void run() {
		try {
			sessionEstablished();
			handleMessage();
			sessionDestroyed();
		} catch (IOException e) {
			handleSessionException(e);
		}

	}

	private boolean closeIo(Closeable closeable, boolean exceptionHappened) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			exceptionHappened = true;
		}
		return exceptionHappened;
	}
}
