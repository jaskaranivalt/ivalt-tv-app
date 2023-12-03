package com.lunatech.ivalt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArrayMap;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Verify extends AppCompatActivity {

    String API_URL = "https://api.ivalt.com";
    String X_API_KEY = "jMQKBdBFIJamh2TARfbUh3NG9h5Nl3mxa72VGfMl";

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        queue = Volley.newRequestQueue(this);
        verify();
    }

    public void saveMobile(String mobile, String country_code) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.lunatech.ivalt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mobile", mobile);
        editor.putString("country_code", country_code);
        editor.apply();
    }

    public void checkMobile(String msg) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.lunatech.ivalt", Context.MODE_PRIVATE);
        String mobile = sharedPreferences.getString("mobile", "");
        String country_code = sharedPreferences.getString("country_code", "");

        if (!mobile.isEmpty() && !country_code.isEmpty()) {
            Toast.makeText(Verify.this, msg, Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(Verify.this, Home.class);
            homeIntent.putExtra("phone", mobile);
            startActivity(homeIntent);
            finish();
        } else {
            Intent intent = getIntent();
            String country_code1 = intent.getStringExtra("country_code");
            String mobile1 = intent.getStringExtra("mobile");
            saveMobile(mobile1, country_code1);
            Intent intent1 = new Intent(Verify.this, Login.class);
            startActivity(intent1);
            finish();
        }
    }

    private void verify() {
        new Handler().postDelayed(() -> {

            Intent intent = getIntent();
            String phone = intent.getStringExtra("phone");

            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("mobile", phone);
                jsonBody.put("requestFrom", "iVALT TV");
                final String requestBody = jsonBody.toString();

                Map<String, String> headers = new ArrayMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-key", X_API_KEY);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL + "/biometric-auth-result", response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject data = jsonObject.getJSONObject("data");
                        String message = data.getString("message");
                        boolean status = data.getBoolean("status");
                        if (status) {
                            checkMobile(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> verify()) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return requestBody.getBytes(StandardCharsets.UTF_8);
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return headers;
                    }
                };
                queue.add(stringRequest);
            } catch (Exception e) {
                e.printStackTrace();
                verify();
            }
        }, 3000); // Delay set to 3000 milliseconds (3 seconds)
    }
}