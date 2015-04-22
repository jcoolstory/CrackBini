package com.jcoolstory.crackbinidemo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.crackbini.R;
import com.jcoolstory.game.BiniScoreDBC;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


public class ScoreView extends Activity implements OnClickListener{

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score);
		Intent intent = getIntent();

		GameResult result = (GameResult) intent
				.getSerializableExtra("GAME_RESULT");// (GameResult)

		TextView tvscore = (TextView) findViewById(R.id.TV_score);
		TextView tvdepth = (TextView) findViewById(R.id.TV_depth);
		TextView tvdistance = (TextView) findViewById(R.id.TV_distance);
		TextView tvtotalscore = (TextView) findViewById(R.id.TV_total_score);

		tvscore.setText(String.valueOf(result.Score));
		tvdepth.setText(String.valueOf(result.Depth)+" X 200");
		tvdistance.setText(String.valueOf(result.Distance)+" X 50");
		Date date = new Date(result.Durringtime);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		long time = result.Durringtime /1000;
		int second = (int) (time % 60);
		int minut = (int) (time / 60); 
		
		
		Log.d("TAG", "Tostring " + date.toString());
		//Time time = new Time();
		//time.set(result.Durringtime);
		//time.format3339(false);
//		String time = sdf.format(date);
		long total = result.Score + result.Depth * 200 + result.Distance * 50;
		tvtotalscore.setText(String.valueOf(total));
		
		findViewById(R.id.sv_restart).setOnClickListener(this);
		//findViewById(R.id.sv_close).setOnClickListener(this);
		findViewById(R.id.sv_exit).setOnClickListener(this);
		TextView title = (TextView)findViewById(R.id.ScoreView_Title);
		title.setText(" Game Over " );
		result.final_score = total;
		result.EndGametime = System.currentTimeMillis();
		result.User_name = GameConfig.username;
		BiniScoreDBC dbc = new BiniScoreDBC(this);
		dbc.write(result);
		dbc.close();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.sv_close:
			setResult(BiniGameActivity.CLOSE);
			finish();
			break;
		case R.id.sv_restart:
			setResult(BiniGameActivity.RESTART);
			finish();
			break;
		case R.id.sv_exit:
			setResult(BiniGameActivity.EXIT);
			finish();
			break;

		default:
			break;
		}
	}
}