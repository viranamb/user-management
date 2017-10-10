/**
 * TODO - Describe purpose and operation of class.
 * 
 * <table border="1" cellpadding="0" cellspacing="0" width="100%">
 * <caption align="center">Edit and Version History</caption>
 * <tr><th>Version</th><th>Date</th><th>Author</th><th>Description</th></tr>
 * <tr><td>1.0</td><td>Aug 12, 2015</td><td>swx334</td><td>Initial creation.</td></tr>
 * </table>
 */
package com.capitalone.migration.service;

import org.springframework.stereotype.Service;

/**
 * @author swx334 -
 * @since 1.0
 */
@Service
public interface LendingApplicationService {

    void submitApplication(String authHeader, String[] lendingApplicationAuditDTO) throws Exception;
}

/*
 * Copyright 2015 Capital One Financial Corporation All Rights Reserved.
 * 
 * This software contains valuable trade secrets and proprietary information of Capital One and is protected by law. It
 * may not be copied or distributed in any form or medium, disclosed to third parties, reverse engineered or used in any
 * manner without prior written authorization from Capital One.
 */
