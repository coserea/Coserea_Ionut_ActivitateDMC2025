package com.example.proiect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.ArrayList;

/* ****************************************
    In aceasta sursa contruiesc PieChart
    ca in laboratorul 12
   **************************************** */
public class CustomChartView extends View {

    private ArrayList<Float> valori;
    private String chartType; // in lab 12 aveam 3 tipuri de chart, dar aici am lasat doar PieChart (nu am mai modificat codul, doar am eliminat BarChart si ColumnChart)
    private Paint paint, textPaint, framePaint;
    private RectF frameRect;

    private int padding = 100;

    private int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.LTGRAY};

    public CustomChartView(Context context, ArrayList<Float> valori, String chartType) {
        super(context);
        this.valori = valori;
        this.chartType = chartType;

        paint = new Paint();
        paint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        framePaint = new Paint();
        framePaint.setColor(Color.parseColor("#EEEEEE"));
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        frameRect = new RectF(padding, padding, getWidth() - padding, getHeight() - padding);
        canvas.drawRect(frameRect, framePaint);

        drawPieChart(canvas);
    }

    private void drawPieChart(Canvas canvas) {
        float total = 0;
        for (float val : valori) {
            total += val;
        }

        float startAngle = 0;

        float centerX = frameRect.centerX();
        float centerY = frameRect.centerY();
        float radius = Math.min(frameRect.width(), frameRect.height()) / 2.5f;

        for (int i = 0; i < valori.size(); i++) {
            paint.setColor(colors[i % colors.length]);
            float sweepAngle = (valori.get(i) / total) * 360;

            canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                    startAngle, sweepAngle, true, paint);

            // Adaug text cu procent pe fiecare felie a chart-ului
            float midAngle = startAngle + sweepAngle / 2;
            float labelX = (float) (centerX + radius * 0.6 * Math.cos(Math.toRadians(midAngle)));
            float labelY = (float) (centerY + radius * 0.6 * Math.sin(Math.toRadians(midAngle)));
            String procent = String.format("%.0f%%", (valori.get(i) / total) * 100);
            canvas.drawText(procent, labelX, labelY, textPaint);

            startAngle += sweepAngle;
        }
    }
}
