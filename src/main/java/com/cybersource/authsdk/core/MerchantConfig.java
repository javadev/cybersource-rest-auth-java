package com.cybersource.authsdk.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.log.Log4j;
import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;

/**
 */
public class MerchantConfig {
	/**/
	private Properties props;
	/**/
	private String keysDirectory;
	private String keyAlias;
	private String keyPass;
	private String keyType;
	private String keyFilename;
	private String password;
	private boolean enableLog;
	private int timeout;
	private String proxyUser;
	private String proxyPassword;
	private String recipientID;
	private String effectivePassword;
	private String requestData;
	private String runEnvironment;
	private String proxyAddress;
	private int proxyPort;
	private File keyFile;
	private boolean isRequestType = false;
	public static boolean isSetMerchantDetails = true;
	/**
	 * HTTP Merchant Config Parameters
	 *
	 */
	private String merchantKeyId;
	private String merchantSecretKey;
	/* Common parameters for HTTP and JWT. */
	private String merchantID;
	private String url;
	private String requestTarget;
	private String authenticationType;
	private boolean sendToProduction;
	private String getID;
	private String requestHost;
	private String responseMessage;
	private String responseCode;
	private String vcCorelationID;
	private String requestType;

	private boolean isRequestJson = true;
	/* Others. */
	private Logger log;

	public void setRunEnvironment(String runEnvironment) {
		this.runEnvironment = runEnvironment;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getProxyAddress() {
		return proxyAddress;
	}

	public void setProxyAddress(String proxyAddress) {
		this.proxyAddress = proxyAddress;
	}

	public boolean isRequestJson() {
		return isRequestJson;
	}

	/**
	 * @return request data according to availability of request Json.
	 */
	public String getRequestData() {

		try {
			if (this.getRequestJsonPath().equalsIgnoreCase(GlobalLabelParameters.POST_OBJECT_METHOD_REQUEST_PATH)) {
				return requestData;
			}

			else if (this.getRequestJsonPath() != null && !this.getRequestJsonPath().isEmpty()) {
				requestData = PropertiesUtil.getJsonInput(this.getRequestJsonPath());
			}
		} catch (Exception e) {
			Utility.log(log, GlobalLabelParameters.FILE_NOT_FOUND, this.getRequestJsonPath(), Level.FATAL);
			Utility.log(log, e);
			return null;
		}
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public String getVcCorelationID() {
		return vcCorelationID;
	}

	public void setVcCorelationID(String vcCorelationID) {
		this.vcCorelationID = vcCorelationID;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) throws NullPointerException {
		this.responseMessage = responseMessage;
	}

	/* Logging Parameters. */
	private boolean logSignedData;
	private String logDirectory;
	private String logFilename;

	private String logMaximumSize;
	/* Others */
	private String date;
	private String requestJsonPath;
	private String urlRequestHeader;

	public String getUrlRequestHeader() {
		return urlRequestHeader;
	}

	public void setUrlRequestHeader(String urlRequestHeader) {
		this.urlRequestHeader = urlRequestHeader;
	}

	public String getRequestHost() {
		return requestHost;
	}

	public String getRequestJsonPath() {

		return requestJsonPath;
	}

	public void setRequestJsonPath(String requestJsonPath) {
		this.requestJsonPath = requestJsonPath;
	}

	public String getGetID() {
		return getID;
	}

	public String getMerchantSecretKey() {
		return merchantSecretKey;
	}

	public String getMerchantKeyId() {
		return merchantKeyId;
	}

	public String getDate() {
		return date;
	}

	public String getMerchantID() {
		return this.merchantID;
	}

	public void setRecipientID(String recipientID) {
		this.recipientID = recipientID;
	}

	public String getRecipientID() {
		return this.recipientID;
	}

	public String getKeysDirectory() {

		return this.keysDirectory;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	public String getKeyAlias() {
		return keyAlias;
	}

	public void setKeyPass(String keyPass) {
		this.keyPass = keyPass;
	}

	public boolean getrequestJson() {
		return false;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getKey() {
		return this.keyPass;
	}

	public String getKeyType() {
		return this.keyType != null ? this.keyType : "p12";
	}

	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}

	public String getAuthenticationType() {
		return this.authenticationType;
	}

	public boolean getSendToProduction() {
		return this.sendToProduction;
	}

	public String getKeyFilename() {
		return this.keyFilename;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequestTarget() {
		return requestTarget;
	}

	public void setRequestTarget(String requestTarget) {
		this.requestTarget = requestTarget;
	}

	public String getPassword() {
		return this.password;
	}

	public boolean getEnableLog() {
		return this.enableLog;
	}

	public Properties getProps() {
		return props;
	}

	public void setKeysDirectory(String keysDirectory) {
		this.keysDirectory = keysDirectory;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public void setKeyFilename(String keyFilename) {
		this.keyFilename = keyFilename;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	public void setEffectivePassword(String effectivePassword) {
		this.effectivePassword = effectivePassword;
	}

	public void setSendToProduction(boolean sendToProduction) {
		this.sendToProduction = sendToProduction;
	}

	public void setGetID(String getID) {
		this.getID = getID;
	}

	public void setRequestHost(String requestHost) {
		this.requestHost = requestHost;
	}

	public void setRequestJson(boolean isRequestJson) {
		this.isRequestJson = isRequestJson;
	}

	public void setLogSignedData(boolean logSignedData) {
		this.logSignedData = logSignedData;
	}

	public void setLogMaximumSize(String logMaximumSize) {
		this.logMaximumSize = logMaximumSize;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setEnableLog(boolean enableLog) {
		this.enableLog = enableLog;
	}

	public boolean getLogSignedData() {
		return this.logSignedData;
	}

	public String getLogDirectory() {
		return this.logDirectory;
	}

	public String getLogFilename() {
		return this.logFilename;
	}

	public String getLogMaximumSize() {
		return this.logMaximumSize;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public int getProxyPort() {
		return this.proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyUser() {
		return this.proxyUser;
	}

	public String getProxyPassword() {
		return this.proxyPassword != null ? this.proxyPassword : "";
	}

	public String getEffectivePassword() {
		return this.effectivePassword;
	}

	public void setLogFilename(String logFilename) {
		this.logFilename = logFilename;
	}

	public MerchantConfig() throws ConfigException {

	}

	public void setIsRequestType(boolean b) {
		this.isRequestType = b;

	}

	public MerchantConfig(Properties _props) throws ConfigException {
		if (_props != null) {
			this.props = _props;
			this.merchantID = this.props.getProperty("merchantID");
			this.date = date();
			setMerchantDetails();
		}
	}

	/**
	 * @param logger
	 *            -logger to log all information in log file.
	 * @return true or false as per validation
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 */
	public boolean validateMerchantDetails(Logger logger) throws ConfigException {
		this.log = logger;
		isRequestType = checkRequestType();

		boolean isMerchantId = true;
		boolean isCheckMerchantSecertKey = false;
		boolean isCheckMerchantKeyId = false;
		boolean isGetKeyAlias = false;
		boolean isCheckKeyPassword = false;
		boolean isCheckKeyFile = false;
		boolean isCheckJsonPath;
		boolean isCheckLogMaxSize = true;
		boolean isCheckRunEnvironment;
		boolean isMerchantDetailStatus = false;

		checkAuthenticationType();
		isMerchantId = checkMerchantId();

		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			isCheckMerchantSecertKey = checkMerchantSecertKey();
			isCheckMerchantKeyId = checkMerchantKeyId();
		} else if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.JWT)
				|| authenticationType.equalsIgnoreCase(GlobalLabelParameters.JWE)) {
			isGetKeyAlias = checkKeyAlias();
			isCheckKeyPassword = checkKeyPassword();
			isCheckKeyFile = checkKeyFile();
		}

		isCheckJsonPath = checkJsonPath(getRequestJsonPath());
		isCheckLogMaxSize = checkLogMaxSizeAndFolder();
		isCheckRunEnvironment = checkRunEvironment();

		if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
			isMerchantDetailStatus = isMerchantId && isCheckMerchantSecertKey && isCheckMerchantKeyId && isCheckJsonPath
					&& isCheckLogMaxSize && isCheckRunEnvironment && isRequestType && isSetMerchantDetails;
		} else if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
			isMerchantDetailStatus = isMerchantId && isGetKeyAlias && isCheckKeyPassword && isCheckKeyFile
					&& isCheckJsonPath && isCheckLogMaxSize && isCheckRunEnvironment && isRequestType
					&& isSetMerchantDetails;
		}
		return isMerchantDetailStatus;
	}

	/**
	 * @return true if valid request type else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean checkRequestType() throws ConfigException {
		if (this.requestType.equalsIgnoreCase(GlobalLabelParameters.PUT)
				|| this.requestType.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| this.requestType.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| this.requestType.equalsIgnoreCase(GlobalLabelParameters.DELETE)
				|| this.requestType.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
			return true;
		} else {
			Utility.log(log, GlobalLabelParameters.INVALID_REQUEST_TYPE, this.requestType, Level.ERROR);
			throw new ConfigException(GlobalLabelParameters.INVALID_REQUEST_TYPE);

		}
	}

	/**
	 * @return true if valid password else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean checkKeyPassword() throws ConfigException {
		if (this.keyPass == null || this.keyPass.isEmpty()) {
			this.keyPass = merchantID;
			Utility.log(log, GlobalLabelParameters.KEY_PASSWORD_EMPTY_NULL, "", Level.ERROR);
		}
		return true;
	}

	/**
	 * @return true if valid run environment else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean checkRunEvironment() throws ConfigException {
		boolean runEnvironment = false;
		if (this.runEnvironment.equalsIgnoreCase(GlobalLabelParameters.RUN_SANDBOX)
				|| this.runEnvironment.equalsIgnoreCase(GlobalLabelParameters.RUN_PRODUCTION)) {
			runEnvironment = true;
		} else if (this.runEnvironment == null || this.runEnvironment.isEmpty()) {
			Utility.log(log, GlobalLabelParameters.RUN_ENVIRONMENT_ERR, "", Level.ERROR);
			throw new ConfigException(GlobalLabelParameters.RUN_ENVIRONMENT_ERR);

		} else {
			runEnvironment = true;
		}
		return runEnvironment;
	}

	/**
	 * @return true if merchant id is not null or empty else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean checkMerchantKeyId() throws ConfigException {
		if (this.merchantKeyId == null || this.merchantKeyId.isEmpty()) {
			Utility.log(log, GlobalLabelParameters.MERCHANT_KEYID_MISSING, "", Level.ERROR);
			throw new ConfigException(GlobalLabelParameters.MERCHANT_KEYID_MISSING);
		}
		return true;
	}

	public void setMerchantKeyId(String merchantKeyId) {
		this.merchantKeyId = merchantKeyId;
	}

	/**
	 * @return true if secretKey is not null or empty else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean checkMerchantSecertKey() throws ConfigException {
		if (this.merchantSecretKey == null || this.merchantSecretKey.isEmpty()) {
			Utility.log(log, GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING, "", Level.ERROR);
			throw new ConfigException(GlobalLabelParameters.MERCHANTSECERT_KEY_MISSING);
		}
		return true;
	}

	public void setMerchantSecretKey(String merchantsecretKey) {
		this.merchantSecretKey = merchantsecretKey;
	}

	/**
	 * @return true if log size is valid else return false,
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean checkLogMaxSizeAndFolder() throws ConfigException {

		if (this.logDirectory == null || this.logDirectory.isEmpty()) {
			this.logDirectory = GlobalLabelParameters.DEFAULT_LOG_PATH;
			Utility.log(log, GlobalLabelParameters.NO_LOG_DIRECTORY, "", Level.ERROR);
			
		}
		return getLogFile();
	}

	/**
	 * @param jsonPath
	 *            -path for request json.
	 * @return true if path is valid else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean checkJsonPath(String jsonPath) throws ConfigException {
		File f = null;
		boolean check = true;
		if (isRequestType) {
			/* Continue, No logic check */
		} else {
			return false;
		}

		if (this.requestType.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| this.requestType.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
			return check;
		}
		if (jsonPath.equalsIgnoreCase(GlobalLabelParameters.POST_OBJECT_METHOD_REQUEST_PATH)) {
			return check;
		}
		if (jsonPath == null || jsonPath.isEmpty()) {
			Utility.log(log, GlobalLabelParameters.REQUEST_JSON_MIS_NULL, "", Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.REQUEST_JSON_MIS_NULL);

		}
		f = new File(jsonPath);
		if (!f.exists()) {
			Utility.log(log, GlobalLabelParameters.REQUEST_JSON_ERROR, f.getAbsolutePath(), Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.REQUEST_JSON_ERROR);

		}
		if (!f.canRead()) {
			Utility.log(log, GlobalLabelParameters.FILE_NOT_READ, f.getAbsolutePath(), Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.FILE_NOT_READ);
		}

		isRequestJson = true;
		return check;
	}

	/**
	 * @return true if keyAlias is not null or empty else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean checkKeyAlias() throws ConfigException {
		
		if (this.keyAlias == null || this.keyAlias.isEmpty()) {
			Utility.log(log, GlobalLabelParameters.KEY_ALIAS_NULL_EMPTY, this.merchantID, Level.ERROR);
			this.keyAlias = merchantID;

		} else if (!this.keyAlias.equalsIgnoreCase(this.merchantID)) {
			this.keyAlias = this.merchantID;
			Utility.log(log, GlobalLabelParameters.KEY_ALIAS_MISMATCH, "", Level.ERROR);
		}
		return true;
	}

	public void setMerchantDetails() throws ConfigException {
		this.authenticationType = this.getProperty(this.merchantID, "authenticationType");
        this.merchantID = this.getProperty(this.merchantID, "merchantID");
		/* Logging Parameters */
		if (this.props.getProperty("logDirectory") == null) {
			this.logDirectory = GlobalLabelParameters.DEFAULT_LOG_PATH;

		} else {
			this.logDirectory = this.getProperty(this.merchantID, "logDirectory").trim();
		}
		if (this.props.getProperty("logFilename") == null) {
			this.logFilename = GlobalLabelParameters.DEFAULT_LOG_NAME;

		} else {
			this.logFilename = this.getProperty(this.merchantID, "logFilename").trim();
		}
		if (this.props.getProperty("logMaximumSize") == null) {
			this.logMaximumSize = GlobalLabelParameters.DEFAULT_LOG_SIZE;

		} else {
		this.logMaximumSize = this.getProperty(this.merchantID, "logMaximumSize").trim();
		}
		/* JWT Parameters */
		if (authenticationType != null) {
			if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.JWT)) {
				if (this.props.getProperty("keysDirectory") == null) {
					this.keysDirectory = GlobalLabelParameters.DEF_KEY_DIRECTORY_PATH;

				} else {
				this.keysDirectory = this.getProperty(this.merchantID, "keysDirectory").trim();
				}
				if (this.props.getProperty("keyAlias") == null) {
					Utility.log(log, GlobalLabelParameters.KEY_ALIAS_NULL_EMPTY, this.merchantID, Level.ERROR);
					this.keyAlias = merchantID;

				} else {
					this.keyAlias = this.getProperty(this.merchantID, "keyAlias").trim();
				}
				if (this.props.getProperty("keyPass") == null) {
					this.keyPass = merchantID;

				} else {
					this.keyPass = this.getProperty(this.merchantID, "keyPass");
				}
				if (this.props.getProperty("keyFileName") == null) {
					this.keyFilename = merchantID;

				} else {
					this.keyFilename = this.getProperty(this.merchantID, "keyFileName").trim();
				}
			}
		}
		this.enableLog = this.checkBooleanLogValue(this.merchantID, "enableLog");
		/* Optional use [ TIME OUT ] */
		this.timeout = this.getIntegerProperty(this.merchantID, "timeout", 130);
		this.proxyPort = this.getIntegerProperty(this.merchantID, "proxyPort", 8080);
		this.proxyPassword = this.getProperty(this.merchantID, "proxyPassword");
		this.proxyAddress = this.getProperty(this.merchantID, "proxyAddress");
		/* HTTP Parameters */
		if (authenticationType != null) {
			if (authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)) {
				if (this.props.getProperty("merchantKeyId") == null) {
					throw new ConfigException("Merchant KeyId is mandatory");
				} else {
					this.merchantKeyId = this.getProperty(this.merchantID, "merchantKeyId").trim();
				}
				if (this.props.getProperty("merchantsecretKey") == null) {
					throw new ConfigException("Merchant SecretKey is mandatory");
				} else {
					this.merchantSecretKey = this.getProperty(this.merchantID, "merchantsecretKey").trim();
				}
			}
		}
		/* Run Environment. */
		if (this.props.getProperty("runEnvironment") == null) {
			throw new ConfigException("Run Environment is mandatory");
		} else {
			this.runEnvironment = this.getProperty(this.merchantID, "runEnvironment").trim();
		}
		
		try {
			if (this.runEnvironment.equalsIgnoreCase(GlobalLabelParameters.RUN_SANDBOX)) {
				this.requestHost = GlobalLabelParameters.SANDBOX_URL;
			} else if (this.runEnvironment.equalsIgnoreCase(GlobalLabelParameters.RUN_PRODUCTION)) {
				this.requestHost = GlobalLabelParameters.PRODUCTION_URL;
			} else {
				this.requestHost = this.runEnvironment;
			}
		} catch (Exception e) {
			this.log = Log4j.getInstance(this);
			Utility.log(this.log, GlobalLabelParameters.BEGIN_TRANSCATION, "", Level.INFO);
			Utility.log(this.log, GlobalLabelParameters.RUN_ENV_NULL_EMPTY, "", Level.ERROR);
			isSetMerchantDetails = false;
		}

	}

	/**
	 * @return true if authentication type is valid else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	private boolean checkAuthenticationType() throws ConfigException {
		/* Authentication Type. */
		boolean check = true;
		if (this.authenticationType == null || this.authenticationType.isEmpty()) {
			this.log = Log4j.getInstance(this);
			Utility.log(this.log, GlobalLabelParameters.BEGIN_TRANSCATION, "", Level.INFO);
			Utility.log(this.log, GlobalLabelParameters.AUTH_NULL_EMPTY, "", Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.AUTH_NULL_EMPTY);
		}
		if (!(this.authenticationType.equalsIgnoreCase(GlobalLabelParameters.JWT)||
				this.authenticationType.equalsIgnoreCase(GlobalLabelParameters.HTTP)||
						this.authenticationType.equalsIgnoreCase(GlobalLabelParameters.JWE))) {
			this.log = Log4j.getInstance(this);
			Utility.log(this.log, GlobalLabelParameters.BEGIN_TRANSCATION, "", Level.INFO);
			Utility.log(this.log, GlobalLabelParameters.AUTH_ERROR, "", Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.AUTH_ERROR);
		}
		return check;
	}

	/**
	 * @return true if merchant id or not null or return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean checkMerchantId() throws ConfigException {
		boolean check = true;
		if (this.merchantID == null || this.merchantID.isEmpty()) {
			this.log = Log4j.getInstance(this);
			Utility.log(this.log, GlobalLabelParameters.BEGIN_TRANSCATION, "", Level.INFO);
			Utility.log(this.log, GlobalLabelParameters.MERCHANT_NULL_EMPTY, "", Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.MERCHANT_NULL_EMPTY);
		}
		return check;

	}

	public void setLogDirectory(String logDirectory) {
		this.logDirectory = logDirectory;
	}

	/**
	 * @return keyFile as per merchant.
	 */
	public File getKeyFile() {
		return keyFile;
	}

	/**
	 * @return true if key file is available else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean checkKeyFile() throws ConfigException {
		boolean check = true;
		keyFile = null;

		if (this.keyFilename == null || this.keyFilename.isEmpty()) {
			Utility.log(log, GlobalLabelParameters.KEY_FILE_EMPTY, this.merchantID, Level.ERROR);
			if (this.merchantID != null) {
				this.keyFilename = this.merchantID;
			}

		}
		if (this.keysDirectory.isEmpty() || this.keysDirectory == null) {
			this.keysDirectory = GlobalLabelParameters.DEF_KEY_DIRECTORY_PATH;
			Utility.log(log, GlobalLabelParameters.KEY_DIRECTORY_EMPTY, "", Level.ERROR);
			
		}

		keyFile = new File(this.keysDirectory);

		if (!keyFile.isDirectory()) {
			Utility.log(log, GlobalLabelParameters.KEY_FILE_DIRECTORY_NOT_FOUND, this.keysDirectory, Level.ERROR);
			keyFile = null;
			check = false;
			return check;
		}
		try {
			keyFile = new File(this.keysDirectory, this.keyFilename.concat(GlobalLabelParameters.P12_EXTENSION));
			if (!keyFile.exists()) {
				Utility.log(log, GlobalLabelParameters.KEY_FILE_NOT_FOUND,
						this.keysDirectory.concat("/").concat(this.keyFilename) + GlobalLabelParameters.P12_EXTENSION,
						Level.ERROR);
				keyFile = null;
				check = false;
				return check;
			}
			Utility.log(log, GlobalLabelParameters.KEY_FILE,
					this.keysDirectory.concat("/").concat(this.keyFilename) + GlobalLabelParameters.P12_EXTENSION,
					Level.INFO);
		} catch (Exception e) {
			keyFile = null;
			check = false;
			return check;
		}
		boolean isRead = keyFile.canRead();

		if (isRead) {
			/* Do Nothing. */
			return check;
		} else {
			Utility.log(log, GlobalLabelParameters.FILE_NOT_READ, this.keysDirectory.concat(this.keyFilename),
					Level.ERROR);
			keyFile = null;
			check = false;
			return check;
		}
	}

	/**
	 * @return true if log file is correct else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean getLogFile() throws ConfigException {
		File dir = new File(this.getLogDirectory());
		if (!dir.isDirectory()) {
			Utility.log(log, GlobalLabelParameters.INVALID_LOG_DIRECTORY, "", Level.ERROR);
			this.logDirectory = GlobalLabelParameters.DEFAULT_LOG_PATH;
		}
		return getLogFileNameStatus();
	}

	/**
	 * @return true if logFtle name is valid else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	private boolean getLogFileNameStatus() throws ConfigException {
		boolean status = true;

		if (this.logFilename == null || this.logFilename.isEmpty()) {
			Utility.log(log, GlobalLabelParameters.NO_LOG_FILENAME, "", Level.ERROR);
			this.logFilename = GlobalLabelParameters.DEFAULT_LOG_NAME.concat(GlobalLabelParameters.LOG_EXTENSION);
		}

		return status;
	}

	/**
	 * @param merchantID
	 *            -merchant id for merchant.
	 * @param prop
	 *            -prop value from properties file.
	 * @return property as merchant.
	 */
	public String getProperty(String merchantID, String prop) {
		return this.getProperty(merchantID, prop, (String) null);
	}

	/**
	 *
	 * @param merchantID
	 *            - merchant id for merchant.
	 * @param prop
	 *            -prop value from properties file.
	 * @param defaultVal
	 *            -default value if its not available.
	 * @return value of the property as per prop value.
	 */
	public String getProperty(String merchantID, String prop, String defaultVal) {
		String val = null;
		String merchantSpecificProp = merchantID != null ? merchantID + "." + prop : null;
		if (this.props != null && merchantSpecificProp != null) {
			val = this.props.getProperty(merchantSpecificProp);
		}

		if (this.props != null && val == null) {
			val = this.props.getProperty(prop);
		}

		if (val == null && merchantSpecificProp != null) {
			val = System.getProperty("cybs." + merchantSpecificProp);
		}

		if (val == null) {
			val = System.getProperty("cybs." + prop);
		}

		if (val == null) {
			val = defaultVal;
		}

		return val;
	}

	/**
	 *
	 * @param merchantID
	 *            - merchant id for merchant.
	 * @param prop
	 *            -prop from properties file.
	 * @param defaultVal
	 *            -default value for property.
	 * @return true if valid else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean getBooleanProperty(String merchantID, String prop, boolean defaultVal) throws ConfigException {
		String strValue = this.getProperty(merchantID, prop);
		if (strValue == null) {
			return defaultVal;
		} else if (!"1".equals(strValue) && !"true".equalsIgnoreCase(strValue)) {
			if (!"0".equals(strValue) && !"false".equalsIgnoreCase(strValue)) {
				throw new ConfigException(prop + " has an invalid value.");
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 *
	 * @param merchantID
	 *            - id for merchant.
	 * @param prop
	 *            - prop from properties file.
	 * @param defaultVal
	 *            - default value for property
	 * @return integer value as per property.
	  */
	public int getIntegerProperty(String merchantID, String prop, int defaultVal) {
		String strValue = this.getProperty(merchantID, prop);
		if (strValue == null) {
			return defaultVal;
		} else {
			try {
				return Integer.parseInt(strValue);
			} catch (NumberFormatException var6) {
				Utility.log(log, GlobalLabelParameters.PROP_INVALID, "", Level.ERROR);

			}
		}
		return defaultVal;
	}

	/**
	 *
	 * @return date as per time Zone.
	 */
	private String date() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		date = dateFormat.format(calendar.getTime());
		return date;
	}

	/**
	 * @param merchantID
	 *            - merchant id for merchant.
	 * @param prop
	 *            - property from properties file.
	 * @return true if valid else return false.
	 */
	private boolean checkBooleanLogValue(String merchantID, String prop) {

		String logBoolean = this.getProperty(merchantID, prop);
		try {
			if (logBoolean == null || logBoolean.isEmpty()) {
				return true;
			} else if (logBoolean.equalsIgnoreCase("1")) {
				return true;
			} else if (logBoolean.equalsIgnoreCase("0")) {
				return false;
			} else if (logBoolean.equalsIgnoreCase("true")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}

}
