
package com.jcoolstory.crackbinidemo;

import com.crackbini.R;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.AdRequest.ErrorCode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint.Align;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;

import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.Toast;

class GameConfig 
{
	public static int[] xarray = {10,11,12,13};
	public static int[] yarray = {10,11,12,13};
	public static String username = "";
	public static boolean SoundOn = true;
	public static int Colors = 5;
	public static int X = 2;
	public static int Y = 2;
	public static boolean VibOn = true;
	public static int Theme;
}

public class CrackBini extends Activity  implements OnClickListener {
    /** Called when the activity is first created. */
	

	public static final int START_YES_NO = 1;
	private AdView adView;
    private static final String MY_AD_UNIT_ID_ADAM = "168eZ1YT133d3a113a3";
	private static final String MY_AD_UNIT_ID = "a14ecd4c3080016";
	public static final String SHARED_PREFS_NAME = "com.jcoolstory.crackbini";
	private Context  mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        mContext = this;
        setContentView(R.layout.logo);
        
        
        new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (firstStart() == true)
				{
					showInputDalog();
				}
				else
				{
					loadPref(mContext);
				}
				setContentView(R.layout.bini_main);
				init();
				FrameLayout fl = (FrameLayout) findViewById(R.id.framefade);
				Animation animation = AnimationUtils.loadAnimation(CrackBini.this, R.anim.fadealpha);

				fl.startAnimation(animation);
				 
				RelativeLayout layout = (RelativeLayout)findViewById(R.id.mainbanner);
				adView = new AdView(CrackBini.this, AdSize.BANNER, MY_AD_UNIT_ID);
				adView.setAdListener(admobListener);
				
				layout.addView(adView);
				
				AdRequest ar = new AdRequest();		     
				adView.loadAd(ar);
		     
			}
		}, 1000);
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
        
        int sd = am.getRingerMode();
        if (sd == AudioManager.RINGER_MODE_NORMAL)
        {
        	GameConfig.SoundOn = true;
        }
        else
        {
        	GameConfig.SoundOn = false;
        }
    }
    AdListener admobListener = new AdListener() {
		
		@Override
		public void onReceiveAd(Ad arg0) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onPresentScreen(Ad arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLeaveApplication(Ad arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onDismissScreen(Ad arg0) {
			// TODO Auto-generated method stub
			
		}
	};
    public void init()
    {
		findViewById(R.id.BT_Start).setOnClickListener(CrackBini.this);
		findViewById(R.id.BT_SCORE).setOnClickListener(CrackBini.this);
		findViewById(R.id.BT_OP).setOnClickListener(CrackBini.this);
		findViewById(R.id.BT_EXIT).setOnClickListener(CrackBini.this);
    }
	public boolean firstStart()
	{
		SharedPreferences pref = mContext.getSharedPreferences(BiniPreference.PREF, Activity.MODE_PRIVATE);
		boolean result = pref.getBoolean("first", true);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("first", false);
		editor.commit();
		Log.d("TAG", "first" + result);
		return result;
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (view.getId()) {
		case R.id.BT_Start:
			intent = new Intent(this,BiniGameActivity.class);
			startActivity(intent);
			break;
		case R.id.BT_SCORE:
			intent = new Intent(this,BiniScoreBoard.class);
			startActivity(intent);
//			Toast.makeText(this, "�غ���", Toast.LENGTH_SHORT).show();
			break;
		case R.id.BT_OP:
//			Toast.makeText(this, "�غ���", Toast.LENGTH_SHORT).show();
			intent = new Intent(this,BiniPreference.class);
			startActivity(intent);
			break;
		case R.id.BT_EXIT:
			finish();
			break;
		default:
			break;
		}
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Utils.ShowExit(this,"Exit?",this);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (adView != null)
		{
			adView.stopLoading();
			//adView.setVisibility(View.GONE);

		}
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
	public static void loadPref(Context context)
	
	{
		SharedPreferences pref = context.getSharedPreferences(BiniPreference.PREF, Activity.MODE_PRIVATE);
		boolean bSound = pref.getBoolean("sound", true);
		boolean bvibrate = pref.getBoolean("vib", true);
		int rowindex = pref.getInt("row", 2);
		String user_name = pref.getString("username", "Bini_noname");
		GameConfig.Theme = pref.getInt("theme", 0);
		GameConfig.X = rowindex;
		GameConfig.SoundOn = bSound;
		GameConfig.VibOn = bvibrate;
		GameConfig.username =user_name;
	}
	public static void savePref(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(BiniPreference.PREF, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
	
		editor.putString("username",GameConfig.username );
		editor.putBoolean("sound", GameConfig.SoundOn);
		editor.putBoolean("vib", GameConfig.VibOn);
		editor.putInt("row",GameConfig.X);
		editor.putInt("theme", GameConfig.Theme );
		editor.commit();
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		savePref(this);
		super.onStop();
	}
	public void showInputDalog()
	{
		LayoutInflater factory = LayoutInflater.from(mContext);
        final View textEntryView = factory.inflate(R.layout.entryname, null);
        final EditText edit = (EditText)textEntryView.findViewById(R.id.username_edit);
        edit.setText("Bini" + hashCode());
		AlertDialog.Builder Builder =  new AlertDialog.Builder(mContext);
		
		Builder.setMessage("Input name")
	       .setCancelable(false)
	       .setView(textEntryView)
	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   SharedPreferences pref = mContext.getSharedPreferences(BiniPreference.PREF, Activity.MODE_PRIVATE);
	       			SharedPreferences.Editor editor = pref.edit();
	       			Editable name = edit.getText();
	       			
	       			editor.putString("username",name.toString().trim() );
	       			Log.d("TAG", "username" +edit.getText() );
	       			editor.commit();
	       			loadPref(mContext);
	           }

	       });

		AlertDialog alert = Builder.create();
		alert.show();
	}
}
