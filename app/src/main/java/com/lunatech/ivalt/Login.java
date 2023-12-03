package com.lunatech.ivalt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hbb20.CountryCodePicker;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Login extends AppCompatActivity {
    CountryCodePicker codePicker;
    Button submitBtn;
    TextView countryName;
    EditText phoneNumber;
    TextView countryCode;
    ImageView countryFlag;
    LinearLayout countryTab;
    Button terms;
    RequestQueue queue;
    Boolean isFirstLaunch = true;

    String API_URL = "https://api.ivalt.com";
    String X_API_KEY = "jMQKBdBFIJamh2TARfbUh3NG9h5Nl3mxa72VGfMl";

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        codePicker = findViewById(R.id.country_code);
        submitBtn = findViewById(R.id.submit);
        countryName = findViewById(R.id.countryNameEditText);
        countryCode = findViewById(R.id.countryCode);
        countryFlag = findViewById(R.id.flag);
        phoneNumber = findViewById(R.id.phone_number);
        countryTab = findViewById(R.id.countryTab);
        terms = findViewById(R.id.terms);

        submitBtn.setFocusable(true);
        submitBtn.setFocusableInTouchMode(true);
        submitBtn.requestFocus();
        countryName.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button_country_f));

        // Initialize Volley request queue
        queue = Volley.newRequestQueue(this);

        // Set listeners and actions
        setListeners();

        // Get saved mobile number if available
        getSavedMobile();
    }

    // Set listeners for various UI elements
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void setListeners() {

        submitBtn.setBackgroundColor(getResources().getColor(R.color.white));
        submitBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
        submitBtn.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button_auth_f));

        countryName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                codePicker.launchCountrySelectionDialog();
                countryName.setTextColor(getResources().getColor(R.color.black));
            }
        });

        codePicker.setOnCountryChangeListener(() -> {
            String countryCode = codePicker.getSelectedCountryCode();
            String countryName = codePicker.getSelectedCountryName();
            Login.this.countryName.setText(countryName);
            Login.this.countryCode.setText(String.format("+%s", countryCode));
            countryFlag.setImageResource(codePicker.getSelectedCountryFlagResourceId());

            // Focus on the phone number EditText
            Login.this.countryName.focusSearch(View.FOCUS_DOWN).requestFocus();
        });

        submitBtn.setOnClickListener(view -> {
            // Get the phone number
            String phone = countryCode.getText().toString() + phoneNumber.getText().toString();

            // Check if the phone number is empty
            if (phoneNumber.getText().toString().isEmpty()) {
                Toast.makeText(Login.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the phone number is less than 8 DC
            if (phoneNumber.getText().toString().length() < 8) {
                Toast.makeText(Login.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the phone number is greater than 15 DC
            if (phoneNumber.getText().toString().length() > 15) {
                Toast.makeText(Login.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the phone number contains only numbers
            if (!phoneNumber.getText().toString().matches("[0-9]+")) {
                Toast.makeText(Login.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Update UI
                submitBtn.setEnabled(false);
//                phoneNumber.requestFocus();
                submitBtn.setText("Please wait...");

                String loginURL = API_URL + "/biometric-auth-request";

                // Generating POST Data
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("mobile", phone);
                jsonBody.put("requestFrom", "iVALT TV");
                final String requestBody = jsonBody.toString();

                // Adding headers
                Map<String, String> headers = new ArrayMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-key", X_API_KEY);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, loginURL, response -> {
                    submitBtn.setEnabled(true);
                    if (isFirstLaunch) {
                        submitBtn.setText("Register");
                    } else {
                        submitBtn.setText("Sign In");
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        try {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String message = data.getString("message");
                            if (message.equals("Biometric Auth Request successfully sent.")) {

                                if (!isFirstLaunch) {
                                    Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                                }

                                Intent intent = new Intent(Login.this, Verify.class);
                                intent.putExtra("phone", phone);
                                intent.putExtra("country_code", countryCode.getText().toString());
                                intent.putExtra("mobile", phoneNumber.getText().toString());
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            JSONObject error = jsonObject.getJSONObject("error");
                            String errorS = error.getString("detail");

                            Toast.makeText(Login.this, errorS, Toast.LENGTH_SHORT).show();
                            submitBtn.setEnabled(true);

                            if (isFirstLaunch) {
                                submitBtn.setText("Register");
                            } else {
                                submitBtn.setText("Sign In");
                            }
                            countryName.setEnabled(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        submitBtn.setEnabled(true);
                        submitBtn.setText("Continue");
                        countryName.setEnabled(true);
                    }
                }, error -> {
                    submitBtn.setEnabled(true);
                    if (isFirstLaunch) {
                        submitBtn.setText("Register");
                    } else {
                        submitBtn.setText("Sign In");
                    }

                    countryName.setEnabled(true);

                    try {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        JSONObject data = new JSONObject(responseBody);
                        JSONObject error1 = data.getJSONObject("error");
                        String errorS = error1.getString("detail");
                        Toast.makeText(Login.this, errorS, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }) {
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
                submitBtn.setEnabled(true);
                if (isFirstLaunch) {
                    submitBtn.setText("Register");
                } else {
                    submitBtn.setText("Sign In");
                }
                e.printStackTrace();
            }
        });

        submitBtn.setOnFocusChangeListener((view, b) -> {
            if (b) {
                submitBtn.setBackgroundColor(getResources().getColor(R.color.white));
                submitBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                submitBtn.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button_auth_f));
            } else {
                submitBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                submitBtn.setTextColor(getResources().getColor(R.color.white));
                submitBtn.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button_auth));
            }
        });

        // Change text color on focus change
        terms.setOnFocusChangeListener((view, b) -> changeTextColorOnFocusChange(terms, b));

        // Open terms and conditions activity
        terms.setOnClickListener(v -> openTermsAndConditionsActivity());
    }

    @SuppressLint("SetTextI18n")
    private void getSavedMobile() {
        // get the saved mobile and country code
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.lunatech.ivalt", Context.MODE_PRIVATE);
        String mobile = sharedPreferences.getString("mobile", "");
        String countryCode = sharedPreferences.getString("country_code", "");
        if (!mobile.isEmpty() && !countryCode.isEmpty()) {
            submitBtn.setText("Sign In");
            phoneNumber.setText(mobile);
            this.countryCode.setText(countryCode);
            phoneNumber.setEnabled(false);
            countryName.setEnabled(false);
            countryTab.setVisibility(View.GONE);
            isFirstLaunch = true;
        }
    }

    // Method to open terms and conditions activity
    private void openTermsAndConditionsActivity() {
        Intent intent = new Intent(Login.this, TermsConditions.class);
        startActivity(intent);
    }

    // Method to change text color based on focus change
    private void changeTextColorOnFocusChange(View view, boolean b) {
        if (b) {
            ((TextView) view).setTextColor(getResources().getColor(R.color.focusedColor));
        } else {
            ((TextView) view).setTextColor(getResources().getColor(R.color.unFocusedColor));
        }
    }
}