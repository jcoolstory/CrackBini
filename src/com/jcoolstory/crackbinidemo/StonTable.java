package com.jcoolstory.crackbinidemo;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import android.graphics.*;
import android.util.Log;
import android.widget.ArrayAdapter;

class SaveOBJ implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4411065732470507415L;
	/**
	 * 
	 */

	public HistoryTable Table;
	public ArrayList<HistoryTable> History; 
}

class HistoryTable implements Serializable
{

	/**
	 * 
	 */
	public HistoryTable() {
		// TODO Auto-generated constructor stub

	}
	public HistoryTable(int score, Stone[][] historyTable) {
		// TODO Auto-generated constructor stub
		mScore = score;
		table = historyTable;
	}
	public Stone[][] table;
	
	public int mScore;
}
class BiniTable extends StoneTable
{

	private Stone[] addStone;
	public BiniTable(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
		
	}
	public Stone[] getAddStone()
	{
		return addStone;
	}
	public void createAddstone()
	{
		addStone = new Stone[ilength];
		for (int i = 0 ; i < ilength ; i++)
		{
			addStone[i] = new Stone(i, 0);
		}
	}
	public void pushAddstone()
	{
		
	}
	public void InsertLine()
	{
		CopyTable();
		for (int i = 0 ; i < Table.length ; i++)
		{
			if (Table[i][0].getShape() <= 0)
				Table[i][0] = addStone[i];
			addStone[i] = null;
		}
		downsStone();
		
		addStone = null;
	}
	
}
class StoneTable
{
	protected Stone[][] Table = null;
//	private Stone[][] historyTable = null;
	private boolean mHistory = false;
	protected int ilength ;
	private int iheight;

	public void setScore(int score) {
		Score = score;
	}
	int[][] tempindex ;
	Vector vt = null;
	private int direction = 1;
	private ArrayList<HistoryTable> historyList = new ArrayList<HistoryTable>();
	private int mDist;
	public StoneTable(int x, int y)
	{
		Table = new Stone[x][y];
		for (int i = 0 ; i < y ; i++)
		{
			for (int j = 0 ; j < x ; j++)
			{
				Table[j][i] = new Stone(i,j);
			}
		}
		ilength = Table.length;
		iheight = Table[0].length;
		
	}
	public void createTable()
	{
		int x = Table.length;
		int y = Table[0].length;
		
		Table = new Stone[x][y];
		for (int i = 0 ; i < y ; i++)
		{
			for (int j = 0 ; j < x ; j++)
			{
				Table[j][i] = new Stone(i,j);
			}
		}
		
	}
	public Stone getStone(int x, int y)
	{
		return Table[x][y];
	}
	public void deleteStone(int x, int y)
	{
		Table[x][y] = new Stone(x,y,0);
	}
	public boolean swap(Point pastPt, Point Pt)
	{
		Stone temp = this.Table[pastPt.x][pastPt.y];
		this.Table[pastPt.x][pastPt.y] =this.Table[Pt.x][Pt.y]; 
		this.Table[Pt.x][Pt.y] = temp;
		return true;
	}
	public void shiftStone()
	{
		
	}
	public int getblankBlock()
	{
		
		int Count =0;
		for (int i = 0 ; i < Table.length ; i++)
		{
			for (int j = 0 ; j < Table[i].length ; j++)
			{
				if (Table[i][j].getShape() <= 0)
					Count++;
			}
		}
		return  Count;
	}
	public int isEnd()
	{
	
		tempindex= new int[this.ilength][this.iheight];
		int Count =0;
		for (int i = 0 ; i < Table.length ; i++)
		{
			for (int j = 0 ; j < Table[i].length ; j++)
			{
				Table[i][j].posible = false;
				if (Table[i][j].getShape() > 0 && tempindex[i][j] <= 1)
				{
	
					Count += findEnd(i, j);

						
				}
			}
		}
//		for (int i = 0 ; i < Table.length ; i++)
//		{
//			for (int j = 0 ; j < Table[i].length ; j++)
//			{
//				if (tempindex[i][j] > 1)
//				{
//					Table[i][j].posible =true;
//				}
//					else
//					{
//						Table[i][j].posible =false;
//
//						
//				}
//				tempindex[i][j] = 0;
//			}
//		}
//		if (d >0)

		return Count ;
	}
	public void InsertLine()
	{
		CopyTable();
		for (int i = 0 ; i < Table.length ; i++)
		{
			if (Table[i][0].getShape() <= 0)
				Table[i][0] = new Stone(i,0);
		}
		downsStone();
	}
	public void downLevel()
	
	{
		CopyTable();
		for (int i = 0 ; i < Table.length ; i++)
		{
			Table[i][Table[0].length - 1] = new Stone(i,Table[0].length - 1,0);
		}
		downsStone();
	}
	public void shiftLeft()
	{

		for (int j = 0 ; j <Table[0].length ; j++)
		{
			for (int i = Table.length-1 ; i >= 0 ; i--)
			{
				if (Table[i][j].getShape() == 0)
				{
					
					int x = i;
					for ( ; x <Table.length-1 ; x++)
					{
						if (Table[x+1][j].getShape() !=0)
							Table[x+1][j].setPastPoint(x,j);
						Table[x][j] = Table[x+1][j];
					}
					Table[x][j] = new Stone(x,i,0);
				}
			}
		}
		int addcount =0;
		boolean crack = true;
		for(int j = 0 ; j < Table[0].length ; j++)
		
		{
			crack = true;
			for(int i = 0 ; i < ilength ; i++)
			{
				if (Table[i][j].getShape() != 0)
					crack = false;
			}
			if (crack)
			{
				int m = direction;
				if (direction ==1)
				{
					for (int x = j ; x < Table.length -1 ; x++)
					{
						for (int y = 0 ; y < Table[j].length ; y++)
							Table[y][x] = Table[y][x+1];
					}
					for (int y = 0 ; y <Table.length  ; y++)
					{
						Table[y][Table[j].length-1] = new Stone(y,Table[j].length-1);
					}
					j--;
				}
				else
				{
					for (int x = j ; x > 0 ; x+=m)
					{
						for (int y = 0 ; y < Table[j].length ; y++)
							Table[y][x] = Table[y][x+m];
					}
					for (int y = 0 ; y <Table.length  ; y++)
					{
						Table[y][0] = new Stone(y,0);
						
					}
					j--;
					
				}
				addcount++;
			}
		}
	}
	public void downsStone()
	{
		for (int i = 0 ; i < Table.length ; i++)
		{
			for (int j = 0 ; j <Table[i].length ; j++)
				if (Table[i][j].getShape() == 0)
				{
					
					int x = j;
					for ( ; x >= 1 ; x--)
					{
						if (Table[i][x-1].getShape() !=0)
							Table[i][x-1].setPastPoint(i,x);
						Table[i][x] = Table[i][x-1];
					}
					Table[i][x] = new Stone(i,x,0);
				}
		}
		int addcount =0;
		boolean crack = true;
		for(int i = 0 ; i < ilength ; i++)
		{
			crack = true;
			for(int j = 0 ; j < Table[0].length ; j++)
			{
				if (Table[i][j].getShape() != 0)
					crack = false;
			}
			if (crack)
			{
				int m = direction;
				if (direction ==1)
				{
					for (int x = i ; x < Table.length-1  ; x++)
					{
						for (int y = 0 ; y < Table[i].length ; y++)
							Table[x][y] = Table[x+1][y];
					}
					for (int y = 0 ; y <Table[i].length  ; y++)
					{
						Table[Table.length-1][y] = new Stone(Table.length-1,y);
					}
					i--;
					
				}
				else
				{
					for (int x = i ; x > 0 ; x+=m)
					{
						for (int y = 0 ; y < Table[i].length ; y++)
							Table[x][y] = Table[x+m][y];
					}
					for (int y = 0 ; y <Table[i].length  ; y++)
					{
						Table[0][y] = new Stone(0,y);
					}
					i--;
					
				}
				addcount++;
			}
		}
		Score += addcount * 50;
		mDist += addcount;
		if (addcount >0)
			direction *= -1;
	//	direction = 1;
	}
	public void changeDirection()
	{
		direction *= -1;
	}
	/**
	 * @return the mDist
	 */
	public int getmDist() {
		return mDist;
	}
	/**
	 * @param mDist the mDist to set
	 */
	public void setmDist(int mDist) {
		this.mDist = mDist;
	}
	public int findEnd(int x,int y)
	{
		int temp = this.Table[x][y].getShape();
		if (temp <= 0)
			return 0;

		return findStoneSub(x,y,temp)-1; 
	}
	public Iterator findwStone(int x, int y)
	{
		tempindex= new int[this.ilength][this.iheight];
		vt = new Vector();
		if (x > ilength -1 || x<0 || y >iheight - 1|| y < 0)
			return vt.iterator();
		int temp = this.Table[x][y].getShape();

		if (temp <= 0)
			return null;
		findStoneSub(x,y,temp);
		if (vt.size() == 1)
		{
			vt.clear();
		}
		return vt.iterator();
	}
	public void CopyTable()
	{
		Stone[][] historyTable = new Stone[ilength][iheight];
		for (int i = 0 ; i < ilength ; i++)
		{
			for (int j = 0 ; j < iheight ; j++)
			{
				historyTable[i][j] = Table[i][j];
			}
		}
		if (historyList.size() >= 2)
		{
			historyList.remove(0);
		}
		HistoryTable ht = new HistoryTable(Score , historyTable);
		historyList.add(ht);
		mHistory = true;
	}
	public int findStone(int x,int y)
	{
		if (x > ilength -1 || x<0 || y >iheight - 1|| y < 0)
			return 0;
		tempindex= new int[this.ilength][this.iheight];
		vt = new Vector();
		int temp = this.Table[x][y].getShape();
		if (temp == 0)
			return 0;
		Stone tempst = Table[x][y];
		int count = findStoneSub(x,y,temp);
		if (count ==1)
		{
			Table[x][y] = tempst;
			return 0;
		}
		Iterator it = vt.iterator();
		Point pt ;
		

		
		CopyTable();
		
		while(it.hasNext())
		{
			pt = (Point)it.next();
			this.deleteStone(pt.x, pt.y);
		}
		downsStone();
		Score += (count* 2)  ;
		if (count > 3)
		{
			Score += (((count-3)^2)*10);
		}
		return count-1;
	}
	public int findStoneSub(int x, int y,int shape)
	{
		int count = 1;
		vt.add(new Point(x,y));
		tempindex[x][y] = 1;
		try
		{
			if (Table[x-1][y].getShape() ==	shape)
			{
				
				if (tempindex[x-1][y]  != 1  )
				{				
					count += findStoneSub(x-1,y,shape);
				}
				Table[x][y].posible = true;
			}
		}
		catch(ArrayIndexOutOfBoundsException ee){}
		try
		{
			if (  Table[x+1][y].getShape() ==	shape)
			{
				
				if (tempindex[x+1][y]  != 1)
				{
					count += findStoneSub(x+1,y,shape);
				}
				Table[x][y].posible = true;
			}
		}
		catch(ArrayIndexOutOfBoundsException ee){}
		try
		{
			if (Table[x][y+1].getShape() ==	shape)
			{
				
				if (tempindex[x][y+1]  != 1 )
				{
					count += findStoneSub(x,y+1,shape);
				}
				Table[x][y].posible = true;
			}
		}
		catch(ArrayIndexOutOfBoundsException ee){}
		try
		{
			 if (Table[x][y-1].getShape() ==	shape)
			 {
				 
				if (tempindex[x][y-1] != 1 )
				{
					count += findStoneSub(x,y-1,shape);
				}
				Table[x][y].posible = true;
			 }
		}
		catch(ArrayIndexOutOfBoundsException ee){}
		
		return count;
	}
	public void end()
	{
		
	}
	public void step(int x , int y)
	{
		for (int i = 0 ; i < iheight  ; i++)
		{
			for (int j = 0 ; j < ilength; j++)
			{
				if (Table[j][i].getShape() != 0)
					Table[j][i].step(x,y);
			}
		}
	}
	public void restorehistory()
	{
		if (historyList.size() == 0)
		{
			mHistory = false;
		}
		else
		{
			if (mHistory == true)
			{
				HistoryTable history = historyList.remove(historyList.size()-1);
				Stone[][] historyTable = history.table;
				Score = history.mScore;
				for (int i = 0 ; i < ilength ; i++)
				{
					for (int j = 0 ; j < iheight ; j++)
					{
						Table[i][j] = historyTable[i][j];
					}
				}
			}	
		}
		//changeDirection();
	}
	private int Score;
	/**
	 * @return the score
	 */
	public int getScore() {
		return Score;
	}
	/**
	 * @param score the score to set
	 */
	public ArrayList<HistoryTable> getHistoryTable() {
		// TODO Auto-generated method stub
		return historyList;
	}
	
	public HistoryTable getTable() {
		// TODO Auto-generated method stub
		
		Stone[][] historyTable = new Stone[ilength][iheight];
		for (int i = 0 ; i < ilength ; i++)
		{
			for (int j = 0 ; j < iheight ; j++)
			{
				historyTable[i][j] = Table[i][j];
			}
		}
		
		HistoryTable ht = new HistoryTable(Score , historyTable);
		
		return ht;
	}
	public void loadTable(HistoryTable table, ArrayList history) throws Exception {
		// TODO Auto-generated method stub

	
		Stone[][] historyTable = table.table;
		Score = table.mScore;
		for (int i = 0 ; i < ilength ; i++)
		{
			for (int j = 0 ; j < iheight ; j++)
			{
				Table[i][j] = historyTable[i][j];
			}
		}
		historyList.clear();
		historyList.addAll(history);
		if (!historyList.isEmpty())
		{
			mHistory = true;
		}

	}
	public int getDirection()
	{
		return direction;
	}
}