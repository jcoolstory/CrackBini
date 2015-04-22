package com.jcoolstory.crackbinidemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.Activity;

public class Utils {
	public static void ShowExit(Context context, String srt ,final Activity act)
	{
		
		AlertDialog.Builder Builder =  new AlertDialog.Builder(context);
		Builder.setMessage(srt)
	       .setCancelable(false)
	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   act.finish();
	        	   dialog.cancel();
	           }

			
	       })
	       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               dialog.cancel();
	           }
	       });
		AlertDialog alert = Builder.create();
		alert.show();

	}
}
