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
package com.exercise.mycode;

import java.net.Socket;
import java.util.Date;

import com.exercise.server.ServerData;
import com.exercise.session.Session;

/**
 * @author ZJ
 * 
 */
public class MySession extends Session {
	public static final String INVALID_INPUT = "invalid input";

	public MySession(Socket socket, ServerData data) {
		super(socket, data);
	}

	@Override
	public String handleInput(String input) {
		if (!isInputValid(input)) {
			return INVALID_INPUT;
		}
		String key = getKey(input);
		String command = getCommand(input);
		if (isRegisterCommand(command)) {
			return doRegister(key);
		}
		if (!isKeyRegistered(key)) {
			return "key [" + key + "] has not been registered";
		}
		if (isUnregisterCommand(command)) {
			return doUnregister(key);
		}
		return doThings(command, key);
	}

	private String doThings(String command, String key) {
		return command + " " + new Date();
	}

	private String doUnregister(String key) {
		data.getKeys().remove(key);
		return "key [" + key + "] has unregistered";
	}

	private String doRegister(String key) {
		if (data.getKeys().contains(key)) {
			return "fail to register key, key [" + key + "] has already been registered before";
		}
		data.getKeys().add(key);
		return "key [" + key + "] has registered";
	}

	private String getCommand(String input) {
		return input.substring(0, input.indexOf(":"));
	}

	private String getKey(String input) {
		return input.substring(input.indexOf(":") + 1, input.length());
	}

	boolean isInputValid(String input) {
		return input.contains(":");
	}

	boolean isKeyRegistered(String key) {
		return data.getKeys().contains(key);
	}

	boolean isRegisterCommand(String command) {
		return "register".equals(command);
	}

	private boolean isUnregisterCommand(String command) {
		return "unregister".equals(command);
	}

}
