package com.example.oli.scaleuser2.gui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oli.scaleuser2.R;
import com.example.oli.scaleuser2.core.BluetoothCommunication;
import com.example.oli.scaleuser2.core.BluetoothMiScale;
import com.example.oli.scaleuser2.core.Calculator;
import com.example.oli.scaleuser2.core.LoginHelper;
import com.example.oli.scaleuser2.core.UserBtHelp;
import com.example.oli.scaleuser2.core.UserDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.oli.scaleuser2.R.id.et_birthday;
import static com.example.oli.scaleuser2.R.id.et_nachname;
import static com.example.oli.scaleuser2.R.id.et_vorname;

/**
 * Zum Anzeigen, Aktualisieren und Löschen der Gewichtsdaten eines Benutzers im Offline modus.
 *
 * @author Oliver Dziedzic, Mamoudou Balde
 *
 * @version 1.0
 */

public class OfflineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    int progressValue;
    private static MenuItem bluetoothStatus, addUser, updateUser, deleteUser;
    private static int bluetoothStatusIcon = R.mipmap.bluetooth_disabled;
    private static boolean firstAppStart = true;
    UserDatabase myDb;

    TextView txtNachname, txtAge, txtGroesse, txtGeschlecht, txtGewicht, txtBMR, txtBMI;
    Button adduser;
    Spinner spinner;

    ArrayList<String> names = new ArrayList<>();
    ArrayAdapter<String> adapter;


    /**
     * Wird aufgerufen, wenn die Activity das erste mal erstellt wird.
     * Views werden initialisiert und Diensten werden gestartet.
     *
     * @param savedInstanceState über diesem Parameter können Werte aus der Activity
     *                           zwischen gespeichert werden.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        myDb = new UserDatabase(this);

        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        spinner.setOnItemSelectedListener(this);

        txtNachname = (TextView) findViewById(R.id.nachnameOut);
        txtAge = (TextView) findViewById(R.id.ageOut);
        txtGroesse = (TextView) findViewById(R.id.groesseOut);
        txtGeschlecht = (TextView) findViewById(R.id.genderOut);
        txtGewicht = (TextView) findViewById(R.id.gewichtOut);
        txtBMI = (TextView) findViewById(R.id.BMIOut);
        txtBMR = (TextView) findViewById(R.id.BMROut);

        adduser = (Button) findViewById(R.id.add_user);

        check_user();
        updateSpinner();
        viewUser();
        //checkBtPermissions();

    }

    /**
     * Prüft beim Starten der Activity, ob einen Benutzer schon in der Datenbank eingefügt wurde.
     * Wenn es nicht der Fall ist, wird eine Nachricht angezeigt
     * um einen neuen Benutzer zu erstellen und einzufügen.
     */
    public void check_user(){

        Cursor cursor = myDb.getAllUser2();
        if(cursor.getCount() == 0) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            AddUser();
                            break;

                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need to Create a User to use this Application").setPositiveButton("Ok", dialogClickListener).show();
        }
    }

    /**
     * Einfügen und Anzeigen der Gewichtsdaten(Gewicht, Bmi, Bmr) eines Benutzers.
     */
    private void add_weight() {

        String weight = Float.toString(BluetoothMiScale.weightdata);
        txtGewicht.setText(weight);
        updateWeight();
        //Toast.makeText(MainActivity.this, "Weightdata: " +weight, Toast.LENGTH_LONG).show();
    }

    /**
     * Einfügen und Aktualisieren der Gewichtsdaten(Gewicht, Bmi, Bmr) eines Benutzers.
     */
    private void updateWeight()
    {
        String weightData = txtGewicht.getText().toString();
        Cursor cursor = myDb.getAllUser2();
        while(cursor.moveToNext())
        {
            int rowCount = Integer.parseInt(cursor.getString(7));
            int dbId = Integer.parseInt(cursor.getString(0));
            int spinnerId = spinner.getSelectedItemPosition();

            if (rowCount == spinnerId)
            {
                boolean isUpdated = myDb.updateData_weight(String.valueOf(dbId), weightData);
                if(Float.parseFloat(cursor.getString(6)) == 0.0) {
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

    /**
     * Aktualisieren der Spinner View bei jedem neuen Benutzer
     */
    public void updateSpinner() {
        names.clear();

        Cursor cursor = myDb.getAllUser();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            names.add(name);
        }
        spinner.setAdapter(adapter);
    }

    /**
     * Anzeigen alle Gewichtsdaten eines Benutzers.
     */
    private void viewUser()
    {
        Cursor cursor = myDb.getAllUser2();

        while (cursor.moveToNext()) {
            int rowNumber = Integer.parseInt(cursor.getString(7));
            int spinnerId = spinner.getSelectedItemPosition();

            if (rowNumber == spinnerId) {

                //String weight = getWeight(spinnerId);

                String name = cursor.getString(1);
                String surname = cursor.getString(2);
                String birthday = cursor.getString(3);
                String gender = cursor.getString(4);
                String height = cursor.getString(5);
                String weight = cursor.getString(6);

                long age = Calculator.age_Calculator(birthday);
                String bmi = Calculator.BMI_Calculator(weight, height);
                String bmr = Calculator.BMR_Calculator(weight, height, age, gender);

                if(Float.parseFloat(weight) != 0.0)
                {
                    String bmiTable = Calculator.bmiTable(gender, bmi, age);
                    Toast.makeText(OfflineActivity.this, "Bmi Categorie: " +bmiTable, Toast.LENGTH_SHORT).show();
                }

                //AUSGABE

                txtNachname.setText(surname);
                //txtNachname.startAnimation(AnimationUtils.loadAnimation(OfflineActivity.this, android.R.anim.slide_in_left));

                txtAge.setText(String.valueOf(age) + " years");
               //txtAge.startAnimation(AnimationUtils.loadAnimation(OfflineActivity.this, android.R.anim.slide_in_left));

                txtGeschlecht.setText(gender);
                //txtGeschlecht.startAnimation(AnimationUtils.loadAnimation(OfflineActivity.this, android.R.anim.slide_in_left));

                txtGroesse.setText(height + " cm");
                //txtGroesse.startAnimation(AnimationUtils.loadAnimation(OfflineActivity.this, android.R.anim.slide_in_left));

                txtGewicht.setText(weight);
                txtGewicht.startAnimation(AnimationUtils.loadAnimation(OfflineActivity.this, android.R.anim.slide_in_left));

                txtBMR.setText(bmr);
                txtBMR.startAnimation(AnimationUtils.loadAnimation(OfflineActivity.this, android.R.anim.slide_in_left));

                txtBMI.setText(bmi);
                txtBMI.startAnimation(AnimationUtils.loadAnimation(OfflineActivity.this, android.R.anim.slide_in_left));


            }
        }
    }


    /**
     * Einfügen einen neuen Benutzer.
     */
    public void AddUser(){

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.user_dialog, null);

        final EditText etVorname     = (EditText) alertLayout.findViewById(et_vorname);
        final EditText etNachname     = (EditText) alertLayout.findViewById(et_nachname);
        final EditText etBirthday     = (EditText) alertLayout.findViewById(et_birthday);
        final SeekBar groesseIn = (SeekBar) alertLayout.findViewById(R.id.groesseBar);
        final TextView groesseOut = (TextView) alertLayout.findViewById(R.id.groesseCm);
        final RadioGroup radioGroupGender = (RadioGroup) alertLayout.findViewById(R.id.radioGender);
        final LoginHelper loginhelper = new LoginHelper(this);
        final Context context;
        context = this;

        etBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar cal = Calendar.getInstance();
                    final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                            //etBirthday.setText(String.format("%02d.%02d.%04d", selectedDay, selectedMonth + 1, selectedYear));
                            etBirthday.setText(String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay));
                        }
                    };

                    DatePickerDialog datePicker = new DatePickerDialog(context, datePickerListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    datePicker.show();
                }
            }
        });

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
        alert.setTitle("Create User");
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

        alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String vorname = etVorname.getText().toString();
                String birthday = etBirthday.getText().toString();
                String nachname = etNachname.getText().toString();
                String groesse = groesseOut.getText().toString();

                String weight = "0.0";

                String gender = "";
                int selectedId = radioGroupGender.getCheckedRadioButtonId();

                switch (selectedId) {
                    case R.id.radioFemale:
                        gender = "Female";
                        break;
                    case R.id.radioMale:
                        gender = "Male";
                        break;
                }


                boolean isInserted = myDb.insertData(vorname, nachname, birthday, gender, groesse, weight);

                if (isInserted == true) {
                    Toast.makeText(OfflineActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    updateSpinner();
                    viewUser();
                } else {
                    Toast.makeText(OfflineActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    /**
     * Aktualisieren der Daten eines Benutzers.
     */
    public void UpdateUser()
    {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.user_dialog, null);

        final EditText etVorname     = (EditText) alertLayout.findViewById(et_vorname);
        final EditText etNachname     = (EditText) alertLayout.findViewById(et_nachname);
        final EditText etBirthday     = (EditText) alertLayout.findViewById(et_birthday);
        final SeekBar groesseIn = (SeekBar) alertLayout.findViewById(R.id.groesseBar);
        final TextView groesseOut = (TextView) alertLayout.findViewById(R.id.groesseCm);
        final RadioGroup radioGroupGender = (RadioGroup) alertLayout.findViewById(R.id.radioGender);
        final Context context;
        context = this;

        etBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar cal = Calendar.getInstance();
                    final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                            //etBirthday.setText(String.format("%02d.%02d.%04d", selectedDay, selectedMonth + 1, selectedYear));
                            etBirthday.setText(String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay));
                        }
                    };

                    DatePickerDialog datePicker = new DatePickerDialog(context, datePickerListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    datePicker.show();
                }
            }
        });

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
        alert.setTitle("Update User");
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

        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String vorname = etVorname.getText().toString();
                String nachname = etNachname.getText().toString();
                String birthday = etBirthday.getText().toString();
                String groesse = groesseOut.getText().toString();

                int selectedId = radioGroupGender.getCheckedRadioButtonId();

                String gender ="";
                switch (selectedId) {
                    case R.id.radioFemale:
                        gender = "Female";
                        break;
                    case R.id.radioMale:
                        gender = "Male";
                        break;
                }

                Cursor cursor = myDb.getAllUser2();

                while (cursor.moveToNext()) {
                    int rowNumber = Integer.parseInt(cursor.getString(7));
                    int dbId = Integer.parseInt(cursor.getString(0));
                    int spinnerId = spinner.getSelectedItemPosition();

                    if (rowNumber == spinnerId) {
                        boolean isUpdate = myDb.updateData(String.valueOf(dbId), vorname, nachname, birthday, gender, groesse);
                        if (isUpdate == true) {
                            Toast.makeText(OfflineActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
                            viewUser();
                            updateSpinner();
                        } else {
                            Toast.makeText(OfflineActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
                        }
                    }

                }

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    /**
     * Löschen einen Benutzer.
     */
    public void DeleteUser()
    {
        Cursor cursor = myDb.getAllUser2();

        while (cursor.moveToNext()) {
            int rowNumber = Integer.parseInt(cursor.getString(7));
            int dbId = Integer.parseInt(cursor.getString(0));
            int spinnerId = spinner.getSelectedItemPosition();

            if (rowNumber == spinnerId) {
                Integer isRowDeleted = myDb.deleteData(String.valueOf(dbId));
                if (isRowDeleted > 0) {
                    Toast.makeText(OfflineActivity.this, "User Deleted", Toast.LENGTH_LONG).show();
                    updateSpinner();
                    viewUser();
                } else {
                    Toast.makeText(OfflineActivity.this, "User not Deleted", Toast.LENGTH_LONG).show();
                }
            }

        }
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
        getMenuInflater().inflate(R.menu.main_offline, menu);

        bluetoothStatus = menu.findItem(R.id.action_bluetooth_status);
        addUser = menu.findItem(R.id.add_user);
        updateUser = menu.findItem(R.id.update_user);
        deleteUser = menu.findItem(R.id.delete_user);

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
        else if (id == R.id.add_user){
            AddUser();
        }
        else if (id == R.id.update_user){
            UpdateUser();

        }
        else if (id == R.id.delete_user){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            DeleteUser();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete User");
            builder.setMessage("Are you Sure? You will lose all your Data from this User").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        viewUser();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
