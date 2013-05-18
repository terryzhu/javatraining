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
package com.exercise.server;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.exercise.session.Session;
import com.exercise.session.Session.SESSION_STATUS;
import com.exercise.util.SvrLogger;

/**
 * @author ZJ
 * 
 */
public class SessionManager implements Observer {
	ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 100, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
	Set<Session> sessions = new HashSet<Session>();

	@Override
	public void update(Observable o, Object arg) {
		if (!(arg instanceof Session.SESSION_STATUS)) {
			return;
		}

		if (SESSION_STATUS.ESTABLISHED.equals(arg)) {
			handleSessionEstablished((Session) o);
		}

		if (SESSION_STATUS.DESTROYED.equals(arg)) {
			handleSessionDestroyed((Session) o);
		}
	}

	public void executeSession(Session session) {
		session.addObserver(this);
		pool.execute(session);
	}

	public void handleSessionEstablished(Session session) {
		sessions.add(session);
		SvrLogger.log("session added, size is " + pool.getActiveCount());
		SvrLogger.log("handleSessionEstablished " + session);
	}

	public void handleSessionDestroyed(Session session) {
		session.deleteObserver(this);
		sessions.remove(session);
		SvrLogger.log("handleSessionDestroyed " + session);
	}

	public void handleServerException(Exception e) {
		for (Session session : sessions) {
			session.onServerException(e);
		}
	}
}
