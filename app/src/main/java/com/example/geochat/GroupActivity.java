package com.example.geochat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {
private ArrayList<Grupo> grupos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        ListView listGrupo = findViewById(R.id.list);
        AdaptadorListView adap= new AdaptadorListView(getApplicationContext(),grupos);
        listGrupo.setAdapter(adap);

        /*listGrupo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("race",((TextView)view.findViewById(R.id.lineText)).getText().toString());
                startActivity(intent);
            }
        }); */
    }
}
