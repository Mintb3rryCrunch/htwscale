package com.example.oli.scaleuser2.core;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.oli.scaleuser2.gui.CommunityActivity;
import com.example.oli.scaleuser2.gui.LoginGui;
import com.example.oli.scaleuser2.gui.OnlineActivity;
import com.example.oli.scaleuser2.R;
import com.example.oli.scaleuser2.gui.TimelineActivity;

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
 * Hilfsklasse für die Online Gui.
 * Sie ermöglicht zeitaufwendige Hintergrundaufgaben in einem Arbeitsthread auszuführen
 * um die empfangene Benutzerdaten vom Server auf die Benutzeroberfläche anzuzeigen bzw.
 * zu aktualisieren.
 *
 * @author Oliver Dziedzic, Mamoudou Balde
 *
 * @version 1.0
 */

public class OnlineHelper extends AsyncTask<String, Void, String>
{
    String json_url, update_url, timeline_url, community_url;
    Context context;

    /**
     * Konstruktor
     *
     * @param ctx der Kontext der Klasse
     */
    public OnlineHelper(Context ctx) {
        context = ctx;
    }

    /**
     * wird verwendet, um die Aufgabe einzurichten, indem sie z.B.
     * eine Progressleiste in die Benutzeroberfläche anzeigt.
     * Hier werden die verschiedene Url und definierten Textview initialisiert.
     */
    protected void onPreExecute()
    {
        //json_url =      "http://10.9.40.159:80/webapp/getData.php";
        //update_url =    "http://10.9.40.159:80/webapp/updateData.php";
        //timeline_url =    "http://10.9.40.159:80/webapp/getTimeline.php";
        //community_url =    "http://10.9.40.159:80/webapp/getCommunity.php";
        json_url =       "http://10.9.42.55:80/webapp/get_data.php";
        update_url =     "http://10.9.42.55:80/webapp/update_data.php";
        timeline_url =   "http://10.9.42.55:80/webapp/get_timeline.php";
        community_url =  "http://10.9.42.55:80/webapp/get_community.php";
        //json_url =      "http://192.168.0.16:80/webapp/get_data.php";
        //update_url =    "http://192.168.0.16:80/webapp/update_data.php";
        //timeline_url =    "http://192.168.0.16:80/webapp/get_timeline.php";
        //community_url =    "http://192.168.0.16:80/webapp/get_community.php";
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
    protected String doInBackground(String... params) {
        String type = params[0];
        {
            if (type.equals("getData"))
                try {
                    //String name = params[1];
                    URL url = new URL(json_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(LoginGui.login_username, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((result = bufferedReader.readLine()) != null) {
                        stringBuilder.append(result + "\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return "Exception: " + e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Exception:" + e.getMessage();
                }

            if(type.equals("updateData")) {
                try {
                    String user_id = params[1];
                    String gewicht = params[2];
                    String bmi = params[3];
                    String bmr = params[4];

                    URL url = new URL(update_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("gewicht","UTF-8")+"="+URLEncoder.encode(gewicht,"UTF-8")+"&"
                            +URLEncoder.encode("bmi","UTF-8")+"="+URLEncoder.encode(bmi,"UTF-8")+"&"
                            +URLEncoder.encode("bmr","UTF-8")+"="+URLEncoder.encode(bmr,"UTF-8")+"&"
                            +URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8");
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
        }
        if(type.equals("getTimeline")) {
            try {
                //String name = params[1];
                URL url = new URL(timeline_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(OnlineActivity.user_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((result = bufferedReader.readLine()) != null) {
                    stringBuilder.append(result + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception:" + e.getMessage();
            }

        }
        if(type.equals("getCommunity")) {
            try {
                //String name = params[1];
                URL url = new URL(community_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(OnlineActivity.user_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((result = bufferedReader.readLine()) != null) {
                    stringBuilder.append(result + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception:" + e.getMessage();
            }

        }
        return null;
    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    /**
     * Verwendet, um die Benutzeroberfläche zu aktualisieren,
     * nachdem der Hintergrundprozess abgeschlossen ist.
     * Hier werden die umgewandelte Json Daten nach dem Empfang in Textview angezeigt.
     *
     * @param result das Ergebnis nach Abschluss der Hintergrundberechnung
     */
    protected void onPostExecute(String result) {
        if (result.contains("result"))
        {
            OnlineActivity.parseJson(result);
        }
        if (result.contains("Data update"))
        {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            OnlineActivity.uploadData.setIcon(context.getResources().getDrawable(R.mipmap.update_data));
        }
        if(result.contains("timeline"))
        {
            //TimelineActivity.txtJson.setText(result);
            TimelineActivity.parse(result);
        }
        if(result.contains("community"))
        {
            //CommunityActivity.txtJson.setText(result);
            CommunityActivity.parse(result);
        }

    }
}