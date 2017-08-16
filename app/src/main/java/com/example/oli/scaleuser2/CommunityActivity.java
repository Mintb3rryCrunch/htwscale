package com.example.oli.scaleuser2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mamoudou on 14.08.2017.
 */

public class CommunityActivity extends AppCompatActivity {
    public static TextView txtJson;

    public static GraphView graph;

    static String[] hLabels;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        txtJson = (TextView) findViewById(R.id.json_community);
        graph = (GraphView) findViewById(R.id.communityGraph);

        //graph.getViewport().setScalable(true);
        //graph.getViewport().setScalableY(false);
        //graph.getViewport().setScrollable(true);

        getJson();

    }

    private static DataPoint[] getDataPoint() {

        int historySize = UserList.listGewichtCommunity.size();
        DataPoint[] dp = new DataPoint[historySize];
        hLabels = new String[UserList.listName.size()];

        for (int i = 0; i < historySize; i++) {
            String nameString = UserList.getItemName(i).toString();
            double gewichtGraph = Double.parseDouble(UserList.getItemGewichtCommunity(i).toString());
            DataPoint v = new DataPoint(i, gewichtGraph);
            dp[i] = v;
            hLabels[i] = nameString;
        }


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(hLabels);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getGridLabelRenderer().setNumHorizontalLabels(historySize);



        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX((double) historySize - 1);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(160.0);


        return dp;


    }

    private void getJson()
    {
        String type = "getCommunity";
        OnlineHelper onlineHelper = new OnlineHelper(this);
        onlineHelper.execute(type);
    }

    public static void parse(String jsonString)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("community");
            int count = 0;

            checkUserList();

            while(count < jsonArray.length())
            {
                JSONObject JO = jsonArray.getJSONObject(count);
                String name = JO.getString("name");
                String gewicht = JO.getString("gewicht");

                //UserHistory userHistory = new UserHistory(gewicht, datum);
                //UserHistoryAdapter userHistoryAdapter = new UserHistoryAdapter();
                UserList.listGewichtCommunity.add(gewicht);
                UserList.listName.add(name);

                count++;
            }


            BarGraphSeries series2 = new BarGraphSeries(getDataPoint());
            series2.setDrawValuesOnTop(true);
            series2.setValuesOnTopColor(Color.rgb(255, 64, 129));
            series2.setSpacing(60);
            series2.setAnimated(true);
            series2.setColor(Color.rgb(63, 81, 181));

            graph.addSeries(series2);


        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private static void checkUserList()
    {
        if (UserList.listGewichtCommunity.size() > 0)
        {
            for (int i = UserList.listGewichtCommunity.size()-1; i >=0; i--)
            {
                UserList.listGewichtCommunity.remove(UserList.listGewichtCommunity.get(i));
                UserList.listName.remove(UserList.listName.get(i));
            }
        }

    }

}
