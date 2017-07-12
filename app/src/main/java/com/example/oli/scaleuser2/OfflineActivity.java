package com.example.oli.scaleuser2;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.oli.scaleuser2.R.id.add_user;
import static com.example.oli.scaleuser2.R.id.et_nachname;
import static com.example.oli.scaleuser2.R.id.et_vorname;

/**
 * Created by Oli on 12.07.2017.
 */

public class OfflineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    int progressValue;
    private static MenuItem bluetoothStatus, addUser;
    private static int bluetoothStatusIcon = R.mipmap.bluetooth_disabled;
    private static boolean firstAppStart = true;
    UserDatabase myDb;

    TextView txtNachname, txtGroesse, txtGeschlecht, txtGewicht, txtBMI;
    Button adduser;
    Spinner spinner;

    ArrayList<String> names = new ArrayList<>();
    ArrayAdapter<String> adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        myDb = new UserDatabase(this);

        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        spinner.setOnItemSelectedListener(this);

        txtNachname = (TextView) findViewById(R.id.nachnameOut);
        txtGroesse = (TextView) findViewById(R.id.groesseOut);
        txtGeschlecht = (TextView) findViewById(R.id.genderOut);
        txtGewicht = (TextView) findViewById(R.id.gewichtOut);
        txtBMI = (TextView) findViewById(R.id.BMIOut);

        adduser = (Button) findViewById(R.id.add_user);

        updateSpinner();
        viewUser();
        checkBtPermissions();

    }

    private void add_weight() {

        String weight = Float.toString(BluetoothMiScale.weightdata);
        txtGewicht.setText(weight);
        updateWeight();
        //Toast.makeText(MainActivity.this, "Weightdata: " +weight, Toast.LENGTH_LONG).show();
    }

    private void updateWeight()
    {
        String weightData = txtGewicht.getText().toString();
        Cursor cursor = myDb.getAllUser2();
        while(cursor.moveToNext())
        {
            int rowCount = Integer.parseInt(cursor.getString(6));
            int dbId = Integer.parseInt(cursor.getString(0));
            int spinnerId = spinner.getSelectedItemPosition();

            if (rowCount == spinnerId)
            {
                boolean isUpdated = myDb.updateData_weight(String.valueOf(dbId), weightData);
                if(Float.parseFloat(cursor.getString(5)) == 0.0) {
                    if (isUpdated == true) {
                        Toast.makeText(OfflineActivity.this, "Data Weight Inserted", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(OfflineActivity.this, "Data Weight not Inserted", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if (isUpdated == true)
                    {
                        Toast.makeText(OfflineActivity.this, "Data Weight Updated", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(OfflineActivity.this, "Data Weight not Updated", Toast.LENGTH_LONG).show();
                    }
                }
                viewUser();
            }
        }
    }

    void BMI_Rechner(String weight, String height)
    {
        float calc_weight = Float.parseFloat(weight);
        float calc_height = Float.parseFloat(height) /100;

        float BMI = calc_weight / (calc_height*calc_height);

        BMI = (float)Math.round(BMI * 10) / 10;

        txtBMI.setText(Float.toString(BMI));
        txtBMI.startAnimation(AnimationUtils.loadAnimation(OfflineActivity.this, android.R.anim.slide_in_left));

    }

    public void updateSpinner() {
        names.clear();

        Cursor cursor = myDb.getAllUser();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            names.add(name);
        }
        spinner.setAdapter(adapter);
    }

    private void viewUser()
    {
        Cursor cursor = myDb.getAllUser2();

        while (cursor.moveToNext()) {
            int rowNumber = Integer.parseInt(cursor.getString(6));
            int spinnerId = spinner.getSelectedItemPosition();

            if (rowNumber == spinnerId) {

                //String weight = getWeight(spinnerId);

                String name = cursor.getString(1);
                String surname = cursor.getString(2);
                String gender = cursor.getString(3);
                String height = cursor.getString(4);
                String weight = cursor.getString(5);

                txtNachname.setText(surname);
                txtGeschlecht.setText(gender);
                txtGroesse.setText(height);

                txtGewicht.setText(weight);
                txtGewicht.startAnimation(AnimationUtils.loadAnimation(OfflineActivity.this, android.R.anim.slide_in_left));

                BMI_Rechner(weight, height);

            }
        }
    }

    public void AddUser(){

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.user_dialog, null);

        final EditText etVorname     = (EditText) alertLayout.findViewById(et_vorname);
        final EditText etNachname     = (EditText) alertLayout.findViewById(et_nachname);
        final SeekBar groesseIn = (SeekBar) alertLayout.findViewById(R.id.groesseBar);
        final TextView groesseOut = (TextView) alertLayout.findViewById(R.id.groesseCm);
        final RadioGroup radioGroupGender = (RadioGroup) alertLayout.findViewById(R.id.radioGender);





        int progress = 150;
        int progressMin = 120;
        int progressMax = 230;

        groesseIn.setMax(progressMax);
        groesseIn.setProgress(progress);

        groesseOut.setText(Integer.toString(progress));


        SeekBar.OnSeekBarChangeListener yourSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //add code here
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //add code here
            }

            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                progressValue = groesseIn.getProgress();
                groesseOut.setText(Integer.toString(progressValue));
            }
        };
        groesseIn.setOnSeekBarChangeListener(yourSeekBarListener);



        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Register");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setPositiveButton("Register", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String vorname = etVorname.getText().toString();
                String nachname = etNachname.getText().toString();
                String groesse = groesseOut.getText().toString();

                String weight = "0.0";

                String gender = "";
                int selectedId = radioGroupGender.getCheckedRadioButtonId();

                switch (selectedId) {
                    case R.id.radioFemale:
                        gender = "Weiblich";
                        break;
                    case R.id.radioMale:
                        gender = "Männlich";
                        break;
                }


                boolean isInserted = myDb.insertData(vorname, nachname, gender, groesse, weight);

                if (isInserted == true) {
                    Toast.makeText(OfflineActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    updateSpinner();
                } else {
                    Toast.makeText(OfflineActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_offline, menu);

        bluetoothStatus = menu.findItem(R.id.action_bluetooth_status);
        addUser = menu.findItem(R.id.add_user);

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
        else if (id == R.id.add_user){
            AddUser();
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        viewUser();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
