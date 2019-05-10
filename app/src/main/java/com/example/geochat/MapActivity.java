package com.example.geochat;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    static String email ;
    private FusedLocationProviderClient mFusedLocationClient;
    private Database database = new Database("https://134.209.235.115/gabad002/WEB/mapaDB.php", this,null);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (getIntent().hasExtra("email")) {
                email = extras.getString("email");
            }
        }
        //Creación Alert Dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setMessage("Aqui aparecen las ubicaciones de la gente de tu grupo");
        final AlertDialog alert = builder.create();
        alert.show();
        SupportMapFragment elfragmento =
                (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        elfragmento.getMapAsync(this);

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        //Conseguir ubicacion actual(GEOLOCALIZACION)
         mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
         //Mirar si tiene permisos para obtener la ubicación
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
        //Pedir permisos
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    34 );
        }
        //Conseguir la ubicación
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        if (location != null) {
                            //Guardar tu ubicación en base de datos
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            final String[][] token = {null};
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Conseguir los tokens de todas las personas que se han subscrito a la aplicacion
                                     database.actualizarLatLong(email,location.getLatitude(),location.getLongitude());
                                }
                            });
                            //crear un marcador donde se indica tu ubicación
                            LatLng bilbo = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.addMarker(new MarkerOptions()
                                    .position(bilbo)
                                    .title("Ubication"));
                            //Acercar la camara a ese lugar
                            CameraPosition cameraPosition = CameraPosition.builder()
                                    .target(bilbo)
                                    .zoom(10)
                                    .build();

                            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {}
                });

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final String[][] token = {null};
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                    String json =database.getLatLong();
                              JSONArray jsonArray = null;
                              //ArrayList<UserLatLong> users= new ArrayList<UserLatLong>();
                              ArrayList<String> vistos= new ArrayList<String>();
                              try {
                                  jsonArray = new JSONArray(json);

                              for (int i = 0; i < jsonArray.length(); i++) {
                                  JSONObject jsonObject = null;

                                      jsonObject = jsonArray.getJSONObject(i);

                                 // UserLatLong pd = new UserLatLong(jsonObject.getString("nick"), jsonObject.getDouble("lat"), jsonObject.getDouble("longitude"));

                                  if(!vistos.contains(jsonObject.getString("nick"))&&!jsonObject.getString("nick").equals(email)){
                                        vistos.add(jsonObject.getString("nick"));
                                        //users.add(pd);
                                        if(!jsonObject.getString("lat").equals(null)&&!jsonObject.getString("longitude").equals(null)) {
                                            LatLng latLng = new LatLng(jsonObject.getDouble("lat"),jsonObject.getDouble("longitude"));
                                            googleMap.addMarker(new MarkerOptions()
                                                    .position(latLng)
                                                    .title("Click: " + latLng.toString())
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

                                        }
                                  }
                              }
                              //Pintar en mapa las ubicaciones

                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }
                      });
        //Mirar si ha clickado en el marcador de ubicación
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                if (marker != null) {
                    if(marker.getTitle().equalsIgnoreCase("Mi ubicación") ){
                        enviarMensaje(marker.getPosition(), "me");
                    }
                }

                return false;
            }
        });
    }
private void enviarMensaje(LatLng latLng,String type){
       /* if(latLng!=null&&!type.isEmpty()) {
            String data;

            if (type.equalsIgnoreCase("go")) {
                data = email+"--user--Quiero ir  " + latLng.toString();
            } else {
                data = email+"--user--Estoy en " + latLng.toString();
            }
            try {
                //Conseguir los tokens de todas las personas que se han subscrito a la aplicacion
                String[] token = new conexionDB(MapActivity.this).execute("gettoken").get();
                //Un token puede estar asigando a mas de un usuario, entonces no queremos enviar mas de una vez el mensaje al mismo dispositivo.
                ArrayList<String> vistos = new ArrayList<>();
                if (token.length > 0) {
                    //Por cada token enviar el mensaje
                    for (int i = 0; i < token.length; i++) {
                        if (token[i] != null && !vistos.contains(token[i])) {
                            vistos.add(token[i]);
                            new firebasePost(MapActivity.this).execute(token[i], data);
                        }
                    }

                }
            } catch (Exception e) {


            }
        }
        */
}

}