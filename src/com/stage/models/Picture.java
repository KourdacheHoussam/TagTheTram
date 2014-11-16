package com.stage.models;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

public class Picture {	
	
	private String titre;
	private String date;
	private String adressdirectory="";	
	Context context;
	
	public Picture(String titre, String date, String adressdirectory){
		this.titre=titre;
		this.date=date;
		this.adressdirectory=adressdirectory;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String nom_date_heure) {
		this.date = nom_date_heure;
	}		
	
	public String getPath() {
		return this.adressdirectory;
	}
	public void setPath(String path) {
		this.adressdirectory = path;
	}	
}
