/* 
 * Created : 2013-5-3 
 * 
 * Copyright (c) 2012 Ericsson AB, Sweden. 
 * All rights reserved. 
 * The Copyright to the computer program(s) herein is the property of Ericsson AB, Sweden. 
 * The program(s) may be used and/or copied with the written permission from Ericsson AB 
 * or in accordance with the terms and conditions stipulated in the agreement/contract 
 * under which the program(s) have been supplied. 
 */
package com.exercise.framework;

import java.io.IOException;

/**
 * @author ZJ
 * 
 */
public abstract class AbstractCentralServer implements Runnable {
	protected abstract void startServer() throws IOException;

	protected abstract void handleConnections() throws IOException;

	protected abstract void handleException(Exception e);

	protected abstract void release();

	@Override
	public void run() {
		try {
			startServer();
			handleConnections();
		} catch (Exception e) {
			handleException(e);
		} finally {
			release();
		}
	}

}
