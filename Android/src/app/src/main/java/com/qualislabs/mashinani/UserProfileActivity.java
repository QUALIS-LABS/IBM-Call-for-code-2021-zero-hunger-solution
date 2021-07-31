package com.qualislabs.mashinani;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        lineChart = findViewById(R.id.linechart_userprofile);
        lineChart.getDescription().setEnabled(false);
        getEntries();
        lineDataSet = new LineDataSet(lineEntries, "");
        lineDataSet.setFillColor(getColor(R.color.tertiary_text));
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setCubicIntensity(0.6f);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);



        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineDataSet.setColors(getColor(R.color.tertiary_text));


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(11f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(lineChart.getYMax() + 150f);
        leftAxis.setTextSize(8f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }
    private void getEntries() {
        lineEntries = new ArrayList<>();
        lineEntries.add(new Entry(1, 312));
        lineEntries.add(new Entry(2, 78));
        lineEntries.add(new Entry(3, 89));
        lineEntries.add(new Entry(4, 109));
        lineEntries.add(new Entry(5, 8));
        lineEntries.add(new Entry(6, 0));
        lineEntries.add(new Entry(7, 78));
        lineEntries.add(new Entry(8, 200));
        lineEntries.add(new Entry(9, 69));
        lineEntries.add(new Entry(10, 90));
        lineEntries.add(new Entry(11, 99));
        lineEntries.add(new Entry(12, 198));
    }
}