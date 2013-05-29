/* 
 * Created : 2013-5-29 
 * 
 * Copyright (c) 2012 Ericsson AB, Sweden. 
 * All rights reserved. 
 * The Copyright to the computer program(s) herein is the property of Ericsson AB, Sweden. 
 * The program(s) may be used and/or copied with the written permission from Ericsson AB 
 * or in accordance with the terms and conditions stipulated in the agreement/contract 
 * under which the program(s) have been supplied. 
 */
package com.ericsson.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.exercise.util.SvrLogger;

/**
 * @author ZJ
 * 
 */
public class ConfigLoader implements Runnable {
	public static final int INTERVAL = 1000;
	public static final String CONFIGFILE = "1.cfg";
	private long lastModifiedTime = -1;

	@Override
	public void run() {
		initConfig();
		while (true) {
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
			}
			if (checkModified()) {
				loadConfig();
			}
		}
	}

	private void initConfig() {
		File file = new File(CONFIGFILE);
		if (!file.exists()) {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				writer.write(ServerConfig.getInstance().getPort()+"\n");
				writer.write(ServerConfig.getInstance().getTimeout()+"\n");
				writer.write(ServerConfig.getInstance().getMax_session());
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace(SvrLogger.getPrintStream());
			} finally {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}

		}
	}

	private boolean checkModified() {
		File file = new File(CONFIGFILE);
		if (lastModifiedTime == file.lastModified()) {
			return false;
		}
		lastModifiedTime = file.lastModified();
		return true;
	}

	private void loadConfig() {
		// TODO
		if (validateConfigFile()) {
			
		}
	}

	private boolean validateConfigFile() {
		return true;
	}
}
