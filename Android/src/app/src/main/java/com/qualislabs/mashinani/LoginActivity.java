package com.qualislabs.mashinani;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.qualislabs.mashinani.Common.Common;
import com.qualislabs.mashinani.Models.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import life.sabujak.roundedbutton.RoundedButton;

public class LoginActivity extends AppCompatActivity {

    private RoundedButton mRoundedButtonLogin;
    private MaterialEditText mMaterialEditTextEmail, mMaterialEditTextPassword;
    private TextView mTextViewGetStarted;
    private ProgressDialog mProgressDialog;
    private CheckBox mCheckBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRoundedButtonLogin = (RoundedButton) findViewById(R.id.button_login);
        mTextViewGetStarted = (TextView) findViewById(R.id.text_get_started);

        mCheckBoxRememberMe = (CheckBox)findViewById(R.id.checkbox_rememberme);

        Intent intent = getIntent();
        String userType = intent.getStringExtra("userType");

        mMaterialEditTextEmail = (MaterialEditText)findViewById(R.id.edit_login_email);
        mMaterialEditTextPassword = (MaterialEditText)findViewById(R.id.edit_login_password);


        mRoundedButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mMaterialEditTextEmail.getText().toString();
                String password = mMaterialEditTextPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,
                            "Kindly fill all details to proceed",Toast.LENGTH_LONG).show();
                }
                else if (!isValidEmailId(email))
                {
                    Toast.makeText(LoginActivity.this,
                            "Kindly enter a valid email",Toast.LENGTH_LONG).show();
                }
                else if ( password.length() < 8 ){
                    Toast.makeText(LoginActivity.this,
                            "Password cannot be less that 8 characters",Toast.LENGTH_LONG).show();
                }
                else {
                    mProgressDialog = new ProgressDialog(LoginActivity.this,
                            R.style.ProgressDialogStyle);

                    mProgressDialog.setMessage("Please wait while we sign you in...");
                    mProgressDialog.setCancelable(true);
                    mProgressDialog.show();

                    try {
                         loginUser(email, password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        mTextViewGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra("userType", userType);
                startActivity(intent);
            }
        });


    }

    private void loginUser(String email, String password) throws JSONException {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://6e2ebd754dc9.ngrok.io/login";

        final Map<String, String>[] authMap = new Map[]{new HashMap<>()};
        authMap[0].put("email", email);
        authMap[0].put("password", password);

        Gson gson = new Gson();
        String json = gson.toJson(authMap[0]);

        final Boolean[] authStatus = {false};
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, URL, new JSONObject(json),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            authStatus[0] = response.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (authStatus[0]) {
                            JSONObject userJson = null;
                            try {
                                userJson = response.getJSONObject("User");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ObjectMapper mapper = new ObjectMapper();
                            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                                    false);

                            User user = null;
                            try {
                                user = mapper.readValue(userJson.toString(), User.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (user != null) {
                                Common.currentUser = user;

                                if (mCheckBoxRememberMe.isChecked()) {
                                    try {
                                        rememberUser(userJson);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                                mProgressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this,
                                        HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        |Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                mProgressDialog.dismiss();;
                                Toast.makeText(LoginActivity.this, "Authentication Failed",
                                        Toast.LENGTH_LONG).show();
                            }

                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.dismiss();;
                        Toast.makeText(LoginActivity.this, "Authentication Failed",
                                Toast.LENGTH_LONG).show();
                }
        }) {
            //Request headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        requestQueue.add(jsonObjReq);
    }

    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private void rememberUser(JSONObject userJson) throws JSONException {
        SharedPreferences shared = getSharedPreferences("com.qualislabs.mashinani", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt("userID", userJson.getInt("id"));
        editor.putString("userName", userJson.getString("userName"));
        editor.putString("email", userJson.getString("email"));
        editor.putString("userType", userJson.getString("userType"));
        editor.putString("token", userJson.getString("token"));
        editor.commit();
    }

}