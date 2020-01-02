package com.jwong.education.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jwong.education.R;
import com.jwong.education.util.Utils;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ((TextView) findViewById(R.id.tv_version)).setText(Utils.getVersionName(getApplicationContext()));

        ((TextView) findViewById(R.id.tv_copyright))
                .setText(getString(R.string.copyright, getString(R.string.company)));

        findViewById(android.R.id.icon).postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 800);
    }


}