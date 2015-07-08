package com.mia.notes.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.mia.notes.R;
import com.mia.notes.common.Note;
import com.mia.notes.common.ViewHolder;

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
	public HashMap<Integer,Boolean> isSelected;
	private Boolean flag = true;
//	private int itemID;
	
	public NoteListAdapter(Context mContext, ArrayList<Note> notes,Boolean flag) 
	{
		this.mContext = mContext;
		this.notes = notes;
		this.flag = flag;
		init();
	}
	
	public void init(){
		isSelected = new HashMap<Integer, Boolean>();
		for(int i = 0;i < notes.size();i++){
			isSelected.put(i, false);
		}
	}
	
//	public int getItemID() {
//		return itemID;
//	}
//
//	public void setItemID(int itemID) {
//		this.itemID = itemID;
//	}

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
		ViewHolder holder = null;
		if ( convertView == null )
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_note, null);
			holder.tvTitle = (TextView) convertView.findViewById( R.id.tv_title );
			holder.cbNoteItem = (CheckBox) convertView.findViewById( R.id.cb_item_note);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Note note = (Note) getItem(position);	
		holder.tvTitle.setText( note.getTitle() );
		
		if(flag){
			holder.cbNoteItem.setVisibility(View.VISIBLE);
			holder.cbNoteItem.setChecked(isSelected.get(position));
			
		}else{
			holder.cbNoteItem.setVisibility(View.GONE);
			holder.cbNoteItem.setChecked(false);
		}	
		return convertView;
	}
}
