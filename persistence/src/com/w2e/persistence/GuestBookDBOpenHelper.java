package com.w2e.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GuestBookDBOpenHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "guest_book";
	public static final String TABLE_NAME = "guests";
	public static final int CURRENT_VERSION = 1;

	public GuestBookDBOpenHelper(Context context) {
		super(context, DB_NAME, null, CURRENT_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		createTables(db);
	}
	
	public void createTables(SQLiteDatabase db) {
		db.execSQL(String.format("drop table if exists %s;", TABLE_NAME));
		db.execSQL(String.format(
				"create table %s (" +
					"_id integer primary key autoincrement not null," +
					"firstName text," +
					"lastName text" +
				");", TABLE_NAME));
	}

}
