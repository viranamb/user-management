package com.capitalone.migration.dto;

import java.io.Serializable;

public class LendingApplicationAuditWrapperDTO implements Serializable {
    private String jsonDump;
    private String createdBy;

    public String getJsonDump() { return this.jsonDump; }

    public void setJsonDump(String jsonDump) {
        this.jsonDump = jsonDump;
    }

    public String getCreatedBy() { return this.createdBy; }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "LendingApplicationAuditDTO{" +
                "jsonDump='" + jsonDump + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
