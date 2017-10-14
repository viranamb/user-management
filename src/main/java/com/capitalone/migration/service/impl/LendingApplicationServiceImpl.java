package com.capitalone.migration.service.impl;

import com.capitalone.migration.constants.OrchestrationConstants;
import com.capitalone.migration.dto.LendingApplicationAuditWrapperDTO;
import com.capitalone.migration.service.LendingApplicationService;
import com.capitalone.migration.utils.ServiceLocatorUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

@Service
public class LendingApplicationServiceImpl implements LendingApplicationService {

    private static final Logger logger = LogManager.getLogger(LendingApplicationServiceImpl.class);

    @Autowired
    @Qualifier("inbranchAPISSLRestTemplate")
    private RestTemplate httpsRestTemplate;

    @Value("${api_endpoints.customer.submit_lending_application}")
    private String submitLendingApplicationURL;

    public void submitApplication(String authHeader, String[] record, Integer indexTin, Integer indexSsn) throws Exception {
        String messageParams = "";
        try {
            ResponseEntity<?> submitLendingApplicationResponse = null;

            httpsRestTemplate.setRequestFactory(new NSBSimpleClientHttpRequestFactory(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            }));

            submitLendingApplicationResponse = httpsRestTemplate.exchange(submitLendingApplicationURL, HttpMethod.POST,
                    getLendingApplicationRequestEntity(authHeader, record, indexTin, indexSsn), String.class);
            if (null != submitLendingApplicationResponse
                    && submitLendingApplicationResponse.getStatusCode() == HttpStatus.OK) {
                logger.info("Lending application saved successfully");
            } else {
                throw new Exception("System error");
            }
        } catch (ServiceException e) {
            logger.info(messageParams, e);
            throw new Exception("System error");
        } catch (Exception e) { //NOPMD
            logger.info(messageParams, e);
            throw new Exception("System error");
        }
    }

    private HttpEntity<?> getLendingApplicationRequestEntity(String authHeader, String[] recordArray, Integer indexTin, Integer indexSsn) throws Exception {

        HttpHeaders headers = ServiceLocatorUtil.getHttpHeaders(OrchestrationConstants.ONE);
        headers.remove(OrchestrationConstants.ACCEPT_ENCODING_KEY);
        headers.set(OrchestrationConstants.CONTENT_TYPE_KEY, OrchestrationConstants.FORMAT_JSON);
        headers.set("Authorization", authHeader);

        JSONObject jsonObject = new JSONObject();
        for (int a = 0; a < recordArray.length - 1; a++) {
            String recordValue = recordArray[a];
            jsonObject.put(a + "", (recordValue == null ? recordValue : recordValue.replace("\"\"", "\"")));
        }

        String businessTin = recordArray[indexTin];
        boolean isBusinessTinEmpty = StringUtils.isEmpty(businessTin) || "\"\"".equalsIgnoreCase(businessTin);
        jsonObject.put("busTinToken", isBusinessTinEmpty ? "EIN not provided" : businessTin);

        String customerSsnToken = recordArray[indexSsn];
        boolean isCustomerSSNEmpty = StringUtils.isEmpty(customerSsnToken) || "\"\"".equalsIgnoreCase(customerSsnToken);
        jsonObject.put("customerSsnToken", isCustomerSSNEmpty ? "SSN not provided" : customerSsnToken);

        LendingApplicationAuditWrapperDTO lendingApplicationAuditWrapperDTO = new LendingApplicationAuditWrapperDTO();
        lendingApplicationAuditWrapperDTO.setJsonDump(jsonObject.toString());
        lendingApplicationAuditWrapperDTO.setCreatedBy("Migrated from on-premise Oracle database");

        return new HttpEntity<LendingApplicationAuditWrapperDTO>(lendingApplicationAuditWrapperDTO, headers);
    }

}

/*
 * Copyright 2015 Capital One Financial Corporation All Rights Reserved.
 * 
 * This software contains valuable trade secrets and proprietary information of Capital One and is protected by law. It
 * may not be copied or distributed in any form or medium, disclosed to third parties, reverse engineered or used in any
 * manner without prior written authorization from Capital One.
 */
