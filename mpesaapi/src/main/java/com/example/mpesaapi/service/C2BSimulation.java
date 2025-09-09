package com.example.mpesaapi.service;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.mpesaapi.config.MpesaConfigProperties;
import com.example.mpesaapi.dto.C2BSimulationDto;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@RequiredArgsConstructor
@Service
public class C2BSimulation {

    private final MpesaService mpesaService; // Service to handle M-Pesa authentication
    private static final String SANDURL = "https://sandbox.safaricom.co.ke";
    private static final String PRODURL = "https://api.safaricom.co.ke";
    private final MpesaConfigProperties config;

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

        String baseUrl = isSandbox() ? SANDURL : PRODURL;
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJson);
        Request request = new Request.Builder()
                .url(baseUrl + "/safaricom/c2b/v1/simulate")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + mpesaService.authenticate())
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        ResponseBody finalResponse = response.body();
        if (finalResponse == null) {
            throw new IOException("Response body is null");
        }
        return finalResponse.string();
    }

    private boolean isSandbox() {
        return "sandbox".equalsIgnoreCase(config.getEnv());
    }
}
