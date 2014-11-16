package com.stage.activities;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tagthetram.R;
import com.stage.adapters.ListViewPicturesAdapter;
import com.stage.models.Picture;

@SuppressLint("ResourceAsColor")
public class ManagerPictures extends Activity{

	private ListView listepictures;
	private TextView titre;
	private TextView date;
	private TextView titre_activite;
	private ImageView pic;
	private Bundle bundle; 
	private Context context;
	private String station_actuelle;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pictures_layout);
		
		//get the title,date of picture and the listview view
		listepictures=(ListView)findViewById(R.id.list_pictures);
		titre=(TextView)findViewById(R.id.picturetitle);
		date=(TextView)findViewById(R.id.picturedate);	
		titre_activite=(TextView)findViewById(R.id.pictures);
		bundle = getIntent().getExtras();
		station_actuelle=bundle.getString("stationactuelle");
		pic=(ImageView)findViewById(R.id.pictureid);
		titre_activite.setText("Station : "+station_actuelle);		
		titre_activite.setTextSize(16);	
		/**
		 * A la fin de l'activite CameraiActivity, rappeller l'asyn task pour rafraichir la liste
		 */
		GetPicturesStation showpictures=new GetPicturesStation();
		showpictures.execute();	
		/**
		 * Show the picture on the AlertDialog
		*/
		BitmapDrawable bitmap=new BitmapDrawable();		
		listepictures.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position,long id) {
				// TODO Auto-generated method stub
				LayoutInflater inflater=(LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
				AlertDialog.Builder alerte=new AlertDialog.Builder(context);
				View layout=inflater.inflate(R.layout.popup_picture, null);
				ImageView img=(ImageView)layout.findViewById(R.id.pic_popup);
				//img.setImageDrawable(bitmap);
				
				// .......  Try to get the number id of the picture selected
				
			}
		});
	}
	private String[] getNamesFiles(String chaine){
		String [] res=null;
		res=chaine.split("_"); //on splite la chaine principale
		return res;
	}
	private String splitDate(String s){
		String annee=s.substring(0, 4);
		String mois=s.substring(4, 6);
		String jour=s.substring(6, s.length());
		
		return "Date :"+jour+ "/" +mois+ "/"+annee+"  ";
	}
	private String splitTime(String s){
		String heure=s.substring(0,2);
		String sec=s.substring(2, 5);
		return "Heure:  "+heure+":"+sec;
	}
	
	private  class GetPicturesStation extends AsyncTask<String, String, ArrayList<Picture>>{
		private ProgressDialog pdialog;
		@Override
	    protected void onPreExecute() {
		    super.onPreExecute();		
		    pdialog = new ProgressDialog(ManagerPictures.this);
		    pdialog.setMessage("Chargement liste photos...");
		    pdialog.setIndeterminate(false);
		    pdialog.setCancelable(true);
		    Log.e("actuelle station :",station_actuelle);
			
		    pdialog.show();	 
	    }
		@Override
		protected ArrayList<Picture> doInBackground(String... arg0) {
			//la liste des Picture que je vais envoyer à mon adapter
			ArrayList<Picture> listedesphotos = new ArrayList<Picture>();
			//on se positionne dans le repertoires pictures ou les photos sont stockées
			File file=new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/");
			//je recuperer la liste sous-repertoires du repertorie racine
			File[] listerepertoires=file.listFiles();
			int nbdir=listerepertoires.length;
			int nb=0;
			int i=0;
				while(i<nbdir){
					//pour chaque repertoire, on recupere la liste de ses fichiers .png
					if(station_actuelle.equals(listerepertoires[i].getName())){
						//je recupere la liste des repertoires du repertoire selectionne
						File[] filesstations=listerepertoires[i].listFiles();
						nb+=filesstations.length;
						Picture pic=null;
						for(int j=0; j<filesstations.length; j++){
							if(filesstations[j].isFile()){
								//je recupere le nom, date, adresse;
								String chemin=filesstations[j].getPath();	
								String nom_complet_fichier=filesstations[j].getName();
								String [] nom_date_heure=getNamesFiles(nom_complet_fichier);
								String titre=nom_date_heure[0];
								String dateheure=splitDate(nom_date_heure[2]);
								String heure=splitTime(nom_date_heure[3]);
								pic=new Picture(titre, dateheure+heure, chemin);
								listedesphotos.add(pic); //je rajoute l'objet picture à la liste
							}
						}
					}
					i++;
				}
			//setPicturesOfStation(fileList);
			return listedesphotos;
		}
		@Override
		protected void onPostExecute(ArrayList<Picture> liste) {			
			pdialog.dismiss();//on arrete la boite de dialog 
			ListViewPicturesAdapter monadapter=new ListViewPicturesAdapter(ManagerPictures.this, liste);
			listepictures.setAdapter(monadapter);
		}
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.menu_manager_pictures, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection       
        switch (item.getItemId()) {
        	case R.id.add_picture:
        		//Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);        		
       		    Intent cameraintent=new Intent(this, CameraActivity.class);
       		    cameraintent.putExtra("stationactuelle", station_actuelle);
        		startActivity(cameraintent);
        		break;        		
        	case R.id.back_to_home:
        		Intent i=new Intent(this, Main.class);
        		i.putExtra("refresh", false);
        		startActivity(i);
        		break;
        	default:
        		break;
        }
		return false;
	}
}
