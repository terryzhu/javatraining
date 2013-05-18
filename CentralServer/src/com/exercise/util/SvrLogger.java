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
package com.exercise.util;

import java.io.PrintStream;

/**
 * @author ZJ
 * 
 */
public class SvrLogger {
	public static final PrintStream printStream = System.out;

	public static void log(String log) {
		printStream.println(log);
	}

	public static PrintStream getPrintStream() {
		return printStream;
	}
}
