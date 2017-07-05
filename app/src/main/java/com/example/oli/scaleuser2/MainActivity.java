package com.example.oli.scaleuser2;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static MenuItem bluetoothStatus;
    private static int bluetoothStatusIcon = R.mipmap.bluetooth_disabled;
    private static boolean firstAppStart = true;
    UserDatabase myDb;
    ScaleDatabase waageDb;
    EditText editName, editSurname, editHeigth;
    RadioGroup radioGroupGender;
    Button btnaddData;
    Button btnviewData;
    Button btnUpdate;
    Button btnDelete;
    String gender;
    Spinner spinner;

    ArrayList<String> names = new ArrayList<>();
    ArrayAdapter<String> adapter;


    private EditText adduser;
    private TextView userout;
    private  TextView weightTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new UserDatabase(this);
        waageDb = new ScaleDatabase(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        editName = (EditText) findViewById(R.id.editName);
        editSurname = (EditText) findViewById(R.id.editSurname);
        radioGroupGender = (RadioGroup) findViewById(R.id.radioGender);

        weightTxt = (TextView) findViewById(R.id.weightdata);

        editHeigth = (EditText) findViewById(R.id.editGroesse);
        btnaddData = (Button) findViewById(R.id.addData);
        btnviewData = (Button) findViewById(R.id.viewData);
        btnUpdate = (Button) findViewById(R.id.updateData);
        btnDelete = (Button) findViewById(R.id.deleteData);
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        spinner.setOnItemSelectedListener(this);


        AddData();
        updateSpinner();
        viewData();
        update_Data();
        delete_Data();
        checkBtPermissions();



    }

    public void AddData() {
        btnaddData.setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View v) {

                        int selectedId = radioGroupGender.getCheckedRadioButtonId();

                        switch (selectedId) {
                            case R.id.radioFemale:
                                gender = "Weiblich";
                                break;
                            case R.id.radioMale:
                                gender = "Männlich";
                                break;
                        }
                        try {
                            String weight = "0.0";
                            boolean isInserted = myDb.insertData(editName.getText().toString(), editSurname.getText().toString(), gender, editHeigth.getText().toString(), weight);
                            if (isInserted == true) {
                                Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                                editName.setText("");
                                editSurname.setText("");
                                editHeigth.setText("");
                                updateSpinner();
                            } else {
                                Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception ex)
                        {
                            Log.e("Mainactivity ", "Error beim Insert Button:" + ex.getMessage());
                        }
                    }
                }
        );

    }

    public void update_Data() {
        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        int selectedId = radioGroupGender.getCheckedRadioButtonId();

                        switch (selectedId) {
                            case R.id.radioFemale:
                                gender = "Weiblich";
                                break;
                            case R.id.radioMale:
                                gender = "Männlich";
                                break;
                        }

                        Cursor cursor = myDb.getAllUser2();

                        while (cursor.moveToNext()) {
                            int rowNumber = Integer.parseInt(cursor.getString(6));
                            int dbId = Integer.parseInt(cursor.getString(0));
                            int spinnerId = spinner.getSelectedItemPosition();

                            if (rowNumber == spinnerId) {
                                boolean isUpdate = myDb.updateData(String.valueOf(dbId), editName.getText().toString(), editSurname.getText().toString(), gender, editHeigth.getText().toString());
                                if (isUpdate == true) {
                                    Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
                                    editName.setText("");
                                    editSurname.setText("");
                                    editHeigth.setText("");

                                    updateSpinner();
                                } else {
                                    Toast.makeText(MainActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }
                }
        );
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


    public void viewData() {
        try {
            btnviewData.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, UserView.class);

                            Cursor cursor = myDb.getAllUser2();

                            while (cursor.moveToNext()) {
                                int rowNumber = Integer.parseInt(cursor.getString(6));
                                int spinnerId = spinner.getSelectedItemPosition();

                                if (rowNumber == spinnerId) {

                                    String weight = getWeight(spinnerId);

                                    String name = cursor.getString(1);
                                    String surname = cursor.getString(2);
                                    String gender = cursor.getString(3);
                                    String heigth = cursor.getString(4);

                                    intent.putExtra("Extra_name", name);
                                    intent.putExtra("Extra_surname", surname);
                                    intent.putExtra("Extra_gender", gender);
                                    intent.putExtra("Extra_heigth", heigth);
                                    intent.putExtra("Extra_weight", weight);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
            );
        } catch (Exception ex) {
            Log.e("Viewdataactivity", "Fehler beim viewallbutton" + ex.getMessage());
        }
    }

    private String getWeight (int spinnerId)
    {
        Cursor cursor = myDb.getAllUser2();
        String weight = "";
        while (cursor.moveToNext())
        {
            int rowCount = Integer.parseInt(cursor.getString(6));
            if(rowCount == spinnerId)
            {
                weight = cursor.getString(5);
            }
        }
        return weight;
    }

    public void delete_Data() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Cursor cursor = myDb.getAllUser2();

                        while (cursor.moveToNext()) {
                            int rowNumber = Integer.parseInt(cursor.getString(6));
                            int dbId = Integer.parseInt(cursor.getString(0));
                            int spinnerId = spinner.getSelectedItemPosition();

                            if (rowNumber == spinnerId) {
                                Integer isRowDeleted = myDb.deleteData(String.valueOf(dbId));
                                if (isRowDeleted > 0) {
                                    Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                                    updateSpinner();
                                } else {
                                    Toast.makeText(MainActivity.this, "Data not Deleted", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }
                }
        );
    }

    private void add_weight() {

                        String weight = Float.toString(BluetoothMiScale.weightdata);
                        weightTxt.setText(weight);
                        updateWeight();
                        //Toast.makeText(MainActivity.this, "Weightdata: " +weight, Toast.LENGTH_LONG).show();
    }

    private void updateWeight()
    {
        String weightData = weightTxt.getText().toString();
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
                        Toast.makeText(MainActivity.this, "Data Weight Inserted", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Data Weight not Inserted", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if (isUpdated == true)
                    {
                        Toast.makeText(MainActivity.this, "Data Weight Updated", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Data Weight not Updated", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

 /*   public void viewList()
    {
        btnviewAll.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        names.clear();

                        Cursor cursor = myDb.getAllUser();
                        while (cursor.moveToNext()) {
                            String name = cursor.getString(1);
                            names.add(name);
                        }
                        spinner.setAdapter(adapter);
                    }
                });
    } */

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        bluetoothStatus = menu.findItem(R.id.action_bluetooth_status);

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
                    setBluetoothStatusIcon(R.mipmap.bluetooth_connection_lost);
                    Toast.makeText(getApplicationContext(), "Lost Bluetooth Connection", Toast.LENGTH_SHORT).show();
                    Log.d("OpenScale", "Bluetooth connection lost");
                    break;
                case BluetoothCommunication.BT_NO_DEVICE_FOUND:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_connection_lost);
                    Toast.makeText(getApplicationContext(), "No Bluetooth device found", Toast.LENGTH_SHORT).show();
                    Log.d("OpenScale", "No Bluetooth device found");
                    break;
                case BluetoothCommunication.BT_CONNECTION_ESTABLISHED:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_connected);
                    Toast.makeText(getApplicationContext(),"Connection successful", Toast.LENGTH_SHORT).show();
                    Log.d("OpenScale", "Bluetooth connection successful established");
                    break;
                case BluetoothCommunication.BT_UNEXPECTED_ERROR:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_connection_lost);
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}