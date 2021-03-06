package com.example.oli.scaleuser2.core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.oli.scaleuser2.gui.LoginGui;
import com.example.oli.scaleuser2.gui.OnlineActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Hilfsklasse für die Login Gui.
 * Sie ermöglicht zeitaufwendige Hintergrundaufgaben in einem Arbeitsthread auszuführen
 * um die empfangene Benutzerdaten vom Server auf die Benutzeroberfläche anzuzeigen bzw.
 * zu aktualisieren.
 *
 * @author Oliver Dziedzic, Mamoudou Balde
 *
 * @version 1.0
 */

public class LoginHelper extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;


    /**
     * Konstruktor
     *
     * @param ctx der Kontext der Klasse
     */
    public LoginHelper(Context ctx) {
        context = ctx;
    }

    /**
     * wird verwendet, um Hintergrundoperationen wie das Erhalten von Daten
     * vom Server usw. durchzuführen.
     * Hier wird ein Json Array von der URL empfangen.
     *
     * @param params die übergebene Parameter
     *
     * @return die empfangene Benutzerdaten
     */
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        //String login_url =      "http://10.9.40.159:80/webapp/login.php";
        //String register_url =   "http://10.9.40.159:80/webapp/register.php";
        String login_url = "http://10.9.42.55:80/webapp/login.php";
        String register_url = "http://10.9.42.55:80/webapp/register.php";
        //String login_url = "http://192.168.0.16:80/webapp/login.php";
        //String register_url = "http://192.168.0.16:80/webapp/register.php";

        if(type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null){
                    result+= line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception:"+e.getMessage();
            }

        }
        if(type.equals("register")) {
            try {
                String name = params[1];
                String nachname = params[2];
                String birthday = params[3];
                String gender = params[4];
                String user_name = params[5];
                String password = params[6];
                String groesse = params[7];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                        +URLEncoder.encode("nachname","UTF-8")+"="+URLEncoder.encode(nachname,"UTF-8")+"&"
                        +URLEncoder.encode("birthday","UTF-8")+"="+URLEncoder.encode(birthday,"UTF-8")+"&"
                        +URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode(gender,"UTF-8")+"&"
                        +URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                        +URLEncoder.encode("groesse","UTF-8")+"="+URLEncoder.encode(groesse,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null){
                    result+= line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception:"+e.getMessage();
            }

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        // super.onPreExecute();

    }

    /**
     * wird verwendet, um die Benutzeroberfläche zu aktualisieren,
     * nachdem der Hintergrundprozess abgeschlossen ist.
     * Hier werden die umgewandelte Json Daten nach dem Empfang in Textview angezeigt.
     *
     * @param result das Ergebnis nach Abschluss der Hintergrundberechnung
     *
     */
    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(aVoid);
        Intent intent = new Intent(context, OnlineActivity.class);
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

        if (result.contains("login success"))
        {
            context.startActivity(intent);
        }

        LoginGui.loading.dismiss();

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}

