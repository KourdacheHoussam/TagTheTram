package com.stage.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class StationParser {
	
	static InputStream entree=null;
	static JSONObject jo=null;
	static String json="";
	
	//contructeur
	public StationParser(){}
	//recuperer les données de l'url;	
	public static JSONObject getJSONFromUrl(String url) {
		 final AndroidHttpClient httpClient;
		 final HttpGet httpPost;
		// defaultHttpClient
         httpClient = AndroidHttpClient.newInstance("Android");
         httpPost = new HttpGet(url);
        // HTTP request
        try { 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if(statusCode !=HttpStatus.SC_OK){
            	Log.w("ImageDownloader", "Error " + statusCode+" while retrieving bitmap from " + url);
				return null;
            }            
            HttpEntity httpEntity = httpResponse.getEntity();
            if(httpEntity !=null){
            	entree = httpEntity.getContent();        
            } 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(entree, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            entree.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
            jo = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        httpClient.close();//close the http connection        
        return jo;// return JSON String
		
	}
	
}
