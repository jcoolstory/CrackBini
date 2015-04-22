package com.jcoolstory.game;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BiniScoreDB extends SQLiteOpenHelper {

	public static final String MainDB = "db_score";
	public BiniScoreDB(Context context) {
		super(context, "BiniScoreDB", null, 1);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS rss_list");
		onCreate(db);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + "db_score"
				+ " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + " name TEXT,"
				+ " score INTEGER ," + " depth INTEGER ," + " distance INTEGER ,"
				+ " durringtime TEXT, " + " writetime TEXT , recordserver INTEGER);");
	}


}