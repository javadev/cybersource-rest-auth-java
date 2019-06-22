package com.cybersource.authsdk.jwtsecurity;

public class JWTPayload {
	public String digest;
	public String digestAlgorithm;
	public String iat;

	public String getDigest() {
		return this.digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getDigestAlgorithm() {
		return this.digestAlgorithm;
	}

	public void setDigestAlgorithm(String digestAlgorithm) {
		this.digestAlgorithm = digestAlgorithm;
	}

	public String getIat() {
		return this.iat;
	}

	public void setIat(String iat) {
		this.iat = iat;
	}
}
