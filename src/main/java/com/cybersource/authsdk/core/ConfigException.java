package com.cybersource.authsdk.core;

public class ConfigException extends Exception {
	private static final long serialVersionUID = -7810688045599318234L;

	/**
	 * @param message
	 *            - messgae to print if exception occurs
	 */
	public ConfigException(String message) {
		super(message);
	}
}
