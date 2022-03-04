package com.servicetransaction.enumeration;

public enum TypeTransaction {
    DEBIT("DEBIT"), CREDIT("CREDIT");
    private final String type;

    TypeTransaction(String type) {
        this.type = type;
    }

    public String getType(){
        return this.type;
    }
}
