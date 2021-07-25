package com.qualislabs.mashinani;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import life.sabujak.roundedbutton.RoundedButton;

public class SignupActivity extends AppCompatActivity {

    private MaterialEditText mMaterialEditTextUserName, mMaterialEditTextEmail,
            mMaterialEditTextPassword, mMaterialEditTextConfirmPassword;
    private RoundedButton mRoundedButtonSignUp;
    private TextView mTextViewAccount;
    private ProgressDialog mProgressDialog;

    private String mUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Intent intent = getIntent();
        mUserType = intent.getStringExtra("userType");

        mMaterialEditTextUserName = (MaterialEditText)findViewById(R.id.edit_signup_name);
        mMaterialEditTextEmail = (MaterialEditText)findViewById(R.id.edit_signup_email);
        mMaterialEditTextPassword = (MaterialEditText)findViewById(R.id.edit_signup_password);
        mMaterialEditTextConfirmPassword = (MaterialEditText)
                findViewById(R.id.edit_signup_confirm_password);

        mRoundedButtonSignUp = (RoundedButton) findViewById(R.id.button_signup);

        mTextViewAccount = (TextView)findViewById(R.id.edit_signup_confirm_password);

        mTextViewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mRoundedButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mMaterialEditTextUserName.getText().toString();
                String email = mMaterialEditTextEmail.getText().toString().trim();
                String password = mMaterialEditTextPassword.getText().toString();
                String confirmPassword = mMaterialEditTextConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword))
                {
                    Toast.makeText(SignupActivity.this,
                            "Kindly fill all details to proceed",Toast.LENGTH_LONG).show();
                }
                else if (!isValidEmailId(email))
                {
                    Toast.makeText(SignupActivity.this,
                            "Kindly enter a valid email",Toast.LENGTH_LONG).show();
                }
                else if (password.length() < 8){
                    Toast.makeText(SignupActivity.this,
                            "Password cannot be less that 8 characters",Toast.LENGTH_LONG).show();
                }
                else if (!password.equals(confirmPassword))
                {
                    Toast.makeText(SignupActivity.this,
                            "Passwords do not match",Toast.LENGTH_LONG).show();
                }
                else
                {
                    mProgressDialog = new ProgressDialog(SignupActivity.this,
                            R.style.ProgressDialogStyle);

                    mProgressDialog.setMessage("Please wait while we create your account...");
                    mProgressDialog.setCancelable(true);
                    mProgressDialog.show();

                    try {
                        registerUser(userName, email, password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



    }

    private void registerUser(String userName, String email, String password) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://68012ddb0cfb.ngrok.io/register";

        final Map<String, String> registerMap = new HashMap<>();
        registerMap.put("userName", userName);
        registerMap.put("email", email);
        registerMap.put("password", password);
        registerMap.put("userType", mUserType);

        Gson gson = new Gson();
        String json = gson.toJson(registerMap);

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

                                mProgressDialog.dismiss();
                                Intent intent = new Intent(SignupActivity.this,
                                        HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        |Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                mProgressDialog.dismiss();;
                                Toast.makeText(SignupActivity.this,
                                        "Registration Failed",
                                        Toast.LENGTH_LONG).show();
                            }

                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.dismiss();
                        JSONObject jsonBody;

                        if(error.networkResponse.data!=null) {
                            try {
                                String body = new String(error.networkResponse.data,"UTF-8");
                                jsonBody = new JSONObject(body);

                                Toast.makeText(SignupActivity.this, jsonBody.getString("error") ,
                                        Toast.LENGTH_LONG).show();
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
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

}