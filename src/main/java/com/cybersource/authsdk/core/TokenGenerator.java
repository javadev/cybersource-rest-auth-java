package com.cybersource.authsdk.core;

public interface TokenGenerator {
	
	/**
	 * @return token generated as per http_signature or JWT
	 */
	public String getToken();

}
