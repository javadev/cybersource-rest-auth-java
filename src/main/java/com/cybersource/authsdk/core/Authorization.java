package com.cybersource.authsdk.core;

import org.apache.logging.log4j.Logger;

/**
 * This class calls for the generation of Signature message depending on the
 * authentication type.
 * 
 * 
 *
 */
public class Authorization {
	private Logger logger;
	private String jwtRequestBody = null;

	/**
	 * @param jwtrequestBody
	 *            - not null and required values for JWT
	 */
	public void setJWTRequestBody(String jwtrequestBody) {
		this.jwtRequestBody = jwtrequestBody;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * @param merchantConfig
	 *            -contains merchant information.
	 * @return token generated as per http_signature or JWT.
	 * @throws ConfigException
	 *             - if some value will be wrong or missing for merchant.
	 */
	public String getToken(MerchantConfig merchantConfig) throws ConfigException {
		return TokenGeneratorFactory.getAuthToken(merchantConfig, jwtRequestBody).getToken();

	}

}
