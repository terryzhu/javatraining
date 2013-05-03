/* 
 * Created : 2013-4-30 
 * 
 * Copyright (c) 2012 Ericsson AB, Sweden. 
 * All rights reserved. 
 * The Copyright to the computer program(s) herein is the property of Ericsson AB, Sweden. 
 * The program(s) may be used and/or copied with the written permission from Ericsson AB 
 * or in accordance with the terms and conditions stipulated in the agreement/contract 
 * under which the program(s) have been supplied. 
 */
package com.exercise.mycode;

import static org.junit.Assert.*;

import org.junit.Test;

import com.exercise.mycode.MyCentralServer;
import com.exercise.mycode.MySession;

/**
 * @author ZJ
 * 
 */
public class MySessionTest {

	@Test
	public void testHandleInput() {
		MyCentralServer server = new MyCentralServer(8888);
		MySession session = new MySession(server, null);
		assertEquals(MySession.INVALID_INPUT, session.handleInput("invalidinput"));
		assertEquals("key [111] has not been registered", session.handleInput("unregister:111"));

		assertEquals("key [111] has registered", session.handleInput("register:111"));
		assertEquals("fail to register key, key [111] has already been registered before",
				session.handleInput("register:111"));
		assertTrue(session.handleInput("dothings:111").startsWith("dothings"));

		assertEquals("key [111] has unregistered", session.handleInput("unregister:111"));
	}

}
