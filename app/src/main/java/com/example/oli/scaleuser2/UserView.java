package com.example.oli.scaleuser2;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;


public class UserView extends AppCompatActivity {
    TextView nameUser, surnameUser, genderUser, heigthUser, weightUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_user);

        String nameData = getIntent().getStringExtra("Extra_name");
        String surnameData = getIntent().getStringExtra("Extra_surname");
        String genderData = getIntent().getStringExtra("Extra_gender");
        String heigthData = getIntent().getStringExtra("Extra_heigth");
        String weightData = getIntent().getStringExtra("Extra_weight");

        nameUser = (TextView) findViewById(R.id.name);
        surnameUser = (TextView) findViewById(R.id.nachname);
        genderUser = (TextView) findViewById(R.id.gender);
        heigthUser = (TextView) findViewById(R.id.groesse);
        weightUser = (TextView) findViewById(R.id.weight);


        nameUser.setText(nameData);
        surnameUser.setText(surnameData);
        genderUser.setText(genderData);
        heigthUser.setText(heigthData);
        weightUser.setText(weightData);

    }
}
