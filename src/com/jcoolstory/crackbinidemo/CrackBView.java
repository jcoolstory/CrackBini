package com.jcoolstory.crackbinidemo;

import com.crackbini.R;
import com.jcoolstory.game.ButtonX;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.ShapeDrawable.ShaderFactory;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

class Animate extends BitmapDrawable 
{
	private long now;
	public Animate(Bitmap foreImage , Rect rect , long dur ) {
		// TODO Auto-generated constructor stub
		super(foreImage);
		dst = new RectF(rect);
		start = System.currentTimeMillis();
		during = dur*2;
		end = start + during;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		matrix = new Matrix();
		
	}

	/* (non-Javadoc)
	 * @see android.graphics.drawable.BitmapDrawable#draw(android.graphics.Canvas)
	 */
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
	//	super.draw(canvas);
	
		paint.setAlpha(alpha); 
//		
		canvas.drawBitmap(getBitmap(), null, str,paint);
		
	}
	public boolean step()
	{
		now = System.currentTimeMillis();
		if (now > start + during/4)
		{
			float p = 1-(float) (now - start) / during ;
			
			if (p <= 0.5)
			{
				p=0;
				return false;
			}
			matrix.reset();
			matrix.setScale(p, p,dst.centerX(),dst.centerY());
			matrix.mapRect(str);
			alpha = (int) (255 * p);
		//	Log.d("TAG", "p :" + p + "alpha " + alpha);
		}
		return true;
		
	}
	Paint paint;
	private long end;
	int alpha = 200 ;
	long start;
	long during;
	long past;
	RectF str;
	RectF dst ;
	Matrix matrix;
	/* (non-Javadoc)
	 * @see android.graphics.drawable.Drawable#setBounds(android.graphics.Rect)
	 */
	@Override
	public void setBounds(Rect bounds) {
		// TODO Auto-generated method stub
		str = new RectF(bounds);
		super.setBounds(bounds);
	}
	
}
public class CrackBView extends SurfaceView implements SurfaceHolder.Callback ,Runnable{

	private Activity parentact;
	private Context mContext;

	private long FRAME = 1000/24;
	 
	//private ArrayList<Integer> mScoreset = new ArrayList<Integer>();
	public int mGameFlag = 0;
//	private String text[];
//	private boolean err = false;
	int vgap =60;
	int hgap = 4;
	private ButtonX exitButton;
	private BiniTable mStoneTalbe;
	private float framerate = 0;
	private int maxX = 10;//GameConfig.xarray[GameConfig.X];
	private int maxY = 10;//GameConfig.yarray[GameConfig.Y];
	private Point pastPt = new Point();
	private Iterator aniIterator = null	;
	private Vector aniVector = new Vector();
	private RefreshHandler refreshhandler = new RefreshHandler();
	private long mGameStartTime = 0;
	
	private Animate mLayer;
	
	//private Paint mSelectPaint;

	
	//////////////////
	/// graphic
	
	private Paint mBorderPaint;
	private Paint mFillPaint;
	private Paint mForePaint;
	private Paint mShaderPaint;
	private Paint mPaint;
	
	//////////////////////////////// 
	///bitmap
	private Bitmap mBM_left;
	private Bitmap mBM_right;
	private Bitmap mScoreBit;
	private Bitmap mRestart;
	private Bitmap mBackBitmap;
	private Bitmap mLoadBitmap;
	private BitmapShader mShader1;
	private Bitmap mBM_number;
	private ArrayList<Bitmap> mStonBitmapArray;
	
	// sound vib
	private int[] mSound_crack = new int[3];
	private int mSound_add;
	private int mSound_fall;
	private SoundPool sp;
	protected Vibrator vibrator;
	private int mSound_Back;
	private long[][] pattern = {{ 100,50},{100,50, 200},{ 50,100,100,50}};
	
	// UI
	private int bgap;
	private float nhgap;
	private float nwgap;
	private int mWidth;
	private int mHeight;
	private int mStoneW;
	private int mStoneH;
	private ArrayList<ButtonX> mInterface = new ArrayList<ButtonX>();
	
	// flow
	private boolean mLoad = false;
	private Random rnd = new Random();
	
	private long lasttime =0;
	private long delaytime = 0;
	public boolean mLock;
	private int[] mScoreset = new int[3];
	private int[] mScoresetlimit = new int[3];
	private boolean mLoading;
	private LoadingThread mLoadthread;
	
	private boolean mLoadStart = false;
	private int sDepth = 0;
	private int pastscore;
	protected Paint mShaderPaint2;
	protected Paint mAntiPaint;
	protected Bitmap mUIBlock;
	private float mRotate;
	private float mEndRotate = 0;
	private RectF mUITopRect;
	private RectF mUIBottom;
	private Thread mThread;
	private Rect mTopArrowRect;
	
	
	public CrackBView(Context context, Activity act) {
		super(context);
		// TODO Auto-generated constructor stub
         setFocusable(true);
         getHolder().addCallback(this);
         Log.d("TAG", "CrackBView");   
     	getHolder().setType(SurfaceHolder.SURFACE_TYPE_GPU);
         mContext = context;
         parentact = act;
	}
	public CrackBView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setFocusable(true);
        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_GPU);
        Log.d("TAG", "CrackBView");
	}
	public CrackBView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setFocusable(true);
        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_GPU);
        Log.d("TAG", "CrackBView");
	}
	public void back() {
		// TODO Auto-generated method stub
		mStoneTalbe.restorehistory();
	}
	public void updateBini() {
		// TODO Auto-generated method stub
		if (aniVector.isEmpty() == false)
		{
			AniBlock aniblock;
			for (int i = 0 ; i < aniVector.size() ; i++)
			{
				aniblock = (AniBlock) aniVector.get(i);
				if (aniblock.getPoint().y > mHeight)
				{
					aniVector.remove(i);
					i--;
				}

			}
			int size = aniVector.size();
			for (int i = 0 ; i < size ; i++)
			{
				aniblock = (AniBlock) aniVector.get(i);

				aniblock.nextstep();
			}
			
		}
		if (mLayer != null)
		{
			if (!mLayer.step())
			{
				
				mLayer = null;
				
			}
		}
	}
	public void ShowErr(String srt)
	{
		
		AlertDialog.Builder Builder =  new AlertDialog.Builder(getContext());
		Builder.setMessage(srt)
	       .setCancelable(false)
	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   loadGame();
	        	   dialog.cancel();
	           }
	       })
	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               dialog.cancel();
	               BiniGameActivity bini = (BiniGameActivity)mContext;
	               bini.showScoreDialog();
	           }
	       });
		AlertDialog alert = Builder.create();
		alert.show();
		
	}

	public Point transPoint(Point pt)
	{
		pt.offset((int)-nwgap  , (int)-(vgap+ nhgap));
		
		int width = mWidth / maxX;
		int height = mHeight/ maxY;
		pt.x = pt.x / width;
		pt.y = pt.y / height;
		return new Point(pt);
	}
	public void close()
    {
    	refreshhandler.stop();
    }
	public void resume()
    {
    	refreshhandler.update(FRAME);
    }
	public boolean onTouchEvent(MotionEvent event) {
		
	    float x = event.getX();
	    float y = event.getY();
	    Point pt = new Point((int)x,(int)y);
	    if (mGameFlag == 0 || mLoading == true)
	    	return false;
	    switch (event.getAction()) 
	    {
			case MotionEvent.ACTION_DOWN:
				pt = transPoint(pt);
				if (pastPt.equals(pt) && pastPt != null)
					break;
				if (pt.x < this.maxX && pt.y < this.maxY && pt.x >= 0 && pt.y >= 0) {

				}
				break;
			case MotionEvent.ACTION_UP:
				pt = transPoint(pt);
	
				if (exitButton.isClick(x, y))
				{
					showExitDialog();
					return true;
				}
				
				int count = 0;
				try {
	
					aniIterator = mStoneTalbe.findwStone(pt.x, pt.y);
					int z;

					Stone tempStone = null;
					while (aniIterator != null && aniIterator.hasNext()) {
	
						Point ptx = (Point) aniIterator.next();
						tempStone = mStoneTalbe.getStone(ptx.x, ptx.y);
						z = tempStone.getShape();

						ptx.x = mStoneW * ptx.x;
						ptx.y = mStoneH * ptx.y;
						aniVector.add(new AniBlock(ptx, z));
	
					}
					count = mStoneTalbe.findStone(pt.x, pt.y);
					if (count > 0) {
						
						soundplay(mSound_crack[2]);
					//	vibplay(0);
						int shpe = tempStone.getShape() -1;
						if (shpe > 2)
							shpe = 2;
						mScoreset[shpe] -= count;
						if (mScoreset[shpe] <= 0 )
						{
							ButtonX button = mInterface.get(shpe);
							if (!button.getEnable())
							{
								button.setEnable();
								mLayer = new Animate(button.foreImage,button.mRect,1000);
								RectF rect = new RectF(0,0,mWidth,mWidth);
								Matrix mat = new Matrix();
								mat.setScale(0.5f, 0.5f);
								mat.mapRect(rect);
								rect.offsetTo(mWidth / 2 - rect.width() / 2  , mHeight / 2 - rect.height()/2);
								mLayer.setBounds(new Rect((int)rect.left,(int)rect.top,(int)rect.right,(int)rect.bottom));
								if (shpe == 1)
								{
									mStoneTalbe.createAddstone();
								}
								
							}
							
						}
						else
						{

						}
						mInterface.get(shpe).setText("" + mScoreset[shpe]);
					}
					for (ButtonX button : mInterface)
					{
						if (button.isClick(x, y))
						{
							int index = button.mIndex;
							switch (index) {
								case 0:
									down();
									soundplay(mSound_fall);
									vibplay(0);
									mScoreset[0] =  mScoresetlimit[0];
									button.setText("" + mScoreset[0]);
									button.setDisable();
									break;
								case 1:
//									RotateStart();
//									mStoneTalbe.shiftLeft();
									insert();
									soundplay(mSound_add);
									vibplay(1);
									mScoreset[1] =  mScoresetlimit[1];
									button.setDisable();
									button.setText("" + mScoreset[1]);
									break;
								case 2:
									back();
									soundplay(mSound_Back);
									mScoreset[2] =  mScoresetlimit[2];
									button.setDisable();
									button.setText("" + mScoreset[2]);
									break;
								default:
									break;
							}
						}
					}

				}
	
				catch (Exception e) {
					e.printStackTrace();
					
					ShowErr("Error Restart? ");
				}
//				{
//					boolean end = true;
//					for (int s : mScoreset)
//					{
//						if (s <= 0)
//							end = false;
//						Log.d("TAG", "s : " + s);
//					}
//					if (end)
//						Toast.makeText(getContext(), "Button0", Toast.LENGTH_SHORT).show();
//				}
				
				if (mStoneTalbe.isEnd() <= 0) {
					boolean end = true;
					if (mScoreset[0] <= 0)
						end = false;
					if (mScoreset[1] <= 0)
						end = false;
				//	if (mScoreset[])
					//for (int s : mScoreset)
					//{
						//if (s <= 0)
						
					//}
					
					if (end)
						showScoreDialog();
				}
				break;
			}
		
	    return true;
	}
	private void RotateStart() {
		// TODO Auto-generated method stub
		mEndRotate = mRotate + 90f; 
		
	}
	public void drawFrame() {
		// TODO Auto-generated method stub
	
		Canvas c = null;
		try
		{
			c = getHolder().lockCanvas();
			if (c != null)
			{
//				c.save();
//				c.rotate(mEndRotate, mWidth/2 , mHeight/2);
				Draw(c);
//				c.restore();
			}
		}
		
		finally
		{
			if (c != null) getHolder().unlockCanvasAndPost(c);
		}
		
	}
	public void Draw(Canvas c)
	{
		c.drawARGB(255, 50	, 50, 50);
		
		
		if (mLoad == false)
		{
			if (mLoadStart == false) 
				prepareImage();
		}

		else if (mGameFlag !=0)
		{
			DrawFrame(c);
			DrawUI(c);
		}


		if (mLoading == true)
		{
			Rect rectA = new Rect();
			Rect rectB = new Rect();
			rectA.left =0;
			rectA.top = 0;
			rectA.right = mWidth;
			rectA.bottom = getHeight() /2 ;
			rectB.left =0;
			rectB.top = getHeight() /2;
			rectB.right = mWidth;
			rectB.bottom = getHeight();
			int a = getHeight() /2,b =getHeight() /2;
			
			if (mLoadthread !=null && mLoadthread.fade == true)
			{
				Point point = mLoadthread.getMatrix(a, b);
				
				rectA.offsetTo(0, point.x - rectA.height());
				rectB.offsetTo(0, point.y);
			}
			if (mLoading == true)
			{
				//c.rotate(180,mWidth,getHeight());
				c.drawBitmap(mLoadBitmap, null, rectA,mPaint);
				c.drawBitmap(mLoadBitmap, null, rectB, mPaint);
			}
		}
		if (mPaint == null)
		{
			mPaint = new Paint();
			mPaint.setTextAlign(Align.LEFT);
			mPaint.setColor(Color.WHITE);
		}	
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setColor(Color.BLACK);
		//c.drawText("delay : " + delaytime, 180, 52, mPaint);
		//c.drawText("fps : " + String.format("%2.2f", framerate), 250, 42, mPaint);

	}
	protected void DrawFrame(Canvas canvas) {
		// TODO Auto-generated method stub

		Rect rt = new Rect();
		int x = 0;
		int y = 0;
		Point tpt = new Point();
		canvas.save();
		canvas.translate(nwgap  , vgap+ nhgap);
		
		for (x = 0 ; x < maxX ; x++)
		{
			for ( y = 0 ; y < maxY ; y++)
			{			
				if (mStoneTalbe.getStone(x, y).getShape() <= 0)
				{

				}
				else
				{
					mStoneTalbe.getStone(x, y).getPoint(tpt);
					rt.setEmpty();
					rt.set(x * mStoneW + tpt.x , y * mStoneH + tpt.y,0,0);
					canvas.drawBitmap(mStonBitmapArray.get(mStoneTalbe.getStone(x, y).getShape()-1), rt.left,rt.top, mPaint );
				}
			}
		}
		
		
		if (aniVector.isEmpty() == false)
		{
			AniBlock anib;
			Point anipt;
			int shape;
			int size = aniVector.size();
			float halfmStoneW = mStoneW /2;
			float halfmStoneH = mStoneH / 2;
			for (int i = 0 ; i < size ; i++)
			{
				canvas.save();
				anib = (AniBlock) aniVector.get(i);
				anipt = anib.getPoint();
				shape = anib.getShape();
				
				
				canvas.translate(anipt.x  , anipt.y);
				canvas.translate(halfmStoneW, halfmStoneH );
				
				canvas.rotate(anib.getDegree());
				canvas.translate(-halfmStoneW, -halfmStoneH);
				
				canvas.drawBitmap(mStonBitmapArray.get(shape-1),0,0, mBorderPaint);

				canvas.restore();
				
			}
		}
		if (mLayer !=null)
			mLayer.draw(canvas);
		canvas.restore();
	}
	public void DrawUI(Canvas c)
	{
		c.save();
		c.clipRect(mUITopRect);
		
		c.drawPaint(mShaderPaint2);
		c.restore();
		
		c.save();
		
		//mUIBottom 
		c.translate(0, mHeight + vgap);
		c.clipRect(mUIBottom);
		c.drawPaint(mShaderPaint2);
		c.restore();
		
		int score = mStoneTalbe.getScore();
		if (pastscore != score || mScoreBit==null ) 
			mScoreBit = makeNumberBitmap(mStoneTalbe.getScore(), mBM_number, mAntiPaint);
		float rate = (float)(vgap - hgap*2) / mScoreBit.getHeight();
		
		Rect scorerect = new Rect(0,0, (int) (rate*mScoreBit.getWidth() -hgap*2),vgap - hgap*2);
		scorerect.offset(hgap*2, hgap);
		c.drawBitmap(mScoreBit,null, scorerect, mPaint);
		pastscore = score;
		int dir = mStoneTalbe.getDirection();
		
		
//		if ( dir > 0)
//		{
//			c.drawBitmap(mBM_right,null, mTopArrowRect, mPaint);	
//		}
//		else
//		{
//			c.drawBitmap(mBM_left,null, mTopArrowRect, mPaint);
//		}
		
		c.save();
		for(ButtonX button :mInterface  )
		{
			button.draw(c);
		}
		exitButton.draw(c);
		Rect bounds = new Rect();
	//	String text = String.format("Depth %4d m   Dis %4d m", sDepth , mStoneTalbe.getmDist());
	//	mForePaint.setColor(0xFF88FF88);
	//	mForePaint.getTextBounds(text, 0, text.length(), bounds );
	      
		mForePaint.setColor(Color.BLACK);
		mForePaint.setTextAlign(Align.CENTER);
		mPaint.setColor(Color.LTGRAY);
		Matrix mt = new Matrix();
		
		RectF recf = new RectF(bounds);
		mt.setScale(1.1f, 1.2f,recf.centerX(),recf.centerY());
		mt.mapRect(recf);
		recf.offsetTo(mTopArrowRect.left - recf.width() - hgap,hgap);
		
//		c.drawRect(bounds, mPaint);
	//	c.drawBitmap(mUIBlock,null, recf, mPaint);
	//	c.drawText(text, recf.centerX()  , recf.bottom-hgap,mForePaint);
		c.restore();
		
	}
	public void prepareImage()
	{
		mLoadStart  = true;
		mLoadBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.block_1);
//		mLoadBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.round_ui);
		mLoadBitmap = Bitmap.createScaledBitmap(mLoadBitmap, getWidth()	, getHeight() /2 , true);

		
		post(new Runnable() {

			



			@Override
			public void run() {
				Log.d("TAG", "PrepareImage");
					
				// TODO Auto-generated method stub
				mTopArrowRect = new Rect(mWidth - hgap*2 - (vgap-hgap), 0 + hgap,mWidth - hgap*2, vgap -hgap);
				
				mUITopRect = new RectF();

				mUITopRect.top= 0;
				mUITopRect.left=0;
				mUITopRect.right = mWidth;
				mUITopRect.bottom = vgap;
				
				mUIBottom = new RectF();
				mUIBottom.top= 0;
				mUIBottom.left=0;
				mUIBottom.right = mWidth;
				mUIBottom.bottom =  bgap;;
				
				mAntiPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
				mBM_number = BitmapFactory.decodeResource(getResources(), R.drawable.number_05);
				mBorderPaint = new Paint();
				mPaint     =   new Paint();
				
				mFillPaint =   new Paint();
				
				mForePaint =   new Paint(Paint.ANTI_ALIAS_FLAG);
				//mForePaint.setFakeBoldText(true);
				
				mBorderPaint.setStyle(Paint.Style.STROKE);
				mBorderPaint.setStrokeWidth(1);
				mBorderPaint.setAlpha(250);
				mBorderPaint.setColor(Color.BLACK);
				//
				mFillPaint.setAlpha(255);
				mFillPaint.setStyle(Paint.Style.FILL);
				
				mForePaint.setColor(Color.BLACK);
				mForePaint.setTextAlign(Align.LEFT);
				mForePaint.setTextSize((float) (mStoneH/2.8));
			
				
				mRestart = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_rotate);
				mRestart = Bitmap.createScaledBitmap(mRestart, 30, 30, false);
				sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
				mSound_crack[0] = sp.load(getContext(),R.raw.crack5,1);
				mSound_crack[1] = sp.load(getContext(),R.raw.boing_2,1);
				mSound_crack[2] = sp.load(getContext(),R.raw.crack4,1);
				mSound_add = sp.load(getContext(),R.raw.addstone4,1);
				mSound_fall = sp.load(getContext(),R.raw.down_stone,1);
				mSound_Back = sp.load(getContext(),R.raw.back,1);
				mStonBitmapArray = new ArrayList<Bitmap>();
				vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
				
				
				Rect rt = new Rect();

				boolean mBimage = GameConfig.Theme == 0 ? false : true;
				if (mBimage)
				{
					Options op = new Options();
					
					
					Bitmap mainbitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.block50_gray_8);
					Bitmap mainBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.block50_gray_6);
					Bitmap mainBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.block50_gray_r);
					Bitmap mainbitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.block50_gray_r_1);
					Bitmap mainBitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.block50_gray_r_2);
					Bitmap mainBitmap6 = BitmapFactory.decodeResource(getResources(), R.drawable.block50_gray_r_3);
					Rect rec = new Rect(0,0,mStoneW,mStoneH);
					for (int i = 0 ; i < Stone_O.colorTable.length -1; i++)
					{
						Bitmap bitmap = null;
						bitmap = Bitmap.createBitmap(mStoneW, mStoneH, Config.RGB_565);
						

					
						Canvas canvas = new Canvas(bitmap);
						if (i == 0)
						{
							canvas.drawBitmap(mainbitmap1, null, rec, null);
						}
						else if (i == 1)
						{
							canvas.drawBitmap(mainBitmap2, null, rec, null);
						}
						else if (i == 2)
						{
							canvas.drawBitmap(mainBitmap3, null, rec, null);
						}
						else  if (i ==3)
						{
							canvas.drawBitmap(mainbitmap4, null, rec, null);
						}
						else if (i ==4)
						{
							canvas.drawBitmap(mainBitmap5, null, rec, null);
						}
						else 
						{
							canvas.drawBitmap(mainBitmap6, null, rec, null);
						}
						
						rt.set(0,0,mStoneW  , mStoneH );
						mFillPaint.setColor(Stone.colorTable[i+1]);
						mFillPaint.setAlpha(100);
						Xfermode xp =  new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
						mFillPaint.setXfermode(xp);
						canvas.drawRect(rt, mFillPaint);
						//canvas.drawRect(rt, mBorderPaint);
						
						mFillPaint.setXfermode(null);
						mStonBitmapArray.add(bitmap);
						Config config = bitmap.getConfig();
						Log.d("TAG", "config" + config.toString());
					}

				}
				else
				{
					
					Bitmap mainbitmap9 = BitmapFactory.decodeResource(getResources(), R.drawable.y_h_block_08);
					Bitmap mainbitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.y_h_block_07);
					Bitmap mainbitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.y_h_block_06);
					
					
					Bitmap mainbitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.y_block_01);
					Bitmap mainBitmap6 = BitmapFactory.decodeResource(getResources(), R.drawable.y_h_block_02);
					Bitmap mainBitmap7 = BitmapFactory.decodeResource(getResources(), R.drawable.y_block_03);
					Bitmap mainBitmap8 = BitmapFactory.decodeResource(getResources(), R.drawable.y_block_02);
					Bitmap mainbitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.y_h_block_04);
	//				
					mainbitmap1 = Bitmap.createScaledBitmap(mainbitmap1,mStoneW, mStoneH,false );
					mainbitmap2 = Bitmap.createScaledBitmap(mainbitmap2,mStoneW, mStoneH,false );
					mainbitmap9 = Bitmap.createScaledBitmap(mainbitmap9,mStoneW, mStoneH,false );
					
					mainbitmap3 = Bitmap.createScaledBitmap(mainbitmap3,mStoneW, mStoneH,false );
					
					mainBitmap6 = Bitmap.createScaledBitmap(mainBitmap6,mStoneW, mStoneH,false );
					mainBitmap7 = Bitmap.createScaledBitmap(mainBitmap7,mStoneW, mStoneH,false );
					mainBitmap8 = Bitmap.createScaledBitmap(mainBitmap8,mStoneW, mStoneH,false );
					
					mStonBitmapArray.add(mainBitmap6);
					mStonBitmapArray.add(mainBitmap7);
					mStonBitmapArray.add(mainBitmap8);
					mStonBitmapArray.add(mainbitmap3);
					mStonBitmapArray.add(mainbitmap1);
					mStonBitmapArray.add(mainbitmap2);
					mStonBitmapArray.add(mainbitmap9);

				}
				Bitmap mainbitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.block_1);
				Bitmap mainbitmap = Bitmap.createBitmap(mStoneW, vgap, Config.RGB_565);
				Canvas c = new Canvas(mainbitmap);
				c.drawBitmap(mainbitmap4, null, new Rect(0,0,mStoneW,vgap), null);
				
				mShader1 = new BitmapShader(mainbitmap, android.graphics.Shader.TileMode.REPEAT,
						android.graphics.Shader.TileMode.REPEAT);
				mShaderPaint = new Paint();
				mShaderPaint2 = new Paint();
				mUIBlock = mainbitmap;
				mShaderPaint.setShader(mShader1);
				
				Bitmap temp1 = Bitmap.createBitmap(mWidth, bgap, Config.RGB_565);
				c = new Canvas(temp1);
				c.drawBitmap(mainbitmap4,null, new Rect(0,0,mWidth,bgap),null);
				Shader mShader2 = new BitmapShader(temp1, android.graphics.Shader.TileMode.REPEAT,
						android.graphics.Shader.TileMode.REPEAT);
				mShaderPaint2.setShader(mShader2);
				int w = getWidth();
				int gap = w / 4;
				
				Rect rect = new Rect(0,0,bgap  - hgap * 2,bgap - hgap * 2);
				rect.offsetTo(0, vgap + mHeight + hgap);
				rect.offset(gap - rect.width()/2, 0);
				ButtonX button1 = new ButtonX(rect, mStonBitmapArray.get(0));
				
				Bitmap bit1 = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_down);
				button1.setNumberImage(mBM_number);
				button1.setText("1");
				button1.setForeImage(bit1);
				rect.offset(gap, 0);
				ButtonX button2 = new ButtonX(rect , mStonBitmapArray.get(1));
				
				Bitmap bit2 = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_up);
				button2.setNumberImage(mBM_number);
				button2.setForeImage(bit2);
				button2.setText("1");
				rect.offset(gap, 0);
				ButtonX button3 = new ButtonX(rect , mStonBitmapArray.get(mStonBitmapArray.size()-1));
				button3.setNumberImage(mBM_number);
				
				Bitmap bit3 = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_back);
				button3.setText("1");
				button3.setForeImage(bit3);
				button1.mIndex = 0;
				button2.mIndex = 1;
				button3.mIndex = 2;
				mInterface.add(button1);
				mInterface.add(button2);
				mInterface.add(button3);
				
				mBM_left = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left);
				mBM_right = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_right);
				Bitmap exit = BitmapFactory.decodeResource(getResources(), R.drawable.exitbutton_gray);
				exitButton = new ButtonX(mTopArrowRect, exit);
				exitButton.setEnable();
				mLoad=true;
			}
		});
		
	}
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		vgap = height / 14;
		hgap = height / 200;
		bgap =  vgap + vgap/4;
		mWidth = width;
		mHeight = height -( vgap + bgap);
		mStoneW = mWidth/ maxX;
		mStoneH = mHeight / maxY;
		nwgap = (mWidth - mStoneW * maxX) /2; 
		nhgap =  (mHeight - mStoneH * maxY) /2;
		Log.d("TAG", "surfaceChanged");
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

		Log.d("TAG", "surfaceCreated");
		refreshhandler.update(1000);
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		Log.d("TAG", "surfaceDestroyed");

		
	}
	class LoadingThread 
	{
		long start_time = 0;
		long end_time = 0;
		public boolean fade = true;
		static final long DUR = 1000;
		int point = 0;
		public LoadingThread(int startPoint)
		{
			start_time = System.currentTimeMillis();
			end_time = start_time + DUR;
			point = startPoint;
			fade = true;
		}
		public  Point getMatrix(int a, int b)
		{
			
			long now = System.currentTimeMillis();
			if (now > end_time )
			{
				mLoading = false;
				fade = false;
				a = -1;
				b = -1;
			}
			else
			{
				long x = now - start_time ;
				
				float rate = x / (float)DUR;
				if ( rate > 0.3f)
				{
					a = (int) (point - (point * rate));
					b = (int) (point + (point * rate));
				}
			}
			return new Point(a,b);
		}

	}
	public static int[] countDigits(int number)
	{
		
		Stack<Integer> stack = new Stack<Integer>();
		while (number > 0)
		{
			int mr = number %10;
			number /= 10;
			stack.push(mr);
			
		}
		int size = stack.size();
		if (size == 0)
		{
			size = 1;
			stack.push(0);
		}
		int[] array = new int [size];
		int index = 0;
		while (!stack.isEmpty())
		{
			array[index] = stack.pop();
			index++;
		}
		
		return array;
	}
	public static Bitmap makeNumberBitmap(final int number, Bitmap bit, Paint paint)
	{
		int temp =  number;
		temp = Math.abs(number);
		int array[] = countDigits(temp);
		Bitmap bitmap = Bitmap.createBitmap(array.length*30, 40, Config.ARGB_4444);
		Canvas c = new Canvas(bitmap);
		int w = bit.getWidth() /10;
		int h = bit.getHeight() ;
		Rect rect = new Rect(0,0,w,h);

		Rect dstrect = new Rect(0,0,30,40);
		for (int i = 0 ; i < array.length ; i++)
		{
			rect.offsetTo(array[i]*rect.width(), 0);
			dstrect.offsetTo(i*dstrect.width(), 0);
			c.drawBitmap(bit,rect,dstrect,paint);
		}
		return bitmap;
	}
	class AniBlock
	{
		Point pt;
		int shape;
		float degree = 0;
		int xincreases = 10;
		int yincreases = -5;
		int direction = 0;
		public AniBlock(Point pt , int shape)
		{
			this.pt = pt;
			this.pt.x = pt.x ;
			this.pt.y = pt.y ;
			this.shape = shape;
			xincreases = rnd.nextInt(mStoneW) -mStoneW /2;
			yincreases = rnd.nextInt(mStoneH/2)-mStoneH/2  ;
			direction = rnd.nextBoolean() == true ? 1:  -1 ;
			
		}
		public Point getPoint()
		{
			return pt;
		}
		public int getShape()
		{
			return shape;
		}
		public float getDegree()
		{
			return degree;
		}
		public void offset(int x, int y) {
			// TODO Auto-generated method stub
			pt.x += x;
			pt.y += y;
			degree = (degree + 30 )%360;
		}
		public void nextstep() {
			// TODO Auto-generated method stub
			pt.x += xincreases;
			pt.y += yincreases;
			degree = (degree + 40*direction ) ;
			xincreases /= 1.05;
			yincreases += 4;
		}
	}
	class RefreshHandler extends Handler
	{
		
		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			long temp = System.currentTimeMillis();
			update(FRAME);
			if (mGameFlag != 0)
			{
				updateBini();
				mStoneTalbe.step(mStoneW,mStoneH);
			}
			drawFrame();
			
			delaytime = System.currentTimeMillis() - lasttime;
			framerate = 1000f /delaytime;
			lasttime = System.currentTimeMillis();
			delaytime = lasttime - temp ;

		}
		public void update(long delayMillis)
		{
			this.removeMessages(0);
			this.sendMessageDelayed(this.obtainMessage(0), delayMillis);
		}
		public void stop()
		{
			this.removeMessages(0);
		}
	}
	public void saveGame(SaveOBJ outState) {
		
		// TODO Auto-generated method stub
		
		ArrayList<HistoryTable> history = mStoneTalbe.getHistoryTable();
		HistoryTable save = (HistoryTable) mStoneTalbe.getTable();
		outState.History = history;
		outState.Table = save;
	}
	public void crackAll()
	{
		if (mStoneTalbe != null ) {
			
			for ( int i = 0 ; i < maxX ; i++)
			{
				for (int j = 0 ; j < maxY ; j++)
				{
					Point ptx = new Point();
					Stone tempStone = mStoneTalbe.getStone(i,j);
					int z = tempStone.getShape();
					if (z != 0)
					{
						ptx.x = mStoneW * i;
						ptx.y = mStoneH * j;
						aniVector.add(new AniBlock(ptx, z));
					}
				}
			}
		}	
	}
	public void loadGame() {
		// TODO Auto-generated method stub
		
		mLoading = true;
		sDepth = 0;

		Log.d("TAG", "X " + maxX + "Y" + maxY);
		mStoneTalbe = new BiniTable(maxX,maxY);
		mGameFlag = 1;
		
		for(int i = 0 ; i < mScoreset.length ; i++)
		{
			mScoresetlimit[i] =5;
			mScoreset[i] = 1;
			
		}
		for (ButtonX button : mInterface)
		{
			button.setDisable();
			button.setText("1");
		}

		postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				mLoadthread = new LoadingThread(getHeight() /2);
				mGameStartTime = System.currentTimeMillis();
			}
		},1500);
		
	}
	public void loadGame(SaveOBJ inState)
	{
		loadGame() ;
	}
	public HistoryTable saveGame(HistoryTable outState) {
		// TODO Auto-generated method stub
		return (HistoryTable) mStoneTalbe.getTable();
	}
	public void soundplay(int index)
	{
		if (GameConfig.SoundOn)
			sp.play(index, 0.3f, 0.3f, 0, 0, 1);
		
	}
	public void vibplay(int index)
	{
		if (GameConfig.VibOn)
			vibrator.vibrate(pattern[index], -1);
	}
	
	public void insert() {
		// TODO Auto-generated method stub
		mStoneTalbe.InsertLine();
	}
	public void down() {
		// TODO Auto-generated method stub
		mStoneTalbe.downLevel();
		sDepth ++;
	}
	public void setXY(int x, int y) {
		// TODO Auto-generated method stub
		//maxX = 8;
		//maxY =8;
	}
	public void getResult(GameResult result) {
		// TODO Auto-generated method stub
		result.Score = mStoneTalbe.getScore();
		result.Distance = mStoneTalbe.getmDist();
		result.Depth = sDepth;
		result.Durringtime = System.currentTimeMillis() - mGameStartTime;
	}
	public void showScoreDialog()
	{
		// TODO Auto-generated method stub
		BiniGameActivity act  = (BiniGameActivity)parentact;
		act.showScoreDialog();		
	}
	public void showExitDialog()
	{
		BiniGameActivity act  = (BiniGameActivity)parentact;
		act.ShowExit(mContext, "Exit?");
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (mRunning)
		{
			long temp = System.currentTimeMillis();
//			update(FRAME);
			synchronized (mStoneTalbe) {
				if (mGameFlag != 0)
				{
					updateBini();
					mStoneTalbe.step(mStoneW,mStoneH);
				}

			}

			drawFrame();
			delaytime = System.currentTimeMillis() - lasttime;
			framerate = 1000f /delaytime;
			lasttime = System.currentTimeMillis();
			delaytime = lasttime - temp ;
			long d = FRAME - delaytime;
			if (d <0)
			{
				d = FRAME;
			}
			try {
				Thread.sleep(d);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.d("TAG","Thread end");
	}
	boolean mRunning = false;


	public void pause() {
		// TODO Auto-generated method stub
		refreshhandler.stop();
	}
}