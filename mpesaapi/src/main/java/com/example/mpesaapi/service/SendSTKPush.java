package com.example.mpesaapi.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.mpesaapi.config.MpesaConfigProperties;
import com.example.mpesaapi.dto.STKPushRequestDto;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Service
@RequiredArgsConstructor
public class SendSTKPush {

    private final MpesaConfigProperties config;
    private final MpesaService mpesaService; // Service to handle M-Pesa authentication
    private static final String SANDURL = "https://sandbox.safaricom.co.ke";
    private static final String PRODURL = "https://api.safaricom.co.ke";
    private static final String AUTH = "authorization";

    public String initiateSTKPush(String businessShortCode, String passkey,
             String callbackUrl, String timeoutUrl, STKPushRequestDto dto) throws IOException {

        String timestamp = getCurrentTimestamp();
        String password = generateSTKPassword(businessShortCode, passkey, timestamp);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("BusinessShortCode", businessShortCode);
        jsonObject.put("Password", password);
        jsonObject.put("Timestamp", timestamp);
        jsonObject.put("TransactionType", dto.getTransactionType());
        jsonObject.put("Amount", dto.getAmount());
        jsonObject.put("PhoneNumber", dto.getPhoneNumber());
        jsonObject.put("PartyA", dto.getPhoneNumber());
        jsonObject.put("PartyB", businessShortCode);
        jsonObject.put("CallBackURL", callbackUrl);
        jsonObject.put("AccountReference", dto.getAccountReference());
        jsonObject.put("QueueTimeOutURL", timeoutUrl);
        jsonObject.put("TransactionDesc", dto.getTransactionDesc());

        String requestJson = jsonObject.toString();
        String baseUrl = isSandbox() ? SANDURL : PRODURL;
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);

        Request request = new Request.Builder()
                .url(baseUrl + "/mpesa/stkpush/v1/processrequest")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader(AUTH, "Bearer " + mpesaService.authenticate())
                .addHeader("cache-control", "no-cache")
                .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody finalBody = response.body(); // Read response body
            if (finalBody == null) {
                throw new IOException("Response body is null");
            }
            return finalBody.string();
        }
    }

    public String sTKPushTransactionStatus(String businessShortCode, String password,
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
                .addHeader(AUTH, "Bearer " + mpesaService.authenticate())
                .addHeader("content-type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody finalBody = response.body(); // Read response body
            if (finalBody == null) {
                throw new IOException("Response body is null");
            }
            return finalBody.string();
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
