package com.servicetransaction.enumeration;

public enum Status {
    PENDING("PENDING"), COMPLETED("COMPLETED"), FAILED("FAILED");
    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus(){
        return this.status;
    }
}
