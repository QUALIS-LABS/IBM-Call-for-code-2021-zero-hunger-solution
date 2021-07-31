package com.qualislabs.mashinani;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.qualislabs.mashinani.Common.Common;
import com.rengwuxian.materialedittext.MaterialEditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import life.sabujak.roundedbutton.RoundedButton;


public class FarmerRequisitionActivity extends AppCompatActivity {
    private MaterialEditText mMaterialEditTextDate, mMaterialEditTextBags, mMaterialEditTextPickupLocation;
    private TextInputEditText mTextInputEditTextInstructions;
    private Spinner mSpinnerProduce;
    private Calendar mCalendar;
    private RoundedButton mRoundedButton;
    private ProgressDialog mProgressDialog;

    private static final int PLACE_PICKER_REQUEST = 2762;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_requisition);

        mCalendar = Calendar.getInstance();

        mMaterialEditTextBags = (MaterialEditText)findViewById(R.id.edit_trader_produce_quantity);
        mMaterialEditTextDate = (MaterialEditText)findViewById(R.id.edit_delivery_date);
        mMaterialEditTextPickupLocation = (MaterialEditText)findViewById(R.id.edit_pickup_location);
        mSpinnerProduce = (Spinner)findViewById(R.id.spinner_trader_produce);
        mTextInputEditTextInstructions = (TextInputEditText) findViewById(R.id.edit_instructions);

        mRoundedButton = (RoundedButton)findViewById(R.id.button_farmer_requisition);
        

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateUIDate();
            }

        };

        mMaterialEditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(FarmerRequisitionActivity.this, date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));

                Calendar calendar = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis() + 3600000);
                datePickerDialog.show();
            }
        });

        mRoundedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String produce = mSpinnerProduce.getSelectedItem().toString();
                String bags = mMaterialEditTextBags.getText().toString();
                String date = mMaterialEditTextDate.getText().toString();
                String instructions = mTextInputEditTextInstructions.getText().toString();
                String latLong = mMaterialEditTextPickupLocation.getText().toString();

                if (TextUtils.isEmpty(produce) || TextUtils.isEmpty(date) || TextUtils.isEmpty(bags)
                        || TextUtils.isEmpty(instructions) || TextUtils.isEmpty(latLong)){
                    Toast.makeText(FarmerRequisitionActivity.this,
                            "Kindly fill all details to proceed", Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(bags) < 1){
                    Toast.makeText(FarmerRequisitionActivity.this,
                            "Number of bags must be greater than 0", Toast.LENGTH_LONG).show();
                }
                else {
                    mProgressDialog = new ProgressDialog(FarmerRequisitionActivity.this,
                            R.style.ProgressDialogStyle);

                    mProgressDialog.setMessage("Please wait as we submit your request");
                    mProgressDialog.setCancelable(true);
                    mProgressDialog.show();

                    try {
                        submitRequisition(produce, Integer.parseInt(bags), date, instructions, latLong);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        mMaterialEditTextPickupLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlacePickerView();
            }
        });
    }

    private void submitRequisition(String produce, int bags, String date, String instructions, String latLong) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(FarmerRequisitionActivity.this);
        String URL = "http://169.57.99.69:30692/farmerRequisition";

        final Map<String, Object> reqDataMap = new HashMap<>();
        reqDataMap.put("productType", produce);
        reqDataMap.put("quantity", bags);
        reqDataMap.put("expectedPickupDate", date);
        reqDataMap.put("pickupLocation", latLong);
        reqDataMap.put("specialInstructions", instructions);
        reqDataMap.put("creatorId", Common.currentUser.getId());

        Gson gson = new Gson();
        String json = gson.toJson(reqDataMap);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, URL, new JSONObject(json),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject = null;

                        try {
                            jsonObject = response.getJSONObject("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (jsonObject != null)
                        {
                            mProgressDialog.dismiss();

                            Toast.makeText(FarmerRequisitionActivity.this,
                                    "Requisition placed.",
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(FarmerRequisitionActivity.this,
                                    HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    |Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(FarmerRequisitionActivity.this,
                                    "Please try again",
                                    Toast.LENGTH_LONG).show();
                        }



                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(FarmerRequisitionActivity.this,
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

    private void updateUIDate() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());

        mMaterialEditTextDate.setText(simpleDateFormat.format(mCalendar.getTime()));
    }


    private void openPlacePickerView(){
        LatLngBounds latLngBounds = new LatLngBounds(
                new LatLng( -4.47166, 33.97559),
                new LatLng(3.93726, 41.85688));
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(latLngBounds);
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);

                LatLng latLngQueriedLocation = place.getLatLng();

                mMaterialEditTextPickupLocation.setText(latLngQueriedLocation.latitude + "," + latLngQueriedLocation.longitude);
            }
        }
    }

}