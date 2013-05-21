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
import java.util.concurrent.RejectedExecutionHandler;
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
	// 1. if running thread is < corePoolSize, Executor create thread, no queue
	// 2. if running thread is >= corePoolSize Executor put next into deque
	// 3. if queue is full, create new thread, if thread is > maximumPoolSize,
	// next will be rejected
	// so the max limit is 5+ 1 = 6 (maximumPoolSize + queue size)
	public static final int MAX_SESSION_LIMIT = 6;
	ThreadPoolExecutor pool = new ThreadPoolExecutor(1, MAX_SESSION_LIMIT - 1, 10, TimeUnit.MINUTES,
			new LinkedBlockingQueue<Runnable>(1), new RejectedExecutionHandler() {
				@Override
				public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
					if (r instanceof Session) {
						Session session = (Session) r;
						session.onExceedSessionLimit();
						session.deleteObservers();
					}
				}
			});

	// useful?
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

	// session limit could also write here, but it's seems not professional
	public void executeSession(Session session) {
		// if (pool.getActiveCount() > 5){
		// xxx
		// }
		session.addObserver(this);
		pool.execute(session);
	}

	public void handleSessionEstablished(Session session) {
		sessions.add(session);
		SvrLogger.log("session added, size is " + pool.getActiveCount());
		SvrLogger.log("handleSessionEstablished " + session);
	}

	public void handleSessionDestroyed(Session session) {
		session.deleteObservers();
		sessions.remove(session);
		SvrLogger.log("handleSessionDestroyed " + session);
	}

	// it's a little bit ugly
	public void handleServerException(Exception e) {
		for (Session session : sessions) {
			session.onServerException(e);
		}
	}
}
