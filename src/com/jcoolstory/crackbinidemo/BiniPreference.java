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
	CheckBox sound;
	CheckBox vibrate;
	EditText username;
	Button cancel;
	SharedPreferences pref = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		
		pref = getSharedPreferences(PREF, Activity.MODE_PRIVATE);
		setContentView(R.layout.pref);
		
		sound = (CheckBox)findViewById(R.id.pref_sound);
		vibrate = (CheckBox)findViewById(R.id.pref_vib);
		username = (EditText)findViewById(R.id.pref_user_name);
		cancel = (Button)findViewById(R.id.pref_cancel);
		
		boolean bSound = pref.getBoolean("sound", true);
		boolean bvibrate = pref.getBoolean("vib", true);
		int rowindex = pref.getInt("row", 2);
		int themeid = pref.getInt("theme", 0);
		
		String user_name = pref.getString("username", "Bini_noname");
		
		sound.setChecked(bSound);
		vibrate.setChecked(bvibrate);
		username.setText(user_name);
		cancel.setOnClickListener(bt_cancel);
	}

	
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
	}
	public void savePref()
	{
		SharedPreferences.Editor editor = pref.edit();
		boolean bSound = sound.isChecked();
		boolean bvibrate  = vibrate.isChecked();
		String user_name = username.getText().toString().trim();
		
		editor.putString("username", user_name);
		editor.putBoolean("sound", bSound);
		editor.putBoolean("vib", bvibrate);
		editor.commit();
		
		GameConfig.SoundOn = bSound;
		
		GameConfig.VibOn = bvibrate;
		
		GameConfig.username =user_name;
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		savePref();
	}
}
