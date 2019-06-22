package com.cybersource.authsdk.cache;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
/**
 * POJO class for caching X509Certificate, lastModified KeyFile time and private key.
 */
public class Identity {
	private X509Certificate x509;
	private RSAPrivateKey rsaPrivateKey;
	private long lastModifiedDate;
	
	
	public long getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(long lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public RSAPrivateKey getRsaPrivateKey() {
		return rsaPrivateKey;
	}
	
	public void setRsaPrivateKey(RSAPrivateKey privateKey) {
		this.rsaPrivateKey = privateKey;
	}
	
	public X509Certificate getX509() {
		return x509;
	}
	
	public void setX509(X509Certificate x509) {
		this.x509 = x509;
	}

}
