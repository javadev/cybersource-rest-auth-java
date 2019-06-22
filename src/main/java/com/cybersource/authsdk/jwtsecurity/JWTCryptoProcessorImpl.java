package com.cybersource.authsdk.jwtsecurity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObject;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;

import com.google.gson.Gson;

public class JWTCryptoProcessorImpl implements CryptoProcessor {
	public static String MERCHANT_ID = "v-c-merchant-id";
	private static final Logger logger = LoggerFactory.getLogger(JWTCryptoProcessorImpl.class);

	private JWEObject decryptPayload(String jweString, PrivateKey privateKey) {
		if ((isNullOrEmpty(jweString)) || (privateKey == null)) {
			logger.error("empty or null jweString or privateKey is null");
			return null;
		}
		try {
			JWEObject jweObject = JWEObject.parse(jweString);
			JWEDecrypter decrypter = new RSADecrypter(privateKey);
			jweObject.decrypt(decrypter);
			if (!JWEObject.State.DECRYPTED.equals(jweObject.getState())) {
				logger.error("Couldn't decrypt the payload:" + jweString);
				return null;
			}
		} catch (Exception exception) {
			logger.error("JWT payload:" + jweString + "can't be decrypted", exception);
			return null;
		}

		JWEObject jweObject = null;
		return jweObject;
	}

	private JOSEObject encryptPayload(String content, X509Certificate x509Certificate,
			Map<String, Object> customHeaders) {
		if ((isNullOrEmpty(content)) || (x509Certificate == null)) {
			logger.error("empty or null content or public certificate is null");
			return null;
		}
		String serialNumber = null;
		String serialNumberPrefix = "SERIALNUMBER=";
		String principal = x509Certificate.getSubjectDN().getName().toUpperCase();
		int beg = principal.indexOf(serialNumberPrefix);
		if (beg >= 0) {
			int end = principal.indexOf(",", beg);
			if (end == -1) {
				end = principal.length();
			}
			serialNumber = principal.substring(beg + serialNumberPrefix.length(), end);
		} else {
			serialNumber = x509Certificate.getSerialNumber().toString();
		}
		List<com.nimbusds.jose.util.Base64> x5cBase64List = new ArrayList();
		try {
			x5cBase64List.add(com.nimbusds.jose.util.Base64.encode(x509Certificate.getEncoded()));
		} catch (CertificateEncodingException e) {
			logger.error("can't signAndEncrypt the payload", e);
			return null;
		}
		JWEObject jweObject = new JWEObject(
				new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP, EncryptionMethod.A256GCM).customParams(customHeaders)
						.contentType("JWT").keyID(serialNumber).x509CertChain(x5cBase64List).build(),
				new Payload(content));
		try {
			JWEEncrypter jweEncrypter = new RSAEncrypter((RSAPublicKey) x509Certificate.getPublicKey());
			if (JWEObject.State.ENCRYPTED.equals(jweObject.getState())) {
				logger.error("Payload is already encrypted not encrypting again");
				return null;
			}
			jweObject.encrypt(jweEncrypter);
			if (!JWEObject.State.ENCRYPTED.equals(jweObject.getState())) {
				logger.error("Couldn't  encrypt the payload ");
				return null;
			}
		} catch (JOSEException joseException) {
			logger.error("payload encryption failed", joseException);
			return null;
		}
		return jweObject;
	}

	public String encrypt(String content, X509Certificate publicCertificate) {
		return serializeToken(encryptPayload(content, publicCertificate, null));
	}

	public String encrypt(String content, X509Certificate publicCertificate, Map<String, Object> customHeaders) {
		return serializeToken(encryptPayload(content, publicCertificate, customHeaders));
	}

	public String signAndEncrypt(String content, PrivateKey senderKey, X509Certificate senderCert,
			X509Certificate recipientCert) {
		return serializeToken(
				encryptPayload(serializeToken(signPayload(content, senderKey, senderCert, null)), recipientCert, null));
	}

	public String signAndEncrypt(String content, PrivateKey senderKey, X509Certificate senderCert,
			X509Certificate recipientCert, Map<String, Object> customHeaders) {
		return serializeToken(encryptPayload(serializeToken(signPayload(content, senderKey, senderCert, customHeaders)),
				recipientCert, customHeaders));
	}

	public String sign(String content, PrivateKey privateKey, X509Certificate x509Certificate) {
		return serializeToken(signPayload(content, privateKey, x509Certificate, null));
	}

	public String sign(String content, PrivateKey privateKey, X509Certificate x509Certificate,
			Map<String, Object> customHeaders) {
		return serializeToken(signPayload(content, privateKey, x509Certificate, customHeaders));
	}

	private JOSEObject signPayload(String content, PrivateKey privateKey, X509Certificate x509Certificate,
			Map<String, Object> customHeaders) {
		if ((isNullOrEmpty(content)) || (x509Certificate == null) || (privateKey == null)) {
			logger.error("empty or null content or Private key  or public certificate is null");
			return null;
		}
		String serialNumber = null;
		String serialNumberPrefix = "SERIALNUMBER=";
		String principal = x509Certificate.getSubjectDN().getName().toUpperCase();
		int beg = principal.indexOf(serialNumberPrefix);
		if (beg >= 0) {
			int end = principal.indexOf(",", beg);
			if (end == -1) {
				end = principal.length();
			}
			serialNumber = principal.substring(beg + serialNumberPrefix.length(), end);
		} else {
			serialNumber = x509Certificate.getSerialNumber().toString();
		}
		List<com.nimbusds.jose.util.Base64> x5cBase64List = new ArrayList();
		try {
			x5cBase64List.add(com.nimbusds.jose.util.Base64.encode(x509Certificate.getEncoded()));
		} catch (CertificateEncodingException e) {
			logger.error("can't signAndEncrypt the payload", e);
			return null;
		}
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
		Payload payload = new Payload(content);

		JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256).customParams(customHeaders).keyID(serialNumber)
				.x509CertChain(x5cBase64List).build();
		JWSObject jwsObject = new JWSObject(jwsHeader, payload);
		try {
			RSASSASigner signer = new RSASSASigner(rsaPrivateKey);
			jwsObject.sign(signer);
			if (!jwsObject.getState().equals(JWSObject.State.SIGNED)) {
				logger.error("Payload signing failed.");
				return null;
			}
		} catch (JOSEException joseException) {
			logger.error("can't signAndEncrypt the payload", joseException);
			return null;
		}
		return jwsObject;
	}

	public String decrypt(String jweString, PrivateKey privateKey) {
		JWEObject jweObject = decryptPayload(jweString, privateKey);
		return getPayload(jweObject);
	}

	public String decryptAndValidateSignature(String jweString, PrivateKey privateKey,
			X509Certificate publicCertificate) {
		JWEObject jweObject = decryptPayload(jweString, privateKey);
		return validateSignature(jweObject.getPayload().toString(), publicCertificate);
	}

	public String validateSignature(String jwsString, X509Certificate publicCertificate) {
		if ((isNullOrEmpty(jwsString)) || (publicCertificate == null)) {
			logger.error("Empty or null JWS or null value for publicCertificate");
			return null;
		}
		try {
			JWSObject jwsObject = JWSObject.parse(jwsString);
			PublicKey publicKey = publicCertificate.getPublicKey();
			if (publicKey == null) {
				return null;
			}
			RSASSAVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicCertificate.getPublicKey());
			if (!jwsObject.verify(verifier)) {
				logger.error("Couldn't verify the signature.");
				return null;
			}
		} catch (Exception e) {
			logger.error("Couldn't verify or parse  JWT Token", e);
			return null;
		}
		JWSObject jwsObject = null;
		return getPayload(jwsObject);
	}

	public String validateSignature(String jwsString, byte[] publicCertificate) {
		if ((isNullOrEmpty(jwsString)) || (publicCertificate == null)) {
			logger.error("Empty or null JWS or null value for publicCertificate");
			return null;
		}
		try {
			InputStream in = new ByteArrayInputStream(publicCertificate);
			Throwable localThrowable3 = null;
			try {
				CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
				Certificate certificate = certFactory.generateCertificate(in);
				X509Certificate x509Certificate = (X509Certificate) certificate;
			} catch (Throwable localThrowable1) {
				X509Certificate x509Certificate;
				localThrowable3 = localThrowable1;
				throw new CertificateException(localThrowable1);
			} finally {
				if (in != null) {
					if (localThrowable3 != null) {
						try {
							in.close();
						} catch (Throwable localThrowable2) {
							localThrowable3.addSuppressed(localThrowable2);
						}
					} else {
						in.close();
					}
				}
			}
		} catch (CertificateException ex) {
			X509Certificate x509Certificate;
			logger.error("couldn't create certificate from encoded cert string {}", ex);
			return null;
		} catch (IOException e) {
			logger.error("Unable to close input stream object: ", e);
			return null;
		}
		X509Certificate x509Certificate = null;
		return validateSignature(jwsString, x509Certificate);
	}

	public String getPayload(String joseStr) {
		JOSEObject joseObject;
		if (isNullOrEmpty(joseStr)) {
			logger.error("Empty or null JWS");
			return null;
		}
		try {
			joseObject = JOSEObject.parse(joseStr);
		} catch (ParseException e) {
			logger.error("Couldn't verify or parse JWT Token", e);
			return null;
		}
		return getPayload(joseObject);
	}

	public boolean isValidSignature(String jwsString, X509Certificate publicCertificate) {
		if ((isNullOrEmpty(jwsString)) || (publicCertificate == null)) {
			return false;
		}
		try {
			JWSObject jwsObject = JWSObject.parse(jwsString);
			PublicKey publicKey = publicCertificate.getPublicKey();
			if (publicKey == null) {
				return false;
			}
			RSASSAVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicCertificate.getPublicKey());
			if (jwsObject.verify(verifier)) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Couldn't verify or parse  JWT Token", e);
			return false;
		}
		JWSObject jwsObject;
		return false;
	}

	public boolean isValidSignature(String jwsString, byte[] publicCertificate) {
		X509Certificate x509Certificate;
		if ((isNullOrEmpty(jwsString)) || (publicCertificate == null)) {
			logger.error("jwsString is null or empty or publiccertificate bytes is null");
			return false;
		}
		try {
			InputStream in = new ByteArrayInputStream(publicCertificate);
			Throwable localThrowable3 = null;
			try {
				CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
				Certificate certificate = certFactory.generateCertificate(in);
				x509Certificate = (X509Certificate) certificate;
			} catch (Throwable localThrowable1) {
				localThrowable3 = localThrowable1;
				throw new CertificateException(localThrowable1);
			} finally {
				if (in != null) {
					if (localThrowable3 != null) {
						try {
							in.close();
						} catch (Throwable localThrowable2) {
							localThrowable3.addSuppressed(localThrowable2);
						}
					} else {
						in.close();
					}
				}
			}
		} catch (CertificateException ex) {
			logger.error("couldn't create certificate from encoded cert string {}", ex);
			return false;
		} catch (IOException e) {
			logger.error("Unable to close input stream object: ", e);
			return false;
		}

		return isValidSignature(jwsString, x509Certificate);
	}

	private String serializeToken(JOSEObject joseObject) {
		return joseObject.serialize();
	}

	private String getPayload(JOSEObject joseObject) {
		if (Optional.of(joseObject.getPayload()).isPresent()) {
			return joseObject.getPayload().toString();
		}
		return null;
	}

	public BigInteger getModulus(String joseStr) {
		try {
			if (isNullOrEmpty(joseStr)) {
				return null;
			}
			JOSEObject joseObject = JOSEObject.parse(joseStr);
			if ((joseObject instanceof JWSObject)) {
				return (BigInteger) ((JWSObject) joseObject).getHeader().getCustomParam("modulus");
			}
			if ((joseObject instanceof JWEObject)) {
				return (BigInteger) ((JWEObject) joseObject).getHeader().getCustomParam("modulus");
			}
			logger.error("Unknown JWT authentication type, not signed, not encrypted");
			return null;
		} catch (ParseException e) {
			logger.error("Couldn't verify or parse  JWT Token", e);
		}
		return null;
	}

	public String getCustomParam(String joseStr, String param) {
		try {
			if (isNullOrEmpty(joseStr)) {
				logger.error("jwt payload is null or empty or  custom param name is null or empty");
				return null;
			}
			JOSEObject joseObject = JOSEObject.parse(joseStr);
			if ((joseObject instanceof JWSObject)) {
				return (String) joseObject.getHeader().getCustomParam(param);
			}
			if ((joseObject instanceof JWEObject)) {
				return (String) joseObject.getHeader().getCustomParam(param);
			}
			logger.error("Unknown JWT authentication type, not signed, not encrypted");
			return null;
		} catch (ParseException e) {
			logger.error("Couldn't verify or parse JWT Token", e);
		}
		return null;
	}

	public String getKid(String joseStr) {
		JOSEObject joseObject;
		try {
			joseObject = JOSEObject.parse(joseStr);
		} catch (ParseException e) {
			logger.error("Couldn't parse JWT Token" + e, e);
			return null;
		}
		return getKid(joseObject);
	}

	public String getKid(JOSEObject joseObject) {
		if ((joseObject instanceof JWSObject)) {
			return ((JWSObject) joseObject).getHeader().getKeyID();
		}
		if ((joseObject instanceof JWEObject)) {
			return ((JWEObject) joseObject).getHeader().getKeyID();
		}
		logger.error("Unknown JWT authentication type, not signed, not encrypted");
		return null;
	}

	public boolean isEncrypted(String joseStr) {
		JOSEObject joseObject;

		try {
			joseObject = JOSEObject.parse(joseStr);
		} catch (ParseException e) {
			logger.error("Couldn't parse JWT Token" + e, e);
			return false;
		}
		return isEncrypted(joseObject);
	}

	public boolean isEncrypted(JOSEObject joseObject) {
		if ((joseObject instanceof JWSObject)) {
			return false;
		}
		if ((joseObject instanceof JWEObject)) {
			return JWEObject.State.ENCRYPTED == ((JWEObject) joseObject).getState();
		}
		logger.error("Unknown JWT authentication type, not signed, not encrypted");
		return false;
	}

	public String getSerialNumber(String joseStr, String issuerCN) {
		JOSEObject joseObject;
		try {
			joseObject = JOSEObject.parse(joseStr);
		} catch (ParseException e) {
			logger.error("Couldn't parse JWT Token" + e, e);
			return null;
		}
		return getSerialNumber(joseObject, issuerCN);
	}

	public String getSerialNumber(JOSEObject joseObject, String issuerCN) {
		if ((joseObject == null) || (isNullOrEmpty(issuerCN))) {
			logger.error("jose object is null or issuerCN is null or empty");
			return null;
		}
		List<com.nimbusds.jose.util.Base64> certList;
		if ((joseObject instanceof JWSObject)) {
			certList = ((JWSObject) joseObject).getHeader().getX509CertChain();
		} else {
			if ((joseObject instanceof JWEObject)) {
				certList = ((JWEObject) joseObject).getHeader().getX509CertChain();
			} else {
				logger.error("Unknown JWT authentication type, not signed, not encrypted");
				return null;
			}
		}
		for (com.nimbusds.jose.util.Base64 base64cert : certList) {
			try {
				InputStream in = new ByteArrayInputStream(base64cert.decode());
				Throwable localThrowable4 = null;
				try {
					CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
					Certificate certificate = certFactory.generateCertificate(in);
					X509Certificate x509Certificate = (X509Certificate) certificate;
					if (issuerCN.equalsIgnoreCase(x509Certificate.getIssuerX500Principal().getName().split("=")[1])) {
						return x509Certificate.getSubjectDN().getName().split(",")[0].split("=")[1];
					}
				} catch (Throwable localThrowable2) {
					localThrowable4 = localThrowable2;
					throw new CertificateException(localThrowable2);
				} finally {
					if (in != null) {
						if (localThrowable4 != null) {
							try {
								in.close();
							} catch (Throwable localThrowable3) {
								localThrowable4.addSuppressed(localThrowable3);
							}
						} else {
							in.close();
						}
					}
				}
			} catch (CertificateException ex) {
				logger.error("couldn't create certificate from encoded cert string {}", ex);
				return null;
			} catch (IOException e) {
				logger.error("Unable to close input stream object: ", e);
				return null;
			}
		}
		logger.error("No Certificate chain available in jwt x5c header");
		return null;
	}

	public AuthAttributes getAuthAttributes(String jwtString, String issuerCN) {
		JOSEObject joseObject;
		if (isNullOrEmpty(jwtString)) {
			logger.error("jwt payload is null or empty .");
			return null;
		}
		try {
			joseObject = JOSEObject.parse(jwtString);
		} catch (ParseException e) {
			logger.error("Couldn't parse JWT Token" + e, e);
			return null;
		}

		AuthAttributes authAttributes = new AuthAttributes();
		authAttributes.setIsEncrypted(isEncrypted(joseObject));
		if (authAttributes.isEncrypted) {
			authAttributes.setRecipientSerialNumber(getKid(joseObject));
			authAttributes.setSenderSerialNumber(null);
		} else {
			authAttributes.setRecipientSerialNumber(null);
			authAttributes.setSenderSerialNumber(getKid(joseObject));
		}
		return authAttributes;
	}

	private boolean isNullOrEmpty(String string) {
		if ((string == null) || (string.trim().length() == 0)) {
			return true;
		}
		return false;
	}

	public boolean isNotEmpty(String cs) {
		return !isNullOrEmpty(cs);
	}

	public boolean validateBodyDigest(String jsonBody, String challengeDigest) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			logger.error("Couldn't instantiate SHA-256 digest", e);
			return false;
		}
		byte[] bodyDigestBytes = messageDigest.digest(jsonBody.getBytes());
		String bodyDigestString = com.cybersource.authsdk.util.Base64.getEncoder().encodeToString(bodyDigestBytes);

		return bodyDigestString.equals(challengeDigest);
	}

	public JWTPayload getJWTPayload(String jwtString) {
		String payload = getPayload(jwtString);
		return (JWTPayload) new Gson().fromJson(payload, JWTPayload.class);
	}

	public String createBodyDigest(String jsonBody) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			logger.error("Couldn't instantiate SHA-256 digest", e);
			return null;
		}
		byte[] bodyDigestBytes = messageDigest.digest(jsonBody.getBytes());
		return com.cybersource.authsdk.util.Base64.getEncoder().encodeToString(bodyDigestBytes);
	}
}
