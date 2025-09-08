package com.example.mpesaapi.service;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.mpesaapi.dto.RegisterURLDto;

import lombok.RequiredArgsConstructor;
import okhttp3.*;

@RequiredArgsConstructor
public class RegisterURL {

    private final MpesaService mpesaService; // Service to handle M-Pesa authentication

    public String registerURL(RegisterURLDto dto) throws IOException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ShortCode", dto.getShortCode());
        jsonObject.put("ResponseType", dto.getResponseType());
        jsonObject.put("ConfirmationURL", dto.getConfirmationURL());
        jsonObject.put("ValidationURL", dto.getValidationURL());

        jsonArray.put(jsonObject);

        String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");
        System.out.println(requestJson);

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/mpesa/c2b/v1/registerurl")
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
