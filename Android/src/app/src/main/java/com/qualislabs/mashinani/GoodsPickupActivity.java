package com.qualislabs.mashinani;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.qualislabs.mashinani.Common.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import life.sabujak.roundedbutton.RoundedButton;


public class GoodsPickupActivity extends AppCompatActivity {
    private TextInputEditText mTextInputEditTextFarmerDetails, mTextInputEditTextGoodsDetails;
    private RoundedButton mRoundedButtonProblem, mRoundedButtonPickup;
    private ProgressDialog mProgressDialog;
    private String mRequisitionId;
    private int mFarmerId;

    private Intent intentSuper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_pickup);

        intentSuper = getIntent();
        mRequisitionId = intentSuper.getStringExtra("requisitionId");

        mTextInputEditTextFarmerDetails = (TextInputEditText)findViewById(R.id.text_field_farmer_details);
        mTextInputEditTextGoodsDetails = (TextInputEditText)findViewById(R.id.text_field_goods_details);

        mRoundedButtonPickup = (RoundedButton)findViewById(R.id.button_problem_submit);
        mRoundedButtonProblem = (RoundedButton)findViewById(R.id.button_driver_report_problem);

        mProgressDialog = new ProgressDialog(GoodsPickupActivity.this,
                R.style.ProgressDialogStyle);

        mProgressDialog.setMessage("Fetching details...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        getGoodsDetails(mRequisitionId);

        //Todo enable this after a get route is created in the db
        //getFarmerDetails(mFarmerId);

        mRoundedButtonProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(GoodsPickupActivity.this,
                        DriverProblemReportActivity.class);
                intent.putExtra("requisitionId", mRequisitionId);
                startActivity(intent);
            }
        });

        mRoundedButtonPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    logPickup(mRequisitionId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getFarmerDetails(int farmerId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://169.57.99.69:30692/farmerRequisition/" + farmerId;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = response.getJSONObject("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (jsonObject != null){
                            try {
                                String farmerDetails = jsonObject.get("userName") + "\n" + jsonObject.get("email") + "\n";
                                mFarmerId = jsonObject.getInt("creatorId");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(GoodsPickupActivity.this,
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
    }

    private void getGoodsDetails(String requisitionId) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://169.57.99.69:30692/farmerRequisition/" + requisitionId;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, URL, null,
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
                            String goodsDetails = "";
                            try {
                                goodsDetails = jsonObject.get("quantity") + " bags of " + jsonObject.get("productType") + "\n";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mTextInputEditTextGoodsDetails.setText(goodsDetails);
                            mProgressDialog.dismiss();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(GoodsPickupActivity.this,
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
    }

    private void logPickup(String requisitionId) throws JSONException {

        mProgressDialog.setMessage("Please wait as we submit...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://169.57.99.69:30692/farmerRequisition/" + requisitionId;

        String json = "{ \"status\":\"complete\"}";
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
                            Toast.makeText(GoodsPickupActivity.this,
                                    "Logged successfully", Toast.LENGTH_LONG).show();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(GoodsPickupActivity.this,
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

        if (intentSuper.getBooleanExtra("lastItem", false))
        {
            RequestQueue requestQueueTrip = Volley.newRequestQueue(this);
            String URLTrip = "http://169.57.99.69:32390/trip/" + Common.tripID;

            JsonObjectRequest jsonObjReqTrip = new JsonObjectRequest(
                    Request.Method.PATCH, URLTrip, new JSONObject(json),
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
                                Toast.makeText(GoodsPickupActivity.this,
                                        "Trip Completed successfully", Toast.LENGTH_LONG).show();
                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgressDialog.dismiss();
                    Toast.makeText(GoodsPickupActivity.this,
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

            requestQueueTrip.add(jsonObjReqTrip);

        }

        mProgressDialog.dismiss();
        onBackPressed();
    }
}