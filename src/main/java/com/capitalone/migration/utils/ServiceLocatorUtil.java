/**
 * TODO - Describe purpose and operation of class.
 * 
 * <table border="1" cellpadding="0" cellspacing="0" width="100%">
 * <caption align="center">Edit and Version History</caption>
 * <tr><th>Version</th><th>Date</th><th>Author</th><th>Description</th></tr>
 * <tr><td>1.0</td><td>Jul 24, 2014</td><td>XUI556</td><td>Initial creation.</td></tr>
 * </table>
 */
package com.capitalone.migration.utils;

import com.capitalone.migration.constants.OrchestrationConstants;
import org.springframework.http.HttpHeaders;

/**
 * @author XUI556 -
 * @since 1.0
 */
public final class ServiceLocatorUtil {

    private ServiceLocatorUtil() {
        // empty private consturctor is added to fix the sonar violation...
    }

    public static HttpHeaders getHttpHeaders(String versionNumber) {
        HttpHeaders headers = new HttpHeaders();

        StringBuilder versionNumberBuilder = new StringBuilder(OrchestrationConstants.FORMAT_JSON).append(OrchestrationConstants.SEMICOLON)
                .append(OrchestrationConstants.VERSION_PREFIX).append(versionNumber);
        headers.set(OrchestrationConstants.ACCEPT_KEY, versionNumberBuilder.toString());
        headers.set(OrchestrationConstants.ACCEPT_ENCODING_KEY, OrchestrationConstants.ACCEPT_ENCODING_VALUE);
        headers.set(OrchestrationConstants.API_KEY, OrchestrationConstants.API_KEY_VALUE);
        headers.set(OrchestrationConstants.CONTENT_TYPE_KEY, versionNumberBuilder.toString());
        headers.set(OrchestrationConstants.USER_ID_KEY, OrchestrationConstants.USER_ID_VALUE);

        return headers;
    }

    /**
     * Create HTTP request headers.
     * 
     * @param versionNumber the version number
     * @param profileRefId the profile reference id
     * @param apiKey the API key to use
     *  
     * @return the HTTP request entity.
     */
    public static HttpHeaders getHttpHeaders(String versionNumber, String profileRefId, String apiKey) {
        HttpHeaders headers = getHttpHeaders(versionNumber);
        headers.set(OrchestrationConstants.API_KEY, apiKey);
        headers.set(OrchestrationConstants.USER_ID_KEY, profileRefId);
        return headers;
    }

    /**
     * @Method to set the header for NSB client
     * @param versionNumber
     * @return
     */
    public static HttpHeaders getHttpHeadersForNSB(String versionNumber, String profileRefId) {
        return getHttpHeaders(versionNumber, profileRefId, OrchestrationConstants.API_KEY_VALUE_NSB);
    }

    /**
     * Set the http headers for Turing API
     * 
     * @return
     */
    public static HttpHeaders getHttpHeadersTokenize() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(OrchestrationConstants.CONTENT_TYPE_KEY, OrchestrationConstants.FORMAT_JSON);
        return headers;
    }

}

/*
 * Copyright 2014 Capital One Financial Corporation All Rights Reserved.
 * 
 * This software contains valuable trade secrets and proprietary information of Capital One and is protected by law. It
 * may not be copied or distributed in any form or medium, disclosed to third parties, reverse engineered or used in any
 * manner without prior written authorization from Capital One.
 */
