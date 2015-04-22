package com.jcoolstory.crackbinidemo;

import java.io.Serializable;
import java.util.Random;

import android.graphics.Color;
import android.graphics.Point;

class Stone_O  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4135474639916817279L;
	private int Shape;
	public static int[] colorTable = 
	{ Color.BLACK, Color.YELLOW,Color.BLUE , Color.RED ,Color.WHITE,  Color.GREEN,
		Color.CYAN ,Color.MAGENTA ,Color.CYAN ,Color.GRAY , Color.BLACK };
	
	public int index= 0;
	public Stone_O()
	{
		Random r = new Random();
		Shape = 1 + r.nextInt(GameConfig.Colors);
//		if (Shape == 1)
//		{
//			if (r.nextBoolean())
//				Shape = 1 + r.nextInt(GameConfig.Colors);
//		}
			

	}
	public Stone_O(int tag)
	{
		Shape = tag;
	
	}
	public Stone_O(Stone_O st)
	{
		Shape = st.Shape;
		this.index = 0; 
	}
	public int getColor()
	{
		if (Shape <= 0)
			return 0;
		return colorTable[Shape-1];
	}
	
	public int getIndex()
	{
		return index;
	}
	public int getShape()
	{
		return Shape;	
	}
	
	
}
class SPoint extends Point implements Serializable
{
	
}
class Stone extends Stone_O
{
	private SPoint point = new SPoint();
	public boolean posible = false;
	private int offsetx = 0;
	private int offsety = 0;
	private int pastx =0;
	private int incre = 5;
	private int pasty =0;
	private boolean down = false;
	public Stone(int x, int y)
	{
		super();
		point.x = x;
		point.y = y;

	}
	public Stone(int x, int y , int tag)
	{
		super(tag);
		point.x = x;
		point.y = y;
	}
	
	public Stone(Stone stone) {
		// TODO Auto-generated constructor stub
		super(stone);
	}
	public void step(int x, int y)
	{
		
		if (down == true )
		{
			if (pasty == 0)
			{
				incre += 5;
				offsety += 5 + incre;
				if (offsety > 0)
				{
					incre = 5;
					offsety= 0;
					down = false;
				}
				
			}
			else
			{
				offsety = -(pasty * y);
				pasty = 0;
			}
	

			offsetx = (int) (Math.random() * 8 ) -4;
		}
		else
		{ 
			if (posible)
			{
				double temp = Math.random();
				if (temp < 0.02)
					offsetx = (int) (Math.random() * 4 ) -2;
			}
		}
		
	}
	public Point getPoint()
	{
		Point temp = new Point();
		temp.offset(offsetx, offsety);
		return temp;
	}
	public void setPastPoint(int i, int x) {
		// TODO Auto-generated method stub
//		if (down == true)
//		{
//
//		}
//		else
		{
			
			pasty ++;
			down = true;

		}
	}
	public void getPoint(Point tpt) {
		// TODO Auto-generated method stub
		tpt.x = offsetx;
		tpt.y = offsety;
	}
}