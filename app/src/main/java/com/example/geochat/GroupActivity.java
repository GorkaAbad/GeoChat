package com.example.geochat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GroupActivity extends AppCompatActivity {
    private ArrayList<Grupo> grupos;
    Database database = new Database("https://134.209.235.115/gabad002/WEB/group.php", this, null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        String nick = intent.getStringExtra("nick");

        AsyncTask<String, String, ArrayList<Grupo>> a = database.execute(nick);

        try {
            grupos = a.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ListView listGrupo = findViewById(R.id.list);

        AdaptadorListView adap= new AdaptadorListView(getApplicationContext(),grupos);
        listGrupo.setAdapter(adap);

        listGrupo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("name", grupos.get(position).getName());
                i.putExtra("id", grupos.get(position).getId());
                startActivity(i);
            }
        });
    }
}
