package com.mia.notes.adapter;

import java.util.ArrayList;

import com.mia.notes.R;
import com.mia.notes.common.Note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class NoteListAdapter extends BaseAdapter 
{
	private Context mContext;
	private ArrayList<Note> notes;
	private Boolean flag = true;
	
	public NoteListAdapter(Context mContext, ArrayList<Note> notes,Boolean flag) 
	{
		this.mContext = mContext;
		this.notes = notes;
		this.flag = flag;
	}

	@Override
	public int getCount() {
		return notes.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return notes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return notes.get(position).getId();
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if ( convertView == null )
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_note, null);
		}
		TextView tvTitle = (TextView) convertView.findViewById( R.id.tv_title );
		CheckBox cbNoteItem = (CheckBox) convertView.findViewById( R.id.cb_item_note);
		
		Note note = (Note) getItem(position);
		if(flag){
			cbNoteItem.setVisibility(View.VISIBLE);
		}else{
			cbNoteItem.setVisibility(View.GONE);
		}
		tvTitle.setText( note.getTitle() );
		return convertView;
	}

}
