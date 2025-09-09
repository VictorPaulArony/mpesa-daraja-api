package com.example.mpesaapi.service;

import okhttp3.*;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.logging.Logger;

import com.example.mpesaapi.config.MpesaConfigProperties;
import com.example.mpesaapi.dto.B2CRequestDto;

@Service
@RequiredArgsConstructor
public class B2CRequest {

    private final MpesaService mpesaService; // Service to handle M-Pesa authentication
    private static final Logger logger = Logger.getLogger(B2CRequest.class.getName());
    private static final String SANDURL = "https://sandbox.safaricom.co.ke";
    private static final String PRODURL = "https://api.safaricom.co.ke";
    private final MpesaConfigProperties config;

    public String sendB2CRequest(B2CRequestDto dto) throws IOException {

        // Create JSON payload for B2C request
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("InitiatorName", dto.getInitiatorName());
        jsonObject.put("SecurityCredential", dto.getSecurityCredential());
        jsonObject.put("CommandID", dto.getCommandID());
        jsonObject.put("Amount", dto.getAmount());
        jsonObject.put("PartyA", dto.getPartyA());
        jsonObject.put("PartyB", dto.getPartyB());
        jsonObject.put("Remarks", dto.getRemarks());
        jsonObject.put("QueueTimeOutURL", dto.getQueueTimeOutURL());
        jsonObject.put("ResultURL", dto.getResultURL());
        jsonObject.put("Occassion", dto.getOccassion());

        String requestJson = jsonObject.toString(); // Convert JSON object to string

        String baseUrl = isSandbox() ? SANDURL : PRODURL;

        OkHttpClient client = new OkHttpClient(); // Create HTTP client
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);

        // Build HTTP request to M-Pesa B2C endpoint
        Request request = new Request.Builder()
                .url(baseUrl + "/mpesa/b2c/v1/paymentrequest")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + mpesaService.authenticate()) // Add access token
                .build();

        // Execute HTTP request and handle response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected HTTP status code: " + response.code());
            }

            ResponseBody finalBody = response.body(); // Read response body
            if (finalBody == null) {
                throw new IOException("Response body is null");
            }
            String responseBody = finalBody.string();
            logger.info(responseBody);
            return responseBody; // Return response to caller
        }
    }

     private boolean isSandbox() {
        return "sandbox".equalsIgnoreCase(config.getEnv());
    }
}
