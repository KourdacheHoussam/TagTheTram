package com.stage.adapters;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tagthetram.R;
import com.stage.models.Picture;

public class ListViewPicturesAdapter extends BaseAdapter {
	
	Context context;
	private ArrayList<Picture> data;
	private LayoutInflater layoutinflate;
	
	public ListViewPicturesAdapter (Context context, ArrayList<Picture> data){
		this.layoutinflate= LayoutInflater.from(context);
		this.context=context;	
		this.data=data;		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.data.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public static class ViewHolder {
		TextView name;
		TextView date;
		TextView heure;
		ImageView pic;
	}	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		convertView = layoutinflate.inflate(R.layout.liste_picture_items , null);
		ViewHolder holder = new ViewHolder();
		holder.name=(TextView)convertView.findViewById(R.id.picturetitle);
		holder.date=(TextView)convertView.findViewById(R.id.picturedate);	
		
		Bitmap  pic=BitmapFactory.decodeFile(data.get(position).getPath());
		holder.pic=(ImageView)convertView.findViewById(R.id.pictureid);
		holder.pic.setImageBitmap(pic);		
		holder.name.setText(data.get(position).getTitre());
		holder.date.setText(data.get(position).getDate());
		convertView.setTag(holder);
		return convertView;		
	}	
}
