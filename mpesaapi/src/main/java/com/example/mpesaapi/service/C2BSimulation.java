package com.example.mpesaapi.service;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.mpesaapi.dto.C2BSimulationDto;


import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@RequiredArgsConstructor
public class C2BSimulation {

    private final MpesaService mpesaService; // Service to handle M-Pesa authentication

    public String sendC2BSimulation(C2BSimulationDto dto) throws IOException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ShortCode", dto.getShortCode());
        jsonObject.put("CommandID", dto.getCommandID());
        jsonObject.put("Amount", dto.getAmount());
        jsonObject.put("Msisdn", dto.getMSISDN());
        jsonObject.put("BillRefNumber", dto.getBillRefNumber());

        jsonArray.put(jsonObject);

        String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");
        System.out.println(requestJson);
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/safaricom/c2b/v1/simulate")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + mpesaService.authenticate())
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        return response.body().string();
    }
}
