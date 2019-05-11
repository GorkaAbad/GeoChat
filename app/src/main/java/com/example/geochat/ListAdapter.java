package com.example.geochat;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends BaseAdapter {

    private LayoutInflater lInflater;
    private List<ChatMessage> listStorage;
    private Context con;

    public ListAdapter(Context context, List<ChatMessage> customizedListView) {
        con=context;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return listStorage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            //Coger el layout que usa cada columna del ListView
            convertView = lInflater.inflate(R.layout.message, parent, false);
        //Cada columna del Listview tiene tres text views
             listViewHolder.text = (TextView)convertView.findViewById(R.id.message_text);
            listViewHolder.user = (TextView)convertView.findViewById(R.id.message_user);
            listViewHolder.date = (TextView)convertView.findViewById(R.id.message_time);

            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        String from= "De:";
        //Asignar los valores a las columnas
        listViewHolder.text.setText(listStorage.get(position).getMessageText());
        listViewHolder.user.setText(from+listStorage.get(position).getMessageUser());
        listViewHolder.date.setText(listStorage.get(position).getMessageTime());


        return convertView;
    }

    static class ViewHolder {

        TextView text;
        TextView date;
        TextView user;

    }
}
