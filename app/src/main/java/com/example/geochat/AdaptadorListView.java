package com.example.geochat;

import android.content.Context;
import android.content.Intent;
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
        inflater = (LayoutInflater) context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
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

        text.append(grupos.get(position).getName());

        ImageButton map = (ImageButton) convertView.findViewById(R.id.lineMap);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InvitarActivity.class);
                i.putExtra("name", grupos.get(position).getName());
                i.putExtra("id", grupos.get(position).getId());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });

        return convertView;
    }
}
