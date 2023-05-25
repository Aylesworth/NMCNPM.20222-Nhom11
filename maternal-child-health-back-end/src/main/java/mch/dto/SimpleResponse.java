package mch.dto;

import java.util.HashMap;

import lombok.Data;

public class SimpleResponse extends HashMap<String, Object> {

	public SimpleResponse() {
		super();
	}
	
	public SimpleResponse(String message) {
		super();
		put("message", message);
	}
	
	
}
