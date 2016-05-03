package de.simontenbeitel.regelfragen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class MyPieChart extends View{
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	Context context;
	float width;
	RectF rectf;
	float temp = -90;
	private float[] values;		//Save the values which have to be presented in the Pie Chart
	private int[] colors;		//The colors for each value
	
	public MyPieChart(Context context, float[] values, int[] colors) {  
		super(context);
		this.context = context;
		this.values = calculateData(values);
		this.colors = colors;
		
		
		width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
		rectf = new RectF((width*2)/100, (width*2)/100, (width*23)/100, (width*23)/100);
		
	}  
	
	@Override  
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < values.length; i++) {
			if (i != 0)
			{
				temp += values[i - 1];
			}
			paint.setColor(colors[i]);
			canvas.drawArc(rectf, temp, values[i], true, paint);			
		}		
	}
	
	private float[] calculateData(float[] data) {  
		float total = 0;  
		for (int i = 0; i < data.length; i++) {  
		total += data[i];  
		}  
		for (int i = 0; i < data.length; i++) {  
		data[i] = 360 * (data[i] / total);  
		}  
		return data;  
		} 
}