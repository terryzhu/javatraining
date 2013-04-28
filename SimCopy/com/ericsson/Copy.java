/* 
 * Created : Apr 23, 2013 
 * 
 * Copyright (c) 2012 Ericsson AB, Sweden. 
 * All rights reserved. 
 * The Copyright to the computer program(s) herein is the property of Ericsson AB, Sweden. 
 * The program(s) may be used and/or copied with the written permission from Ericsson AB 
 * or in accordance with the terms and conditions stipulated in the agreement/contract 
 * under which the program(s) have been supplied. 
 */
package com.ericsson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * <p>
 * try to write a java application to simulate ‘cp’ command, coping a source
 * file to a destination file.<br>
 * <br>
 * the basic is to read the source file byte by byte, then write to the
 * destination file.<br>
 * the command line is like:
 * 
 * <pre>
 * # java –cp . copy abc.txt dest.txt
 * </pre>
 * 
 * output proper error message to the user.<br>
 * 
 * @author eprtuxy
 * 
 * 
 */
public class Copy {
	public static final String COPY_FAILURE_MSG = "failed to copy files";
	public static final String COPY_IO_ERROR_MSG = "failed to copy file because an I/O error occurs";
	public static final String COPY_USAGE = "Usage:\n\tjava [ -cp . ] Copy.jar source destination \n";

	public static void main(String[] args) {
		if (isInputValid(args)) {
			copyFile(args[0], args[1]);
		}
	}

	/**
	 * validate the input parameters<br>
	 * The parameter should be<br>
	 * 
	 * <pre>
	 * args[0] srouce file path
	 * args[1] destination file path
	 * </pre>
	 * 
	 * @param args
	 *            the input parameters in command line
	 * @return
	 * 
	 *         <code>false</code> paramters count is not 2 <br>
	 *         <code>true</code> if source file exists and destination file does
	 *         not exist<br>
	 *         <code>true</code> if source file exists and destination file
	 *         exist and user choose to overwrite it<br>
	 *         <code>false</code> if source file exists and destination file
	 *         exists and user choose not to overwrite it<br>
	 *         <code>false</code> if source file does not exist <br>
	 */
	private static boolean isInputValid(String[] args) {
		if (args.length != 2) {
			printError(COPY_FAILURE_MSG);
			printError(COPY_USAGE);
			return false;
		}

		String src = args[0];
		String des = args[1];
		if (!fileExists(src)) {
			printError(COPY_FAILURE_MSG);
			printError(src + " does not exist");
			return false;
		}

		if (!fileExists(des)) {
			return true;
		}

		if (isCopyOverwrite(des)) {
			printMessage("choose to overwrite the existing file " + des);
			return true;
		} else {
			printMessage("choose not to overwrite the existing file " + des);
			return false;
		}
	}

	/**
	 * check whether file <code>pathname</code> exist
	 * 
	 * @param pathname
	 * @return
	 */
	private static boolean fileExists(String pathname) {
		return new File(pathname).exists();
	}

	/**
	 * let use choose whether overwrite the existing file <code>des</code>
	 * 
	 * @param des
	 * @return
	 */
	private static boolean isCopyOverwrite(String des) {
		printMessage(des + " already exists, overwrite it? (y/n)");
		try {
			int input = System.in.read();
			if (input == 'y' || input == 'Y') {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			printMessage(COPY_IO_ERROR_MSG);
			return false;
		}
	}

	/**
	 * Copy the file <code>src</code> to <code>des</code> <br>
	 * print error message when failed to copy
	 * 
	 * @param src
	 *            source file path
	 * @param des
	 *            destination file path
	 */
	private static void copyFile(String src, String des) {
		try {
			printMessage("start to copy file");
			doRealCopyIO(src, des);
			printMessage("succeed to copy file");
		} catch (FileNotFoundException e) {
			printError(COPY_FAILURE_MSG);
			printError(src + " can't be read or " + des + " can't be written or " + des + " is directory");
		} catch (IOException e) {
			printError(COPY_FAILURE_MSG);
			printError(COPY_IO_ERROR_MSG);
		}
	}

	/**
	 * do the real copy file I/O
	 * 
	 * @param src
	 * @param des
	 * @throws IOException
	 */
	private static void doRealCopyIO(String src, String des) throws IOException {
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(src));
			outputStream = new BufferedOutputStream(new FileOutputStream(des));
			int read;
			while ((read = inputStream.read()) != -1) {
				outputStream.write(read);
			}
			outputStream.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				outputStream.close();
			} catch (Exception e2) {
			}
			try {
				inputStream.close();
			} catch (Exception e2) {
			}

		}

	}

	private static void printMessage(String string) {
		System.out.println(string);
	}

	private static void printError(String string) {
		System.err.println(string);
	}
}
