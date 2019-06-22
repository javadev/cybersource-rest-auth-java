package com.cybersource.authsdk.cache;

import java.io.File;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.Logger;
import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.Utility;

/**
 * 
 * 
 *
 */
public class Cache {
	public static boolean isCache = false;
	private boolean isTimeStamp;
	private MerchantConfig merchantConfig;
	public static long lastModifiedTime;
	private Identity identity = new Identity();
	public static ConcurrentHashMap<String, Identity> cacheP12 = new ConcurrentHashMap<String, Identity>();
	private X509Certificate x509Certificate;
	private RSAPrivateKey rsaPrivateKey;
	private String merchantID;
	Logger logger;

	public void setX509Certificate(X509Certificate x509Certificate) {
		this.x509Certificate = x509Certificate;
	}

	public void setRsaPrivateKey(RSAPrivateKey rsaPrivateKey) {
		this.rsaPrivateKey = rsaPrivateKey;
	}

	public RSAPrivateKey getRsaPrivateKey() {
		return rsaPrivateKey;
	}

	public X509Certificate getX509Certificate() {
		return x509Certificate;
	}

	/**
	 * @param merchantConfig
	 *            - contains all information for merchant.
	 */
	public Cache(MerchantConfig merchantConfig) {
		this.merchantConfig = merchantConfig;
		logger = Log4j.getInstance(this.merchantConfig);
		this.merchantID = merchantConfig.getMerchantID();
		if (cacheP12.isEmpty()) {
			Cache.isCache = true;
		} else {
			Cache.isCache = false;
		}
	}

	/**
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 * 
	 */
	public void setP12FileDetailsInCache() throws ConfigException {
		if (cacheP12.isEmpty()) {
			setUpP12Cache();
		} else {
			isTimeStamp = isLastModifiedTimeP12();
			if (isTimeStamp) {
				retriveP12DataFromCache();
			} else {
				setUpP12Cache();
			}
		}
	}

	/**
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public void setUpP12Cache() throws ConfigException {
		identity.setLastModifiedDate(getLastModifiedFileP12());
		identity.setX509(x509Certificate);
		identity.setRsaPrivateKey(rsaPrivateKey);
		String tempMerchantID = merchantConfig.getMerchantID();
		cacheP12.put(tempMerchantID, identity);
		lastModifiedTime = merchantConfig.getKeyFile().lastModified();
	}

	/**
	 * 
	 */
	public void retriveP12DataFromCache() {
		Identity tempIdentity = cacheP12.get(merchantID);
		if (tempIdentity != null) {
			x509Certificate = tempIdentity.getX509();
			rsaPrivateKey = tempIdentity.getRsaPrivateKey();
		}
	}

	/**
	 * @return true if file is modified else return false.
	 */
	public boolean isLastModifiedTimeP12() {
		Identity tempIdentity = cacheP12.get(merchantID);
		if (tempIdentity != null) {
			long tempLastModifiedTime = tempIdentity.getLastModifiedDate();
			if (lastModifiedTime == tempLastModifiedTime) {
				return true;
			} else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	/**
	 * @return value for last modified.
	 */
	public long getLastModifiedFileP12() {
		File f;
		try {
			f = new File(merchantConfig.getKeyFile().getAbsolutePath());
			return f.lastModified();
		} catch (Exception e) {
			Utility.log(logger, e);
			return (Long) null;
		}
	}
}
