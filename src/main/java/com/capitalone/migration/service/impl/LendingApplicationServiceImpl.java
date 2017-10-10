package com.capitalone.migration.service.impl;

import com.capitalone.migration.dto.LendingApplicationAuditWrapperDTO;
import com.capitalone.migration.constants.OrchestrationConstants;
import com.capitalone.migration.service.LendingApplicationService;
import com.capitalone.migration.utils.ServiceLocatorUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

@Service
public class LendingApplicationServiceImpl implements LendingApplicationService {

    private static final Logger logger = LogManager.getLogger(LendingApplicationServiceImpl.class);

    @Autowired
    @Qualifier("customerAPISSLRestTemplate")
    private RestTemplate httpsRestTemplate;

    @Inject
    private ObjectMapper objectMapper;

    @Value("${api_endpoints.customer.submit_lending_application}")
    private String submitLendingApplicationURL;

    public void submitApplication(String authHeader, String[] record) throws Exception {
        String messageParams = "";
        try {
            ResponseEntity<?> submitLendingApplicationResponse = null;

            submitLendingApplicationResponse = httpsRestTemplate.exchange(submitLendingApplicationURL, HttpMethod.POST,
                    getLendingApplicationRequestEntity(authHeader, record), String.class);
            if (null != submitLendingApplicationResponse
                    && submitLendingApplicationResponse.getStatusCode() == HttpStatus.OK) {
                logger.info("Lending application saved successfully");
            }
            else {
                throw new Exception("System error");
            }
        }
        catch (ServiceException e) {
            logger.info(messageParams, e);
            throw new Exception("System error");
        }
        catch (Exception e) { //NOPMD
            logger.info(messageParams, e);
            throw new Exception("System error");
        }
    }

    private HttpEntity<?> getLendingApplicationRequestEntity(String authHeader, String[] recordArray) throws Exception {

        String apiBody = null;
        HttpHeaders headers = ServiceLocatorUtil.getHttpHeaders(OrchestrationConstants.ONE);
        headers.remove(OrchestrationConstants.ACCEPT_ENCODING_KEY);
        headers.set(OrchestrationConstants.CONTENT_TYPE_KEY, OrchestrationConstants.FORMAT_JSON);

        headers.set("Authorization", authHeader);

        LendingApplicationAuditWrapperDTO lendingApplicationAuditWrapperDTO = new LendingApplicationAuditWrapperDTO();

        try {
            lendingApplicationAuditWrapperDTO.setJsonDump(objectMapper.writeValueAsString(recordArray));
        }
        catch (JsonProcessingException exception) {
            logger.error("JsonProcessingException thrown while serializing ApplicationDTO: " + exception.getMessage());
            logger.info("JsonProcessingException thrown while serializing ApplicationDTO in info: " + exception
                    .getMessage());
            throw new Exception("System error");
        }
        lendingApplicationAuditWrapperDTO.setCreatedBy("Migrated from on-premise Oracle database");

        try {
            apiBody = objectMapper.writeValueAsString(lendingApplicationAuditWrapperDTO);
        }
        catch (JsonProcessingException exception) {
            logger.error("JsonProcessingException thrown while serializing ApplicationDTO: " + exception.getMessage());
            logger.info("JsonProcessingException thrown while serializing ApplicationDTO in info: " + exception
                    .getMessage());
            throw new Exception("System error");
        }
        apiBody = apiBody.replaceAll("\\\\\"","").replaceAll("\\\\","\"").replaceAll("\"\"","\"").replaceAll("\"\"", "\"");
        return new HttpEntity<String>(apiBody, headers);
    }

}

/*
 * Copyright 2015 Capital One Financial Corporation All Rights Reserved.
 * 
 * This software contains valuable trade secrets and proprietary information of Capital One and is protected by law. It
 * may not be copied or distributed in any form or medium, disclosed to third parties, reverse engineered or used in any
 * manner without prior written authorization from Capital One.
 */
