package com.redbravo.inventorylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StarterPage extends AppCompatActivity {
private static int timeout = 6000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter_page);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StarterPage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, timeout);
    }
}