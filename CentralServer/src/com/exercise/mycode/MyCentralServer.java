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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.exercise.framework.CentralServer;
import com.exercise.framework.Session;


/**
 * @author ZJ
 *         <p>
 *         1. write a component, test it with junit.
 *         <p>
 *         2. several clients will access the component concurrently, register a
 *         key to the component, then do something with the key. then un
 *         register the key from the component. ( you can imagine the key is
 *         MOId in CAI3G request )
 *         <p>
 *         3. if the key has been registered, the latter register operation
 *         should return failure.
 */
public class MyCentralServer extends CentralServer {
	Set<String> keys = Collections.synchronizedSet(new HashSet<String>());

	public Set<String> getKeys() {
		return keys;
	}

	public MyCentralServer(int port) {
		super(port);
	}

	@Override
	public Session newSessionInstance(Socket socket) {
		return new MySession(this, socket);
	}
}
