package com.qualislabs.mashinani;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qualislabs.mashinani.Common.Common;
import com.qualislabs.mashinani.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import life.sabujak.roundedbutton.RoundedButton;

public class DriverProblemReportActivity extends AppCompatActivity {

    private TextInputEditText mTextInputEditTextProblem;
    private RoundedButton mRoundedButtonSubmit;
    private RatingBar mRatingBarProblem;
    private ProgressDialog mProgressDialog;
    private String mRequisitionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_problem_report);

        Intent intent = getIntent();
        mRequisitionId = intent.getStringExtra("requisitionId");

        mProgressDialog = new ProgressDialog(DriverProblemReportActivity.this,
                R.style.ProgressDialogStyle);

        mTextInputEditTextProblem = (TextInputEditText)findViewById(R.id.text_field_problem);
        mRoundedButtonSubmit = (RoundedButton)findViewById(R.id.button_problem_submit);
        mRatingBarProblem = (RatingBar)findViewById(R.id.rating_bar_driver_problem_severity);

        mRoundedButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String problem = mTextInputEditTextProblem.getText().toString();
                int rating = (int) mRatingBarProblem.getRating();

                if (TextUtils.isEmpty(problem) || rating == 0){
                    Toast.makeText(DriverProblemReportActivity.this,
                            "Kindly fill all details to proceed",Toast.LENGTH_LONG).show();
                } else {
                    try {
                        submitProblem(mRequisitionId, problem, rating);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void submitProblem(String requisitionId, String details, int rating) throws JSONException {

        mProgressDialog.setMessage("Please wait as we submit...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://169.57.99.69:30692/farmerRequisition/" + requisitionId;

        String json = "{ \"status\":\"problem\"}";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PATCH, URL, new JSONObject(json),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = response.getJSONObject("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (jsonObject != null) {

                            JSONObject problemJson = null;
                            try {
                                problemJson = new JSONObject()
                                        .put("problemId", Integer.parseInt(requisitionId))
                                        .put("rating", rating)
                                        .put("details", details);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            RequestQueue requestQueueProblem = Volley.newRequestQueue(DriverProblemReportActivity.this);
                            String URLProblem = "http://169.57.99.69:31194/problem";

                            JsonObjectRequest jsonObjReqProblem = new JsonObjectRequest(
                                    Request.Method.POST, URLProblem, problemJson,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                            JSONObject jsonObjectResponse = null;
                                            try {
                                                jsonObjectResponse = response.getJSONObject("data");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            if (jsonObjectResponse != null) {
                                                Toast.makeText(DriverProblemReportActivity.this,
                                                        "Submitted successfully", Toast.LENGTH_LONG).show();
                                                onBackPressed();
                                            }

                                        }

                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(DriverProblemReportActivity.this,
                                            "Unknown error occurred. Please try again",
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
                            requestQueueProblem.add(jsonObjReqProblem);

                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(DriverProblemReportActivity.this,
                        "Unknown error occurred. Please try again",
                        Toast.LENGTH_LONG).show();
            }
        }) {
            //Request headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
//                headers.put("Bearer ", Common.currentUser.getToken());
                return headers;
            }

        };

        requestQueue.add(jsonObjReq);
        mProgressDialog.dismiss();
    }
}