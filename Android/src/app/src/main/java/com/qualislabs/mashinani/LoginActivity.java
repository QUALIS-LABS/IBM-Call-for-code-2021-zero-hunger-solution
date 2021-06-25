package com.qualislabs.mashinani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import life.sabujak.roundedbutton.RoundedButton;

public class LoginActivity extends AppCompatActivity {

    RoundedButton mRoundedButtonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRoundedButtonLogin = (RoundedButton) findViewById(R.id.button_login);
        mRoundedButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}