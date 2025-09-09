package com.example.mpesaapi.service;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.mpesaapi.config.MpesaConfigProperties;
import com.example.mpesaapi.dto.RegisterURLDto;

import lombok.RequiredArgsConstructor;
import okhttp3.*;

@RequiredArgsConstructor
@Service
public class RegisterURL {

    private final MpesaService mpesaService; // Service to handle M-Pesa authentication
    private static final String SANDURL = "https://sandbox.safaricom.co.ke";
    private static final String PRODURL = "https://api.safaricom.co.ke";
    private final MpesaConfigProperties config;

    public String registerURL(RegisterURLDto dto) throws IOException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ShortCode", dto.getShortCode());
        jsonObject.put("ResponseType", dto.getResponseType());
        jsonObject.put("ConfirmationURL", dto.getConfirmationURL());
        jsonObject.put("ValidationURL", dto.getValidationURL());

        jsonArray.put(jsonObject);

        String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");
        String baseUrl = isSandbox() ? SANDURL : PRODURL;
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);
        Request request = new Request.Builder()
                .url(baseUrl + "/mpesa/c2b/v1/registerurl")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + mpesaService.authenticate())
                .addHeader("cache-control", "no-cache")
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
