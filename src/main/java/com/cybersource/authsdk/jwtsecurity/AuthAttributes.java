package com.cybersource.authsdk.jwtsecurity;

public class AuthAttributes {
	String recipientSerialNumber;
	String senderSerialNumber;
	boolean isEncrypted;
	String decryptedMessage;

	public String getSenderSerialNumber() {
		return this.senderSerialNumber;
	}

	public String getRecipientSerialNumber() {
		return this.recipientSerialNumber;
	}

	public boolean isEncrypted() {
		return this.isEncrypted;
	}

	public String getDecryptedMessage() {
		return this.decryptedMessage;
	}

	public void setRecipientSerialNumber(String serialNumber) {
		this.recipientSerialNumber = serialNumber;
	}

	public void setSenderSerialNumber(String serialNumber) {
		this.senderSerialNumber = serialNumber;
	}

	public void setIsEncrypted(boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}

	public void setDecryptedMessage(String decryptedMessage) {
		this.decryptedMessage = decryptedMessage;
	}
}
