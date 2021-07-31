package com.qualislabs.mashinani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.qualislabs.mashinani.Common.Common;
import com.qualislabs.mashinani.Models.User;

import life.sabujak.roundedbutton.RoundedButton;

public class IntroActivity extends AppCompatActivity {
    RoundedButton mRoundedButtonFarmer, mRoundedButtonDriver, mRoundedButtonTrader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        SharedPreferences shared = getSharedPreferences("com.qualislabs.mashinani", Context.MODE_PRIVATE);
        if(shared.contains("userID") && shared.contains("userName") && shared.contains("email")
                && shared.contains("userType") && shared.contains("token")){

            int userId = shared.getInt("userID", 1);
            String userName = shared.getString("userName", "");
            String email = shared.getString("email", "");
            String userType = shared.getString("userType", "");
            String token = shared.getString("token", "");

            Common.currentUser = new User(userName, email, userType, token, userId);
            Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        mRoundedButtonFarmer = (RoundedButton) findViewById(R.id.button_farmer);
        mRoundedButtonDriver = (RoundedButton) findViewById(R.id.button_driver);
        mRoundedButtonTrader = (RoundedButton) findViewById(R.id.button_driver_report_problem);

        mRoundedButtonFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                intent.putExtra("userType", "farmer");
                startActivity(intent);
            }
        });

        mRoundedButtonDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                intent.putExtra("userType", "driver");
                startActivity(intent);
            }
        });

        mRoundedButtonTrader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                intent.putExtra("userType", "trader");
                startActivity(intent);
            }
        });

    }

}