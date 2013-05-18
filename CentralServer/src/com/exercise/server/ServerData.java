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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ZJ
 * 
 */
public class ServerData {
 	Set<String> keys = Collections.synchronizedSet(new HashSet<String>());

	public Set<String> getKeys() {
		return keys;
	}

	public void setKeys(Set<String> keys) {
		this.keys = keys;
	}
 	
}
