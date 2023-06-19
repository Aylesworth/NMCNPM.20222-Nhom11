package com.aylesw.mch.backend.dto;

import java.util.HashMap;

public class SimpleResponse extends HashMap<String, Object> {

	public SimpleResponse() {
		super();
	}
	
	public SimpleResponse(String message) {
		super();
		put("message", message);
	}
	
	
}
