package com.example.geochat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGroupActivity extends AppCompatActivity {

    private String nick;
    private Button boton;
    private EditText nombre;
    Database database = new Database("https://134.209.235.115/gabad002/WEB/group.php", this, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Intent i = getIntent();
        nick = i.getStringExtra("nick");


        boton = findViewById(R.id.button);
        nombre = findViewById(R.id.group);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database.execute("create", nick, String.valueOf(nombre.getText()));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.group_succes)+": "+nombre.getText(), Toast.LENGTH_LONG).show();
                /*Intent returnIntent = new Intent();
                returnIntent.putExtra()*/
                finish();
            }
        });
    }
}
