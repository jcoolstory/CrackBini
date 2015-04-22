package com.jcoolstory.crackbinidemo;

import com.crackbini.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.app.Activity;
import android.app.AlertDialog;

public class BiniPreference extends Activity {

	/* (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	public static final String PREF = "BiniPreference";
//	Spinner spiner_row;
//	Spinner spiner_theme;
	CheckBox sound;
	CheckBox vibrate;
	EditText username;
	//Button accept;
	Button cancel;
	SharedPreferences pref = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		
		pref = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
		setContentView(R.layout.pref);
//		
		
//		spiner_row = (Spinner)findViewById(R.id.pref_row_col);
//		spiner_theme = (Spinner)findViewById(R.id.pref_theme);
		
		sound = (CheckBox)findViewById(R.id.pref_sound);
		vibrate = (CheckBox)findViewById(R.id.pref_vib);
		username = (EditText)findViewById(R.id.pref_user_name);
	//	accept = (Button)findViewById(R.id.pref_accept);
		cancel = (Button)findViewById(R.id.pref_cancel);
		
		boolean bSound = pref.getBoolean("sound", true);
		boolean bvibrate = pref.getBoolean("vib", true);
		int rowindex = pref.getInt("row", 2);
		int themeid = pref.getInt("theme", 0);
		
		String user_name = pref.getString("username", "Bini_noname");
		
//		spiner_row.setSelection(rowindex);
//		spiner_theme.setSelection(themeid);
		sound.setChecked(bSound);
		vibrate.setChecked(bvibrate);
		username.setText(user_name);
	//	accept.setOnClickListener(bt_accept);
		cancel.setOnClickListener(bt_cancel);
	}


//	View.OnClickListener bt_accept = new View.OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			savePref();
//			CrackBini.loadPref(BiniPreference.this);
//			finish();
//		}
//	};
	
	View.OnClickListener bt_cancel = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}
	public void ShowExit(Context context, String srt)
	{
//		AlertDialog.Builder Builder =  new AlertDialog.Builder(context);
//		Builder.setMessage(srt)
//	       .setCancelable(true)
//	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//	           public void onClick(DialogInterface dialog, int id) {
//	        //	   saveGame(true);
//	        	   savePref();
//	        	   
//	        	   dialog.cancel();
//	        	   finish();
//	           }
//
//			
//	       })
//	       .setOnKeyListener( new DialogInterface.OnKeyListener() {
//			
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//				// TODO Auto-generated method stub
//				if (keyCode == KeyEvent.KEYCODE_BACK)
//				{
//					dialog.cancel();
//		        	  finish();
//					return false;
//				}
//				return true;
//			}
//		})
//			
//
////           .setNeutralButton("No", new DialogInterface.OnClickListener() {
////		    public void onClick(DialogInterface dialog, int whichButton) {
////		
////		        /* User clicked Something so do some stuff */
////	        //(false);
////	        	   finish();
////	        	   dialog.cancel();
////		    }
////           })
//	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
//	           public void onClick(DialogInterface dialog, int id) {
//	               dialog.cancel();
//	               finish();
//	           }
//	       });
//		AlertDialog alert = Builder.create();
//		
//		alert.show();

	}
	public void savePref()
	{
		SharedPreferences.Editor editor = pref.edit();
		boolean bSound = sound.isChecked();
		boolean bvibrate  = vibrate.isChecked();
//		int rowindex = spiner_row.getSelectedItemPosition();
//		int theme  =   spiner_theme.getSelectedItemPosition();
		String user_name = username.getText().toString().trim();
		
		editor.putString("username", user_name);
		editor.putBoolean("sound", bSound);
		editor.putBoolean("vib", bvibrate);
//		editor.putInt("row",rowindex);
//		editor.putInt("theme",theme);
		editor.commit();
//		GameConfig.Theme = theme;
		
	//	GameConfig.X = rowindex;
		
		GameConfig.SoundOn = bSound;
		
		GameConfig.VibOn = bvibrate;
		
		GameConfig.username =user_name;
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		if (keyCode == KeyEvent.KEYCODE_BACK)
//		{
//			ShowExit(this,"Save?");
//			return false;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		savePref();
	}
}
