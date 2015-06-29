package com.mia.notes.adapter;

import java.util.ArrayList;

import com.mia.notes.R;
import com.mia.notes.common.Note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoteGridAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<Note> notes;
	
	
	public NoteGridAdapter(Context mContext,ArrayList<Note> notes){
		this.mContext = mContext;
		this.notes = notes;
	}
	
	@Override
	public int getCount() {
		return notes.size();
	}

	@Override
	public Object getItem(int position) {
		return notes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return notes.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if ( convertView == null )
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_note, null);
		}
		TextView tvGridItem = (TextView) convertView.findViewById( R.id.tv_grid_item );
		
		Note note = (Note) getItem(position);
		
		tvGridItem.setText( note.getDetial() );
		
		return convertView;
	}

}
