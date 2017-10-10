package com.capitalone.migration;

import com.capitalone.migration.constants.OrchestrationConstants;
import com.capitalone.migration.service.LendingApplicationService;
import com.capitalone.migration.utils.turing.TuringHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.ws.rs.QueryParam;
import javax.xml.ws.ResponseWrapper;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fjl161
 */
@RestController
@RequestMapping(OrchestrationConstants.ORCHESTRATOR_RESOURCE_PATH)
public class MigrationResource {

    private static final Logger LOGGER = LogManager.getLogger(MigrationResource.class);

    @Inject
    private LendingApplicationService lendingApplicationService;

    @Inject
    private TuringHandler turingHandler;

    @RequestMapping(value = OrchestrationConstants.ORCHESTRATOR_PATH, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseWrapper> handlePostServiceRequest(@RequestHeader("Authorization") String authHeader, @QueryParam(OrchestrationConstants.PATH_TO_FILE) String pathToFile) throws Exception {

        List<String[]> dataList = Files.lines(Paths.get(pathToFile))
                .map(line -> line.split("\\|"))
                .collect(Collectors.toList());

        String[] columnArray = dataList.get(0);
        Integer indexTin = null;
        Integer indexSsn = null;
        Integer indexJsonDump = null;

        ColumnIndex columnIndex = new ColumnIndex(columnArray, indexTin, indexSsn, indexJsonDump).invoke();
        indexTin = columnIndex.getIndexTin();
        indexSsn = columnIndex.getIndexSsn();
        indexJsonDump = columnIndex.getIndexJsonDump();

        for (int i = 1; i < dataList.size(); i++) {
            String[] recordArray = dataList.get(i);

            tokenizeAndReplaceValue(recordArray, i, indexTin, indexSsn, indexJsonDump);

            saveRecord(authHeader, i, recordArray);
        }

        return new ResponseEntity<ResponseWrapper>(HttpStatus.OK);
    }

    private void saveRecord(String authHeader, int i, String[] recordArray) throws Exception {
        LOGGER.info("Saving record number " + (i + 1) + " of application data");
        try {
            lendingApplicationService.submitApplication(authHeader, recordArray);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("Error saving record " + (i + 1) + " of application data");
            throw new Exception("Aborting migration at record " + (i + 1) + " of application data");
        }
        LOGGER.info("Lending application for record " + (i + 1) + " saved successfully");
    }

    private void tokenizeAndReplaceValue(String[] recordArray, Integer index, Integer indexTin, Integer indexSsn, Integer indexJsonDump) throws Exception {

        String businessTin = recordArray[indexTin];
        String businessTinToken = null;
        if (!StringUtils.isEmpty(businessTin) && !"".equalsIgnoreCase(businessTin)) {
            try {
                // Invoke turing API for businessTin
                businessTinToken = turingHandler.getTokenFromTuring(businessTin);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.info("Turing exception for businessTin " + businessTin + " of record number " + (index + 1) + " of application data");
            }
            // Replace value with tokenized value
            LOGGER.info("Replacing tokenized value :" + businessTinToken + " for businessTin " + businessTin + " of record number " + (index + 1) + " of application data");
            recordArray[indexTin] = businessTinToken;
        }

        String ssn = recordArray[indexSsn];
        String ssnToken = null;
        if (!StringUtils.isEmpty(ssn) && !"".equalsIgnoreCase(ssn)) {
            try {
                // Invoke turing API for businessTin
                ssnToken = turingHandler.getTokenFromTuring(ssn);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.info("Turing exception for ssn " + ssn + " of record number " + (index + 1) + " of application data");
            }
            // Replace value with tokenized value
            LOGGER.info("Replacing tokenized value :" + ssnToken + " for ssn " + ssn + " of record number " + (index + 1) + " of application data");
            recordArray[indexSsn] = ssnToken;
        }

        String jsonDump = recordArray[indexJsonDump];
        if (!StringUtils.isEmpty(jsonDump) && !"".equalsIgnoreCase(jsonDump)) {
            if (!StringUtils.isEmpty(businessTin)) {
                jsonDump = jsonDump.replaceAll(businessTin, businessTinToken);
            }

            if (!StringUtils.isEmpty(ssn) && !"\"\"".equalsIgnoreCase(ssn)) {
                jsonDump = jsonDump.replaceAll(ssn, ssnToken);
            }
            recordArray[indexJsonDump] = jsonDump;
        }
    }

    private class ColumnIndex {

        private String[] columnArray;

        private Integer indexTin;

        private Integer indexSsn;

        private Integer indexJsonDump;

        public ColumnIndex(String[] columnArray, Integer indexTin, Integer indexSsn, Integer indexJsonDump) {
            this.columnArray = columnArray;
            this.indexTin = indexTin;
            this.indexSsn = indexSsn;
            this.indexJsonDump = indexJsonDump;
        }

        public Integer getIndexTin() {
            return indexTin;
        }

        public Integer getIndexSsn() {
            return indexSsn;
        }

        public Integer getIndexJsonDump() {
            return indexJsonDump;
        }

        public ColumnIndex invoke() {
            for (int i = 0; i < columnArray.length; i++) {
                if ("\"BUS_TIN\"".equalsIgnoreCase(columnArray[i])) {
                    indexTin = i;
                } else if ("\"CUST_SSN\"".equalsIgnoreCase(columnArray[i])) {
                    indexSsn = i;
                } else if ("\"DUMP_JSON_REQUEST\"".equalsIgnoreCase(columnArray[i])) {
                    indexJsonDump = i;
                }

                if (indexTin != null && indexSsn != null && indexJsonDump != null) {
                    break;
                }
            }
            return this;
        }
    }
}

/*
 * Copyright 2016 Capital One Financial Corporation All Rights Reserved.
 * 
 * This software contains valuable trade secrets and proprietary information of
 * Capital One and is protected by law. It may not be copied or distributed in
 * any form or medium, disclosed to third parties, reverse engineered or used in
 * any manner without prior written authorization from Capital One.
 */