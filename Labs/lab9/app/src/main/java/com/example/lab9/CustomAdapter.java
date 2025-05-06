package com.example.lab9;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String[] imageUrls;
    String[] descriptions;
    String[] links;
    LayoutInflater inflater;
    ExecutorService executorService;

    public CustomAdapter(Context context, String[] imageUrls, String[] descriptions, String[] links) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.descriptions = descriptions;
        this.links = links;
        this.inflater = LayoutInflater.from(context);
        this.executorService = Executors.newFixedThreadPool(5);
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return descriptions[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.textView = convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(descriptions[position]);

        // Load images async
        executorService.execute(() -> {
            try {
                URL url = new URL(imageUrls[position]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                new Handler(Looper.getMainLooper()).post(() -> holder.imageView.setImageBitmap(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        convertView.setOnClickListener(view -> {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", links[position]);
            context.startActivity(intent);
        });

        return convertView;
    }
}
