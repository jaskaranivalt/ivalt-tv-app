package com.lunatech.ivalt;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class TermsConditions extends AppCompatActivity {
    WebView fullWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        fullWeb = findViewById(R.id.webView);
        fullWeb.loadUrl("https://www.termsandconditionsgenerator.com/live.php?token=kINwKiM9dTaxncEOk1U3xm84LWA7JWt8");
    }
}