package com.jcoolstory.crackbinidemo;

import android.app.ProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.crackbini.R;
import com.jcoolstory.game.BiniScoreDBC;
import com.jcoolstory.game.WebUtil;

import android.app.Dialog;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
public class BiniScoreBoard  extends Activity{

	Cursor mList = null;
	ListView mListView = null;
	Context mContext = null;
	TextView mTitleTextView = null;
	boolean mPrivate = true;
	Dialog mDialog = null;
	public BiniScoreDBC mDbc = null;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	
	class dHandler extends Handler
	{

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what ==1)
			{
				mDialog = onprogres();
			}
			else if (msg.what ==2)
			{
				loadList();
				stopprogress(mDialog);
			}
			else if (msg.what ==3)
			{
				upload();
				stopprogress(mDialog);
			}
		}
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.scoreboard);
		
		mDbc = new BiniScoreDBC(this);
		init();
		loadList();
	}
	public void loadList()
	{

		Button btswitch = (Button)findViewById(R.id.sv_switch);
		Button btupload = (Button)findViewById(R.id.sv_upload);
		if (!isposibleupdate())
			btupload.setVisibility(View.GONE);
		btswitch.setOnClickListener(btswicth);
		if (mPrivate)
		{
			mTitleTextView.setText("Private Record");
			btswitch.setText("World");
			requestP();
		}
		else
		{
			mTitleTextView.setText("World Record");
			btswitch.setText("Private");
			if ( -1 == requestW())
			{
				mPrivate = !mPrivate;
				loadList();
			}
			
		}
	}
	public boolean isposibleupdate()
	{
		Cursor list = mDbc.readTop();
		boolean result = false;
		if (list.moveToFirst())
		{
			int record = list.getColumnIndex("recordserver");
			record = list.getInt(record);
			if (record == 0)
			{
				result =  true;
			}
			else
			{
				result = false;
			}
		}
		
		list.close();
		return result;
	}
	public void upload()
	{
		mList.close();
		
		Cursor list = mDbc.readTop();
		WebUtil web = new WebUtil();
		
		if (list.moveToFirst())
		{
			int record = list.getColumnIndex("recordserver");
			record = list.getInt(record);
			if (record == 0)
			{
				GameResult gameResult = new GameResult();
				gameResult.User_name = list.getString(1);
				gameResult.final_score = Integer.valueOf(list.getString(2));
				int result = web.upload(gameResult);
				if (result == 1)
				{
					mDbc.updateSuccess();
				}
				else
				{
					Toast.makeText(this, "Network conntection Error", Toast.LENGTH_SHORT).show();
				}
				Log.d("TAG", "Up result : " + result);
			}
			else
			{
				Toast.makeText(this, "Already uploaded", Toast.LENGTH_SHORT).show();
			}
		}
		else
			Toast.makeText(this, "Empty data", Toast.LENGTH_SHORT).show();;
		list.close();
	}
	public void init()
	{
		mTitleTextView = (TextView) findViewById(R.id.sv_title);
		Button btupload = (Button)findViewById(R.id.sv_upload);
		Button btclose = (Button)findViewById(R.id.sv_close); 
		Button btswitch = (Button)findViewById(R.id.sv_switch);

		btswitch.setOnClickListener(btswicth);
		mListView = (ListView) findViewById(R.id.sv_listView);
		
		btupload.setOnClickListener(upload);
		
		btclose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
	}
	View.OnClickListener btswicth = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mPrivate = !mPrivate;
			dHandler hander = new dHandler();
			hander.sendEmptyMessage(1);

			hander.sendEmptyMessageDelayed(2, 1000);
			
		}
	};
	View.OnClickListener upload = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dHandler hander = new dHandler();
			hander.sendEmptyMessage(1);

			hander.sendEmptyMessageDelayed(3, 1000);
		}
	};
	protected int requestW() {
		// TODO Auto-generated method stub
		mList.close();
		WebUtil web = new WebUtil();
		String result = web.RequestScore("1");
		ArrayList<Map<String,String>> list = web.parserScore(result);
		if (list ==null)
		{
			Toast.makeText(this, "Network conntection Error", Toast.LENGTH_SHORT).show();
			return -1;
		}
		BiniAdapter adapter = new BiniAdapter(mContext, list);
		mListView.setAdapter(adapter);
		return 1;
	}
	protected void requestP()
	{
		mList = mDbc.read();
		
		BiniAdapter adapter = new BiniAdapter(this, mList);
		if (adapter ==null)
			Toast.makeText(this, "Empty Ranking", Toast.LENGTH_SHORT).show();

		mListView.setAdapter(adapter);
		
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mList != null)
			mList.close();
		mList.close();
		if (mDbc != null)
			mDbc.close();
		super.onDestroy();
	}
	public Dialog onprogres()
	{
		
	    ProgressDialog dialog = new ProgressDialog(this);
	    dialog.setMessage("Please wait while loading...");
	    dialog.setIndeterminate(true);
	    dialog.setCancelable(true);
	    dialog.show();
	    return dialog;
	}
	public void stopprogress(Dialog diloag)
	{
		diloag.dismiss();
	}
}

class BiniAdapter extends BaseAdapter
{
	private Context mContext;
	private Cursor mList;
	private ArrayList<Map<String,String>> mArrayList;
	private boolean bcursor = false;
	LayoutInflater inflater;
	private int mSize;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	public BiniAdapter(Context context)
	{
		
		mContext = context;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mSize = mList.getColumnCount();
	}
	public BiniAdapter(Context context, ArrayList list)
	{
		mContext = context;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mArrayList = list;
		
		mSize = mArrayList.size();
	}
	public BiniAdapter(Context context, Cursor list)
	{
		bcursor = true;
		mContext = context;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = list;
		mSize = mList.getColumnCount();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return mSize;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mSize;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		  
		if (position == 0)
		{
			convertView = inflater.inflate(R.layout.scoretopinstace, parent, false);
		}
        if (convertView == null) {
        	convertView = inflater.inflate(R.layout.scoreinstance, parent, false);
        }
        
        TextView rank = (TextView) convertView.findViewById(R.id.sv_number);
        TextView name = (TextView) convertView.findViewById(R.id.sv_name);
        TextView score = (TextView) convertView.findViewById(R.id.sv_score);
        TextView endtime = (TextView) convertView.findViewById(R.id.sv_endt);
      
        rank.setText("" + (position+1));
        if (bcursor)
        {
        	if (mList.moveToPosition(position) == true)
        	{
		        name.setText( "Name : " + mList.getString(1));
		        score.setText( "Score : " + mList.getString(2));
		        endtime.setText( sdf.format(new Date(mList.getLong(6))));
        	}
        }
	    else
	    {
	    	Map<String,String> map = mArrayList.get(position);
	    	name.setText( "Name : " + map.get("user_name"));
	        score.setText( "Score : " + map.get("score"));
	        endtime.setText( sdf.format(new Date(map.get("date"))));
	    }
	        	
        
        
        return convertView;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		
		super.finalize();
	}


}