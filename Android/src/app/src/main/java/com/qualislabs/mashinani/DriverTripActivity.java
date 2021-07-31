package com.qualislabs.mashinani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qualislabs.mashinani.Adapters.PickupItemAdapter;
import com.qualislabs.mashinani.Common.Common;
import com.qualislabs.mashinani.MapRoutesHelper.FetchURL;
import com.qualislabs.mashinani.MapRoutesHelper.TaskLoadedCallback;
import com.qualislabs.mashinani.Models.FarmerRequisition;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class DriverTripActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private static final String TAG = DriverTripActivity.class.getSimpleName();

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1020;
    private LocationManager mLocationManager;
    private Location mLocationDriver;
    private Polyline currentPolyline;

    private RecyclerView mRecyclerViewDriverTrip;
    private PickupItemAdapter mPickupItemAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog mProgressDialog;
    private List<FarmerRequisition> mFarmerRequisitionList;
    private LatLng mLatLngDestination;
    private List<String> pickupItemsArray;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    private int traderRequisitionID;
    private LatLng mLatLngUserCurrent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trip);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_driver_map);
        mapFragment.getMapAsync(this);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        try {
            gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(DriverTripActivity.this)
                    .setMessage("Please turn on location services to continues")
                    .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            DriverTripActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show();
        }

        enableDriverLocation();

        mRecyclerViewDriverTrip = (RecyclerView) findViewById(R.id.recycler_driver_trip_pickup_items);

        mProgressDialog = new ProgressDialog(DriverTripActivity.this,
                R.style.ProgressDialogStyle);

        mProgressDialog.setMessage("Fetching your pickup items...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        mRecyclerViewDriverTrip.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewDriverTrip.setLayoutManager(mLayoutManager);

        mFarmerRequisitionList = new ArrayList<>();
        pickupItemsArray = new ArrayList<>();


//        mFarmerRequisitionList.add(new FarmerRequisition(1, 15, 4, "maize", "27/12/2020", "None", "-1.101981,37.014191"));
//        mFarmerRequisitionList.add(new FarmerRequisition(1, 10, 4, "maize", "27/12/2020", "None", "-1.219147,36.891618"));
//        mFarmerRequisitionList.add(new FarmerRequisition(2, 20, 4, "maize", "30/06/2021", "None", "-1.037967,37.080014"));
//        mFarmerRequisitionList.add(new FarmerRequisition(2, 40, 4, "maize", "30/06/2021", "None", "-1.151256,36.899619"));


        getTripItems();


    }

    private void getTripItems() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://169.57.99.69:32390/trips";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray requisitionListResponse = null;
                        try {
                            requisitionListResponse = response.getJSONArray("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < requisitionListResponse.length(); i++) {
                            try {
                                JSONObject requisitionObject
                                        = requisitionListResponse.getJSONObject(i);


                                if (Common.currentUser.getId()
                                        == requisitionObject.getInt("driverId")
                                        && requisitionObject.getString("status")
                                        .equalsIgnoreCase("active")) {
                                     JSONArray jsonElements = requisitionObject.getJSONArray("farmerRequisitionId");
                                     for (int k = 0; i < jsonElements.length(); i ++)
                                        pickupItemsArray.add(jsonElements.getString(i));

                                    traderRequisitionID = requisitionObject.getInt("traderRequisitionID");
                                    Common.tripID = requisitionObject.getInt("id");

                                    break;

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        RequestQueue requestQueuePickup = Volley.newRequestQueue(DriverTripActivity.this);
                        String URLPickup = "http://169.57.99.69:30692/farmerRequisitions";

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.GET, URLPickup, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        JSONArray pickupListResponse = null;
                                        try {
                                            pickupListResponse = response.getJSONArray("data");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        for (int i = 0; i < pickupListResponse.length(); i++) {
                                            JSONObject requisitionObject = null;
                                            try {
                                                requisitionObject = pickupListResponse.getJSONObject(i);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            for (int j = 0; j < pickupItemsArray.size(); j++) {

                                                try {
                                                    if (Integer.parseInt(pickupItemsArray.get(j)) == requisitionObject.getInt("creatorId")
                                                            && requisitionObject.getString("status").equalsIgnoreCase("active")) {

                                                        JSONObject pickupObject = pickupListResponse.getJSONObject(i);

                                                        //Map json object to Requisition Class
                                                        ObjectMapper mapper = new ObjectMapper();
                                                        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                                                        FarmerRequisition pickupItem = mapper.readValue(pickupObject.toString(), FarmerRequisition.class);

                                                        mFarmerRequisitionList.add(pickupItem);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        mPickupItemAdapter = new PickupItemAdapter(DriverTripActivity.this, mFarmerRequisitionList);
                                        mRecyclerViewDriverTrip.setAdapter(mPickupItemAdapter);
                                        mProgressDialog.dismiss();

                                        RequestQueue requestQueueTrader = Volley.newRequestQueue(DriverTripActivity.this);
                                        String URLTrader = "http://169.57.99.69:30692/traderRequisition/" + traderRequisitionID;

                                        JsonObjectRequest jsonObjReqTrader = new JsonObjectRequest(
                                                Request.Method.GET, URLTrader, null,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {

                                                        JSONObject traderReq = null;
                                                        try {
                                                            traderReq = response.getJSONObject("data");
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                        List<Float> listLatlong = new ArrayList<>();
                                                        Scanner scan = null;
                                                        try {
                                                            scan = new Scanner(traderReq.getString("deliveryLocation"));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        scan.useDelimiter(",");

                                                        while (scan.hasNext()) {
                                                            listLatlong.add(Float.valueOf(scan.next()));
                                                        }
                                                        scan.close();

                                                        mLatLngDestination = new LatLng(listLatlong.get(0), listLatlong.get(1));


                                                        if (mFarmerRequisitionList.size() > 0) {
                                                            for (int i = 0; i < mFarmerRequisitionList.size(); i++) {
                                                                List<Float> newListLatlong = new ArrayList<>();

                                                                Scanner newScan = new Scanner(mFarmerRequisitionList.get(i).getPickupLocation());
                                                                newScan.useDelimiter(",");

                                                                while (newScan.hasNext()) {
                                                                    newListLatlong.add(Float.valueOf(newScan.next()));
                                                                }
                                                                scan.close();

                                                                if (listLatlong != null) {
                                                                    LatLng latLng1 = new LatLng(newListLatlong.get(0), newListLatlong.get(1));
                                                                    mMap.addMarker(new MarkerOptions()
                                                                            .position(latLng1)
                                                                            .title(String.valueOf(mFarmerRequisitionList.get(i).getProductType()))
                                                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                                                }
                                                            }
                                                            //Todo remove next line
                                                            mLatLngDestination = new LatLng(-0.303099,36.080025);
                                                            mMap.addMarker(new MarkerOptions()
                                                                    .title("Destination")
                                                                    .position(mLatLngDestination)
                                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));


                                                            new FetchURL(DriverTripActivity.this).execute(createUrl(mLatLngUserCurrent, mLatLngDestination, "driving"), "driving");
                                                        }

                                                    }

                                                }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                mProgressDialog.dismiss();
                                                Toast.makeText(DriverTripActivity.this,
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

                                        requestQueueTrader.add(jsonObjReqTrader);


                                    }

                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                mProgressDialog.dismiss();
                                Toast.makeText(DriverTripActivity.this,
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

                        requestQueuePickup.add(jsonObjectRequest);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(DriverTripActivity.this,
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

    private void enableDriverLocation() {
        if (ContextCompat.checkSelfPermission(DriverTripActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DriverTripActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(DriverTripActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(DriverTripActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                    1f, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000,
                    1f, mLocationListener);

            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(DriverTripActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull @NotNull Location location) {
            mLocationDriver = location;
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        if(gps_enabled && network_enabled) {
            mLatLngUserCurrent = new LatLng(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(), mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(mLatLngUserCurrent)
                    .zoom(17)
                    .bearing(90)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        }
    }

    private String createUrl(LatLng origin, LatLng destination, String directionMode) {
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDestination = "destination=" + destination.latitude + "," + destination.longitude;

        String wayPoints = "waypoints=optimize:true|";

        for (int i = 0; i < mFarmerRequisitionList.size(); i++){
            wayPoints +=mFarmerRequisitionList.get(i).getPickupLocation();
            if (i != mFarmerRequisitionList.size() - 1)
                 wayPoints += "|";
        }


        String strMode = "mode=" + directionMode;
        String params = strOrigin + "&" + strDestination + "&" + wayPoints + "&" + strMode;

        String createdUrl = "https://maps.googleapis.com/maps/api/directions/json?" + params + "&key=AIzaSyC1mqv3W0bo4YaKfOgCyQOYXX76r1Ji8EA";

        return createdUrl;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}