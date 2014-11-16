package com.stage.activities;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tagthetram.R;
import com.stage.models.Picture;


public class CameraActivity extends Activity  {	
	Bitmap myImageBitmap;
	ImageView view_picture;
	EditText nom_image;
	Button valider_image;
	Picture pic;
	ImageButton take_picture;
	Bundle bundle;	
	File photofile;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri; // file url to store image
	public Intent i=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
				super.onCreate(savedInstanceState);
				setContentView(R.layout.editpicture);		
				
				//on recuperer les differents views : button et imageview ...
				nom_image=(EditText)findViewById(R.id.namepicture);
				valider_image=(Button)findViewById(R.id.validepicture);
				view_picture=(ImageView)findViewById(R.id.picture_view_token);
				take_picture=(ImageButton)findViewById(R.id.takepicture);	
				bundle=getIntent().getExtras();		
				Toast.makeText(getApplicationContext(), "(1)Name the picture."
						+"(2)Take the picture"
						+"(3) Save the picture, at the end", Toast.LENGTH_LONG);
				//si on clique sur le bouton take_picture
				take_picture.setOnClickListener(new View.OnClickListener() {			
					@Override
					public void onClick(View v) {
						if(isDeviceSupportCamera()){// verifier si l'application dispose d'une camera
							//si oui on appel la méthode qui capture la photo
							capturerImage(nom_image.getText().toString(), bundle.getString("stationactuelle"));		
						}
						else{
							Toast.makeText(getApplicationContext(), "Votre appareil ne dispose pas d'un appareil", Toast.LENGTH_LONG).show();
						}
					}
				});				
				//si on clique sur le bouton valider_picture				
				valider_image.setOnClickListener(new View.OnClickListener() {						
					@SuppressLint("ResourceAsColor")
					@Override
					public void onClick(View v) {						   
						final String inputfilename=nom_image.getText().toString();//récuperr le nom de l'image	
						Toast.makeText(getApplicationContext(), inputfilename, Toast.LENGTH_LONG).show();
						bundle=getIntent().getExtras(); //recuperer le nom de la station envoyée par l'activité précédente
						Date currentDate = new Date(System.currentTimeMillis());
						
						BitmapFactory.Options options = new BitmapFactory.Options(); 
						myImageBitmap = BitmapFactory.decodeFile(fileUri.getPath(),options); 
						//call fucntion saving picture token 
						SavePictureOnDirectory(myImageBitmap,inputfilename,bundle.getString("stationactuelle"), view_picture, currentDate);
						finish();
					}
				});
	}
	
	/** get the result at the end ,of the camera activity.	*/
	private void capturerImage(String filename, String stationname) {
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);	 
	    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE,filename, stationname);	 
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}
	/**
	 *cette methode permet d'afficher sur l'imgeview l'image capture par l'activite 
	 */
	private void AfficherImageCapture() {
        try { 
        	view_picture.setVisibility(View.VISIBLE); 
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();             
            myImageBitmap = BitmapFactory.decodeFile(fileUri.getPath(),options); 
            view_picture.setImageBitmap(myImageBitmap);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
	/**
	 *  A la fin de l'exécution de l'activite Camera, on revient sur l'activite précédente
	 *  afin d'afficher la photo prise sur l'imageview	 */
	 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==CAMERA_CAPTURE_IMAGE_REQUEST_CODE){
			if(resultCode ==RESULT_OK){  //si l'activite camera de prise de photo est un succes, 
				AfficherImageCapture();  // on appelle la fonction qui affiche l'image sur l'imageview
				Toast.makeText(getApplicationContext(), "Picture token", Toast.LENGTH_SHORT);
			}
			else if (resultCode == RESULT_CANCELED) {
	            // user cancelled Image capture
	            Toast.makeText(getApplicationContext(),"User cancelled image capture", Toast.LENGTH_SHORT).show();
	        } else {
	            // failed to capture image
	            Toast.makeText(getApplicationContext(),"Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
	        }					
		}
	}	
	
	/** fonction vérifiant si l'appareil embarque une camera **/
	@SuppressLint("NewApi")
	private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {           
            return true;
        } else {
        	return false;
        }
    }
	/**
     * Creating file uri to store image
     ***/     
    public Uri getOutputMediaFileUri(int type, String filename, String stationname) {
        return Uri.fromFile(getOutputMediaFile(type,  filename,stationname));
    }
    /**
     *returning image 
     **/ 
    @SuppressLint("NewApi")
	private static File getOutputMediaFile(int type, String namepicture, String stationname) { 
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), stationname); 
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(stationname, "Oops! Failed creation " + stationname + " directory");
                return null;
            }
        } 
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() ,namepicture+"_"+stationname+"_"+timeStamp+".png");
        } 
        else {
            return null;
        } 
        return mediaFile;
    }
    
    /** **************************************************************/
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState); 
        // save file url in bundle as it will be null on scren orientation changes        
        outState.putParcelable("file_uri", fileUri);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState); 
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }
    
	/** The method to save the picture on the Directory	 */
	public void SavePictureOnDirectory(Bitmap pic,String filename, String stationname, ImageView imageview, Date d){	
		//get the default directory store of the phone sdcard
		String storagepath=Environment.getExternalStorageDirectory().getAbsolutePath();
		//create the directory for the station if doesn't exist	
        File directorystation  = new File(storagepath, stationname);
        Time t=new Time();
        t.setToNow();
        //set the picture name        
        String picture_name= filename + "_" + d.toString() + "_" +  t.toString() + ".png";//gare_2013.x.x_12:12.png
        //preparer l'imageview
        imageview.setDrawingCacheEnabled(true);
        BitmapFactory.Options options = new BitmapFactory.Options();
        myImageBitmap = BitmapFactory.decodeFile(fileUri.getPath(),options); 
        if(!directorystation.exists()){   
        	if(!directorystation.mkdir()){       	
        		Toast.makeText(this.getApplicationContext(), "Impossible de créer le dossier :"+ stationname,Toast.LENGTH_SHORT).show();
        	}
        } 	
        if(directorystation.exists()){    
			File newpicture=new File(directorystation, picture_name); //on declare une instance du fichier img
    		try{
    			FileOutputStream  outstream=new FileOutputStream(newpicture);//on l'ouvre en ecriture    
    			myImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outstream); 
    			outstream.flush();//save file picture on the directory.
        		outstream.close();//close the stream        		 
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        } 
	}
}