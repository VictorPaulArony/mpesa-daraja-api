package com.example.mpesaapi.service;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class B2CRequest {

    private final MpesaService mpesaService; // Service to handle M-Pesa authentication

    public String B2CRequest(String initiatorName, String securityCredential, String commandID, String amount,
            String partyA, String partyB, String remarks, String queueTimeOutURL, String resultURL, String occassion)
            throws IOException {

        // Create JSON payload for B2C request
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("InitiatorName", initiatorName);
        jsonObject.put("SecurityCredential", securityCredential);
        jsonObject.put("CommandID", commandID);
        jsonObject.put("Amount", amount);
        jsonObject.put("PartyA", partyA);
        jsonObject.put("PartyB", partyB);
        jsonObject.put("Remarks", remarks);
        jsonObject.put("QueueTimeOutURL", queueTimeOutURL);
        jsonObject.put("ResultURL", resultURL);
        jsonObject.put("Occassion", occassion);

        String requestJson = jsonObject.toString(); // Convert JSON object to string

        OkHttpClient client = new OkHttpClient(); // Create HTTP client
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);

        // Build HTTP request to M-Pesa B2C endpoint
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/mpesa/b2c/v1/paymentrequest")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + mpesaService.authenticate()) // Add access token
                .build();

        // Execute HTTP request and handle response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected HTTP status code: " + response.code());
            }

            String responseBody = response.body().string(); // Read response body
            System.out.println("M-Pesa B2C API Response: " + responseBody);
            return responseBody; // Return response to caller
        }
    }
}
