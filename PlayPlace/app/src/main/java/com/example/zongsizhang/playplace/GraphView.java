package com.example.zongsizhang.playplace;//add your own package name
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * GraphView creates a scaled line or bar graph with x and y axis labels.
 * @author Arno den Hond
 *
 */
public class GraphView extends View {

	public static boolean BAR = false;
	public static boolean LINE = true;

	private Paint paint = new Paint();
	private float[] values = new float[0];
    private List<float[]> muti_values = new ArrayList<float[]>();
	private String[] horlabels = new String[0];
	private String[] verlabels = new String[0];
	private String title = "graphview";
	private boolean type = this.LINE;

    private float muti_max;
    private float muti_min;

    private int DRAW_MODE = 0;//0 single line, 1 mutiple line

	public GraphView(Context context){
		super(context);
	}

	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	public void init(float[] values, String title, String[] horlabels, String[] verlabels, boolean type) {
		if (values == null)
			values = new float[0];
		else
			this.values = values;
		if (title == null)
			title = "";
		else
			this.title = title;
		if (horlabels == null)
			this.horlabels = new String[0];
		else
			this.horlabels = horlabels;
		if (verlabels == null)
			this.verlabels = new String[0];
		else
			this.verlabels = verlabels;
		this.type = type;
		paint = new Paint();
	}

    public void clearGraph(){
        values = new float[0];
		if(DRAW_MODE == 1){
			muti_values.clear();
		}
        this.horlabels = new String[0];
        this.verlabels = new String[0];
        this.title = "";
        paint = new Paint();
    }

	public void setValues(float[] newValues)
	{
		this.values = newValues;
	}

	public void setGraph(String[] newhv, String[] newvv, float[] newValues){
		this.values = newValues;
		this.horlabels = newhv;
		this.verlabels = newvv;
		for(int i = 0;i < values.length; ++i){
			BigDecimal b   =   new   BigDecimal(values[i]);
			values[i] = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			horlabels[i] = horlabels[i].substring(0,3);
			verlabels[i] = verlabels[i].substring(0,3);
		}
		this.invalidate();
	}

    public void setGraph(List<float[]> values, String[] hl, String[] vl, float mmax, float mmin){
        this.muti_values = values;
        this.horlabels = hl;
        this.verlabels = vl;
        this.DRAW_MODE = 1;
        this.muti_max = mmax;
        this.muti_min = mmin;
        this.invalidate();

    }

	@Override
	protected void onDraw(Canvas canvas) {
        float ltsize = 30;
        float ttsize = 40;
		float border =  ttsize * 2;
		float horstart = 10 + ltsize;
		float height = getHeight();
		float width = getWidth() - 1;
		float max = getMax();
		float min = getMin();
        if(DRAW_MODE == 1){
            max = this.muti_max;
            min = this.muti_min;
        }
		float diff = max - min;
		float graphheight = height - (2 * border);
		float graphwidth = width - (2 * border);


		this.paint.setTextAlign(Align.LEFT);
		int vers = verlabels.length - 1;
		for (int i = 0; i < verlabels.length; i++) {
			paint.setColor(Color.LTGRAY);
			float y = ((graphheight / vers) * i) + border;
			canvas.drawLine(horstart, y, width, y, paint);
			paint.setColor(Color.BLACK);
            paint.setTextSize(ltsize);
			canvas.drawText(verlabels[i], 0, y, paint);
		}
		int hors = horlabels.length - 1;
		for (int i = 0; i < horlabels.length; i++) {
			paint.setColor(Color.LTGRAY);
			float x = ((graphwidth / hors) * i) + horstart;
			canvas.drawLine(x, height - border, x, border, paint);
			paint.setTextAlign(Align.CENTER);
			if (i==horlabels.length-1)
				paint.setTextAlign(Align.RIGHT);
			if (i==0)
				paint.setTextAlign(Align.LEFT);
			paint.setColor(Color.BLACK);
            paint.setTextSize(ltsize);
			canvas.drawText(horlabels[i], x, height - 4, paint);
		}

		paint.setTextAlign(Align.CENTER);
        paint.setTextSize(40);
		canvas.drawText(title, (graphwidth / 2) + horstart, border / 2 , paint);
        if(DRAW_MODE == 0){
            if (max != min) {
                paint.setColor(Color.LTGRAY);
                if (type == BAR) {
                    float datalength = values.length;
                    float colwidth = (width - (2 * border)) / datalength;
                    for (int i = 0; i < values.length; i++) {
                        float val = values[i] - min;
                        float rat = val / diff;
                        float h = graphheight * rat;
                        canvas.drawRect((i * colwidth) + horstart, (border - h) + graphheight, ((i * colwidth) + horstart) + (colwidth - 1), height - (border - 1), paint);
                    }
                } else {
                    float datalength = values.length;
                    float colwidth = (width - (2 * border)) / datalength;
                    float halfcol = colwidth / 2;
                    float lasth = 0;
                    for (int i = 0; i < values.length; i++) {
                        float val = values[i] - min;
                        float rat = val / diff;
                        float h = graphheight * rat;
                        if (i > 0)
                            paint.setColor(Color.GREEN);
                        paint.setStrokeWidth(2.0f);
                        canvas.drawLine(((i - 1) * colwidth) + (horstart + 1) + halfcol, (border - lasth) + graphheight, (i * colwidth) + (horstart + 1) + halfcol, (border - h) + graphheight, paint);
                        lasth = h;
                    }
                }
            }
        }else if(DRAW_MODE == 1){
            if (max != min) {
                paint.setColor(Color.LTGRAY);
                if (type == BAR) {
                    for(int j = 0;j < muti_values.size(); ++j){
                        float[] tmp_values = muti_values.get(j);
                        float datalength = tmp_values.length;
                        float colwidth = (width - (2 * border)) / datalength;
                        for (int i = 0; i < tmp_values.length; i++) {
                            float val = tmp_values[i] - min;
                            float rat = val / diff;
                            float h = graphheight * rat;
                            canvas.drawRect((i * colwidth) + horstart, (border - h) + graphheight, ((i * colwidth) + horstart) + (colwidth - 1), height - (border - 1), paint);
                    }
                    }
                } else {
                    for(int j = 0;j < muti_values.size(); ++j) {
                        float[] tmp_values = muti_values.get(j);
                        float datalength = tmp_values.length;
                        float colwidth = (width - (2 * border)) / datalength;
                        float halfcol = colwidth / 2;
                        float lasth = 0;
                        for (int i = 0; i < tmp_values.length; i++) {
                            float val = tmp_values[i] - min;
                            float rat = val / diff;
                            float h = graphheight * rat;
                            if (i > 0)
                                paint.setColor(Color.GREEN);
                            paint.setStrokeWidth(2.0f);
                            canvas.drawLine(((i - 1) * colwidth) + (horstart + 1) + halfcol, (border - lasth) + graphheight, (i * colwidth) + (horstart + 1) + halfcol, (border - h) + graphheight, paint);
                            lasth = h;
                        }
                    }

                }
            }

        }

	}

	private float getMax() {
		float largest = Integer.MIN_VALUE;
		for (int i = 0; i < values.length; i++)
			if (values[i] > largest)
				largest = values[i];

		//largest = 3000;
		return largest;
	}

	private float getMin() {
		float smallest = Integer.MAX_VALUE;
		for (int i = 0; i < values.length; i++)
			if (values[i] < smallest)
				smallest = values[i];

		//smallest = 0;
		return smallest;
	}

}