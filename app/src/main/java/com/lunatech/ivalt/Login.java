package com.lunatech.ivalt;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

public class Login extends AppCompatActivity {
    CountryCodePicker codePicker;
    Button submit;
    TextView country_name;
    EditText phone_number;
    TextView country_code;
    ImageView country_flag;
    LinearLayout countryTab;
    Button terms;
    RequestQueue queue;
    Boolean isFirstLaunch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        codePicker=findViewById(R.id.country_code);
        submit=findViewById(R.id.submit);
        country_name =  findViewById(R.id.countryNameEditText);
        country_code = findViewById(R.id.countryCode);
        country_flag = findViewById(R.id.flag);
        phone_number = findViewById(R.id.phone_number);
        queue = Volley.newRequestQueue(this);
        countryTab = findViewById(R.id.countryTab);
        submit.setFocusable(true);
        submit.setFocusableInTouchMode(true);
        submit.requestFocus();
        country_name.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button_country_f));
        getSavedMobile();
//        add borders on right side of country name

        country_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               if (hasFocus) {
                  codePicker.launchCountrySelectionDialog();
                  country_name.setTextColor(getResources().getColor(R.color.black));
               }
            }
        });

        submit.setBackgroundColor(getResources().getColor(R.color.white));
        submit.setTextColor(getResources().getColor(R.color.colorPrimary));
        submit.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button_auth_f));
        codePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                String countryCode=codePicker.getSelectedCountryCode();
                String countryName=codePicker.getSelectedCountryName();
                country_name.setText(countryName);
                country_code.setText("+"+countryCode);
                country_flag.setImageResource(codePicker.getSelectedCountryFlagResourceId());


//                moving to next edit text
                country_name.focusSearch(View.FOCUS_DOWN).requestFocus();

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                
                String phone = country_code.getText().toString() + phone_number.getText().toString();

                if (phone_number.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(phone_number.getText().toString().length() < 8) {
                    Toast.makeText(Login.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phone_number.getText().toString().length() >15) {
                    Toast.makeText(Login.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
//                check if phone_number contains only numbers
                if(!phone_number.getText().toString().matches("[0-9]+")){
                    Toast.makeText(Login.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
//                send a request to the server
                String loginURL = null;
                try{
                    submit.setEnabled(false);
                    phone_number.requestFocus();
                    submit.setText("Please wait...");
                    loginURL = "https://api.ivalt.com/biometric-auth-request";
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("mobile", phone);
                    final String requestBody = jsonBody.toString();
//                    add headers
                    Map<String, String> headers = new ArrayMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("x-api-key", "jMQKBdBFIJamh2TARfbUh3NG9h5Nl3mxa72VGfMl");

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, loginURL,

                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    submit.setEnabled(true);
                                    if(isFirstLaunch){
                                        submit.setText("Register");
                                    }
                                    else{
                                        submit.setText("Sign In");
                                    }
                                    Log.i("VOLLEY", response);
                                    // Display the first 500 characters of the response string.
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response);

                                        try{
                                            JSONObject data = jsonObject.getJSONObject("data");
                                            String message = data.getString("message");
                                            if (message.equals("Biometric Auth Request successfully sent.")) {
                                                if(!isFirstLaunch){
                                                    Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                                                }

                                                Intent intent = new Intent(Login.this, Verify.class);
                                                intent.putExtra("phone", phone);
                                                intent.putExtra("country_code", country_code.getText().toString());
                                                intent.putExtra("mobile", phone_number.getText().toString());
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            JSONObject error = jsonObject.getJSONObject("error");
                                            String errorS = error.getString("detail");

                                            Toast.makeText(Login.this, errorS, Toast.LENGTH_SHORT).show();
                                            submit.setEnabled(true);
                                            if(isFirstLaunch){
                                                submit.setText("Register");
                                            }
                                            else{
                                                submit.setText("Sign In");
                                            }
                                            country_name.setEnabled(true);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        submit.setEnabled(true);
                                        submit.setText("Continue");
                                        country_name.setEnabled(true);
                                    }


                                }

                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            submit.setEnabled(true);
                            if(isFirstLaunch){
                                submit.setText("Register");
                            }
                            else{
                                submit.setText("Sign In");
                            }
                            country_name.setEnabled(true);

//                            on error we receive {"data":null,"error":{"type":"https:\/\/httpstatuses.com\/404","title":"Not Found","status":404,"detail":"The mobile number provided (+4915783502811) was not found.","instance":"\/services\/ivalt_apis\/ivalt_laravel_api\/public\/api\/send\/global\/notification"},"debug":{"timestamp":"2023-11-11T10:48:54.180105Z","activityId":"Z8UUwZ86nf1Y8Wh3GFeelpbHR7crvHgM"}}
//                            i want to display the detail
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                JSONObject error1 = data.getJSONObject("error");
                                String errorS = error1.getString("detail");
                                Toast.makeText(Login.this, errorS, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
                }

                catch (Exception e){
                    submit.setEnabled(true);
                    if(isFirstLaunch){
                        submit.setText("Register");
                    }
                    else{
                        submit.setText("Sign In");
                    }
                    e.printStackTrace();
                }

// Add the request to the RequestQueue.

//
            }
        });

//        terms when focused create border
//        terms when not focused remove border
//        terms when clicked open terms and conditions page
        submit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                            @Override
                                            public void onFocusChange(View view, boolean b) {
                                                if (b) {
                                                    submit.setBackgroundColor(getResources().getColor(R.color.white));
                                                    submit.setTextColor(getResources().getColor(R.color.colorPrimary));
                                                    submit.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button_auth_f));
                                                } else {
                                                    submit.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                                    submit.setTextColor(getResources().getColor(R.color.white));
                                                    submit.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button_auth));



                                            }
//                                                add border radius

                                        }
        });
        terms = findViewById(R.id.terms);
        terms.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
//                    set text Color to blue
                    terms.setTextColor(getResources().getColor(R.color.focusedColor));
                }
                else{
                    terms.setTextColor(getResources().getColor(R.color.unFocusedColor));
                }
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, TermsConditions.class);
                startActivity(intent);

            }
        });


    }

    private void getSavedMobile() {
        // get the saved mobile and country code
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.lunatech.ivalt", Context.MODE_PRIVATE);
        String mobile = sharedPreferences.getString("mobile", "");
        String countryCode = sharedPreferences.getString("country_code", "");
        Log.d(TAG, "getSavedMobile: " + mobile + " " + countryCode );
        if (!mobile.isEmpty() && !countryCode.isEmpty()) {
            submit.setText("Sign In");
            phone_number.setText(mobile);
            country_code.setText(countryCode);
            phone_number.setEnabled(false);
            country_name.setEnabled(false);
            countryTab.setVisibility(View.GONE);
            isFirstLaunch = false;

        }
    }
}