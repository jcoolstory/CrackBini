package com.jcoolstory.crackbinidemo;
//package com.crackbini;
//
//import java.io.BufferedReader;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
////import com.google.ads.*;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.ContextWrapper;
//import android.content.DialogInterface;
//import android.media.AudioManager;
//import android.media.SoundPool;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.widget.Toast;
//class GameConfig 
//{
//	public static boolean SoundOn = true;
//	public static int Colors = 5;
//	public static boolean Background = true;
//	public static int X = 12;
//	public static int Y = 12;
//	public static boolean VibOn = true;
//}
//
//public class CrackBini extends Activity {
//    /** Called when the activity is first created. */
//	
////	   private AdView adView;
//	private static final String MY_AD_UNIT_ID = "a14ec364736489a";
//	public static final int START_YES_NO = 1;
//	
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//      
//
//        setContentView(R.layout.logo);
//        
//        
//        new Handler().postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				if (firstStart() == true)
//				{
//					
//				}
//				else
//				{
////					loadGame();
//		//	        loadGame();
//					
//				}
//				setContentView(R.layout.bini_main);
////				LinearLayout sv = (LinearLayout)findViewById(R.id.surface);
////				sv.addView(mCrackbview);
////				setContentView(mCrackbview);
//			}
//		}, 1000);
//
////        // Create the adView     
//////        adView = new AdView(this, AdSize.BANNER, MY_AD_UNIT_ID);
////        adView = (AdView)this.findViewById(R.id.adView);
////        // Lookup your LinearLayout assuming it¡¯s been given    
////        // the attribute android:id="@+id/mainLayout"
//////        RelativeLayout layout = (RelativeLayout)findViewById(R.id.logo);
////        // Add the adView to it     
//////        layout.addView(adView);
////        // Initiate a generic request to load it with an ad
////        adView.loadAd(new AdRequest());   
//    }
//
//	public boolean firstStart()
//	{
//		try
//		{
//			File file = getFileStreamPath("config.ini");
//			if (!file.isFile())
//			{
//				file.createNewFile();
//				return true;
//			}
//			else
//			{
//				FileInputStream fis = new FileInputStream(file);
//				return false;
//			}
//
//		}
//		catch(IOException e)
//		{
//			
//		}
//		return true;
//	}
////	public boolean loadGame()
////	{
////		boolean flag = false;
////		String buffer = "";
////		try {
////			FileInputStream fis = openFileInput("config.ini");
////			InputStreamReader isr = new InputStreamReader(fis);
//////			OutputStreamWriter osw = new IputStreamWriter(os1);
////			BufferedReader br = new BufferedReader(isr);
////
////			buffer = br.readLine();
////			
////			flag = Boolean.valueOf(buffer);
////			
////		} catch (FileNotFoundException e1) {
////			// TODO Auto-generated catch block
////			e1.printStackTrace();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		if (flag)
////		{
////			try {
////				File file = getFileStreamPath("main.save");
////				FileInputStream fis = new FileInputStream(file);
////				ObjectInputStream ois = new ObjectInputStream(fis);
////				SaveOBJ load = (SaveOBJ) ois.readObject();
////				mCrackbview.loadGame(load);
////				return true;
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////		       
////			} catch (ClassNotFoundException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////		      
////			}
////			
////		}
////		 mCrackbview.loadGame();
////		return false;
////	}
//	
//}
