package com.jcoolstory.crackbinidemo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


import com.crackbini.R;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BiniGameActivity extends Activity {
	public static final int RESTART = 2;
	public static final int CLOSE = 3;
	public static final int EXIT = 4;
	static final int SCORE_BOARD = 10;
	CrackBView mCrackbview ;
	private AdView adView;
		private static final String MY_AD_UNIT_ID = "a14ec364736489a";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		LinearLayout sv = (LinearLayout)findViewById(R.id.surface);
		mCrackbview = new CrackBView(this,this);
		sv.addView(mCrackbview);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.banner);
			
		adView = new AdView(BiniGameActivity.this, AdSize.BANNER, MY_AD_UNIT_ID);
		layout.addView(adView);

		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 adView.loadAd(new AdRequest());
				
			}
		}, 2000);
		loadGame();
		
		
	}
	public void loadAd()
	{
		new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
				}
			}, 5000);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 0, "Restart");
		menu.add(0,3,0,"Sound");
		menu.add(0,4,0,"Vibrate");
		return super.onCreateOptionsMenu(menu);
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */


	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case 1:
			loadGame();
			return true;
		case 3:
			GameConfig.SoundOn = GameConfig.SoundOn ? false : true;
			return true;
		case 4:
			GameConfig.VibOn = GameConfig.VibOn ? false : true;
			return true;
		case 5:
			showScoreDialog();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void showScoreDialog()
	{
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BiniGameActivity.this,ScoreView.class);
				
				GameResult result = new GameResult();
				mCrackbview.getResult(result);
				
				intent.putExtra("GAME_RESULT", result);
				
				startActivityForResult(intent,SCORE_BOARD);
			}
		},1000);

		
	}
	public void showSelectDialog()
	{
		String[] str = getResources().getStringArray(R.array.select_x_y_count);
		StringBuffer sb = new StringBuffer();
		for(String strx : str)
			sb.append(strx+'\n');
		Log.d("TAG", sb.toString());
		AlertDialog.Builder Builder =  new AlertDialog.Builder(this);
		Builder.setTitle("Select ")
			    .setItems(R.array.select_x_y_count, new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					 //TODO Auto-generated method stub
					int[] array = getResources().getIntArray(R.array.select_x_y_int);
					String str ="";
					for (int x : array)
					{
						str += ","+x;
					}
					Log.d("TAG", "whiche ;"+which + str);
					GameConfig.X = array[which];
					GameConfig.Y = array[which];
					mCrackbview.close();
					mCrackbview = null;
					mCrackbview = new CrackBView(BiniGameActivity.this ,BiniGameActivity.this);
					
					mCrackbview.setXY(array[which], array[which]);
					setContentView(mCrackbview);
					mCrackbview.loadGame();
				}
			});
		AlertDialog alert = Builder.create();
		alert.show();
		
		
	}
/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mCrackbview.close();
		super.onDestroy();   

	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			ShowExit(this,"Exit?");
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void ShowExit(Context context, String srt)
	{
		AlertDialog.Builder Builder =  new AlertDialog.Builder(context);
		Builder.setMessage(srt)
	       .setCancelable(false)
	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   finish();
	        	   dialog.cancel();
	           }
	       })
	       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               dialog.cancel();
	               mCrackbview.resume();
	           }
	       });
		AlertDialog alert = Builder.create();
		alert.show();
		mCrackbview.close();
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */

	protected void onResume() {
		// TODO Auto-generated method stub
		mCrackbview.resume();
		super.onResume();
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */

	private void saveGame(boolean flag) {
		// TODO Auto-generated method stub
		String filename = "main.save";
		
		try {
			FileOutputStream os1 = openFileOutput("config.ini",  ContextWrapper.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(os1);
			BufferedWriter bw = new BufferedWriter(osw);
			if (flag)
				bw.write("true");
			else 
				bw.write("false");
			bw.flush();
			bw.close();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (flag)
		{
			try {
				
				OutputStream os = openFileOutput(filename, ContextWrapper.MODE_PRIVATE);
				ObjectOutputStream oos = new ObjectOutputStream(os);
				
				SaveOBJ outState = new SaveOBJ() ;
				mCrackbview.saveGame(outState);
				oos.writeObject(outState);
				
				oos.flush();
				oos.close();
				
			} catch (IOException e) {	
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public boolean firstStart()
	{
		try
		{
			File file = getFileStreamPath("config.ini");
			if (!file.isFile())
			{
				file.createNewFile();
				return true;
			}
			else
			{
				FileInputStream fis = new FileInputStream(file);
				return false;
			}

		}
		catch(IOException e)
		{
			
		}
		return true;
	}
	public boolean loadGame()
	{
		
		loadAd();
		 mCrackbview.loadGame();
		return false;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESTART:
			loadGame();
			break;
		case CLOSE:
			
			break;
		case EXIT:
			finish();
			break;
		default:
			break;
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		CrackBini.savePref(this);
		if (mCrackbview != null)
			mCrackbview.close();
		super.finish();
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mCrackbview.pause();
		Log.d("TAG", "onPause");
	}
}
