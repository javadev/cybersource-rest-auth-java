package com.cybersource.authsdk.jwt;

import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import com.cybersource.authsdk.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import com.cybersource.authsdk.cache.Cache;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.core.TokenGenerator;
import com.cybersource.authsdk.jwtsecurity.JWTCryptoProcessorImpl;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.Utility;

public class JwtSignatureToken implements TokenGenerator {
	private MerchantConfig merchantConfig;
	private String requestBody, jwtMethod;
	private Logger logger;
	private Cache cache;
	private RSAPrivateKey rsaPrivateKey = null;
	private X509Certificate x509Certificate = null;
	private String signature;

	/**
	 * @param merchantConfig
	 *            - list of parameters required for JWT token.
	 * @param requestBody
	 *            -user information to be sent.
	 */
	public JwtSignatureToken(MerchantConfig merchantConfig, String requestBody) {

		this.merchantConfig = merchantConfig;
		this.requestBody = requestBody;
		this.jwtMethod = merchantConfig.getRequestType();
		this.logger = Log4j.getInstance(merchantConfig);
		this.cache = new Cache(this.merchantConfig);
	}

	@Override
	public String getToken() {
		try {
			signature = generateSignature();
		} catch (Exception e) {
			signature = null;
		}
		return signature;
	}

	/**
	 * 
	 * @return the generated signature.
	 * @throws Exception - 
	 *             if some exception will occur.
	 */
	public String generateSignature() throws Exception {

		try {
			if (merchantConfig != null && merchantConfig.getKeyType() != null) {
				/* Generating private key. */
				if (Cache.isCache) {
					rsaPrivateKey = KeyCertificateGenerator.initializePrivateKey(merchantConfig);
					x509Certificate = KeyCertificateGenerator.initializeCertificate(merchantConfig);
					Utility.log(logger, GlobalLabelParameters.CACHE_BEGIN, "", Level.INFO);
					setUpCache();
					Cache.isCache = false;
				} else {
					boolean lastModified = cache.isLastModifiedTimeP12();
					if (lastModified) {
						Utility.log(logger, GlobalLabelParameters.CACHE_EXTRACT, "", Level.INFO);
						cache.retriveP12DataFromCache();
						rsaPrivateKey = cache.getRsaPrivateKey();
						x509Certificate = cache.getX509Certificate();
					} else { /**/
						Utility.log(logger, GlobalLabelParameters.CACHE_EXTEND, "", Level.INFO);
						rsaPrivateKey = KeyCertificateGenerator.initializePrivateKey(merchantConfig);
						x509Certificate = KeyCertificateGenerator.initializeCertificate(merchantConfig);
						setUpCache();
					}
				}
				/* Generating certificate. */

				String encryptedSigMessage = "";

				if (requestBody != null && !requestBody.isEmpty()) {
					MessageDigest jwtBody = MessageDigest.getInstance(GlobalLabelParameters.SHA_256);
					byte[] jwtClaimSetBody = jwtBody.digest(requestBody.getBytes());
					encryptedSigMessage = Base64.getEncoder().encodeToString(jwtClaimSetBody);
				}

				String claimSet = null;
				String method = jwtMethod;
				if (method.equalsIgnoreCase(GlobalLabelParameters.GET)
						|| method.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
					claimSet = "{\n            \"iat\":\"" + String.format("%tFT%<tRZ", Calendar.getInstance(TimeZone.getTimeZone("Z")))
							+ "\"\n} \n\n";
				} else if (method.equalsIgnoreCase(GlobalLabelParameters.POST)
						|| method.equalsIgnoreCase(GlobalLabelParameters.PUT)
						|| method.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
					claimSet = "{\n            \"digest\":\"" + encryptedSigMessage
							+ "\",\n            \"digestAlgorithm\":\"SHA-256\",\n            \"iat\":\""
							+ String.format("%tFT%<tRZ", Calendar.getInstance(TimeZone.getTimeZone("Z"))) + "\"\n} \n\n";

				}
				/* Preparing the Signature Header. */
				HashMap<String, Object> customHeaders = new HashMap<String, Object>();
				if (merchantConfig.getMerchantID() != null) {
					customHeaders.put(JWTCryptoProcessorImpl.MERCHANT_ID, merchantConfig.getMerchantID());
				}

				if (merchantConfig.getMerchantID() != null) {
					customHeaders.put("v-c-partner-id", merchantConfig.getMerchantID());
				}

				String authType = merchantConfig.getAuthenticationType().trim();
				JWTCryptoProcessorImpl jwtCryptoProcessor = new JWTCryptoProcessorImpl();

				if (GlobalLabelParameters.JWT.equalsIgnoreCase(authType)) {
					signature = jwtCryptoProcessor.sign(claimSet, rsaPrivateKey, x509Certificate, customHeaders);

				} else {
					if (!GlobalLabelParameters.JWE.equalsIgnoreCase(authType)) {
						throw new ConfigException("merchant config field is invalid: authType");
					}

					X509Certificate x509Cert = KeyCertificateGenerator.initializeRecipientCertificate(merchantConfig);
					signature = jwtCryptoProcessor.signAndEncrypt(claimSet, rsaPrivateKey, x509Certificate, x509Cert,
							customHeaders);
				}

			} else {
				throw new ConfigException("merchant config fields missing: keyType, key type");
			}
		} catch (Exception e) {
			Utility.log(logger, GlobalLabelParameters.JWT_SIG_FAILED, "", Level.FATAL);
			Utility.log(logger, e);
			signature = null;
		}
		return signature;
	}

	/**
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	private void setUpCache() throws ConfigException {
		cache.setX509Certificate(x509Certificate);
		cache.setRsaPrivateKey(rsaPrivateKey);
		cache.setP12FileDetailsInCache();
	}
}
