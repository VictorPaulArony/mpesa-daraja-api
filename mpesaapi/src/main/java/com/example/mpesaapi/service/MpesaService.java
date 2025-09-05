package com.example.mpesaapi.service;

import com.example.mpesaapi.config.MpesaConfigProperties;

import lombok.Data;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
@Data
public class MpesaService {

    private final MpesaConfigProperties config;
    private String accessToken;
    private long tokenExpiryTime;
    private static final String SANDURL = "https://sandbox.safaricom.co.ke";
    private static final String PRODURL = "https://api.safaricom.co.ke";

    public MpesaService(MpesaConfigProperties config) {
        this.config = config;
        this.accessToken = null;
        this.tokenExpiryTime = 0;
    }

    public String getAppKey() {
        return config.getAppKey();
    }

    public String getAppSecret() {
        return config.getAppSecret();
    }

    public String getBusinessShortcode() {
        return config.getBusinessShortcode();
    }

    public String getPasskey() {
        return config.getPasskey();
    }

    public String getCallbackUrl() {
        return config.getCallbackUrl();
    }

    public String getTimeoutUrl() {
        return config.getTimeoutUrl();
    }

    public synchronized String authenticate() throws IOException {
        // Check if token is still valid (expires in 1 hour)
        if (accessToken != null && System.currentTimeMillis() < tokenExpiryTime) {
            return accessToken;
        }

        String appKeySecret = config.getAppKey() + ":" + config.getAppSecret();
        

        byte[] bytes = appKeySecret.getBytes("ISO-8859-1");
        String encoded = Base64.getEncoder().encodeToString(bytes);

        String baseUrl = isSandbox() ? SANDURL : PRODURL;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(baseUrl + "/oauth/v1/generate?grant_type=client_credentials")
                .get()
                .addHeader("authorization", "Basic " + encoded)
                .addHeader("cache-control", "no-cache")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                throw new IOException("Authentication failed. Code: " + response.code() +
                        ", Message: " + response.message() +
                        ", Body: " + errorBody);
            }

            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);

            if (jsonObject.has("access_token")) {
                accessToken = jsonObject.getString("access_token");
                tokenExpiryTime = System.currentTimeMillis() + 3600000;
                return accessToken;
            } else {
                throw new IOException("Authentication failed. Response: " + responseBody);
            }
        }
    }

    public String initiateSTKPush(String businessShortCode, String passkey, String transactionType,
            String amount, String phoneNumber, String accountReference,
            String transactionDesc, String callbackUrl, String timeoutUrl) throws IOException {

        String timestamp = getCurrentTimestamp();
        String password = generateSTKPassword(businessShortCode, passkey, timestamp);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("BusinessShortCode", businessShortCode);
        jsonObject.put("Password", password);
        jsonObject.put("Timestamp", timestamp);
        jsonObject.put("TransactionType", transactionType);
        jsonObject.put("Amount", amount);
        jsonObject.put("PhoneNumber", phoneNumber);
        jsonObject.put("PartyA", phoneNumber);
        jsonObject.put("PartyB", businessShortCode);
        jsonObject.put("CallBackURL", callbackUrl);
        jsonObject.put("AccountReference", accountReference);
        jsonObject.put("QueueTimeOutURL", timeoutUrl);
        jsonObject.put("TransactionDesc", transactionDesc);

        String requestJson = jsonObject.toString();
        String baseUrl = isSandbox() ? SANDURL : PRODURL;
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);

        Request request = new Request.Builder()
                .url(baseUrl + "/mpesa/stkpush/v1/processrequest")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + authenticate())
                .addHeader("cache-control", "no-cache")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            return responseBody;
        }
    }

    public String STKPushTransactionStatus(String businessShortCode, String password,
            String timestamp, String checkoutRequestID) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("BusinessShortCode", businessShortCode);
        jsonObject.put("Password", password);
        jsonObject.put("Timestamp", timestamp);
        jsonObject.put("CheckoutRequestID", checkoutRequestID);

        String requestJson = jsonObject.toString();

        String baseUrl = isSandbox() ? SANDURL : PRODURL;
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);

        Request request = new Request.Builder()
                .url(baseUrl + "/mpesa/stkpushquery/v1/query")
                .post(body)
                .addHeader("authorization", "Bearer " + authenticate())
                .addHeader("content-type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            return responseBody;
        }
    }

    private boolean isSandbox() {
        return "sandbox".equalsIgnoreCase(config.getEnv());
    }

    private String generateSTKPassword(String businessShortCode, String passkey, String timestamp) {
        String rawPassword = businessShortCode + passkey + timestamp;
        return Base64.getEncoder().encodeToString(rawPassword.getBytes());
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}