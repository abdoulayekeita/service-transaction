package com.servicetransaction.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Data
public class InvoiceDto {
    @Column(name = "costomerName")
    private String costomerName;
    @Column(name = "costomerPhone")
    private String costomerPhone;
    @Column(name = "costomerEmail")
    private String costomerEmail;
    @Column(name = "description")
    private String description;
    @Column(name = "amount")
    private String amount;
    @Column(name = "masterKey")
    @NotEmpty(message = "Please provide the masterKey")
    private String masterKey;
    @Column(name = "livePrivateKey")
    private String livePrivateKey;
    @Column(name = "testPrivateKey")
    private String testPrivateKey;
    @Column(name = "mode")
    @NotEmpty(message = "Please provide the mode")
    private String mode;
}
