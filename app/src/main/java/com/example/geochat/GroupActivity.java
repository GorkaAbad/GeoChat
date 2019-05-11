package com.example.geochat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {
    private ArrayList<Grupo> grupos;
    String userNick;
    Database database = new Database("https://134.209.235.115/gabad002/WEB/group.php", this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar bar= findViewById(R.id.bar);
        setSupportActionBar(bar);

        ListView listGrupo = findViewById(R.id.list);
       /* AdaptadorListView adap= new AdaptadorListView(getApplicationContext(),grupos);
        listGrupo.setAdapter(adap);*/

        Bundle extras=getIntent().getExtras();
        if (extras != null) {
            userNick=extras.getString("nick");
        }
        database.execute(userNick);

        /*listGrupo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("race",((TextView)view.findViewById(R.id.lineText)).getText().toString());
                startActivity(intent);
            }
        }); */
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.valuesbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.add_group){

        }else if(id==R.id.signout){
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }else if(id==R.id.share_nick){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Este es mi nick: "+userNick);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
