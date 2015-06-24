package com.mia.notes.Activity;


import java.util.ArrayList;

import com.mia.notes.R;
import com.mia.notes.R.id;
import com.mia.notes.R.layout;
import com.mia.notes.adapter.NoteListAdapter;
import com.mia.notes.common.Note;
import com.mia.notes.database.DatabaseHelper;
import com.mia.notes.database.NoteDao;
import com.mia.notes.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener{

	private ImageView addNote;
	private ImageView delNote;
	private ListView noteListView;
	private ArrayList<Note> notes;
	private NoteDao mNoteDao;
	private static final int REQUEST_CODE_ADD = 0; 
	
	private NoteListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mNoteDao = new NoteDao(new DatabaseHelper(this, Constants.DBNAME));
		notes = mNoteDao.queryAll();
		
		findViews();
		setListener();
	
	}
	
	public void findViews(){
		addNote = (ImageView)findViewById(R.id.iv_addnote);
		delNote = (ImageView)findViewById(R.id.iv_delnote);
		noteListView = (ListView)findViewById(R.id.lv_notelist);
		mAdapter = new NoteListAdapter( this, notes );
		noteListView.setAdapter(mAdapter);
		
		
		noteListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {					
				String detail = notes.get(position).getDetial().toString();
				int ID = notes.get(position).getId();
				System.out.println("Click List send ID:" + ID);
				Intent intent = new Intent();
				intent.putExtra("subNote", detail);
				intent.putExtra("ID", ID);
				intent.setClass(MainActivity.this, NoteEditActivity.class);
				startActivityForResult(intent, REQUEST_CODE_ADD);				
			}
		});
	}

	public void setListener(){
		addNote.setOnClickListener(this);
		delNote.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.iv_addnote:
			Intent intent = new Intent();
			intent.putExtra("subNote", "new");
			intent.setClass(this,NoteEditActivity.class);
			startActivityForResult(intent, REQUEST_CODE_ADD);
			break;
		case R.id.iv_delnote:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if ( resultCode == RESULT_OK )
		{
			if ( requestCode == REQUEST_CODE_ADD )
			{
				String node = data.getStringExtra( "noteAdd" );
				if ( node.equals( "1" ) )
				{
					notes.clear();
					notes.addAll( mNoteDao.queryAll() );
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	}
}
