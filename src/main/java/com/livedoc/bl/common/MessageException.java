package com.livedoc.bl.common;

public class MessageException extends Exception {

	private static final long serialVersionUID = 441398067878300198L;

	private String messageCode;
	private String[] parameters;

	public MessageException() {
		super();
	}

	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageException(String message) {
		super(message);
	}

	public MessageException(Throwable cause) {
		super(cause);
	}

	public MessageException(String message, Throwable cause,
			String messageCode, String... parameters) {
		super(message, cause);
		this.messageCode = messageCode;
		this.parameters = parameters;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String[] getParameters() {
		return parameters;
	}

	public void setParameters(String... parameters) {
		this.parameters = parameters;
	}

}
