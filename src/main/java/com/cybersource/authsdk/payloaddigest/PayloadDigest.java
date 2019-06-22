package com.cybersource.authsdk.payloaddigest;

import java.security.MessageDigest;
import com.cybersource.authsdk.util.Base64;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

/**
 *
 */
public class PayloadDigest {

	private String bluePrint = null, messageBody = null;
	private MerchantConfig merchantConfig = null;
	private Logger logger;
	private String sha256 = GlobalLabelParameters.SHA_256;

	/**
	 * @param merchantConfig-
	 *            contains all information for merchant.
	 */
	public PayloadDigest(MerchantConfig merchantConfig) {
		this.merchantConfig = merchantConfig;
	}

	/**
	 * @return generated digest from requestBody.
	 */
	private String digestGeneration() {

		/*
		 * This method return Digest value which is SHA-256 hash of payload that
		 * is BASE64 encoded
		 */
		String messageBody = payloadGeneration();
		MessageDigest digestString;
		byte[] digestBytes = null;
		try {
			digestString = MessageDigest.getInstance(sha256);
			digestBytes = digestString.digest(messageBody.getBytes("UTF-8"));
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.DIGEST_GEN_FAILED, "", Level.FATAL);
			Utility.log(logger, e);
			return null;
		}
		bluePrint = Base64.getEncoder().encodeToString(digestBytes);
		bluePrint = sha256 + "=" + bluePrint;
		return bluePrint;
	}

	/**
	 * Reads the Payload data present in the request json file, if present, else
	 * reads form the merchant config object.
	 * 
	 * @return generated payLoad from request body.
	 */
	private String payloadGeneration() {
		messageBody = merchantConfig.getRequestData();
		return messageBody;
	}

	/**
	 * @return payLoad generated.
	 */
	public String getPayLoad() {

		payloadGeneration();
		return messageBody;
	}

	/**
	 * 
	 * @return Digest generated from payload.
	 */
	public String getDigest() {
		digestGeneration();
		return bluePrint;
	}

	/**
	 * @return messageBody for request.
	 */
	public String getMessageBody() {
		return messageBody;
	}

}
