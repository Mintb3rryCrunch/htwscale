package com.example.oli.scaleuser2;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
 * Created by Oli on 04.07.2017.
 */

class OnlineHelper extends AsyncTask<String, Void, String>
{
    String json_url, update_url;
    Context context;

    OnlineHelper(Context ctx) {
        context = ctx;
    }
    protected void onPreExecute()
    {
        //json_url =      "http://10.9.41.233:80/webapp/getData.php";
        //update_url =    "http://10.9.41.233:80/webapp/updateData.php";
        json_url =      "http://10.9.41.233:80/webapp/get_data.php";
        update_url =    "http://10.9.41.233:80/webapp/update_data.php";
    }

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

                    URL url = new URL(update_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("gewicht","UTF-8")+"="+URLEncoder.encode(gewicht,"UTF-8")+"&"
                            +URLEncoder.encode("bmi","UTF-8")+"="+URLEncoder.encode(bmi,"UTF-8")+"&"
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
        return null;
    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

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

    }
}