package com.mia.notes.database;

import java.util.ArrayList;

import com.mia.notes.common.Note;
import com.mia.notes.utils.Constants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NoteDao {

	private SQLiteDatabase db;
	private Cursor cursor;
	// private Context ctx;
	private String sql;

	public NoteDao(DatabaseHelper dbHelper) {
		db = dbHelper.getWritableDatabase();
	}

	// public NoteDao(Context ctx) {
	// this.ctx = ctx;
	// }

	// 判断数据库是否存在

	// public void dbIsExit(String dbName){
	// db = ctx.openOrCreateDatabase(dbName,0, null);
	// }

	// 判断表是否存在
	public boolean tabIsExist(String tabName) {

		boolean result = false;
		if (tabName == null) {
			return false;
		}

		// sql = "select count(*) as c from " + contents.DBNAME
		// + " where tpye = 'table' and name = '" + tabName + "';";
		sql = "select * from sqlite_master where name=" + "'" + tabName + "'";
		cursor = db.rawQuery(sql, null);
		if (cursor.getCount() != 0) {
			result = true;
		}
		// if (cursor.moveToNext()) {
		// int count = cursor.getInt(0);
		// if (count > 0) {
		// result = true;
		// }
		// }
		cursor.close();
		return result;
	}

	// 创建表
	public void createTab() {
		sql = "create table " + Constants.TABLE_NAME + "(" + Constants.ID
				+ " integer primary key AUTOINCREMENT," + Constants.TITLE
				+ " text," + Constants.DETAIL + " text);";
		System.out.println(sql);
		db.execSQL(sql);
	}

	// 插入数据
	public void insert(Note mNote) {

		sql = "insert into " + Constants.TABLE_NAME + "(" + Constants.TITLE
				+ "," + Constants.DETAIL + ")" + " values('" + mNote.getTitle()
				+ "','" + mNote.getDetial() + "');";
		System.out.println(sql);
		db.execSQL(sql);
	}

	/**
	 * 查询所有的Note
	 * 
	 * @return
	 */

	public void update(Note mNote) {
		System.out.println("mNote.getId() = " + mNote.getId());
		sql = "update " + Constants.TABLE_NAME + " set " + Constants.TITLE
				+ "='" + mNote.getTitle() + "'," + Constants.DETAIL + "='"
				+ mNote.getDetial() + "' where " + Constants.ID + "="
				+ mNote.getId() + ";";
		System.out.println(sql);
		db.execSQL(sql);
	}

	public void delete(Note mNote) {
		sql = "delete from " + Constants.TABLE_NAME + " where " + Constants.ID
				+ " = " + mNote.getId();
		System.out.println(sql);
		db.execSQL(sql);
	}

	public ArrayList<Note> queryAll() {
		ArrayList<Note> noteList = new ArrayList<Note>();

		// sql = "select * from " + Constants.TABLE_NAME + ";";
		sql = "select * from " + Constants.TABLE_NAME + " order by "
				+ Constants.ID + " desc;";
		// sql = "select " + Constants.ID + "," + Constants.TITLE + ","
		// + Constants.DETAIL + " from " + Constants.TABLE_NAME
		// + " order by " + Constants.ID + " desc;";
		System.out.println(sql);
		// db.execSQL(sql);

		// 获取一个游标Cursor
		cursor = db.rawQuery(sql, null);
		// 移动游标到第一个之前
		// cursor.moveToPosition(-1);

		// while循环游标，根据moveToNext进行下一个判断
		while (cursor.moveToNext()) {
			Note sqlNote = new Note();
			sqlNote.setId(cursor.getInt(cursor.getColumnIndex(Constants.ID)));
			sqlNote.setTitle(cursor.getString(cursor
					.getColumnIndex(Constants.TITLE)));
			sqlNote.setDetial(cursor.getString(cursor
					.getColumnIndex(Constants.DETAIL)));
			noteList.add(sqlNote);
		}

		return noteList;
	}
}
