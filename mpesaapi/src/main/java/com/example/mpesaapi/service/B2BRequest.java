package com.example.mpesaapi.service;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.mpesaapi.config.MpesaConfigProperties;
import com.example.mpesaapi.dto.B2BRequestDto;

import lombok.Data;
import okhttp3.*;

@Data
@Service
public class B2BRequest {

    private final MpesaService mpesaService; // Service to handle M-Pesa authentication
    private static final String SANDURL = "https://sandbox.safaricom.co.ke";
    private static final String PRODURL = "https://api.safaricom.co.ke";
    private final MpesaConfigProperties config;

    public String sendB2BRequest(B2BRequestDto dto) throws IOException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Initiator", dto.getInitiatorName());
        jsonObject.put("SecurityCredential", dto.getSecurityCredential());
        jsonObject.put("CommandID", dto.getCommandID());
        jsonObject.put("SenderIdentifierType", dto.getSenderIdentifierType());
        jsonObject.put("RecieverIdentifierType", dto.getReceiverIdentifierType());
        jsonObject.put("Amount", dto.getAmount());
        jsonObject.put("PartyA", dto.getPartyA());
        jsonObject.put("PartyB", dto.getPartyB());
        jsonObject.put("Remarks", dto.getRemarks());
        jsonObject.put("AccountReference", dto.getAccountReference());
        jsonObject.put("QueueTimeOutURL", dto.getQueueTimeOutURL());
        jsonObject.put("ResultURL", dto.getResultURL());

        jsonArray.put(jsonObject);

        String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

        String baseUrl = isSandbox() ? SANDURL : PRODURL;
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);
        Request request = new Request.Builder()
                .url(baseUrl + "/mpesa/stkpushquery/v1/query")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + mpesaService.authenticate())
                .addHeader("cache-control", "no-cache")

                .build();

        Response response = client.newCall(request).execute();
        ResponseBody finalBody = response.body(); // Read response body
            if (finalBody == null) {
                throw new IOException("Response body is null");
            }
            return finalBody.string();
    }

    private boolean isSandbox() {
        return "sandbox".equalsIgnoreCase(config.getEnv());
    }
}
