package com.mia.notes.Activity;

import com.mia.notes.R;
import com.mia.notes.Activity.MainActivity;
import com.mia.notes.R.layout;
import com.mia.notes.common.Note;
import com.mia.notes.database.DatabaseHelper;
import com.mia.notes.database.NoteDao;
import com.mia.notes.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteEditActivity extends Activity implements OnClickListener {

	private ImageView saveNote;
	private ImageView delNote;
	private TextView titleView;
	private EditText noteEdit;
	private SQLiteDatabase db;
	private String detail;
	private String sql;
	private int ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_edit);
		onFindID();
		isExistNote();
		// 判断是新增 还是打开已经存在的note
		Intent existIntent = getIntent();
		ID = existIntent.getIntExtra("ID", -1);

		System.out.println("Click Save_btn receive ID:" + ID);
		onBindListener();
	}

	public void onFindID() {
		saveNote = (ImageView) findViewById(R.id.iv_savenote);
		delNote = (ImageView) findViewById(R.id.iv_delnote);
		noteEdit = (EditText) findViewById(R.id.tv_note_edit);
		titleView = (TextView) findViewById(R.id.tv_title);
	}

	public void onBindListener() {
		saveNote.setOnClickListener(this);
		delNote.setOnClickListener(this);
	}

	public void isExistNote() {
		Intent intent = getIntent();
		detail = intent.getStringExtra("subNote").toString();
		if (!detail.equals("new")) {
			noteEdit.setText(detail.toCharArray(), 0, detail.length());
			if( detail.length() <= 3 ){
				titleView.setText(detail);
			}else{
				titleView.setText(detail.substring(0, 3).concat("..."));
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.iv_savenote: {

			DatabaseHelper dbHelper = new DatabaseHelper(NoteEditActivity.this,
					Constants.DBNAME);
			NoteDao nd = new NoteDao(dbHelper);
			// nd.dbIsExit(contents.DBNAME);
			if (!nd.tabIsExist(Constants.TABLE_NAME)) {
				nd.createTab();
			}

			String detail = noteEdit.getText().toString();

			Note note = new Note();
			note.setDetial(detail);
			if (detail.length() < 10) {
				note.setTitle(detail);
			} else {
				note.setTitle(detail.substring(0, 10).concat("..."));
			}
			if (ID == -1) {
				nd.insert(note);
			} else {
				note.setId(ID);
				nd.update(note);
			}
			Intent intent = new Intent();
			intent.putExtra("noteAdd", "1");
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
		case R.id.iv_delnote: {

			DatabaseHelper dbHelper = new DatabaseHelper(NoteEditActivity.this,
					Constants.DBNAME);
			NoteDao nd = new NoteDao(dbHelper);
			Note note = new Note();
			note.setId(ID);
			nd.delete(note);
			
			Intent intent = new Intent();
			intent.putExtra("noteAdd", "1");
			setResult(RESULT_OK, intent);
			finish();
			break;
		}

		}
	}
}
