package com.stage.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.tagthetram.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.stage.adapters.ListStationAdapter;
import com.stage.models.Station;

public class Carte extends FragmentActivity{
	
	private double latitude = 0;
	private double longitude = 0;
	List<Station> ListeStations=new ArrayList<Station>(); // la liste des stations
	private final static String TAG_RES="response";
	private final static String TAG_ID="id";
	private final static String TAG_NAME="name";
	private final static String TAG_VILLE="town";
	private final static String TAG_LIGNES="lines";
	private final static String TAG_LATITUDE="lat";
	private final static String TAG_LONGITUDE="lon";
	private final static String url="http://modulaweb.fr/apitam/?request=getStopsList&fullInfos=1";	
//	private GoogleMap macarte;
	private MapController mc;
	private GeoPoint location;
	JSONParseStationDetails jpd;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		try{			
			initialiserLaCarte();//on va initialiser la carte
			jpd=new JSONParseStationDetails();
			jpd.execute(); // on execute la tache asynchrone pour afficher les markers des stations sur la carte.
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * fonction initialisant la carte
	 */
	
	public void initialiserLaCarte(){
		Bundle b=new Bundle();
		List<Station> ListeStations=new ArrayList<Station>();				
//		if(	macarte ==null){
//			//macarte=((SupportMapFragment)(getSupportFragmentManager().findFragmentById(R.id.map))).getMap();
//			//MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps ");		 
//			// adding marker
//			//macarte.addMarker(marker);
//			//verifier si la carte a ete recuperée
//			if(macarte==null){
//				Toast.makeText(getApplicationContext(), "Carte focus de montpellier", Toast.LENGTH_LONG);
//			}
//		}
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		initialiserLaCarte();
	}
	/**
	 * class JsonParseur des stations dans le details : longitude, latitude, id ...etc
	 */
	private  class JSONParseStationDetails extends AsyncTask<String, String, JSONObject>{
		private ProgressDialog pdialog;
		@Override
	    protected void onPreExecute() {
		    super.onPreExecute();		
		    pdialog = new ProgressDialog(Carte.this);
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
				int j=1;
				while(j<reponse.length()){
					String indexTab=Integer.toString(j);					
					if(reponse.has(indexTab)){
						JSONArray listarret=reponse.getJSONArray(indexTab);
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
							
				ListStationAdapter adapter=new ListStationAdapter(Carte.this, listfinalestations, 12);				
				//listeitems.setAdapter(adapter);	
				
				// Ajouter ICI l'ajout des marker sur la carte.
			} 
			catch (JSONException e) {				
				e.printStackTrace();
			}			
		}			
	}	
}
