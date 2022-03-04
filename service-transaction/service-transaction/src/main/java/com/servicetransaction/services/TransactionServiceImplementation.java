package com.servicetransaction.services;

import com.servicetransaction.dto.RequestTransDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.servicetransaction.constante.App.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class TransactionServiceImplementation implements TransactionService{
    @Value("${system.url.app.exist}")
    private String urlAppExist;
    @Value("${system.url.login}")
    private String loginUrl;
    @Value("${system.username}")
    private String username;
    @Value("${system.password}")
    private String password;
    @Value("$system.url.debit.account}")
    private String urlDebit;
    @Value("${system.url.credit.account}")
    private String urlCredit;
    @Value("${paydunya.url.credit.account}")
    private String urlPaydunyaCredit;
    @Value("${paydunya.url.debit.account}")
    private String urlPaydunyaDebit;
    @Value("${paydunya.master.key}")
    private String paydunyaMasterKey;
    @Value("${paydunya.token}")
    private String paydunyaToken;
    @Value("${paydunya.private.key}")
    private String paydunyaPrivateKey;
    @Value("${paydunya.mode}")
    private String paydunyaMode;
    @Value("${paydunya.url.checkout.invoice}")
    private String paydunyaUrlCheckoutInvoice;

    @Override
    public Map<String, String> credit(RequestTransDto requestTransDto, HttpServletRequest request) throws IOException {
        String masterKey = request.getHeader(MASTERKEY);
        String privateKey = request.getHeader(LIVE_PRIVATE_KEY);
        String mode = request.getHeader(MODE);
        if (masterKey != null && privateKey != null && mode != null) {
            if (!this.verifyIappExist(masterKey, privateKey))
                throw new RuntimeException("incorrect master and private key combination");
        }else {
            throw new RuntimeException("Invalide headers!");
        }
        Paydunyacredit(requestTransDto,request);
        HashMap<String, String> result = new HashMap<>();
        if (Paydunyacredit(requestTransDto,request)) {
            result.put("message", "Transaction éffectuée avec succès");
        }else {
            result.put("message", "Transaction échouée");
        }
        return result;
    }

    @Override
    public Boolean Paydunyacredit(RequestTransDto requestTransDto, HttpServletRequest request) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        // request to get payement token from paydunya
        HttpPost requestInvoiceToken =new HttpPost(urlPaydunyaCredit);
        requestInvoiceToken.addHeader("PAYDUNYA-MASTER-KEY", paydunyaMasterKey);
        requestInvoiceToken.addHeader("PAYDUNYA-PRIVATE-KEY", paydunyaPrivateKey);
        requestInvoiceToken.addHeader("PAYDUNYA-TOKEN", paydunyaToken);
        requestInvoiceToken.setHeader("Content-type", APPLICATION_JSON_VALUE);
        JSONObject data = new JSONObject();
        data.put("amount", requestTransDto.getAmount());
        data.put("account_alias", requestTransDto.getCostomerPhone());
        data.put("withdraw_mode", "orange-money-senegal");
        StringEntity requestInvoiceTokenEntity = new StringEntity(data.toString());
        requestInvoiceToken.setEntity(requestInvoiceTokenEntity);
        CloseableHttpResponse requestInvoiceTokenResponse = client.execute(requestInvoiceToken);
        Object requestTokenresult = JSONValue.parse(EntityUtils.toString(requestInvoiceTokenResponse.getEntity(), "UTF-8"));
        JSONObject resultData = (JSONObject) requestTokenresult;
        System.out.println(resultData);
        if (resultData.get("response_code").equals("00")) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean PaydunyaDebit(RequestTransDto requestTransDto, HttpServletRequest request) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        // request to get payement token from paydunya
        HttpPost requestInvoiceToken =new HttpPost(paydunyaUrlCheckoutInvoice);
        requestInvoiceToken.addHeader("PAYDUNYA-MASTER-KEY", paydunyaMasterKey);
        requestInvoiceToken.addHeader("PAYDUNYA-PRIVATE-KEY", paydunyaPrivateKey);
        requestInvoiceToken.addHeader("PAYDUNYA-TOKEN", paydunyaToken);
//        requestInvoiceToken.addHeader("MODE", paydunyaMode);
        requestInvoiceToken.setHeader("Content-type", APPLICATION_JSON_VALUE);
        JSONObject data = new JSONObject();
        JSONObject invoice = new JSONObject();
        JSONObject store = new JSONObject();
        invoice.put("total_amount", requestTransDto.getAmount());
        store.put("phone", requestTransDto.getCostomerPhone());
        store.put("postal_address", requestTransDto.getCostomerEmail());
        store.put("name", requestTransDto.getCostomerName());
        data.put("store", store);
        data.put("invoice", invoice);
        StringEntity requestInvoiceTokenEntity = new StringEntity(data.toString());
        requestInvoiceToken.setEntity(requestInvoiceTokenEntity);
        CloseableHttpResponse requestInvoiceTokenResponse = client.execute(requestInvoiceToken);
        Object requestTokenresult = JSONValue.parse(EntityUtils.toString(requestInvoiceTokenResponse.getEntity(), "UTF-8"));
        JSONObject resultData = (JSONObject) requestTokenresult;
        HttpPost payementRequest =new HttpPost(urlPaydunyaDebit);
        JSONObject dataSoftpay = new JSONObject();
        dataSoftpay.put("phone_number", requestTransDto.getCostomerPhone());
        dataSoftpay.put("invoice_token", resultData.get("token"));
        dataSoftpay.put("authorization_code", requestTransDto.getAutorizationCode());
        StringEntity payementRequestEntity = new StringEntity(dataSoftpay.toString());
        payementRequestEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON_VALUE));
        payementRequest.setEntity(payementRequestEntity);
        CloseableHttpResponse payementRequestResponse = client.execute(payementRequest);
        Object payementRequestresult = JSONValue.parse(EntityUtils.toString(payementRequestResponse.getEntity(), "UTF-8"));
        JSONObject sftpayResult = (JSONObject) payementRequestresult;
        return (Boolean) sftpayResult.get("success");
    }

    @Override
    public Map<String, String>  debit(RequestTransDto requestTransDto, HttpServletRequest request) throws IOException {
        String masterKey = request.getHeader(MASTERKEY);
        String privateKey = request.getHeader(LIVE_PRIVATE_KEY);
        String mode = request.getHeader(MODE);
        if (masterKey != null && privateKey != null && mode != null) {
            if (!this.verifyIappExist(masterKey, privateKey))
                throw new RuntimeException("incorrect master and private key combination");
        }else {
            throw new RuntimeException("Invalide headers!");
        }
        Paydunyacredit(requestTransDto,request);
        HashMap<String, String> result = new HashMap<>();
        if (PaydunyaDebit(requestTransDto,request)) {
            result.put("message", "Transaction éffectuée avec succès");
        }else {
            result.put("message", "Transaction échouée");
        }
        return result;
    }

    @Override
    public Boolean verifyIappExist(String masterKey, String LivePrivateKey) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost =new HttpPost(urlAppExist);
        httpPost.addHeader("Authorization", "Bearer "+this.getAuthenticationSystemManagementToken());
        httpPost.setHeader("Content-type", APPLICATION_FORM_URLENCODED_VALUE);
        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("masterKey", masterKey));
        params.add(new BasicNameValuePair("livePrivateKey", LivePrivateKey));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        CloseableHttpResponse response = client.execute(httpPost);
        Object result= JSONValue.parse(EntityUtils.toString(response.getEntity(), "UTF-8"));
        JSONObject resultData = (JSONObject) result;
        JSONObject finalResult = (JSONObject) resultData.get("data");
        return (Boolean) finalResult.get("result");
    }

    @Override
    public String getAuthenticationSystemManagementToken() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost =new HttpPost(loginUrl);
        httpPost.setHeader("Content-type", APPLICATION_FORM_URLENCODED_VALUE);
        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        CloseableHttpResponse response = client.execute(httpPost);
        if (!(response.getStatusLine().getStatusCode() ==200)) {
            throw new RuntimeException("authentication to  system manager failed");
        }
        Object obj= JSONValue.parse(EntityUtils.toString(response.getEntity(), "UTF-8"));
        JSONObject jsonObject = (JSONObject) obj;
        String token = (String) jsonObject.get("access_token");
        return token;
    }

    @Override
    public Boolean transactionRequest(String masterKey, String livePrivateKey, Double amount, String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost =new HttpPost(url);
        httpPost.addHeader("Authorization", "Bearer "+this.getAuthenticationSystemManagementToken());
        httpPost.setHeader("Content-type", APPLICATION_FORM_URLENCODED_VALUE);
        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("masterKey", masterKey));
        params.add(new BasicNameValuePair("livePrivateKey", livePrivateKey));
        params.add(new BasicNameValuePair("amount", Double.toString(amount)));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        CloseableHttpResponse response = client.execute(httpPost);
        if (!(response.getStatusLine().getStatusCode() ==200)) {
            throw new RuntimeException("transaction échouée");
        }
        return true;
    }
}
