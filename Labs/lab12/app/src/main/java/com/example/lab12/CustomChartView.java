package com.example.lab12;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.ArrayList;

public class CustomChartView extends View {

    private ArrayList<Float> valori;
    private String chartType;
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
        framePaint.setColor(Color.BLACK);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        frameRect = new RectF(padding, padding, getWidth() - padding, getHeight() - padding);
        canvas.drawRect(frameRect, framePaint);

        if (chartType.equals("PieChart")) {
            drawPieChart(canvas);
        } else if (chartType.equals("BarChart")) {
            drawBarChart(canvas);
        } else if (chartType.equals("ColumnChart")) {
            drawColumnChart(canvas);
        }
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

            // Text procent pe fiecare felie
            float midAngle = startAngle + sweepAngle / 2;
            float labelX = (float) (centerX + radius * 0.6 * Math.cos(Math.toRadians(midAngle)));
            float labelY = (float) (centerY + radius * 0.6 * Math.sin(Math.toRadians(midAngle)));
            String procent = String.format("%.0f%%", (valori.get(i) / total) * 100);
            canvas.drawText(procent, labelX, labelY, textPaint);

            startAngle += sweepAngle;
        }
    }

    private void drawBarChart(Canvas canvas) {
        float left = frameRect.left;
        float bottom = frameRect.bottom;
        float width = frameRect.width();
        float height = frameRect.height();

        int barCount = valori.size();
        float barWidth = width / (barCount * 2);
        float space = barWidth;

        float maxVal = 0;
        for (float val : valori) {
            if (val > maxVal) maxVal = val;
        }

        float x = left + space / 2;

        for (int i = 0; i < barCount; i++) {
            paint.setColor(colors[i % colors.length]);
            float scaledHeight = (valori.get(i) / maxVal) * (height - 50);
            canvas.drawRect(x, bottom - scaledHeight, x + barWidth, bottom, paint);

            // Valoare scrisa deasupra
            canvas.drawText(String.format("%.0f", valori.get(i)), x + barWidth / 2, bottom - scaledHeight - 10, textPaint);

            x += barWidth + space;
        }
    }

    private void drawColumnChart(Canvas canvas) {
        float left = frameRect.left;
        float top = frameRect.top;
        float width = frameRect.width();
        float height = frameRect.height();

        int colCount = valori.size();
        float colHeight = height / (colCount * 2);
        float space = colHeight;

        float maxVal = 0;
        for (float val : valori) {
            if (val > maxVal) maxVal = val;
        }

        float y = top + space / 2;

        for (int i = 0; i < colCount; i++) {
            paint.setColor(colors[i % colors.length]);
            float scaledWidth = (valori.get(i) / maxVal) * (width - 50);
            canvas.drawRect(left, y, left + scaledWidth, y + colHeight, paint);

            // Valoare scrisa la capat
            canvas.drawText(String.format("%.0f", valori.get(i)), left + scaledWidth + 40, y + colHeight / 2 + 10, textPaint);

            y += colHeight + space;
        }
    }
}
