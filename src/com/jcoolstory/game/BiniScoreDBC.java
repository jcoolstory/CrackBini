package com.jcoolstory.game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jcoolstory.crackbinidemo.GameResult;

public class BiniScoreDBC 
{
	private Context mContext;
	private BiniScoreDB mHelper;
	public BiniScoreDBC(Context context)
	{
		mContext = context;
		mHelper = new BiniScoreDB(mContext);
	}
	public void write(GameResult result)
	{
		SQLiteDatabase db;
		ContentValues row;

		db = mHelper.getReadableDatabase();
		row = new ContentValues();
		row.put("name",result.User_name);
		row.put("score", result.final_score);
		row.put("depth", result.Depth);
		row.put("distance", result.Distance);
		row.put("durringtime", result.Durringtime);
		row.put("writetime", result.EndGametime);
		row.put("recordserver", 0);
		
		db.insert(BiniScoreDB.MainDB, null, row);
		mHelper.close();
	}
	public Cursor read()
	{
		SQLiteDatabase db;
		
		db = mHelper.getReadableDatabase();
		String qurey = "SELECT * FROM "+ BiniScoreDB.MainDB +" ORDER  BY score DESC LIMIT 10;";
		Cursor cursor;
		cursor = db.rawQuery(qurey, null);	
		return cursor;
	}
	public void clear()
	{
		SQLiteDatabase db;
		
		db = mHelper.getReadableDatabase();
		db.delete(BiniScoreDB.MainDB, null,null);
	}
	public Cursor readTop() {
		// TODO Auto-generated method stub
		
		SQLiteDatabase db;
		
		db = mHelper.getReadableDatabase();
		String qurey = "SELECT * FROM "+ BiniScoreDB.MainDB +" ORDER  BY score DESC LIMIT 1;";
		Cursor cursor;
		cursor = db.rawQuery(qurey, null);	
		return cursor;
	}
	public void close() {
		// TODO Auto-generated method stub
		mHelper.close();
	}
	public void updateSuccess() {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		db = mHelper.getWritableDatabase();
		String qurey = "Update " + BiniScoreDB.MainDB + " set recordserver = 1 ;"; 
		Log.d("TAG","update");
		db.execSQL(qurey);
//		db.rawQuery(qurey,null);
		
	}
}