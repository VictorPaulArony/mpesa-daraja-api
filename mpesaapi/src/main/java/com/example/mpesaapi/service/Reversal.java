package com.example.mpesaapi.service;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.mpesaapi.dto.ReversalDto;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Reversal {

    public String requestReversal(ReversalDto dto) throws IOException {
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("Initiator", dto.getInitiator());
        jsonObject.put("SecurityCredential", dto.getSecurityCredential());
        jsonObject.put("CommandID", dto.getCommandID());
        jsonObject.put("TransactionID", dto.getTransactionID());
        jsonObject.put("Amount",dto.getAmount());
        jsonObject.put("ReceiverParty", dto.getReceiverParty());
        jsonObject.put("RecieverIdentifierType", dto.getRecieverIdentifierType());
        jsonObject.put("QueueTimeOutURL", dto.getQueueTimeOutURL());
        jsonObject.put("ResultURL", dto.getResultURL());
        jsonObject.put("Remarks", dto.getRemarks());
        jsonObject.put("Occasion", dto.getOcassion());



        jsonArray.put(jsonObject);

        String requestJson=jsonArray.toString().replaceAll("[\\[\\]]","");
        System.out.println(requestJson);

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/safaricom/reversal/v1/request")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer xNA3e9KhKQ8qkdTxJJo7IDGkpFNV")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        return response.body().string();
    }
}
