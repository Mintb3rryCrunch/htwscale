package com.example.oli.scaleuser2.gui;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oli.scaleuser2.R;
import com.example.oli.scaleuser2.core.BluetoothCommunication;
import com.example.oli.scaleuser2.core.BluetoothMiScale;
import com.example.oli.scaleuser2.core.Calculator;
import com.example.oli.scaleuser2.core.OnlineHelper;
import com.example.oli.scaleuser2.core.UserBtHelp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Zum Anzeigen und Aktualisieren der Gewichtsdaten eines Benutzers
 * mit Hilfe der OnlineHelper Klasse im Online modus.
 *
 * @author Oliver Dziedzic, Mamoudou Balde
 *
 * @version 1.0
 */

public class OnlineActivity extends AppCompatActivity {
    private static TextView status, txtName, txtSurname, txtBirthday, txtGender, txtGroesse, txtGewicht, txtUsername, txtBMI, txtBMR;
    private  static ProgressDialog loading;

    public static String user_id, name, surname, birthday, gender, username, groesse, gewicht, bmi, bmr;
    private static long age;
    public  static String bmiTable;

    public static MenuItem bluetoothStatus, uploadData;
    public static int bluetoothStatusIcon = R.mipmap.bluetooth_disabled, uploadDataIcon = R.mipmap.update_data;
    private static boolean firstAppStart = true;
    FloatingActionButton myFabOption, myFab, myFabCommunity;
    Animation FabOpen, FabClose, FabRClockwise, FabRanticlockwise;
    boolean isOpen = false;

    /**
     * Wird aufgerufen, wenn die Activity das erste mal erstellt wird.
     * Views werden initialisiert und Diensten werden gestartet.
     *
     * @param savedInstanceState Über diesem Parameter können Werte aus der Activity
     *                           zwischen gespeichert werden.
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        txtName = (TextView) findViewById(R.id.nameOut);
        txtSurname = (TextView) findViewById(R.id.surnameOut);
        txtBirthday = (TextView) findViewById(R.id.ageOut);
        txtGender = (TextView) findViewById(R.id.genderOut);
        txtGroesse = (TextView) findViewById(R.id.groesseOut);
        txtGewicht = (TextView) findViewById(R.id.gewichtOut);
        txtBMI = (TextView) findViewById(R.id.BMIOut);
        txtBMR = (TextView) findViewById(R.id.BMROut);

        getJson();

        myFabOption = (FloatingActionButton) findViewById(R.id.myFabOption);
        myFab = (FloatingActionButton) findViewById(R.id.myFAB);
        myFabCommunity = (FloatingActionButton) findViewById(R.id.myFabCommunity);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabRanticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        myFabOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOpen)
                {
                   fabCloseAnimation();
                }
                else
                {
                   fabOpenAnimation();
                }

            }
        });

        //checkBtPermissions();

    }

    /**
     * Bei Klick wird der Floating Action Button geöffnet
     * um die Timeline und Community Buttons auszuführen.
     */
    public void fabOpenAnimation()
    {
        myFab.startAnimation(FabOpen);
        myFabCommunity.startAnimation(FabOpen);
        myFabOption.startAnimation(FabRClockwise);
        myFab.setClickable(true);
        myFabCommunity.setClickable(true);
        isOpen = true;
    }

    /**
     * Bei Klick wird der Floating Action Button geschlossen.
     * Die Timeline und Community Buttons sind nicht klickbar.
     */
    public void fabCloseAnimation()
    {
        myFab.startAnimation(FabClose);
        myFabCommunity.startAnimation(FabClose);
        myFabOption.startAnimation(FabRanticlockwise);
        myFab.setClickable(false);
        myFabCommunity.setClickable(false);
        isOpen = false;
    }


    /**
     * Bei Klick wird die Timeline Activity aufgerufen und gestartet
     * um die Gewichtsdaten eines Benutzers in einem History Graph zu visualisieren.
     *
     * @param v graphisches Element, mit dem der Benutzer interagieren kann
     *
     */
    public void onTimeline(View v) {
        final Intent intent = new Intent(OnlineActivity.this, TimelineActivity.class);
        startActivity(intent);
        fabCloseAnimation();


    }

    /**
     * Bei Klick wird die Community Activity aufgerufen und gestartet
     * um die Gewichtsdaten alle Benutzer in einem Graph zu visualisieren.
     *
     * @param v graphisches Element, mit dem der Benutzer interagieren kann
     *
     */
    public void onCommunity(View v){
        final Intent intent = new Intent(OnlineActivity.this, CommunityActivity.class);
        startActivity(intent);
        fabCloseAnimation();
    }

    /**
     * Gibt  die empfangene Daten als Json Format zurueck.
     */
    private void getJson(){

        loading = ProgressDialog.show(OnlineActivity.this, "Please Wait...",null,true,true);
        String type = "getData";
        OnlineHelper onlineHelper = new OnlineHelper(this);
        onlineHelper.execute(type);

        txtName.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        txtSurname.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        txtBirthday.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        txtGender.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        txtGroesse.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        txtGewicht.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        txtBMI.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        txtBMR.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));

    }

    /**
     * Wandelt die empfangene Json Daten in String format um.
     *
     * @param jsonString die gegebene Json Daten
     *
     */
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
                birthday = JO.getString("birthday");
                gender = JO.getString("gender");
                groesse = JO.getString("groesse");
                gewicht = JO.getString("gewicht");
                bmi = JO.getString("bmi");
                bmr = JO.getString("bmr");
                username = JO.getString("username");

                age = Calculator.age_Calculator(birthday);

                txtName.setText(name);
                txtSurname.setText(surname);
                txtBirthday.setText(String.valueOf(age) + " years");
                txtGender.setText(gender);
                txtGroesse.setText(groesse + " cm");
                txtGewicht.setText(gewicht);
                txtBMI.setText(bmi);
                txtBMR.setText(bmr);


            loading.dismiss();

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Einfügen, Aktualisieren und Anzeigen der Gewichtsdaten(Gewicht, Bmi, Bmr)
     * eines Benutzers.
     */
    private void add_weight() {

        gewicht = Float.toString(BluetoothMiScale.weightdata);
        txtGewicht.setText(gewicht);
        txtGewicht.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
        //Toast.makeText(MainActivity.this, "Weightdata: " +weight, Toast.LENGTH_LONG).show();
        bmi = Calculator.BMI_Calculator(gewicht, groesse);
        txtBMI.setText(bmi);
        bmiTable = Calculator.bmiTable(gender, bmi, age);
        Toast.makeText(OnlineActivity.this, "Bmi Categorie: " +bmiTable, Toast.LENGTH_LONG).show();
        txtBMI.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));

        bmr = Calculator.BMR_Calculator(gewicht, groesse, age, gender);
        txtBMR.setText(bmr);
        txtBMR.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_in_left));
    }

    /**
     * Einfügen und Anzeigen einen Menüeintrag in der Action Bar.
     *
     * @param menu der gegebene Menüeintrag
     *
     * @return true, wenn der Menüeintrag in der Action Bar eingefügt ist, sonst false
     *
     */
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

    /**
     * Wird aufgerufen um nach der Bluetoothgerät zu suchen.
     * Wenn dieses gefunden ist, wird die Verbindung hergestellt.
     */
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
        UserBtHelp.getInstance(getApplicationContext()).startSearchingForBluetooth(Integer.parseInt(deviceType), deviceName, callbackBtHandler);
    }

    /**
     * Überprüft, ob der Menüeintrag angeklickt wurde
     * und führt die gewünschte Aktion aus.
     *
     * @param item der gegebene Menüeintrag
     *
     * @return true, wenn der Menüeintrag angeklickt wurde, sonst false
     *
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_bluetooth_status) {
            invokeSearchBluetoothDevice();
            return true;
        }

        else if (id == R.id.update_data){
            String type = "updateData";
            OnlineHelper onlineHelper = new OnlineHelper(this);
            onlineHelper.execute(type, user_id, gewicht, bmi, bmr);

            txtBMI.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_out_right));
            txtBMR.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_out_right));
            txtGewicht.startAnimation(AnimationUtils.loadAnimation(OnlineActivity.this, android.R.anim.slide_out_right));

            uploadData.setIcon(getResources().getDrawable(R.mipmap.data_update));
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Anzeige der Bluetoothiconstatus.
     *
     * @param iconRessource das gegebene Bluetoothicon
     *
     */
    private void setBluetoothStatusIcon(int iconRessource) {
        bluetoothStatusIcon = iconRessource;
        bluetoothStatus.setIcon(getResources().getDrawable(bluetoothStatusIcon));
    }

    /**
     * Anzeige der Meldung über den aktuellen Bluetoothstatus.
     */
    private final Handler callbackBtHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case BluetoothCommunication.BT_RETRIEVE_SCALE_DATA:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_connected);
                    add_weight();
                    break;
                case BluetoothCommunication.BT_INIT_PROCESS:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_connected);
                    Toast.makeText(getApplicationContext(), "Initialize Bluetooth device", Toast.LENGTH_SHORT).show();
                    Log.d("HTWScale", "Bluetooth initializing");
                    break;
                case BluetoothCommunication.BT_CONNECTION_LOST:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_disabled);
                    Toast.makeText(getApplicationContext(), "Lost Bluetooth Connection", Toast.LENGTH_SHORT).show();
                    Log.d("HTWScale", "Bluetooth connection lost");
                    break;
                case BluetoothCommunication.BT_NO_DEVICE_FOUND:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_disabled);
                    Toast.makeText(getApplicationContext(), "No Bluetooth device found", Toast.LENGTH_SHORT).show();
                    Log.d("HTWScale", "No Bluetooth device found");
                    break;
                case BluetoothCommunication.BT_CONNECTION_ESTABLISHED:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_connected);
                    Toast.makeText(getApplicationContext(),"Connection successful", Toast.LENGTH_SHORT).show();
                    Log.d("HTWScale", "Bluetooth connection successful established");
                    break;
                case BluetoothCommunication.BT_UNEXPECTED_ERROR:
                    setBluetoothStatusIcon(R.mipmap.bluetooth_disabled);
                    Toast.makeText(getApplicationContext(), "Bluetooth has an unexcepted Error" + ": " + msg.obj, Toast.LENGTH_SHORT).show();
                    Log.e("HTWScale", "Bluetooth unexpected error: " + msg.obj);
                    break;
            }
        }
    };



}



