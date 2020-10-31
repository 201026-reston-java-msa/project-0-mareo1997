package com.revature.exceptions;

public class OverDraftException extends RuntimeException {

	public OverDraftException(String message) {
		super(message);
	}
}
