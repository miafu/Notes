package com.mia.notes.database;


import com.mia.notes.utils.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public DatabaseHelper(Context context,String name){
		this(context,name,VERSION);
	}
	
	public DatabaseHelper(Context context,String name,int version){
		this(context,name,null,version);
	}

	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("create a database");
//		db.execSQL("create table Note(id int,title varchar(20),contents varchar(20))");
		String sql = "create table " + Constants.TABLE_NAME + "(" + Constants.ID
				+ " integer primary key AUTOINCREMENT," + Constants.TITLE
				+ " text," + Constants.DETAIL + " text);";
		System.out.println(sql);
		db.execSQL( sql );
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("update a Database");
	}


}
