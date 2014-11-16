package com.stage.adapters;

import java.util.ArrayList;

import com.example.tagthetram.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ListStationAdapter extends BaseAdapter implements OnClickListener{

	private ArrayList<String> stations;
	private LayoutInflater myInflater;
	private int current_page;
	private int pagination;
	private Context context;
	
	public ListStationAdapter (Context context, ArrayList<String> stations, int pagination){
		this.myInflater = LayoutInflater.from(context);
		this.stations = stations;
		this.pagination=pagination;
		this.current_page=1;
		this.context=context;
	}	
	@Override
	public int getCount() {
		return Math.min(pagination*current_page, stations.size());
	}
	@Override
	public Object getItem(int arg0) {
		return this.stations.get(arg0);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	public static class ViewHolder {
		TextView txt;
	}	
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;		
		if(position==current_page*pagination-1){
			ImageButton expand=new ImageButton(context);
			expand.setImageResource(R.drawable.buttonadd1);
			expand.setOnClickListener(this);
			return expand;
		}
		else{			
			convertView = myInflater.inflate(com.example.tagthetram.R.layout.listitem, null);
			holder = new ViewHolder();
			holder.txt=(TextView) convertView.findViewById(com.example.tagthetram.R.id.nomstation);		
			holder.txt.setLayoutParams(new LayoutParams(200,30));
			holder.txt.setTextSize(12);
			holder.txt.setPadding(10, 10, 10,10);
			holder.txt.setText((CharSequence)stations.get(position));
			convertView.setTag(holder);
			return convertView;		
		}
	}
	@Override
	public void onClick(View arg0) {
		current_page++;
		notifyDataSetChanged();		
	}
}
