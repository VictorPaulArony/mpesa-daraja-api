package com.example.mpesaapi.service;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.mpesaapi.config.MpesaConfigProperties;
import com.example.mpesaapi.dto.BalanceInquiryDto;
import org.springframework.stereotype.Service;
import lombok.Data;

import okhttp3.*;

@Data
@Service
public class BalanceInquiry {

    private final MpesaService mpesaService; // Service to handle M-Pesa authentication
    private static final String SANDURL = "https://sandbox.safaricom.co.ke";
    private static final String PRODURL = "https://api.safaricom.co.ke";
    private final MpesaConfigProperties config;

    public String requestBalanceInquiry(BalanceInquiryDto dto) throws IOException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Initiator", dto.getInitiator());
        jsonObject.put("SecurityCredential", dto.getSecurityCredential());
        jsonObject.put("CommandID", dto.getCommandID());
        jsonObject.put("PartyA", dto.getPartyA());
        jsonObject.put("IdentifierType", dto.getIdentifierType());
        jsonObject.put("Remarks", dto.getRemarks());
        jsonObject.put("QueueTimeOutURL", dto.getQueueTimeOutURL());
        jsonObject.put("ResultURL", dto.getResultURL());

        jsonArray.put(jsonObject);

        String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

        String baseUrl = isSandbox() ? SANDURL : PRODURL;
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);
        Request request = new Request.Builder()
                .url(baseUrl + "/safaricom/accountbalance/v1/query")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + mpesaService.authenticate())
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "2aa448be-7d56-a796-065f-b378ede8b136")
                .build();

        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            throw new IOException("Response body is null");
        }
        return responseBody.string();
    }

    private boolean isSandbox() {
        return "sandbox".equalsIgnoreCase(config.getEnv());
    }
}
