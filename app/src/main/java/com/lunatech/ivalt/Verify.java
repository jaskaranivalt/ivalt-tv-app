package com.lunatech.ivalt;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class Verify extends AppCompatActivity {
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        queue = Volley.newRequestQueue(this);
//        call the function
        verify();
    }

    public void saveMobile(String mobile, String country_code) {

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.lunatech.ivalt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mobile", mobile);
        editor.putString("country_code", country_code);
        editor.apply();
//        if saved log.d
        Log.d(TAG,"mobile"+mobile);
        Log.d(TAG,"country_code"+country_code);
    }
//    CHECK IF mobile data is saved in shared preferences
    public boolean checkMobile(String msg) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.lunatech.ivalt", Context.MODE_PRIVATE);
        String mobile = sharedPreferences.getString("mobile", "");
        String country_code = sharedPreferences.getString("country_code", "");
        if (mobile != "" && country_code != "") {
            Toast.makeText(Verify.this, msg, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Verify.this, Home.class);
            intent.putExtra("phone", mobile);
            startActivity(intent);
            finish();
            return true;
        }
        else{
            Intent intent = getIntent();
            String country_code1 = intent.getStringExtra("country_code");
            String mobile1 = intent.getStringExtra("mobile");
            saveMobile(mobile1,country_code1);
            Intent intent1 = new Intent(Verify.this, Login.class);
            startActivity(intent1);
            finish();

            return false;
        }
    }

    private void verify() {
//        set a loop to check every 5 seconds
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String verifyURL = null;
                        Intent intent = getIntent();
                        String country_code = intent.getStringExtra("country_code");
                        String mobile = intent.getStringExtra("mobile");
                        try {
                            String phone = intent.getStringExtra("phone");
                            verifyURL = "https://api.ivalt.com/biometric-auth-result";
                            JSONObject jsonBody = new JSONObject();
                            jsonBody.put("mobile", phone);
                            final String requestBody = jsonBody.toString();
//                    add headers
                            Map<String, String> headers = new ArrayMap<String, String>();
                            headers.put("Content-Type", "application/json");
                            headers.put("x-api-key", "jMQKBdBFIJamh2TARfbUh3NG9h5Nl3mxa72VGfMl");

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, verifyURL,

                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            Log.i("VOLLEY", response);
                                            // Display the first 500 characters of the response string.
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(response);
//                                        response is in form {"data":{"status":true,"message":"Biometric Auth Request successfully sent.","details":null},"error":null,"debug":{"timestamp":"2023-11-10T17:28:37.471373Z","activityId":"7VunVYQE6waUtuu8DL27KfR9uERIQcMe"}}
                                                JSONObject data = jsonObject.getJSONObject("data");
                                                String message = data.getString("message");
                                                Boolean status = data.getBoolean("status");
                                                if (status) {
                                                   checkMobile(message);

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();

                                            }


                                        }

                                    }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {

//                                    Toast.makeText(Verify.this, "Trying Again", Toast.LENGTH_SHORT).show();
                                    verify();

                                }
                            }) {
                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=utf-8";
                                }

                                @Override
                                public byte[] getBody() throws AuthFailureError {
                                    try {
                                        return requestBody.getBytes("utf-8");
                                    } catch (UnsupportedEncodingException uee) {
                                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                        return null;
                                    }
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
                    }


    }, 10000);
    }
}