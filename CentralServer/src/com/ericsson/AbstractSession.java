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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author ZJ
 * 
 */
public abstract class AbstractSession implements Runnable {
	public static final String EXIT="EXIT";
	Socket socket;
	ServerSocket server;

	public AbstractSession(AbstractCentralServer server, Socket socket) {
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
			String input;
			while(!EXIT.equals((input = reader.readLine()))){
				String ouput = handleData(input);
				writer.write(ouput+"\r\n");
				writer.flush();
			}
		} finally {
			boolean exHappen = false;
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				exHappen = true;
			}
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				exHappen = true;
			}
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				exHappen = true;
			}
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				exHappen = true;
			}
			if (exHappen) {
				throw new IOException("IO Exception occurs when handleMessage()");
			}

		}

	}

	public abstract void connect();

	public abstract void disconnect();

	public abstract String handleData(String input);

	public abstract void handleException(Exception e);

	@Override
	public void run() {
		try {
			connect();
			handleMessage();
			disconnect();
		} catch (IOException e) {
		}

	}

}
