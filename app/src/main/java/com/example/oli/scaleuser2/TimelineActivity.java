package com.example.oli.scaleuser2;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Oli on 22.07.2017.
 */

public class TimelineActivity extends AppCompatActivity {

    static TimelineActivity thisActivity = null;

    public static TextView txtJson;
    ProgressDialog loading;

    private static Date date;



    public static GraphView graph;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        thisActivity = this;

        txtJson = (TextView) findViewById(R.id.json_timeline);
        graph = (GraphView) findViewById(R.id.timelineGraph);

        graph.getViewport().setScalable(true);


        //DataPoint[] dataPoints = new DataPoint[UserHistoryAdapter.listGewicht.size()];
        //



        getJson();
    }

    private static void dateFormater(String dateString)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dateString);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.format(date);
        }catch (ParseException e)
        {
            e.printStackTrace();
        }
    }




    private static DataPoint[] getDataPoint() {

        int historySize = UserHistoryList.listGewicht.size();
        DataPoint[] dp = new DataPoint[historySize];
        String[] hLabels = new String[UserHistoryList.listDatum.size()];

        for (int i = 0; i < historySize; i++) {
            String dateString = UserHistoryList.getItemDatum(i).toString();
            double gewichtGraph = Double.parseDouble(UserHistoryList.getItemGewicht(i).toString());
            DataPoint v = new DataPoint(i, gewichtGraph);
            dp[i] = v;
            hLabels[i] = dateString;
        }

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(hLabels);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0.0);
        graph.getViewport().setMaxX((double) historySize - 1);


        return dp;


    }



    private void getJson()
    {
       // loading = ProgressDialog.show(TimelineActivity.this, "Please Wait...",null,true,true);
        String type = "getTimeline";
        OnlineHelper onlineHelper = new OnlineHelper(this);
        onlineHelper.execute(type);
    }

    private static void checkUserList()
    {
        if (UserHistoryList.listGewicht.size() > 0)
        {
            for (int i = UserHistoryList.listGewicht.size()-1; i >=0; i--)
            {
                UserHistoryList.listGewicht.remove(UserHistoryList.listGewicht.get(i));
                UserHistoryList.listDatum.remove(UserHistoryList.listDatum.get(i));
            }
        }

    }

    public static void parse(String jsonString)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("timeline");
            int count = 0;

            checkUserList();

            while(count < jsonArray.length())
            {
                JSONObject JO = jsonArray.getJSONObject(count);
                String gewicht = JO.getString("gewicht");
                String datum = JO.getString("datum");
                dateFormater(datum);


                //UserHistory userHistory = new UserHistory(gewicht, datum);
                //UserHistoryAdapter userHistoryAdapter = new UserHistoryAdapter();
                //dateFormater(datum);
                UserHistoryList.listGewicht.add(gewicht);
                UserHistoryList.listDatum.add(datum);
                //dateFormater(datum);


                count++;
            }

            LineGraphSeries series = new LineGraphSeries(getDataPoint());
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(12);
            series.setAnimated(true);
            series.setColor(Color.rgb(255, 64, 129));
            series.setThickness(8);


            graph.addSeries(series);

            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(thisActivity, "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
                }
            });

        }catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}
