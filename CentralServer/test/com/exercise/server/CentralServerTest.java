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
package com.exercise.server;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ZJ
 * 
 *         Retrospective from last exercise:
 * 
 *         1. I've read the source code of gnu cp tool and found that the copy
 *         buffer is a file system block
 * 
 *         2. the cp code in the book you mentioned last time is not like the
 *         exercise
 * 
 * 
 *         Questions for this exercise:
 * 
 *         Q1: how to write test case for some code about framework such as
 *         socket, logging, database ect, I think it's like functional test?
 * 
 *         Q2: is it normal that close server will throw some exception? how to
 *         avoid this problem? use non-block socket?
 * 
 *         Q3: do I need to use selector (multiplexing) to implement this
 *         exercise?
 * 
 *         Q4: multi threads VS single thread?
 * 
 *         Q5: how to write clean code to close several streams? because
 *         stream.close() will also throw exception
 * 
 *         Q6: will this code cause some unexpected networking programming
 *         problem out of my competence such as TCP self-connection?
 * 
 *         Q7: do we need to stream1 = null after stream1.close() ?
 * 
 *         Q8: should I use thread pool?
 * 
 *         Q9: when I do this and last exercises, I always refactor when I have
 *         done the functionality, and then found some improvements, and then
 *         refactor again, which cost me several days, but in real working
 *         environment, it's impossible, how to avoid it?
 * 
 */
public class CentralServerTest {
	public static final int PORT = 8888;
	CentralServer server = null;
	Socket client = null;

	private void writeString(Socket socket, String output) throws Exception {
		socket.getOutputStream().write((output + "\r\n").getBytes());
		socket.getOutputStream().flush();
		Thread.sleep(200);
	}

	@Before
	public void setUp() throws Exception {
		server = new CentralServer(PORT);
		new Thread(server).start();
		Thread.sleep(200);
		client = new Socket("localhost", PORT);
		Thread.sleep(200);
	}

	@After
	public void tearDown() throws Exception {
		Thread.sleep(200);
		server.close();
		Thread.sleep(200);
	}

	@Test
	public void testNormalConnection() throws Exception {
		writeString(client, "hello");
		assertEquals(1, server.manager.pool.getActiveCount());
		assertEquals(0, server.data.getKeys().size());

		// smooth close
		writeString(client, "EXIT");
		assertEquals(0, server.manager.pool.getActiveCount());
		Thread.sleep(200);
	}

	@Test
	public void testClientCloseUnexcepted() throws Exception {
		client.close();
		Thread.sleep(200);
		assertEquals(0, server.manager.pool.getActiveCount());
	}

	@Test
	public void testServerCloseUnexcepted() throws Exception {
		server.close();
		BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		assertEquals("> sever error happen,socket closed and will shutdown all sessions", reader.readLine());
		Thread.sleep(200);
		assertEquals(0, server.manager.pool.getActiveCount());
	}

}
