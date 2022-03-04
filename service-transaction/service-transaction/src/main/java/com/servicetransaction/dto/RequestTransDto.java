package com.servicetransaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestTransDto {
    @Column(name = "costomerName")
    private String costomerName;
    @Column(name = "costomerPhone")
    @NotEmpty(message = "Please provide the phone number")
    private String costomerPhone;
    @Column(name = "costomerEmail")
    private String costomerEmail;
    @Column(name = "description")
    private String description;
    @Column(name = "amount")
    @NotEmpty(message = "Please provide the amount")
    private Double amount;
    @Column(name = "autorizationCode")
    private String autorizationCode;
}
