package com.stage.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tagthetram.R;
import com.stage.adapters.ListStationAdapter;

public class Main extends Activity {
	ListView listeitems;
	public static  String url="http://modulaweb.fr/apitam/?request=getStopsList";		
	private final static String TAG_RES="response";
	//tableau de type Json pour stocker les stations
	JSONArray stations=null;
	TextView nomstation;    
	JSONParse jp;
	List<String> station_actuelle=null;
	Bitmap pic=null;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//on recupere la listeview
		listeitems=(ListView)findViewById(R.id.listView);
	    nomstation = (TextView)findViewById(R.id.nomstation);	   
	    //on lance le thread pour le parseur : pour eviter le problème de thread avec le httpRequest().
	    jp=new JSONParse();
		jp.execute();				
		listeitems.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				Intent intent=new Intent(Main.this, ManagerPictures.class);
				intent.putExtra("stationactuelle", station_actuelle.get(position));
				startActivity(intent);
			}			
		});
	}		
	
	private  class JSONParse extends AsyncTask<String, String, JSONObject>{
		private ProgressDialog pdialog;
		@Override
	    protected void onPreExecute() {
		    super.onPreExecute();		
		    pdialog = new ProgressDialog(Main.this);
		    pdialog.setMessage("Recherche Stations...");
		    pdialog.setIcon(R.drawable.buttonsearch);
		    pdialog.setIndeterminate(false);
		    pdialog.setCancelable(false);
		    pdialog.show();	 
	    }
		@Override
		protected JSONObject doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			JSONObject obj=null;			
			obj=StationParser.getJSONFromUrl(url);	
			//Log.e("de", "results de la methode getJsonFromUrl " + obj.toString());
			return obj;
		}		
		@Override
        protected void onPostExecute(JSONObject json) {
			pdialog.dismiss();
					
			if(isCancelled() ){
				json=null;
			}			
			try{
				ArrayList<String> listfinalestations = new ArrayList<String>();	
				JSONObject reponse= json.getJSONObject(TAG_RES);
				//Log.e("de", "results de la methode getJsonFromUrl " + reponse.toString());
				int j=1;
				while(j<reponse.length()){
					String indexTab=Integer.toString(j);					
					if(reponse.has(indexTab)){
						JSONArray listarret=reponse.getJSONArray(indexTab);
						//Log.e("resultats", "results de la methode getJsonFromUrl " + listarret.toString());						
						for(int i=0; i<listarret.length(); i++){
							Object station=listarret.get(i);
							String arretstring=station.toString();	
							if(!listfinalestations.contains(arretstring)){//eviter les doublons
								listfinalestations.add(arretstring);								
							}
						}
						j++;
					}
					else{
						j++;
					}						
				}				
				station_actuelle=listfinalestations;
				Collections.sort(listfinalestations); //trier la liste				
				ListStationAdapter adapter=new ListStationAdapter(Main.this, listfinalestations, 12);				
				listeitems.setAdapter(adapter);	
			} 
			catch (JSONException e) {				
				e.printStackTrace();
			}			
		}			
	}	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection       
        switch (item.getItemId()) {
        	case R.id.map:
        		Intent i=new Intent(Main.this, Carte.class);
        		i.putExtra("carte",R.id.map);
        		startActivity(i);
        		break;
        	default:
        		break;
        }
        return false;
	}
}
