package com.example.geochat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InvitarActivity extends AppCompatActivity {

    private Button boton;
    private EditText nick;
    Database database = new Database("https://134.209.235.115/gabad002/WEB/group.php", this, null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitar);

        boton = findViewById(R.id.button);
        nick = findViewById(R.id.nick);

        Intent i = getIntent();
        i.getStringExtra("name");
        final String id = i.getStringExtra("id");

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = String.valueOf(nick.getText());

                database.execute("add", texto, id);

                Toast.makeText(getApplicationContext(), "User added to group", Toast.LENGTH_LONG).show();


            }
        });

    }
}
