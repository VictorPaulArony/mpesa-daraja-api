package com.example.mpesaapi.service;

import java.io.IOException;
import java.util.Base64;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.mpesaapi.config.MpesaConfigProperties;

import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Service
@Data
public class MpesaService {

    private final MpesaConfigProperties config;
    private String accessToken;
    private long tokenExpiryTime;
    private static final String SANDURL = "https://sandbox.safaricom.co.ke";
    private static final String PRODURL = "https://api.safaricom.co.ke";
    private static final String AUTH = "authorization";

    public MpesaService(MpesaConfigProperties config) {
        this.config = config;
        this.accessToken = null;
        this.tokenExpiryTime = 0;
    }

    public String getAppKey() {
        return config.getAppKey();
    }

    public String getAppSecret() {
        return config.getAppSecret();
    }

    public String getBusinessShortcode() {
        return config.getBusinessShortcode();
    }

    public String getPasskey() {
        return config.getPasskey();
    }

    public String getCallbackUrl() {
        return config.getCallbackUrl();
    }

    public String getTimeoutUrl() {
        return config.getTimeoutUrl();
    }

    public synchronized String authenticate() throws IOException {
        // Check if token is still valid (expires in 1 hour)
        if (accessToken != null && System.currentTimeMillis() < tokenExpiryTime) {
            return accessToken;
        }

        String appKeySecret = config.getAppKey() + ":" + config.getAppSecret();

        byte[] bytes = appKeySecret.getBytes("ISO-8859-1");
        String encoded = Base64.getEncoder().encodeToString(bytes);

        String baseUrl = isSandbox() ? SANDURL : PRODURL;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(baseUrl + "/oauth/v1/generate?grant_type=client_credentials")
                .get()
                .addHeader(AUTH, "Basic " + encoded)
                .addHeader("cache-control", "no-cache")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                throw new IOException("Authentication failed. Code: " + response.code() +
                        ", Message: " + response.message() +
                        ", Body: " + errorBody);
            }

            ResponseBody finalBody = response.body(); // Read response body
            if (finalBody == null) {
                throw new IOException("Response body is null");
            }
            String responseBody = finalBody.string();
            JSONObject jsonObject = new JSONObject(responseBody);

            if (jsonObject.has("access_token")) {
                accessToken = jsonObject.getString("access_token");
                tokenExpiryTime = System.currentTimeMillis() + 3600000;
                return accessToken;
            } else {
                throw new IOException("Authentication failed. Response: " + responseBody);
            }
        }
    }

    private boolean isSandbox() {
        return "sandbox".equalsIgnoreCase(config.getEnv());
    }

}