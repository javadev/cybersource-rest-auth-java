package com.cybersource.authsdk.http;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import com.cybersource.authsdk.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;

/* This method returns value for parameter Signature which is then passed to Signature header
 * paramter 'Signature' is calucated based on below key values and then signed with SECRET KEY -
 * host: Sandbox (apitest.cybersource.com) or Production (api.cybersource.com) hostname
 * date: "HTTP-date" format as defined by RFC7231.
 * (request-target): Should be in format of httpMethod: path
                     Example: "post /pts/v2/payments"
 * Digest: Only needed for POST calls.
 * v-c-merchant-id: set value to Cybersource Merchant ID
                     This ID can be found on EBC portal */
public class SignatureGenerator {

	private MerchantConfig merchantConfig;
	private String signatureParameterBase64Encoded;
	private String httpMethod;
	private String merchantSecertKey;
	private String merchantId;

	/**
	 * @param merchantConfig-contains
	 *            all information of merchant.
	 * @throws InvalidKeyException
	 *             - if key is not valid.
	 * @throws NoSuchAlgorithmException
	 *             - if algorithm is not available.
	 */
	public SignatureGenerator(MerchantConfig merchantConfig) throws InvalidKeyException, NoSuchAlgorithmException {
		this.merchantConfig = merchantConfig;
		this.httpMethod = merchantConfig.getRequestType();
		this.merchantSecertKey = merchantConfig.getMerchantSecretKey();
		this.merchantId = merchantConfig.getMerchantID();
	}

	/**
	 * @return generated signature.
	 * @throws NoSuchAlgorithmException
	 *             - if algorithm is not available.
	 * @throws InvalidKeyException
	 *             - if key is not valid.
	 */
	public String signatureGeneration() throws NoSuchAlgorithmException, InvalidKeyException {

		StringBuilder signatureString = new StringBuilder();
		signatureString.append('\n');
		signatureString.append(GlobalLabelParameters.HOST.toLowerCase());
		signatureString.append(": ");
		signatureString.append(merchantConfig.getRequestHost());
		signatureString.append('\n');
		signatureString.append(GlobalLabelParameters.DATE.toLowerCase());
		signatureString.append(": ");
		signatureString.append(PropertiesUtil.date);
		signatureString.append('\n');
		signatureString.append("(request-target)");
		signatureString.append(": ");

		String requestTarget = null;
		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.GET)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.GET);
		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.POST);
		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.PUT);
		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.DELETE);
		} else if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
			requestTarget = getRequestTarget(GlobalLabelParameters.PATCH);
		}
		signatureString.append(requestTarget);
		signatureString.append('\n');

		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT)
				|| (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH))) {
			signatureString.append(GlobalLabelParameters.DIGEST.toLowerCase());
			signatureString.append(": ");
			signatureString.append(new PayloadDigest(merchantConfig).getDigest());
			signatureString.append('\n');
		}

		signatureString.append(GlobalLabelParameters.V_C_MERCHANTID);
		signatureString.append(": ");
		signatureString.append(merchantId);
		signatureString.delete(0, 1);
		String signatureStr = signatureString.toString();

		/*
		 * Signature string generated from above parameters is Signed with
		 * SecretKey hased with SHA256 and base64 encoded. Secret Key is Base64
		 * decoded before signing
		 */

		SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(merchantSecertKey), "HmacSHA256");
		Mac aKeyId = Mac.getInstance("HmacSHA256");
		aKeyId.init(secretKey);
		aKeyId.update(signatureStr.getBytes());
		byte[] aHeaders = aKeyId.doFinal();
		signatureParameterBase64Encoded = Base64.getEncoder().encodeToString(aHeaders);
		secretKey = null;
		return signatureParameterBase64Encoded;
	}

	/**
	 * 
	 * @param requestType
	 *            - GET/PUT/POST
	 * @return request target as per request type.
	 */
	public String getRequestTarget(String requestType) {
		String requestTarget;
		if (GlobalLabelParameters.POST.equals(requestType)) {
			requestTarget = GlobalLabelParameters.POST.toLowerCase() + GlobalLabelParameters.SPACE
					+ merchantConfig.getRequestTarget();
		} else if (GlobalLabelParameters.GET.equals(requestType)) {
			requestTarget = GlobalLabelParameters.GET.toLowerCase() + GlobalLabelParameters.SPACE
					+ merchantConfig.getRequestTarget();
		} else if (GlobalLabelParameters.PUT.equals(requestType)) {
			requestTarget = GlobalLabelParameters.PUT.toLowerCase() + GlobalLabelParameters.SPACE
					+ merchantConfig.getRequestTarget();
		} else if (GlobalLabelParameters.DELETE.equals(requestType)) {
			requestTarget = GlobalLabelParameters.DELETE.toLowerCase() + GlobalLabelParameters.SPACE
					+ merchantConfig.getRequestTarget();
		} else if (GlobalLabelParameters.PATCH.equals(requestType)) {
			requestTarget = GlobalLabelParameters.PATCH.toLowerCase() + GlobalLabelParameters.SPACE
					+ merchantConfig.getRequestTarget();
		} else {
			requestTarget = null;
		}
		return requestTarget;
	}
}
