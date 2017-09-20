package com.example.oli.scaleuser2.gui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oli.scaleuser2.R;
import com.example.oli.scaleuser2.core.Calculator;
import com.example.oli.scaleuser2.core.OnlineHelper;
import com.example.oli.scaleuser2.core.UserList;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
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

    static String[] hLabels, emptyLabels;
    public static String ideal;
    public static GraphView graph;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        thisActivity = this;

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

        int historySize = UserList.listGewichtTimeline.size();
        DataPoint[] dp = new DataPoint[historySize];
        hLabels = new String[UserList.listDatum.size()];
        emptyLabels = new String[UserList.listDatum.size()];
        for (int i = 0; i < historySize; i++) {
            String dateString = UserList.getItemDatum(i).toString();
            double gewichtGraph = Double.parseDouble(UserList.getItemGewichtTimeline(i).toString());
            DataPoint v = new DataPoint(i, gewichtGraph);
            dp[i] = v;
            hLabels[i] = dateString;
            emptyLabels[i] = "";
        }


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(emptyLabels);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getGridLabelRenderer().setNumHorizontalLabels(historySize);



        /*
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

        */


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

    private static DataPoint[] getDataPoint2() {

        int historySize = UserList.listGewichtTimeline.size();
        DataPoint[] dp = new DataPoint[historySize];
        //hLabels = new String[UserList.listDatum.size()];
        //emptyLabels = new String[UserList.listDatum.size()];
        String gender = OnlineActivity.gender;
        String height = OnlineActivity.groesse;
        ideal = Calculator.idealWeight(gender, height);
        double gewichtGraph = Double.parseDouble(Calculator.idealWeight(gender, height));
        for (int i = 0; i < historySize; i++) {
            //String dateString = UserList.getItemDatum(i).toString()
            DataPoint v = new DataPoint(i, gewichtGraph);
            dp[i] = v;
            //hLabels[i] = dateString;
            //emptyLabels[i] = "";

        }
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
        if (UserList.listGewichtTimeline.size() > 0)
        {
            for (int i = UserList.listGewichtTimeline.size()-1; i >=0; i--)
            {
                UserList.listGewichtTimeline.remove(UserList.listGewichtTimeline.get(i));
                UserList.listDatum.remove(UserList.listDatum.get(i));
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
            UserList.listGewichtTimeline.add(0.0);
            UserList.listDatum.add(0);

            while(count < jsonArray.length())
            {
                JSONObject JO = jsonArray.getJSONObject(count);
                String gewicht = JO.getString("gewicht");
                String datum = JO.getString("datum");

                //UserHistory userHistory = new UserHistory(gewicht, datum);
                //UserHistoryAdapter userHistoryAdapter = new UserHistoryAdapter();
                UserList.listGewichtTimeline.add(gewicht);
                UserList.listDatum.add(datum);

                count++;
            }




            if (UserList.listGewichtTimeline.size() > 1)
            {
                LineGraphSeries series = new LineGraphSeries(getDataPoint());
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(12);
                series.setAnimated(true);
                series.setColor(Color.rgb(255, 64, 129));
                series.setThickness(8);
                series.setTitle("Weight");

                graph.getLegendRenderer().setVisible(true);
                graph.getLegendRenderer().setBackgroundColor(Color.rgb(223, 223, 223));
                graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);



                LineGraphSeries series2 = new LineGraphSeries(getDataPoint2());
                series2.setAnimated(true);
                series2.setColor(Color.rgb(63, 81, 181));
                series2.setThickness(8);
                series2.setDrawBackground(true);
                series2.setTitle("Ideal weight = " +ideal+ " Kg");

                //series2.setBackgroundColor(Color.rgb(63, 81, 181));
                graph.addSeries(series);
                graph.addSeries(series2);

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
            }
            else
            {
                Toast.makeText(thisActivity,"You need to insert weightdata to use this graph", Toast.LENGTH_LONG).show();
            }



        }catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}
