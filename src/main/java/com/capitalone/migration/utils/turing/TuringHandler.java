package com.capitalone.migration.utils.turing;

import com.capitalone.migration.utils.ServiceLocatorUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by iwh812 on 8/25/17.
 */
@Component
public class TuringHandler {

    private static final Logger logger = LogManager.getLogger(TuringHandler.class);

    @Value("${api_endpoint.turing}")
    private String getTuringUrl;

    @Value("${nsb.certificate.turing}")
    private String nsbCertTuring;

    @Autowired
    @Qualifier("turingAPISSLRestTemplate")
    private RestTemplate turingServiceLocator;

    @Inject
    private ObjectMapper objectMapper;

    private static final String SSN = "ssn";

    private static final String CLIENT = "client";

    /**
     * Get the tokenized data from the Turing API
     *
     * @param data SSN/EIN
     * @return tokenizedId from turing
     */
    public String getTokenFromTuring(String data) throws Exception {
        URI uri = null;
        String tokenizedId = null;
        HttpStatus status = null;
        TuringToken turingToken = null;
        ResponseEntity<?> response = null;

        logger.info("Turing API URL in tokenize : {}"+ getTuringUrl);
        uri = UriComponentsBuilder.fromUriString(getTuringUrl).build(true).toUri();

        response = turingServiceLocator.exchange(uri, HttpMethod.POST, getHttpEntityForTuringToken(data), TuringToken.class);
        status = response.getStatusCode();
        logger.info("Turing API status in tokenize : {}" + status);
        if (status == HttpStatus.OK) {
            turingToken = (TuringToken) response.getBody();
            tokenizedId = turingToken.getTokenizedSsn();
            logger.debug("TokenizedSsn from Turing API in getTokenFromTuring : {}" + tokenizedId);
        }
        else {
            handleErrorsTuring(status, response);
            throw new Exception("System error");
        }

        return tokenizedId;
    }

    /**
     * Set the request map for the Turing API call
     *
     * @param data SSN/EIN
     * @return HttpEntity<String>
     */
    private HttpEntity<String> getHttpEntityForTuringToken(String data) throws Exception {
        Map<String, String> requestMap = new LinkedHashMap<String, String>();
        String apiBody = null;
        try {
            logger.info("Turing client : {} in request to Turing"+ nsbCertTuring);
            requestMap.put(SSN, data);
            requestMap.put(CLIENT, nsbCertTuring);
            apiBody = objectMapper.writeValueAsString(requestMap);
        }
        catch (JsonProcessingException e) {
            logger.info("JsonProcessingException in TuringHandler.getHttpEntityForTuringToken", e);
            throw new Exception("JsonProcessingException in TuringHandler.getHttpEntityForTuringToken");
        }
        return new HttpEntity<String>(apiBody, ServiceLocatorUtil.getHttpHeadersTokenize());
    }

    /**
     * Handle errors while making the Turing API call
     *
     * @param httpStatus http status of the api response
     * @param response   response object of the spring rest template
     */
    private void handleErrorsTuring(HttpStatus httpStatus, ResponseEntity<?> response) {
        TuringToken turingToken = (TuringToken) response.getBody();
        if (httpStatus == HttpStatus.BAD_REQUEST) {
            logger.info("Invalid Turing Request status : {}, text : {}, detail : {}"+ turingToken.getId() + turingToken
                    .getText() + turingToken.getDeveloperText());
        }
        else { // All other errors
            logger.info("Error while calling Turing status : {} " + httpStatus);
        }
    }
}

/*
 * Copyright 2016 Capital One Financial Corporation All Rights Reserved.
 *
 * This software contains valuable trade secrets and proprietary information of Capital One and is protected by law. It
 * may not be copied or distributed in any form or medium, disclosed to third parties, reverse engineered or used in any
 * manner without prior written authorization from Capital One.
 */
