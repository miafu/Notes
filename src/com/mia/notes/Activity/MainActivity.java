package com.mia.notes.Activity;

import java.util.ArrayList;




import com.mia.notes.R;
import com.mia.notes.R.id;
import com.mia.notes.R.layout;
import com.mia.notes.adapter.NoteGridAdapter;
import com.mia.notes.adapter.NoteListAdapter;
import com.mia.notes.common.Note;
import com.mia.notes.database.DatabaseHelper;
import com.mia.notes.database.NoteDao;
import com.mia.notes.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	boolean flag = true;
	private ImageView addNote;
	private ImageView delNote;
	private ImageView listShowNote;
	private ImageView gridShowNote;
	private TextView titleNote;
	private GridView noteGridView;
	private ListView noteListView;
	private CheckBox checkBox;
	private ArrayList<Note> notes;
	private NoteDao mNoteDao;
	private static final int REQUEST_CODE_ADD = 0;
	private NoteListAdapter mAdapter;
	private NoteGridAdapter gAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mNoteDao = new NoteDao(new DatabaseHelper(this, Constants.DBNAME));
		notes = mNoteDao.queryAll();
		findViews();
		titleNote.setText("Note(" + notes.size() + ")");
		setListener();
	}

	public void findViews() {
		titleNote = (TextView) findViewById(R.id.tv_title);
		addNote = (ImageView) findViewById(R.id.iv_addnote);
		delNote = (ImageView) findViewById(R.id.iv_delnote);
		
		listShowNote = (ImageView) findViewById(R.id.iv_listshownote);
		gridShowNote = (ImageView) findViewById(R.id.iv_gridshownote);
		
		noteListView = (ListView) findViewById(R.id.lv_notelist);
		noteGridView = (GridView) findViewById(R.id.gv_notegrid);
		
		checkBox = (CheckBox) findViewById(R.id.cb_item_note);
		
		mAdapter = new NoteListAdapter(this, notes);
		gAdapter = new NoteGridAdapter(this, notes);
		
		noteListView.setAdapter(mAdapter);
		noteGridView.setAdapter(gAdapter);
			
	}

	public void setListener() {

		addNote.setOnClickListener(this);
		delNote.setOnClickListener(this);

		listShowNote.setOnClickListener(this);
		gridShowNote.setOnClickListener(this);	
		
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
		
		
		noteGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String detail = notes.get(position).getDetial().toString();
				int ID = notes.get(position).getId();
				Intent intent = new Intent();
				intent.putExtra("subNote", detail);
				intent.putExtra("ID", ID);
				intent.setClass(MainActivity.this, NoteEditActivity.class);
				startActivityForResult(intent, REQUEST_CODE_ADD);
			}
			
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_addnote:
			Intent intent = new Intent();
			intent.putExtra("subNote", "new");
			intent.setClass(this, NoteEditActivity.class);
			startActivityForResult(intent, REQUEST_CODE_ADD);
			break;
		case R.id.iv_delnote:
			break;
		case R.id.iv_listshownote:
			gridShowNote.setVisibility(View.VISIBLE);
			listShowNote.setVisibility(View.INVISIBLE);
			noteGridView.setVisibility(View.VISIBLE);
			noteListView.setVisibility(View.GONE);
			break;
		case R.id.iv_gridshownote:
			gridShowNote.setVisibility(View.INVISIBLE);
			listShowNote.setVisibility(View.VISIBLE);
			noteGridView.setVisibility(View.GONE);
			noteListView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_ADD) {
				String node = data.getStringExtra("noteAdd");
				if (node.equals("1")) {
					notes.clear();
					notes.addAll(mNoteDao.queryAll());
					titleNote.setText("Note(" + notes.size() + ")");
					mAdapter.notifyDataSetChanged();
					gAdapter.notifyDataSetChanged();
				}
			}
		}
	}
}
