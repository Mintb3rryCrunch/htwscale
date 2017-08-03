package com.example.oli.scaleuser2;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
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


/**
 * Created by Oli on 22.07.2017.
 */

public class TimelineActivity extends AppCompatActivity {

    static TimelineActivity thisActivity = null;

    public static TextView txtJson, txtBegin, txtEnd;
    ProgressDialog loading;

    static String[] hLabels;

    public static GraphView graph;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        thisActivity = this;

        txtJson = (TextView) findViewById(R.id.json_timeline);
        txtBegin = (TextView) findViewById(R.id.firstDate);
        txtEnd = (TextView) findViewById(R.id.lastDate);
        graph = (GraphView) findViewById(R.id.timelineGraph);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(false);
        graph.getViewport().setScrollable(true);



        //DataPoint[] dataPoints = new DataPoint[UserHistoryAdapter.listGewicht.size()];
        //



        getJson();
    }



    private static DataPoint[] getDataPoint() {

        int historySize = UserHistoryList.listGewicht.size();
        DataPoint[] dp = new DataPoint[historySize];
        hLabels = new String[UserHistoryList.listDatum.size()];

        for (int i = 0; i < historySize; i++) {
            String dateString = UserHistoryList.getItemDatum(i).toString();
            double gewichtGraph = Double.parseDouble(UserHistoryList.getItemGewicht(i).toString());
            DataPoint v = new DataPoint(i, gewichtGraph);
            dp[i] = v;
            hLabels[i] = dateString;
        }



        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        //staticLabelsFormatter.setHorizontalLabels(hLabels);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getGridLabelRenderer().setNumHorizontalLabels(0);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX) + ". day";
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });


        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX((double) historySize - 1);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(160.0);


        txtBegin.setText(hLabels[1]);
        txtEnd.setText(hLabels[historySize - 1]);




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
            UserHistoryList.listGewicht.add(0.0);
            UserHistoryList.listDatum.add(0);

            while(count < jsonArray.length())
            {
                JSONObject JO = jsonArray.getJSONObject(count);
                String gewicht = JO.getString("gewicht");
                String datum = JO.getString("datum");

                //UserHistory userHistory = new UserHistory(gewicht, datum);
                //UserHistoryAdapter userHistoryAdapter = new UserHistoryAdapter();
                UserHistoryList.listGewicht.add(gewicht);
                UserHistoryList.listDatum.add(datum);

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
                    String data = dataPoint.toString();
                    int dataX = (int)dataPoint.getX();
                    double dataY = dataPoint.getY();
                    String strDate = hLabels[dataX];

                    Toast.makeText(thisActivity,strDate+ "     " +dataY+ " kg", Toast.LENGTH_SHORT).show();
                }
            });


        }catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}
