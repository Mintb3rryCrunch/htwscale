package com.example.oli.scaleuser2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Mamoudou on 14.08.2017.
 */

public class CommunityActivity extends AppCompatActivity {
    public static TextView txtJson;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        txtJson = (TextView) findViewById(R.id.json_community);

        getJson();

    }

    private void getJson()
    {
        String type = "getCommunity";
        OnlineHelper onlineHelper = new OnlineHelper(this);
        onlineHelper.execute(type);
    }

}
