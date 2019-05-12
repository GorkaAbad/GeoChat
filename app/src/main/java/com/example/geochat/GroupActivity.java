package com.example.geochat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GroupActivity extends AppCompatActivity {
    private ArrayList<Grupo> grupos;
    String nick;
    String latitud;
    String longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        this.initialize();
        Intent geointent = new Intent(this,GeoService.class);
        geointent.putExtra("nick",nick);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            startForegroundService(geointent);
        } else{
            startService(geointent);
        }
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitud = String.valueOf(location.getLatitude());
                        longitud = String.valueOf(location.getLongitude());
                        Database database = new Database("https://134.209.235.115/gabad002/WEB/mapaDB.php", GroupActivity.this, null);
                        database.actualizarLatLong(nick, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                        Log.d("HOLA","OnSuccess "+latitud+" "+longitud);
                    } else {
                        latitud = null;
                        longitud = null;
                        Log.d("HOLA","OnSuccessFAIL "+latitud+" "+longitud);
                    }

                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    latitud = null;
                    longitud = null;
                }
            });
        }
    }

    private void initialize() {
        Toolbar bar = findViewById(R.id.bar);
        setSupportActionBar(bar);
        Intent intent = getIntent();
        if (intent.hasExtra("nick")) {
            nick = intent.getStringExtra("nick");
        }

        //AsyncTask<String, String, ArrayList<Grupo>> a = database.execute(nick);
        Database database = new Database("https://134.209.235.115/gabad002/WEB/group.php", this, null);
        try {
            grupos = database.execute(nick).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ListView listGrupo = findViewById(R.id.list);

        AdaptadorListView adap = new AdaptadorListView(getApplicationContext(), grupos);
        listGrupo.setAdapter(adap);

        listGrupo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("name", grupos.get(position).getName());
                i.putExtra("id", grupos.get(position).getId());
                i.putExtra("email", nick);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.valuesbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_group) {
            Intent intent = new Intent(getApplicationContext(), CreateGroupActivity.class);
            intent.putExtra("nick", nick);
            startActivity(intent);
        } else if (id == R.id.signout) {
            Intent stopservice= new Intent(this, GeoService.class);
            stopService(stopservice);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.share_nick) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.send_nick)+" " + nick);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        return super.onOptionsItemSelected(item);
    }


}
