package com.example.oli.scaleuser2;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Oli on 21.06.2017.
 */

public class OnlineActivity extends AppCompatActivity {
    private static TextView status, txtName, txtSurname, txtGender, txtGroesse, txtGewicht, txtUsername, txtBMI;
    private  static ProgressDialog loading;


    public static String user_id, name, surname, gender, username, groesse, gewicht, bmi;

    public static MenuItem bluetoothStatus, uploadData;
    public static int bluetoothStatusIcon = R.mipmap.bluetooth_disabled, uploadDataIcon = R.mipmap.update_data;
    private static boolean firstAppStart = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);


        txtName = (TextView) findViewById(R.id.nameOut);
        txtSurname = (TextView) findViewById(R.id.surnameOut);
        txtGender = (TextView) findViewById(R.id.genderOut);
        txtGroesse = (TextView) findViewById(R.id.groesseOut);
        txtGewicht = (TextView) findViewById(R.id.gewichtOut);
        txtBMI = (TextView) findViewById(R.id.BMIOut);


        getJson();
        checkBtPermissions();

    }

    public void onTimeline(View v) {
        final Intent intent = new Intent(OnlineActivity.this, TimelineActivity.class);
        startActivity(intent);
    }

    private void getJson(){

        loading = ProgressDialog.show(OnlineActivity.this, "Please Wait...",null,true,true);
        String type = "getData";
        OnlineHelper onlineHelper = new OnlineHelper(this);
        onlineHelper.execute(type);

        txtName.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        txtSurname.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        txtGender.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        txtGroesse.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        txtGewicht.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        txtBMI.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));

    }

    public static void parseJson(String jsonString)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            JSONObject JO = jsonArray.getJSONObject(0);
                user_id = JO.getString("id");
                name = JO.getString("name");
                surname = JO.getString("nachname");
                gender = JO.getString("gender");
                groesse = JO.getString("groesse");
                gewicht = JO.getString("gewicht");
                bmi = JO.getString("bmi");
                username = JO.getString("username");

                txtName.setText(name);
                txtSurname.setText(surname);
                txtGender.setText(gender);
                txtGroesse.setText(groesse);
                txtGewicht.setText(gewicht);
                txtBMI.setText(bmi);


            loading.dismiss();

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    void BMI_Rechner(String weight, String height)
    {
        float calc_weight = Float.parseFloat(weight);
        float calc_height = Float.parseFloat(height) /100;

        float BMI = calc_weight / (calc_height*calc_height);

        BMI = (float)Math.round(BMI * 10) / 10;

        bmi = Float.toString(BMI);

        txtBMI.setText(bmi);
        txtBMI.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));




    }

    public void add_weight() {

        gewicht = Float.toString(BluetoothMiScale.weightdata);
        txtGewicht.setText(gewicht);
        txtGewicht.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        //Toast.makeText(MainActivity.this, "Weightdata: " +weight, Toast.LENGTH_LONG).show();
        BMI_Rechner(gewicht, groesse);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_online, menu);

        bluetoothStatus = menu.findItem(R.id.action_bluetooth_status);
        uploadData = menu.findItem(R.id.update_data);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Just search for a bluetooth device just once at the start of the app and if start preference enabled
        if (firstAppStart && prefs.getBoolean("btEnable", false)) {
            invokeSearchBluetoothDevice();
            firstAppStart = false;
        } else {
            // Set current bluetooth status icon while e.g. orientation changes
            setBluetoothStatusIcon(bluetoothStatusIcon);
        }

        return true;
    }

    private void invokeSearchBluetoothDevice() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = bluetoothManager.getAdapter();

        if (btAdapter == null || !btAdapter.isEnabled()) {
            setBluetoothStatusIcon(R.mipmap.bluetooth_disabled);
            Toast.makeText(getApplicationContext(), "Bluetooth " + "is disabled", Toast.LENGTH_SHORT).show();

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String deviceName = prefs.getString("btDeviceName", "MI_SCALE");
        String deviceType = prefs.getString("btDeviceTypes", "0");

        // Check if Bluetooth 4.x is available
        if (Integer.parseInt(deviceType) == BluetoothCommunication.BT_MI_SCALE) {
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                setBluetoothStatusIcon(R.mipmap.bluetooth_disabled);
                Toast.makeText(getApplicationContext(), "Bluetooth 4.x " + "is not available", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Toast.makeText(getApplicationContext(), "trying to connect" + " " + deviceName, Toast.LENGTH_SHORT).show();
        setBluetoothStatusIcon(R.mipmap.bluetooth_searching);

        UserBtHelp.getInstance(getApplicationContext()).stopSearchingForBluetooth();
        checkBtPermissions();
        UserBtHelp.getInstance(getApplicationContext()).startSearchingForBluetooth(Integer.parseInt(deviceType), deviceName, callbackBtHandler);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_bluetooth_status) {
            invokeSearchBluetoothDevice();
            return true;
        }

        else if (id == R.id.update_data){
            String type = "updateData";
            OnlineHelper onlineHelper = new OnlineHelper(this);
            onlineHelper.execute(type, user_id, gewicht, bmi);

            txtBMI.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_out_right));
            txtGewicht.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_out_right));

            uploadData.setIcon(getResources().getDrawable(R.mipmap.data_update));
        }

        return super.onOptionsItemSelected(item);
    }

    private void setBluetoothStatusIcon(int iconRessource) {
        bluetoothStatusIcon = iconRessource;
        bluetoothStatus.setIcon(getResources().getDrawable(bluetoothStatusIcon));
    }

    private final Handler callbackBtHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case BluetoothCommunication.BT_RETRIEVE_SCALE_DATA:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_connected);
                    add_weight();
                    //ScaleData scaleBtData = (ScaleData) msg.obj;

                  /*  if (UserBtHelp.getInstance(getApplicationContext()).addScaleData(scaleBtData) == -1) {
                        Toast.makeText(getApplicationContext(), "No User Exists, please create a User", Toast.LENGTH_SHORT).show();
                    }*/
                    break;
                case BluetoothCommunication.BT_INIT_PROCESS:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_connected);
                    Toast.makeText(getApplicationContext(), "Initialize Bluetooth device", Toast.LENGTH_SHORT).show();
                    Log.d("OpenScale", "Bluetooth initializing");
                    break;
                case BluetoothCommunication.BT_CONNECTION_LOST:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_disabled);
                    Toast.makeText(getApplicationContext(), "Lost Bluetooth Connection", Toast.LENGTH_SHORT).show();
                    Log.d("OpenScale", "Bluetooth connection lost");
                    break;
                case BluetoothCommunication.BT_NO_DEVICE_FOUND:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_disabled);
                    Toast.makeText(getApplicationContext(), "No Bluetooth device found", Toast.LENGTH_SHORT).show();
                    Log.d("OpenScale", "No Bluetooth device found");
                    break;
                case BluetoothCommunication.BT_CONNECTION_ESTABLISHED:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_connected);
                    Toast.makeText(getApplicationContext(),"Connection successful", Toast.LENGTH_SHORT).show();
                    Log.d("OpenScale", "Bluetooth connection successful established");
                    break;
                case BluetoothCommunication.BT_UNEXPECTED_ERROR:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_disabled);
                    Toast.makeText(getApplicationContext(), "Bluetooth has an unexcepted Error" + ": " + msg.obj, Toast.LENGTH_SHORT).show();
                    Log.e("OpenScale", "Bluetooth unexpected error: " + msg.obj);
                    break;
            }
        }
    };

    public void checkBtPermissions()
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
        {
            int permissionCheck = this.checkCallingOrSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if(permissionCheck != 0)
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }
    }



}



