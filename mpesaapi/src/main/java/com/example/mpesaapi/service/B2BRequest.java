package com.example.mpesaapi.service;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.mpesaapi.dto.B2BRequestDto;

import lombok.Data;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Data
@Service
public class B2BRequest {

    private final MpesaService mpesaService; // Service to handle M-Pesa authentication

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
        System.out.println(requestJson);

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/safaricom/b2b/v1/paymentrequest")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + mpesaService.authenticate())
                .addHeader("cache-control", "no-cache")

                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
