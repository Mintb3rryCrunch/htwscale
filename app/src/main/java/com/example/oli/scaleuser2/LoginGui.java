package com.example.oli.scaleuser2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Oli on 21.06.2017.
 */


public class LoginGui extends AppCompatActivity implements TextWatcher,
        CompoundButton.OnCheckedChangeListener {
    public EditText user_name, passwd;
    public static String login_username;
    public static ProgressDialog loading;

    private CheckBox rem_userpass;
    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASS = "password";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int progressValue;
    AlertDialog alertDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_name = (EditText) findViewById(R.id.username);
        user_name.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        passwd = (EditText) findViewById(R.id.password);
        rem_userpass = (CheckBox)findViewById(R.id.cb_remember);

        if(sharedPreferences.getBoolean(KEY_REMEMBER, false))
            rem_userpass.setChecked(true);
        else
            rem_userpass.setChecked(false);

        user_name.setText(sharedPreferences.getString(KEY_USERNAME,""));
        passwd.setText(sharedPreferences.getString(KEY_PASS,""));

        user_name.addTextChangedListener(this);
        passwd.addTextChangedListener(this);
        rem_userpass.setOnCheckedChangeListener(this);



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }


    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        managePrefs();
    }


    public void afterTextChanged(Editable editable) {

    }


    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        managePrefs();
    }

    private void managePrefs(){
        if(rem_userpass.isChecked()){
            editor.putString(KEY_USERNAME, user_name.getText().toString().trim());
            editor.putString(KEY_PASS, passwd.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        }else{
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS);//editor.putString(KEY_PASS,"");
            editor.remove(KEY_USERNAME);//editor.putString(KEY_USERNAME, "");
            editor.apply();
        }
    }

    public void onLogin(View v){
        login_username = user_name.getText().toString();
        String password = passwd.getText().toString();
        String type = "login";

        loading = ProgressDialog.show(LoginGui.this, "Please Wait...",null,true,true);

        LoginHelper loginHelper = new LoginHelper(this);
        loginHelper.execute(type, login_username, password);




    }

    public void onOffline(View v){
        final Intent intent = new Intent(LoginGui.this, MainActivity.class);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sind sie sicher? Nutzen sie den Offline Modus, haben sie keine Berechtigungen auf die Onlinefunktion zu nutzen").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    public void onRegister(View v){

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.register_dialog, null);
        final EditText etUsername = (EditText) alertLayout.findViewById(R.id.et_username);
        final EditText etPassword = (EditText) alertLayout.findViewById(R.id.et_password);
        final EditText etName     = (EditText) alertLayout.findViewById(R.id.et_name);
        final SeekBar groesseIn = (SeekBar) alertLayout.findViewById(R.id.groesseBar);
        final TextView groesseOut = (TextView) alertLayout.findViewById(R.id.groesseCm);
        final CheckBox cbShowPassword = (CheckBox) alertLayout.findViewById(R.id.cb_show_password);
        final LoginHelper loginhelper = new LoginHelper(this);

        etName.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etUsername.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);



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

        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // to encode password in dots
                    etPassword.setTransformationMethod(null);
                } else {
                    // to display the password in normal text
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Register");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setPositiveButton("Register", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etName.getText().toString();
                String groesse = groesseOut.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String type = "register";

                loginhelper.execute(type, name, username, password, groesse);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }
}



