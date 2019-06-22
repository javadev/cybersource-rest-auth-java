package com.cybersource.authsdk.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class Utility {
	private enum validationType {
		HTTP_SIGNATURE, JWT
	};

	public static boolean logEnable = false;

	/**
	 * Checks whether the authentication type is rightly entered with no typo
	 * error.
	 * 
	 * @param authenticationType
	 *            : HTTP or JWT
	 * @return -true if valid authentication type else return false.
	 */
	public static boolean checkAuthenticationValidation(String authenticationType) {
		boolean status = false;

		if (authenticationType.equalsIgnoreCase(validationType.HTTP_SIGNATURE.toString())
				|| authenticationType.equalsIgnoreCase(validationType.JWT.toString())) {
			status = true;
		}
		return status;
	}

	/**
	 * Depending on the logging LEVEL, the message is logged.
	 * 
	 * @param logger
	 *            : Logging object (Log4j2).
	 * @param exceptionMessage
	 *            : Custom message for logging the log file.
	 * @param obj
	 *            : object to get exception message.
	 * @param logLevel
	 *            : Level at which logging should occur
	 *            [INFO,ERROR,DEBUG,FATAL].
	 */
	public static void log(Logger logger, String exceptionMessage, Object obj, Level logLevel) {
		try {
			if (logEnable) {
				exceptionMessage = exceptionMessage.concat("%s");
				if (exceptionMessage != null && !exceptionMessage.isEmpty()) {
					if (logLevel == Level.ERROR) {
						exceptionMessage = String.format(exceptionMessage, obj);
						logger.error(exceptionMessage);
					} else if (logLevel == Level.DEBUG) {
						exceptionMessage = String.format(exceptionMessage, obj);
						logger.debug(exceptionMessage);
					} else if (logLevel == Level.FATAL) {
						exceptionMessage = String.format(exceptionMessage, obj);
						logger.fatal(exceptionMessage);
					} else if (logLevel == Level.OFF) {
						exceptionMessage = String.format(exceptionMessage, obj);
						logger.printf(Level.OFF, "%n");
					} else {
						exceptionMessage = String.format(exceptionMessage, obj);
						logger.info(exceptionMessage);
					}
				}
			}
		} catch (Exception e) {
			log(logger, e);
		}
	}

	/**
	 * 
	 * @param logger
	 *            : Logging object (Log4j2).
	 * @param e
	 *            : Exception caught during the process.
	 */
	public static void log(Logger logger, Exception e) {

		if (Utility.logEnable) {
			String logMessage = getStackTrace(e);
			logger.error(logMessage);
		}
	}

	/**
	 * This method returns the exception in string format to log, caught during
	 * the process.
	 * 
	 * @param e
	 *            : Exception caught during the process.
	 * @return stackTrace of Exception.
	 */
	private static String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String stackTrace = sw.toString();
		pw.close();
		return stackTrace;
	}

	/**
	 * 
	 * @param requestTarget
	 *            : request target from where we will retrieve Id
	 * @return id from request target.
	 */
	public static String retrieveGetIDFromRequestTarget(String requestTarget) {
		StringTokenizer str = new StringTokenizer(requestTarget, "/");
		String temp = null;
		while (str.hasMoreTokens()) {
			temp = str.nextToken();
			if (temp.matches("^[0-9]+$")) {
				return temp;
			}
		}
		return temp;
	}

	/**
	 * 
	 * @param responseCode
	 *            :responseCode to log .
	 * @param logger
	 *            :Log4j object
	 */
	public static void logResponseCodeMessage(int responseCode, Logger logger) {
		if (logEnable) {
			String tempResponseCode = String.valueOf(responseCode);
			String tempResponseCodeMessage = "Status Information :: ";
			String unDefinedResponse = "Un-Indentified";
			if ("200".equals(tempResponseCode)) {
				tempResponseCodeMessage = tempResponseCodeMessage + "Transaction Successful";
				logger.info(tempResponseCodeMessage);
			} else if ("400".equals(tempResponseCode)) {
				tempResponseCodeMessage = tempResponseCodeMessage + "Bad Request";
				logger.info(tempResponseCodeMessage);
			} else if ("401".equals(tempResponseCode)) {
				tempResponseCodeMessage = tempResponseCodeMessage + "Authentication Failed";
				logger.info(tempResponseCodeMessage);
			} else if ("201".equals(tempResponseCode)) {
				tempResponseCodeMessage = tempResponseCodeMessage + "Transaction Successful";
				logger.info(tempResponseCodeMessage);
			} else if ("500".equals(tempResponseCode)) {
				tempResponseCodeMessage = tempResponseCodeMessage + "Internal Server Error";
				logger.info(tempResponseCodeMessage);
			} else if ("502".equals(tempResponseCode)) {
				tempResponseCodeMessage = tempResponseCodeMessage + "Bad Gateway";
				logger.info(tempResponseCodeMessage);
			} else if ("503".equals(tempResponseCode)) {
				tempResponseCodeMessage = tempResponseCodeMessage + "SERVICE UNAVAILABLE";
				logger.info(tempResponseCodeMessage);
			} else if ("504".equals(tempResponseCode)) {
				tempResponseCodeMessage = tempResponseCodeMessage + "Gateway Timeout";
				logger.info(tempResponseCodeMessage);
			} else if ("404".equals(tempResponseCode)) {
				tempResponseCodeMessage = tempResponseCodeMessage + "Not Found";
				logger.info(tempResponseCodeMessage);
			} else if ("403".equals(tempResponseCode)) {
				tempResponseCodeMessage = tempResponseCodeMessage + "Forbidden";
				logger.info(tempResponseCodeMessage);
			} else {
				logger.info(tempResponseCodeMessage + unDefinedResponse);
			}
		}
	}
}
