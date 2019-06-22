package com.cybersource.authsdk.jwtsecurity;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;

public abstract interface CryptoProcessor {
	public abstract String encrypt(String paramString, X509Certificate paramX509Certificate);

	public abstract String encrypt(String paramString, X509Certificate paramX509Certificate,
			Map<String, Object> paramMap);

	public abstract String signAndEncrypt(String paramString, PrivateKey paramPrivateKey,
			X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2);

	public abstract String signAndEncrypt(String paramString, PrivateKey paramPrivateKey,
			X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2, Map<String, Object> paramMap);

	public abstract String sign(String paramString, PrivateKey paramPrivateKey, X509Certificate paramX509Certificate);

	public abstract String sign(String paramString, PrivateKey paramPrivateKey, X509Certificate paramX509Certificate,
			Map<String, Object> paramMap);

	public abstract String validateSignature(String paramString, X509Certificate paramX509Certificate);

	public abstract String validateSignature(String paramString, byte[] paramArrayOfByte);

	public abstract boolean isValidSignature(String paramString, X509Certificate paramX509Certificate);

	public abstract boolean isValidSignature(String paramString, byte[] paramArrayOfByte);

	public abstract String decryptAndValidateSignature(String paramString, PrivateKey paramPrivateKey,
			X509Certificate paramX509Certificate);

	public abstract String decrypt(String paramString, PrivateKey paramPrivateKey);

	public abstract BigInteger getModulus(String paramString);

	public abstract String getKid(String paramString);

	public abstract boolean isEncrypted(String paramString);

	public abstract String getSerialNumber(String paramString1, String paramString2);

	public abstract String getPayload(String paramString);

	public abstract AuthAttributes getAuthAttributes(String paramString1, String paramString2);

	public abstract String getCustomParam(String paramString1, String paramString2);

	public abstract String createBodyDigest(String paramString);

	public abstract JWTPayload getJWTPayload(String paramString);
}
