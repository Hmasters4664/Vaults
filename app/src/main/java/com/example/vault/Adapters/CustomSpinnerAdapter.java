package com.example.vault.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vault.R;

public class CustomSpinnerAdapter extends BaseAdapter {
    Context context;
    String colours[];
    String[] names;
    LayoutInflater inflter;

    public CustomSpinnerAdapter(Context applicationContext, String[] colours, String[] names) {
        this.context = applicationContext;
        this.colours = colours;
        this.names = names;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return colours.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView name = (TextView) view.findViewById(R.id.textView);
        //icon.setBackgroundColor(colours[i]);
        name.setText(names[i]);
        return view;
    }
}
