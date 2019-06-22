package com.cybersource.authsdk.http;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.TokenGenerator;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

/* HttpSigToken return SignatureHeader Value that contains following paramters
 * keyid     -- Merchant ID obtained from EBC portal
 * algorithm -- Should have value as "HmacSHA256"
 * headers   -- List of all header name passed in the Signature paramter 
                Note: Digest is not passed for GET calls
 * signature -- Signature header has paramter called signature
                Paramter 'Signature' must contain all the paramters mentioned in header above in given order  */

public class HttpSignatureToken implements TokenGenerator {

	private MerchantConfig merchantConfigSigHead = null;
	private StringBuilder signatureHeader;
	private String merchantkeyId = null;
	private String merchantSecertKey = null;
	private String httpMethod = null;
	private String httpMerchantID = null;
	private String requestData = null;
	private Logger logger;

	/**
	 * @param merchantConfig
	 *            -contains information for merchant.
	 */
	public HttpSignatureToken(MerchantConfig merchantConfig) {

		this.merchantConfigSigHead = merchantConfig;
		this.merchantkeyId = merchantConfig.getMerchantKeyId();
		this.merchantSecertKey = merchantConfig.getMerchantSecretKey();
		this.httpMethod = merchantConfig.getRequestType();
		this.httpMerchantID = merchantConfig.getMerchantID();
		this.logger = Log4j.getInstance(merchantConfig);
	}

	/**
	 * Method to get Token.
	 */
	@Override
	public String getToken() {
		String signature = null;
		try {
			signature = signatureHeader();
		} catch (NoSuchAlgorithmException e) {
			Utility.log(logger, e);

		} catch (InvalidKeyException e) {
			Utility.log(logger, e);

		}
		return signature;
	}

	/**
	 * @return headers to generate signature.
	 * @throws InvalidKeyException
	 *             - if key is not valid.
	 * @throws NoSuchAlgorithmException
	 *             - if algorith is not available.
	 */
	private String signatureHeader() throws InvalidKeyException, NoSuchAlgorithmException {

		signatureHeader = new StringBuilder();
		/* KeyId is the key obtained from EBC */
		signatureHeader.append("keyid=\"" + merchantkeyId + "\"");
		/* Algorithm should be always HmacSHA256 for http signature */
		signatureHeader.append(", algorithm=\"HmacSHA256\"");

		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.GET))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.GET) + "\"");
		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.DELETE))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.DELETE) + "\"");
		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.POST) + "\"");
		
		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.PATCH) + "\"");
		
		else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT))
			signatureHeader.append(", headers=\"" + getRequestHeaders(GlobalLabelParameters.PUT) + "\"");
		/*
		 * Get Value for paramter 'Signature' to be passed to Signature Header
		 */
		String signatureValue = new SignatureGenerator(merchantConfigSigHead).signatureGeneration();
		signatureHeader.append(", signature=\"" + signatureValue + "\"");
		return signatureHeader.toString();
	}

	/**
	 * 
	 * @param requestType
	 *            : GET/POST/PUT
	 * @return request header as per request type.
	 */
	private String getRequestHeaders(String requestType) {
		String requestHeader = null;
		if (GlobalLabelParameters.GET.equals(requestType)) {
			requestHeader = "host (request-target)" + " " + "v-c-merchant-id";
		} else if (GlobalLabelParameters.DELETE.equals(requestType)) {
			requestHeader = "host (request-target)" + " " + "v-c-merchant-id";
		} else if (GlobalLabelParameters.POST.equals(requestType)) {
			requestHeader = "host (request-target) digest v-c-merchant-id";
		} else if (GlobalLabelParameters.PUT.equals(requestType)) {
			requestHeader = "host (request-target) digest v-c-merchant-id";
		} else if (GlobalLabelParameters.PATCH.equals(requestType)) {
			requestHeader = "host (request-target) digest v-c-merchant-id";
		} else {
			return requestHeader;
		}
		return requestHeader;
	}
}
