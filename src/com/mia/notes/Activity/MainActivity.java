package com.mia.notes.Activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.mia.notes.common.ViewHolder;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private boolean longPressFlag = false;
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
	private int itemID;
	//继承Activity类必须重写的方法
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

	//通过控件ID获得控件
	public void findViews() {
		titleNote = (TextView) findViewById(R.id.tv_title);
		addNote = (ImageView) findViewById(R.id.iv_addnote);
		delNote = (ImageView) findViewById(R.id.iv_delnote);
		
		listShowNote = (ImageView) findViewById(R.id.iv_listshownote);
		gridShowNote = (ImageView) findViewById(R.id.iv_gridshownote);
		
		noteListView = (ListView) findViewById(R.id.lv_notelist);
		noteGridView = (GridView) findViewById(R.id.gv_notegrid);
		
		checkBox = (CheckBox) findViewById(R.id.cb_item_note);
		
		mAdapter = new NoteListAdapter(this, notes,longPressFlag);
		gAdapter = new NoteGridAdapter(this, notes);
		
		noteListView.setAdapter(mAdapter);
		noteGridView.setAdapter(gAdapter);
			
	}

	//设置按钮、item、长按事件的监听
	public void setListener() {

		addNote.setOnClickListener(this);
		delNote.setOnClickListener(this);

		listShowNote.setOnClickListener(this);
		gridShowNote.setOnClickListener(this);	
		
		noteListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemID = notes.get(position).getId();
				System.out.println("ListShow flag is:" + longPressFlag);
				if(longPressFlag){
					
					ViewHolder holder = (ViewHolder) view.getTag();
					holder.cbNoteItem.toggle();
					mAdapter.isSelected.put(position,holder.cbNoteItem.isChecked());
					mAdapter.notifyDataSetChanged();
				
				}else{
					String detail = notes.get(position).getDetial().toString();
					Intent intent = new Intent();
					intent.putExtra("subNote", detail);
					intent.putExtra("ID", itemID);
					intent.setClass(MainActivity.this, NoteEditActivity.class);
					startActivityForResult(intent, REQUEST_CODE_ADD);
				}
			}
		});
		
		
		noteGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemID = notes.get(position).getId();
				System.out.println("GridShow flag is:" + longPressFlag);
				String detail = notes.get(position).getDetial().toString();
				Intent intent = new Intent();
				intent.putExtra("subNote", detail);
				intent.putExtra("ID", itemID);
				intent.setClass(MainActivity.this, NoteEditActivity.class);
				startActivityForResult(intent, REQUEST_CODE_ADD);
			}
		});
		
		noteListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				longPressFlag = true;
				System.out.println("ItemLongClick: " + notes.get(position).getId() );
				mAdapter.setFlag(longPressFlag);
				//获取长按Item的ID
				ViewHolder holder = (ViewHolder) view.getTag();
				holder.cbNoteItem.toggle();
				mAdapter.isSelected.put(position,holder.cbNoteItem.isChecked());
				mAdapter.notifyDataSetChanged();
				return true;
			}
		});
		
	}

	//长按后出现checkBox，监听系统返回按钮
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && longPressFlag == true){
			System.out.println("flag");
			longPressFlag = false;
			mAdapter.setFlag(longPressFlag);
			for(int i = 0;i < mAdapter.isSelected.size();i++){
				mAdapter.isSelected.put(i, false);
			}
			mAdapter.notifyDataSetChanged();
			return false;
		}
        return super.onKeyDown(keyCode, event); 
	}

	//监听listview界面 增加、不同展示方式、删除按钮的监听
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
			DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this,
					Constants.DBNAME);
			NoteDao nd = new NoteDao(dbHelper);
			Note note = new Note();
			Set<Integer> positions = mAdapter.isSelected.keySet();
			for(Iterator<Integer> i = positions.iterator();i.hasNext();){
				int pos = i.next();
				if( mAdapter.isSelected.get( pos ) )
				{
					note.setId( notes.get( pos ).getId() );
					nd.delete(note);
				}
			}
			mAdapter.isSelected.clear();
			notes.clear();
			notes.addAll(mNoteDao.queryAll());
			mAdapter.notifyDataSetChanged();
			
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

	//startActivityForResult()方法在接收setResult()返回结果时自动调用，刷新ListView/GridView界面
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
