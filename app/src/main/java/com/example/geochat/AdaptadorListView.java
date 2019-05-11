package com.example.geochat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorListView extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Grupo> grupos;

    public AdaptadorListView(Context _context, ArrayList<Grupo> _grupos) {

        this.context = _context;
        this.grupos = _grupos;
    }

    @Override
    public int getCount() {
        return grupos.size();
    }

    @Override
    public Object getItem(int position) {
        return grupos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.line_layout, null);
        final TextView text = convertView.findViewById(R.id.lineText);
        ImageButton map = (ImageButton) convertView.findViewById(R.id.lineMap);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Map", Integer.toString(grupos.get(position).getId()));
            }
        });

        return convertView;
    }
}
