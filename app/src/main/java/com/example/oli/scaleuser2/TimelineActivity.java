package com.example.oli.scaleuser2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mamoudou on 23.07.2017.
 */

public class TimelineActivity extends AppCompatActivity {
    //private static TextView tx_userid, tx_gewicht, tx_datum;
    public static String user_id, gewicht, datum;
    public static UserAdapter userAdapter;
    ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewhistory);
        listView =(ListView) findViewById(R.id.listview);
        userAdapter = new UserAdapter(this, R.layout.historylistview_activity);
        listView.setAdapter(userAdapter);

        //tx_userid = (TextView) findViewById(R.id.tx_userid);
        //tx_gewicht = (TextView) findViewById(R.id.tx_gewicht);
        //tx_datum = (TextView) findViewById(R.id.tx_datum);


        getJson();




    }

    private void getJson() {

        //loading = ProgressDialog.show(OnlineActivity.this, "Please Wait...", null, true, true);
        String type = "getTimeline";
        OnlineHelper onlineHelper= new OnlineHelper(this);
        onlineHelper.execute(type);

    }

    public static void parseJson(String jsonString) {
        try
        {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("timeline");


            int count = 0;
            while(count < jsonArray.length()){
                JSONObject JO = jsonArray.getJSONObject(count);
                user_id = JO.getString("user_id");
                gewicht = JO.getString("gewicht");
                datum = JO.getString("datum");

                //tx_userid.setText(user_id);
                //tx_gewicht.setText(gewicht);
                //tx_datum.setText(datum);
                User user = new User(user_id,gewicht,datum);
                userAdapter.add(user);

                count++;


            }


        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
