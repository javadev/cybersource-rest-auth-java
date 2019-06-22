package com.cybersource.authsdk.jwt;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;

public class KeyCertificateGenerator {

	/**
	 * @param merchantConfig
	 *            -contains merchant information.
	 * @return certificate which will be used to generate token.
	 ** @throws CertificateException
	 *              - if certificate is missing or wrong.
	 * @throws NoSuchAlgorithmException
	 *              - if algorithm is not available.
	 * @throws IOException
	 *              - if file is not found.
	 * @throws KeyStoreException
	 *              - if file is not available in key store or wrong.
	 * @throws ConfigException
	 *              - if some values is missing or wrong for merchant.
	 * @throws Exception
	 *              - if some other exception will happen.
	 */
	public static X509Certificate initializeCertificate(MerchantConfig merchantConfig) throws CertificateException,
			NoSuchAlgorithmException, IOException, KeyStoreException, ConfigException, Exception {

		if (merchantConfig != null && merchantConfig.getKeyAlias() != null && merchantConfig.getKeyFile() != null) {
			KeyStore merchantKeyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
			merchantKeyStore.load(new FileInputStream(merchantConfig.getKeyFile()),
					merchantConfig.getKey().toCharArray());
			String merchantKeyAlias = null;
			Enumeration<String> enumKeyStore = merchantKeyStore.aliases();
			ArrayList<String> array = new ArrayList<String>();

			while (enumKeyStore.hasMoreElements()) {

				merchantKeyAlias = (String) enumKeyStore.nextElement();
				array.add(merchantKeyAlias);

			}
			merchantKeyAlias = keyAliasValidator(array, merchantConfig.getMerchantID());

			try {
				PrivateKeyEntry e = (PrivateKeyEntry) merchantKeyStore.getEntry(merchantKeyAlias,
						new PasswordProtection(merchantConfig.getKey().toCharArray()));
				return (X509Certificate) e.getCertificate();
			} catch (UnrecoverableEntryException var5) {
				return null;
			}

		} else {
			return null;
		}
	}

	/**
	 * @param array
	 *            -list of keyAlias.
	 * @param merchantID
	 *            -Id of merchant.
	 * @return merchantKeyalias for merchant.
	 */
	private static String keyAliasValidator(ArrayList<String> array, String merchantID) {
		int size = array.size();
		String tempKeyAlias, merchantKeyAlias, result;
		StringTokenizer str;
		for (int i = 0; i < size; i++) {
			merchantKeyAlias = array.get(i).toString();
			str = new StringTokenizer(merchantKeyAlias, ",");
			while (str.hasMoreTokens()) {
				tempKeyAlias = str.nextToken();
				if (tempKeyAlias.contains("CN")) {
					str = new StringTokenizer(tempKeyAlias, "=");
					while (str.hasMoreElements()) {
						result = str.nextToken();
						if (result.equalsIgnoreCase(merchantID)) {
							return merchantKeyAlias;
						} /* End if */
					} /* End while (str.hasMoreElements() */
				} /* End if (tempKeyAlias.contains("CN")) */
			} /* End while (str.hasMoreTokens() */
		} /* End for */
		return null;
	}

	/**
	 * @param merchantConfig
	 *            -contains merchant information.
	 * @return certificate which will be used to generate token.
	 ** @throws CertificateException
	 *              - if certificate is missing or wrong.
	 * @throws NoSuchAlgorithmException
	 *              - if algorithm is not available.
	 * @throws IOException
	 *              - if file is not found.
	 * @throws KeyStoreException
	 *              - if file is not available in key store or wrong.
	 * @throws ConfigException
	 *              - if some values is missing or wrong for merchant.
	 * @throws Exception
	 *              - if some other exception will happen.
	 */
	/* Currently this method is not used in the logic */
	public static X509Certificate initializeRecipientCertificate(MerchantConfig merchantConfig)
			throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, ConfigException,
			Exception {
		if (merchantConfig != null && merchantConfig.getRecipientID() != null && merchantConfig.getKeyFile() != null) {

			KeyStore merchantKeyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
			merchantKeyStore.load(new FileInputStream(merchantConfig.getKeyFile()),
					merchantConfig.getKey().toCharArray());
			String merchantKeyAlias = null;

			for (Enumeration<String> enumKeyStore = merchantKeyStore.aliases(); enumKeyStore
					.hasMoreElements(); merchantKeyAlias = null) {
				merchantKeyAlias = (String) enumKeyStore.nextElement();
				if (merchantKeyAlias.contains(merchantConfig.getRecipientID())) {
					break;
				}
			}

			Certificate certificate = null;
			if (merchantKeyAlias == null) {
				throw new ConfigException("merchant config error: cannot find recipient certificate in key file, "
						+ merchantConfig.getRecipientID());
			} else {
				certificate = merchantKeyStore.getCertificate(merchantKeyAlias);
				if (!(certificate instanceof X509Certificate)) {
					throw new ConfigException(
							"specified recipient certificate incorrect type(x509): " + merchantConfig.getRecipientID());
				} else {
					return (X509Certificate) certificate;
				}
			}
		} else {
			throw new ConfigException("merchant config fields missing: recipientId, key file");
		}
	}

	/**
	 * @param merchantConfig
	 *            -contains merchant information.
	 * @return certificate which will be used to generate token.
	 ** @throws CertificateException
	 *              - if certificate is missing or wrong.
	 * @throws NoSuchAlgorithmException
	 *              - if algorithm is not available.
	 * @throws IOException
	 *              - if file is not found.
	 * @throws KeyStoreException
	 *              - if file is not available in key store or wrong.
	 * @throws ConfigException
	 *              - if some values is missing or wrong for merchant.
	 * @throws Exception
	 *              - if some other exception will happen.
	 */
	public static RSAPrivateKey initializePrivateKey(MerchantConfig merchantConfig) throws CertificateException,
			NoSuchAlgorithmException, IOException, KeyStoreException, ConfigException, Exception {
		if (merchantConfig != null && merchantConfig.getKeyAlias() != null && merchantConfig.getKeyFile() != null) {
			KeyStore merchantKeyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
			merchantKeyStore.load(new FileInputStream(merchantConfig.getKeyFile()),
					merchantConfig.getKey().toCharArray());
			String merchantKeyAlias = null;
			Enumeration<String> enumKeyStore = merchantKeyStore.aliases();

			while (enumKeyStore.hasMoreElements()) {
				merchantKeyAlias = (String) enumKeyStore.nextElement();
				if (merchantKeyAlias.contains(merchantConfig.getKeyAlias())) {
					break;
				}
			}

			try {
				PrivateKeyEntry e = (PrivateKeyEntry) merchantKeyStore.getEntry(merchantKeyAlias,
						new PasswordProtection(merchantConfig.getKey().toCharArray()));
				return (RSAPrivateKey) e.getPrivateKey();
			} catch (UnrecoverableEntryException var5) {
				return null;
			}
		} else {
			return null;
		}
	}
}
