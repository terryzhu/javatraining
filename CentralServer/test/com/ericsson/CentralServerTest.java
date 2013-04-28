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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author ZJ
 * 
 */
public class CentralServerTest {

	@Test
	public void test() throws InterruptedException {
		// xunlei will use 8888 :-(
		new Thread(new CentralServer(8888)).start();

		Thread.sleep(500000);
	}

}
