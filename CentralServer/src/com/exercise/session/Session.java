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
package com.exercise.session;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Observable;

import com.exercise.server.ServerData;
import com.exercise.util.SvrLogger;

/**
 * @author ZJ
 * 
 */
public abstract class Session extends Observable implements Runnable {
	public enum SESSION_STATUS {
		ESTABLISHED, DESTROYED
	};

	public static final String EXIT = "EXIT";
	public static final String PROMPT = "> ";
	protected Socket socket = null;
	protected ServerData data;

	public Session(Socket socket, ServerData data) {
		super();
		this.socket = socket;
		this.data = data;
	}

	@Override
	public void run() {
		try {
			handleMessage();
		} catch (Exception e) {
			handleException(e);
		} finally {
			release();
		}
	}

	protected void handleMessage() throws IOException {
		try {
			setChanged();
			notifyObservers(SESSION_STATUS.ESTABLISHED);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write(PROMPT);
			writer.flush();
			String input;
			while (!EXIT.equals((input = reader.readLine()))) {
				String ouput = handleInput(input.trim());
				writer.write(ouput + "\r\n" + PROMPT);
				writer.flush();
			}
		} finally {
			socket.close();
		}
	}

	protected void handleException(Exception e) {
		SvrLogger.log("handle session exception " + e.getMessage());
		e.printStackTrace(SvrLogger.getPrintStream());
	}

	protected void release() {
		SvrLogger.log("session release");
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
		setChanged();
		notifyObservers(SESSION_STATUS.DESTROYED);
	}

	public abstract String handleInput(String input);

	public void onServerException(Exception e) {
		try {
			if (socket != null && !socket.isClosed()) {
				socket.getOutputStream().write(
						("sever error happen," + e.getMessage() + " and will shutdown all sessions\r\n").getBytes());
				socket.getOutputStream().flush();
				socket.close();
			}
		} catch (Exception e1) {
			e1.printStackTrace(SvrLogger.getPrintStream());
		}
	}

}
