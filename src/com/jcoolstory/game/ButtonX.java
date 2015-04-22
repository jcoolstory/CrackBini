package com.jcoolstory.game;

import java.util.Stack;

import com.jcoolstory.crackbinidemo.CrackBView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.util.Log;

class ButtonZ 
{
	protected Bitmap mImage;
	protected String mText;
	public Rect mRect;
	protected Paint mPaint;
	
	protected boolean mEnable ;
	public int mIndex =0;
	public ButtonZ(Rect rect , Bitmap Image)
	{
		Rect temp = new Rect(rect);
		initButtonX(temp,Image,"");
	}	
	public ButtonZ(int x, int y , Bitmap Image)
	{
		Rect rect = new Rect(x,y,Image.getHeight()+ x , Image.getHeight() + y);
		
		initButtonX(rect,Image,"");
	}
	public void setText(String text)
	{
		mText = text;
	}
	public void setPaint(Paint paint)
	{
		mPaint = paint;
	}
	public void setEnable()
	{
		mEnable = true;
	}
	public void setDisable()
	{
		mEnable = false;
	}
	protected void initButtonX(Rect rect, Bitmap image, String text) {
		// TODO Auto-generated method stub
		 mEnable = false;
		mPaint = new Paint();
		mPaint.setTextAlign(Align.CENTER);
		
		mPaint.setColor(Color.BLACK);
		mRect = rect;
		mImage = image;
		mText = text;

	}
	public boolean isClick(float x, float y)
	{
		if (mEnable == true)
			return mRect.contains((int)x, (int)y);
		return false;
	}
	public void setPosition(int x, int y)
	{
		mRect.offset(x, y);
	}
	public void draw(Canvas c)
	{

		c.save();
	//	c.translate(mRect.left, mRect.top);
		
		c.drawBitmap(mImage,null, mRect, mPaint);

		
		c.restore();
	}
}
public class ButtonX extends ButtonZ
{
	public RectF forerect = null;
	public Bitmap mNuber = null;
	public Bitmap forenumImage = null;
	public Bitmap foreImage = null;
	public ButtonX(int x, int y, Bitmap Image) {
		super(x, y, Image);
		// TODO Auto-generated constructor stub
		
	}
	public void setForeImage(Bitmap bit)
	{
		foreImage = bit;
	}
	public void setNumberImage(Bitmap bit)
	{
		mNuber = bit;
	}
	public void draw(Canvas c)
	{
		super.draw(c);
		
		if (mEnable && foreImage != null)
		{
			c.drawBitmap(foreImage, null,mRect, null);
		}
		else
		{

			if (mNuber != null)
				c.drawBitmap(forenumImage,null,forerect,null);
//			else
//				c.drawText(" "+number+' ', mRect.centerX(), rect.centerY(), mPaint);
		}

	}

	public Bitmap makeNumberBitmap(int number)
	{
		int temp =  number;
		temp = Math.abs(number);
		int array[] = CrackBView.countDigits(temp);
		Bitmap bitmap = Bitmap.createBitmap(array.length*30, 40, Config.ARGB_4444);
		Canvas c = new Canvas(bitmap);
		int w = mNuber.getWidth() /10;
		int h = mNuber.getHeight() ;
		Rect rect = new Rect(0,0,w,h);
//		Rect rect = new Rect(0,0,50,80);
		RectF dstrect = new RectF(0,0,30,40);
		for (int i = 0 ; i < array.length ; i++)
		{
			rect.offsetTo(array[i]*rect.width(), 0);
			dstrect.offsetTo(i*dstrect.width(), 0);
			c.drawBitmap(mNuber,rect,dstrect,mPaint);
		}
		return bitmap;
	}
	public ButtonX(Rect rect, Bitmap Image) {
		super(rect, Image);
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see com.jcoolstory.game.ButtonZ#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		super.setText(text);
		int number = Integer.valueOf(text.trim());
		if (number > 9)
			number =0;
		forenumImage = makeNumberBitmap(number);
		forerect = new RectF(mRect);
		Matrix nt = new Matrix();
		nt.setScale(0.6f, 0.6f,forerect.centerX(),forerect.centerY());
		nt.mapRect(forerect);
		//Log.d("TAG","setText" + (forenumImage == null ?  true :false));
	}
	/* (non-Javadoc)
	 * @see com.jcoolstory.game.ButtonZ#setPaint(android.graphics.Paint)
	 */

	/* (non-Javadoc)
	 * @see com.jcoolstory.game.ButtonZ#initButtonX(android.graphics.Rect, android.graphics.Bitmap, java.lang.String)
	 */
	@Override
	protected void initButtonX(Rect rect, Bitmap image, String text) {
		// TODO Auto-generated method stub
		super.initButtonX(rect, image, text);

	}
	/* (non-Javadoc)
	 * @see com.jcoolstory.game.ButtonZ#setPaint(android.graphics.Paint)
	 */
	@Override
	public void setPaint(Paint paint) {
		// TODO Auto-generated method stub
		super.setPaint(paint);

	}
	public boolean getEnable() {
		// TODO Auto-generated method stub
		return mEnable;
	}
}