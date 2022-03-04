package com.servicetransaction.services;

import com.servicetransaction.dto.RequestTransDto;
import com.servicetransaction.entities.Response;
import com.servicetransaction.entities.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public interface TransactionService {
    Map<String, String> credit(RequestTransDto requestTransDto, HttpServletRequest request) throws IOException;
    Boolean Paydunyacredit(RequestTransDto requestTransDto, HttpServletRequest request) throws IOException;
    Boolean PaydunyaDebit(RequestTransDto requestTransDto, HttpServletRequest request) throws IOException;
    Map<String, String> debit(RequestTransDto requestTransDto, HttpServletRequest request) throws IOException;
    Boolean verifyIappExist(String masterKey, String LivePrivateKey) throws IOException;
    String getAuthenticationSystemManagementToken() throws IOException;
    Boolean transactionRequest(String masterKey, String livePrivateKey, Double amount, String url) throws IOException ;
    }
