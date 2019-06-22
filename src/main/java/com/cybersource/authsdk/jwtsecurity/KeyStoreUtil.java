package com.cybersource.authsdk.jwtsecurity;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyStoreUtil {
	private static Logger logger = LoggerFactory.getLogger(KeyStoreUtil.class);

	public static PrivateKey getPrivateKey(String keyStorePath, String keystorePassword, String privateKeyPassword,
			String keyAlias) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(keyStorePath);
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(is, keystorePassword.toCharArray());

			Key key = keystore.getKey(keyAlias, privateKeyPassword.toCharArray());
			if ((key instanceof PrivateKey)) {
				return (PrivateKey) key;
			}
			return null;
		} catch (Exception e) {
			logger.error("Couldn't retrieve private key from keystore:{} {}", keyStorePath, e);
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				logger.error("Error while closing file input stream " + keyStorePath, e);
			}
		}
	}

	public static X509Certificate getX509Certificate(String keyStorePath, String keystorePassword, String alias) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(keyStorePath);
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(is, keystorePassword.toCharArray());
			return (X509Certificate) keystore.getCertificate(alias);
		} catch (Exception e) {
			logger.error("Couldn't retrieve public key from keystore:{} {}", keyStorePath, e);
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				logger.error("Error while closing file input stream " + keyStorePath, e);
			}
		}
	}
}
