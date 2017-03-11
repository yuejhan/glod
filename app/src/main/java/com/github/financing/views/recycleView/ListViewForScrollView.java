package com.github.financing.views.recycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ListView;
//次控制是Scrollview，子控件是Listview。
public class ListViewForScrollView extends RecyclerView
{

	public ListViewForScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	 public ListViewForScrollView(Context context, AttributeSet attrs) { 
	        super(context, attrs); 
	    } 

	    public ListViewForScrollView(Context context, AttributeSet attrs, 
	        int defStyle) { 
	        super(context, attrs, defStyle); 
	    } 

	    @Override 
	    /** 
	     * 重写该方法，达到使ListView适应ScrollView的效果 
	     */ 
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	    { 
	        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, 
	        MeasureSpec.AT_MOST); 
	        super.onMeasure(widthMeasureSpec, expandSpec); 
	    } 

}
