package com.qualislabs.mashinani;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qualislabs.mashinani.Adapters.HistoryItemAdapter;
import com.qualislabs.mashinani.Adapters.PickupItemAdapter;
import com.qualislabs.mashinani.Models.FarmerRequisition;
import com.qualislabs.mashinani.Models.History;

import java.util.ArrayList;
import java.util.List;


public class DriverTripActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private static final String TAG = DriverTripActivity.class.getSimpleName();

    private RecyclerView mRecyclerViewDriverTrip;
    private PickupItemAdapter mPickupItemAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog mProgressDialog;
    private List<FarmerRequisition> mFarmerRequisitionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trip);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_driver_map);
        mapFragment.getMapAsync(this);

        mRecyclerViewDriverTrip = (RecyclerView)findViewById(R.id.recycler_driver_trip_pickup_items);

        mFarmerRequisitionList = new ArrayList<>();

        mFarmerRequisitionList.add(new FarmerRequisition("maize","27/12/2020","None",1,4,4 ));
        mFarmerRequisitionList.add(new FarmerRequisition("Beans","30/06/2021","None",2,4,4 ));

        mProgressDialog = new ProgressDialog(DriverTripActivity.this,
                R.style.ProgressDialogStyle);

        mProgressDialog.setMessage("Fetching your pickup items...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        mRecyclerViewDriverTrip.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewDriverTrip.setLayoutManager(mLayoutManager);

        // ToDo call trip items from db
        //getTripItems();

        if (mFarmerRequisitionList != null){
            mPickupItemAdapter = new PickupItemAdapter(this, mFarmerRequisitionList);
            mRecyclerViewDriverTrip.setAdapter(mPickupItemAdapter);
            mProgressDialog.dismiss();
        }

        // Todo implement ask user for location permissions
    }

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

        // Add a marker in Nairobi and move the camera
        LatLng latLng = new LatLng(-1.286389, 36.817223);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Nairobi"));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(9)
                .bearing(90)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


}