package com.qualislabs.mashinani;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.qualislabs.mashinani.Adapters.HistoryItemAdapter;
import com.qualislabs.mashinani.Common.Common;
import com.qualislabs.mashinani.Models.History;
import com.qualislabs.mashinani.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewHistory;
    private HistoryItemAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog mProgressDialog;
    private List<History> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        historyList = new ArrayList<>();

        mProgressDialog = new ProgressDialog(HistoryActivity.this,
                R.style.ProgressDialogStyle);

        mProgressDialog.setMessage("Fetching items...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        mRecyclerViewHistory = (RecyclerView) findViewById(R.id.recycler_history);
        mRecyclerViewHistory.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewHistory.setLayoutManager(mLayoutManager);

        getHistoryItems();

        if (historyList != null){
            adapter = new HistoryItemAdapter(HistoryActivity.this, historyList);
            mRecyclerViewHistory.setAdapter(adapter);
            mProgressDialog.dismiss();
        }

    }

    private void getHistoryItems(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://6e2ebd754dc9.ngrok.io/requisionHistory";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray historyListResponse = null;
                        try {
                            historyListResponse = response.getJSONArray("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < historyListResponse.length(); i++) {
                            try {
                                JSONObject historyObject = historyListResponse.getJSONObject(i);

                                //Map json object to History Class
                                ObjectMapper mapper = new ObjectMapper();
                                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                                History historyItem = mapper.readValue(historyObject.toString(), History.class);

                                historyList.add(historyItem);
                            } catch (IOException e) {
                                e.printStackTrace();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }



                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(HistoryActivity.this, "Unknown error occurred. Please try again",
                        Toast.LENGTH_LONG).show();
            }
        }) {
            //Request headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Bearer ", Common.currentUser.getToken());
                return headers;
            }

        };

        requestQueue.add(jsonObjReq);

    }
}