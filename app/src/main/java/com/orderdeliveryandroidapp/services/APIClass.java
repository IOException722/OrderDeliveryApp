package com.orderdeliveryandroidapp.services;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.Toast;

import com.orderdeliveryandroidapp.common.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by abhay on 14/7/16.
 */
public class APIClass extends AsyncTask<String, String, String> {
    private MyResultReceiver Receiver;
    private int type;
    private final Bundle b = new Bundle();
    HttpURLConnection urlConnection;
    private String name  , contact;
    public APIClass(int type, MyResultReceiver Receiver, String name, String contact)
    {
        this.type= type;
        this.Receiver=Receiver;
        this.name = name;
        this.contact = contact;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = "";

        try {


            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            List<Pair<String, String>> p = new ArrayList<>();
            p.add(new Pair<>("name", name));
            p.add(new Pair<>("contact", contact));


            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(p));
            writer.flush();
            writer.close();
            os.close();
            Log.e("Abhay", "After close");
            urlConnection.connect();

            int responseCode=urlConnection.getResponseCode();

            Log.e("Abhay", "Response code is:");

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }

            b.putString("apiresult",response.toString());
            Receiver.send(type, b);


        } catch (Exception e) {
            b.putString("apiresult","");
            Receiver.send(type,b);
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return null;
    }


    private String getQuery(List<Pair<String, String>> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.first.toString(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.second.toString(), "UTF-8"));
        }

        return result.toString();
    }
}
