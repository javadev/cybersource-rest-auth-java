package com.cybersource.authsdk.util;

/**
 * These variables are basically used for logging purpose.
 */
public class GlobalLabelParameters {

	public static final String POST = "POST";
	public static final String GET = "GET";
	public static final String PUT = "PUT";
	public static final String PATCH = "PATCH";
	public static final String DELETE = "DELETE";
	public static final String SPACE = " ";
	/* Header Labels. */
	public static final String AUTENTICATION_TYPE = "Authentication Type : ";
	public static final String HTTP = "HTTP_Signature";
	public static final String JWT = "JWT";
	public static final String JWE = "JWE";
	public static final String V_C_MERCHANTID = "v-c-merchant-id";
	public static final String DATE = "Date";
	public static final String HOST = "Host";
	public static final String SIGNATURE = "Signature";
	public static final String USERAGENT = "User-Agent";
	public static final String USER_AGENT_VALUE = "Mozilla/5.0";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String APPLICATION_JSON = "application/json";
	public static final String V_C_CORRELATION_ID = "v-c-correlation-id";
	public static final String DIGEST = "Digest";
	public static final String REQUEST_TYPE = "Request Type";
	public static final String REQUEST_TARGET = "/pts/v2/payments/";
	public static final String URL_PREFIX = "https://";
	public static final String SANDBOX_URL = "apitest.cybersource.com";
	public static final String RUN_SANDBOX = "CyberSource.Environment.SANDBOX";
	public static final String RUN_PRODUCTION = "CyberSource.Environment.PRODUCTION";
	public static final String PRODUCTION_URL = "api.cybersource.com";
	public static final String P12_EXTENSION = ".p12";
	public static final String LOG_EXTENSION = ".log";
	public static final String DEFAULT_LOG_PATH = "log";
	public static final String DEFAULT_LOG_NAME = "cybs";
	public static final String DEF_KEY_DIRECTORY_PATH = "src/main/resources";
	/* Digest Constants */
	public static String SHA_256 = "SHA-256";
	/* Logging Labels */
	public static final String RESPONSE_CODE = "Response Code";
	public static final String DEFAULT_LOG_SIZE = "5K";
	/* Logging Messages. */
	public static final String BEGIN_TRANSCATION = "********* START *********";
	public static final String END_TRANSCATION = "********* END   *********";
	/* Exceptions */
	public static final String FILE_NOT_FOUND = "File not found, Entered path/file name :: ";
	public static final String KEY_FILE_NOT_FOUND = "KeyFile not found, Entered path/file name :: ";
	public static final String KEY_FILE_DIRECTORY_NOT_FOUND = "KeyDirectory not found, Entered directory :: ";
	public static final String KEY_FILE = "KeyFile, Entered path/file name :: ";
	public static final String FILE_NOT_READ = "File cannot be read permission denied :: ";
	public static final String AUTH_NULL_EMPTY="Authentication type is mandatory";
	public static final String AUTH_ERROR = "Invalid Authentication Type in cybs.properties :: ";
	public static final String KEY_ALIAS_NULL_EMPTY = "KeyAlias Empty/Null. Assigning merchantID value :: ";
	public static final String KEY_ALIAS_MISMATCH = "KeyAlias Mismatch. Assigning merchantID value ";
	public static final String KEY_PASSWORD_EMPTY_NULL = "KeyPassword Empty/Null. Assigning merchantID value";
	public static final String REQUEST_JSON_ERROR = "Request Json File missing. File Path :: ";
	public static final String REQUEST_JSON_MIS_NULL = "Request Json File Missing/Null";
	public static final String KEY_FILE_EMPTY = "KeyFileName is empty, assigning merchantID value :: ";
	public static final String KEY_DIRECTORY_EMPTY = "Key Directory is empty, assigning default path :: "
			+ DEF_KEY_DIRECTORY_PATH;
	// public static final String REFER_LOG = "Refer log for details";
	public static final String USER_AGENT_ERROR = "Unable to set USER_AGENT";
	public static final String SIG_SET_ERROR = "Unable to set Signature ";
	public static final String REQUEST_SET_ERROR = "Unable to set Request type :: ";
	public static final String PROXY_HOST_PORT_ERR = "Unable to set Proxy Host/Port";
	public static final String DIGEST_GEN_FAILED = "Digest generation failed";
	public static final String MERCHANTID_NOT_SET = "Unable to set merchantID";
	public static final String CONTENT_TYPE_NOT_SET = "Unable to set content type";
	public static final String URL_NOT_SET = "Unable to open/set URL :: ";
	public static final String MERCHANT_KEYID_MISSING = "Merchant KeyID missing/null, check cybs.properties";
	public static final String MERCHANTSECERT_KEY_MISSING = "Merchant Secret Key missing/null, check cybs.properties";
	public static final String JWT_SIG_FAILED = "JWT Signature production failed.";
	public static final String RUN_ENVIRONMENT_ERR = "Run Environment value is missing in cybs.properties";
	public static final String INVALID_REQUEST_TYPE = "Invalid Request Type :: ";
	public static final String INVALID_LOG_DIRECTORY = "Invalid log directory, creating default log directory";
	public static final String NO_LOG_DIRECTORY = "No log directory, setting default directory path.";
	public static final String NO_LOG_FILENAME = "No log file name, setting default log file name :: "
			+ DEFAULT_LOG_NAME + LOG_EXTENSION;
	public static final String MERCHANT_NULL_EMPTY = "MerchantID mandatory : NO MerchantID entered. ";
	public static final String RUN_ENV_NULL_EMPTY = "Run Environment mandatory : NO Run Environment entered. ";
	public static final String POST_OBJECT_METHOD_REQUEST_PATH="not required";
	public static final String PROP_INVALID="Prop has an invalid Value";
	/* Masking Parameter. */
	public static final String MASKING_PATTERN = "XXXXXXXXX";
	/* Proxy Parameter */
	public static final String PROXY_HOST = "http.proxyHost";
	public static final String PROXY_PORT = "http.proxyPort";
	/* Cache logging.*/
	public static final String CACHE_BEGIN = " Empty Cache :: Adding Data";
	public static final String CACHE_EXTRACT = " Data present in Cache :: Extracting Data";
	public static final String CACHE_EXTEND = " New Data present:: Inserting Data in Cache";

}
