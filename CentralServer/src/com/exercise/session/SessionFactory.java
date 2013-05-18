/* 
 * Created : 2013-5-18 
 * 
 * Copyright (c) 2012 Ericsson AB, Sweden. 
 * All rights reserved. 
 * The Copyright to the computer program(s) herein is the property of Ericsson AB, Sweden. 
 * The program(s) may be used and/or copied with the written permission from Ericsson AB 
 * or in accordance with the terms and conditions stipulated in the agreement/contract 
 * under which the program(s) have been supplied. 
 */
package com.exercise.session;

import java.net.Socket;

import com.exercise.mycode.MySession;
import com.exercise.server.ServerData;

/**
 * @author ZJ
 * 
 */
public class SessionFactory {
	public static final String MYSESSION = "MySession";

	public static Session createSession(String name, Socket socket,ServerData data) {
		if (MYSESSION.equals(name)) {
			return new MySession(socket,data);
		}

		return null;
	}
}
