package com.example.geochat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String data;
    static String email ;
     String messagesJson;
    final List<ChatMessage> lista= new ArrayList<>();
    private Database database = new Database("https://134.209.235.115/gabad002/WEB/chatMessages.php", this,null);

    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Si envian un mensaje aunque no se haya logeado llega aqui, asique se le redireccionará a loginActivity
        //Actividad camera
        final Intent ilo= new Intent(this, LoginActivity.class);
        //Sumar mensajes a ListView
        // Código modificado de: https://code.tutsplus.com/es/tutorials/how-to-create-an-android-chat-app-using-firebase--cms-27397
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (getIntent().hasExtra("id")) {
                id= extras.getString("id");
                this.cargarMensajes();
            }
            if(getIntent().hasExtra("email")) {
                email = extras.getString("email");
            }else if (getIntent().hasExtra("data")) {
                if(email==null||email.isEmpty()){
                    startActivity(ilo);
                }
                data = extras.getString("data");
                String[] parts = data.split("--user--");
                String[] parts2 = parts[1].split("--group--");
                id=parts2[0];

                this.cargarMensajes();

            }


        }
        //Crear el adapter para la lista de mensajes
        ListAdapter listAdapter = new ListAdapter(this, lista);
        listOfMessages.setAdapter(listAdapter);

        FloatingActionButton fab =
                findViewById(R.id.fab);
        //Crear dialogo para decir que quiere enviar un mensaje vacio
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setMessage("Inserte un mensaje");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.input);
                final String data = input.getText().toString();
                if (data == null||data.isEmpty()) {

                    //lanzar un alert dialog diciendo que el mensaje estan vacios. (dialogo1)
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        final String[][] token = {null};
                        runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              //Conseguir los tokens de todas las personas del grupo
                                              token[0] = database.getTokens(id);
                                              database.insertarMensaje(email,data,id);
                                          }
                                      });
                        //Un token puede estar asigando a mas de un usuario, entonces no queremos enviar mas de una vez el mensaje al mismo dispositivo.
                        ArrayList<String> vistos= new ArrayList<>();
                        if (token[0].length > 0) {
                            //Por cada token enviar el mensaje
                            for (int i = 0; i < token[0].length; i++) {
                                if (token[0][i] != null&&!vistos.contains(token[0][i])) {
                                    vistos.add(token[0][i]);
                                    Log.i("MENSAJE",data);
                                    new firebasePost(MainActivity.this).execute(token[0][i], email+"--user--"+id+"--group--"+data);
                                }
                            }

                        }
                    } catch (Exception e) {


                    }
                    // Limpiar el input
                    input.setText("");
                    finish();
                }
            }
        });



        //Location
        FloatingActionButton loc =
                findViewById(R.id.loc);
        //Actividad mapa
        final Intent i2= new Intent(this, MapActivity.class);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i2.putExtra("email",email);
                i2.putExtra("id",id);
                startActivity(i2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.cargarMensajes();
    }

    private void cargarMensajes(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final String[][] token = {null};
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                messagesJson= database.getMessages(id);
                Log.i("JSON", messagesJson);
            }
        });


        JSONArray jsonArray = null;
        ArrayList<String> vistos= new ArrayList<String>();
        try {
            jsonArray = new JSONArray(messagesJson);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = null;

                jsonObject = jsonArray.getJSONObject(i);
                lista.add(new ChatMessage(jsonObject.getString("mensaje"), jsonObject.getString("user"), jsonObject.getString("fecha")));
            }
        }catch (Exception e){

        }
    }
}
