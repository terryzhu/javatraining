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

import java.net.Socket;
import java.util.Date;

/**
 * @author ZJ
 * 
 */
public class Session extends AbstractSession {

	/**
	 * @param server
	 * @param socket
	 */
	public Session(AbstractCentralServer server, Socket socket) {
		super(server, socket);
	}

	@Override
	public void connect() {
	}

	@Override
	public void disconnect() {

	}

	@Override
	public String handleData(String input) {
		return input + " " + new Date();
	}

	@Override
	public void handleException(Exception e) {
	}

}
