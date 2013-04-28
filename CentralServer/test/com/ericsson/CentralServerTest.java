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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import org.junit.Test;

/**
 * @author ZJ
 * 
 */
public class CentralServerTest {
	private void writeString(Socket socket, String output) throws Exception {
		socket.getOutputStream().write((output + "\r\n").getBytes());
		socket.getOutputStream().flush();
		Thread.sleep(200);
	}

	@Test
	public void testConnection() throws Exception {
		// xunlei will use 8888 :-(
		CentralServer server = new CentralServer(8888);
		new Thread(server).start();
		Thread.sleep(200);
		Socket socket = new Socket("localhost", 8888);

		// normal input
		writeString(socket, "hello");
		assertEquals(1, server.getSessions().size());
		assertEquals(0, server.getKeys().size());
		
		// ugly close
		socket.close();
		Thread.sleep(200);
		assertEquals(0, server.getSessions().size());

		// smooth close
		Socket socket2 = new Socket("localhost", 8888);
		writeString(socket2, "EXIT");
		assertEquals(0, server.getSessions().size());
		Thread.sleep(200);

		// server is close unexpected
		Socket socket3 = new Socket("localhost", 8888);
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket3.getInputStream()));
		server.server.close();
		assertEquals("sever error happen,Socket is closed and will shutdown all sessions", reader.readLine());
		socket3.close();
		Thread.sleep(200);
	}

	@Test
	public void testBusinessLogic() throws Exception {
		CentralServer server = new CentralServer(8888);
		Session session = new Session(server, null);
		assertEquals(Session.INVALID_INPUT, session.handleInput("invalidinput"));
		assertEquals("key [111] has not been registered", session.handleInput("unregister:111"));

		assertEquals("key [111] has registered", session.handleInput("register:111"));
		assertEquals("fail to register key, key [111] has already been registered before",
				session.handleInput("register:111"));
		assertTrue(session.handleInput("dothings:111").startsWith("dothings"));

		assertEquals("key [111] has unregistered", session.handleInput("unregister:111"));
	}
}
