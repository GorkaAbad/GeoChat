package com.example.geochat;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ProtocolException;
import java.util.Arrays;
import java.util.List;


import javax.net.ssl.HttpsURLConnection;


public class Database extends AsyncTask<String, String, Void> {
    String direccion = "";
    Context context;

    private Firebase firebase = null;

    public Database(String _direccion, Context baseContext) {
        direccion = _direccion;
        context = baseContext;
        firebase = new Firebase();
    }

    public Database(String _direccion, Context baseContext, Nullable nulable) {
        direccion = _direccion;
        context = baseContext;
    }

    private HttpsURLConnection getUrlConnection() {

        return getUrlConnection(this.direccion);
    }

    /**
     * Conseigue una Conexion https
     *
     * @param url
     * @return
     */
    private HttpsURLConnection getUrlConnection(String url) {

        HttpsURLConnection urlConnection = null;

        try {
            urlConnection = GeneradorConexionesSeguras.getInstance().crearConexionSegura(context, url);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);


        } catch (Exception e) {
            Log.i("Conexion", "Error al conseguir la conexion");
        }
        return urlConnection;
    }

    /**
     * Recger los tokens de un grupo
     *
     *
     * @return
     */
    public String[] getTokens(){
        String[] tokens =null;
        HttpsURLConnection connection = getUrlConnection();
        Uri.Builder builder = new Uri.Builder();


        String result = hacerConexion(builder, connection);
        try {

            if (result.length()>0) {
                JSONArray jsonArr = new JSONArray(result);
                tokens=new String[jsonArr.length()];
                for (int i = 0; i < jsonArr.length(); i++)
                {
                    org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);
                    tokens[i]=jsonObj.getString("token");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    /**
     * Ver si el token de ahora esta ya registrado para ese usuario
     * @parans nick
     *
     * @return
     */
    public boolean isTokenUser(String nick){
        String[] tokens =null;
        HttpsURLConnection connection = getUrlConnection();
        Uri.Builder builder = new Uri.Builder().appendQueryParameter("nick",nick)
                .appendQueryParameter("case", "3");
        ;


        String result = hacerConexion(builder, connection);
        try {

            if (result.length()>0) {
                JSONArray jsonArr = new JSONArray(result);
                tokens=new String[jsonArr.length()];
                for (int i = 0; i < jsonArr.length(); i++)
                {
                    org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);
                    tokens[i]=jsonObj.getString("token");
                }
            }
            List<String> list = Arrays.asList(tokens);
            if(list.contains(firebase.getToken())){
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Inserta un usuario en la base de datos
     *
     * @param nick
     * @param password
     * @return
     */
    public User insertUser(String nick, String password, String name) {


        HttpsURLConnection connection = getUrlConnection();

        User user = null;
        final String token = firebase.getToken();
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("nick", nick)
                .appendQueryParameter("password", password)
                .appendQueryParameter("token", token)
                .appendQueryParameter("name", name)
                .appendQueryParameter("case", "1");


        String result = hacerConexion(builder, connection);
        String status = "0";
        try {
            Log.i("register", result);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result);
            status = (String) json.get("status");

            if (status.equals("1")) {

                user = new User(nick, name, token, password);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return user;


    }
    /**
     * Actualiza la longitud y la latitud de un usuario
     *
     * @param nick
     * @param lat
     * @param longitu
     * @return
     */
    public void actualizarLatLong(String nick, double lat,double longitu) {


        HttpsURLConnection connection = getUrlConnection();

        User user = null;
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("nick", nick)
                .appendQueryParameter("lat", String.valueOf(lat))
                .appendQueryParameter("long", String.valueOf(longitu))
                .appendQueryParameter("case", "0");


        String result = hacerConexion(builder, connection);





    }
    /**
     * Conseguir longitud y la latitud de los usuarios
     *

     * @return
     */
    public String getLatLong() {


        HttpsURLConnection connection = getUrlConnection();

        User user = null;
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("case", "1");


        String result = hacerConexion(builder, connection);
    return result;



    }

    /**
     * Recupera el usuario, si existe, asociado a la pareja, email contraseña.
     *
     * @param nick
     * @param password
     * @return
     */
    public User getUser(String nick, String password) {
        User user = null;
        HttpsURLConnection connection = getUrlConnection();

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("nick", nick)
                .appendQueryParameter("password", password)
                .appendQueryParameter("case", "0");


        String result = hacerConexion(builder, connection);

        try {
            if (result.length() != 0) {

                final String token = firebase.getToken();
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(result);
                String name = (String) json.get("name");

                if (name != null) {
                    //Ususario correcto
                    user = new User(nick, name, token, password);
                }
            } else {
                return null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;

        }

        return user;

    }

    /**
     * Crea una conexion y gestiona la respuesta recibida. Convierte el mensaje recibido en texto, disponbible posteriormente para convertirlo en un JSON.
     *
     * @param builder
     * @param connection
     * @return
     */
    private String hacerConexion(Uri.Builder builder, HttpsURLConnection connection) {
        String parametros = builder.build().getEncodedQuery();

        try {
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(parametros);
            out.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("error", e.toString());
        }

        return gestionarRespuesta(connection);
    }

    /**
     * Convierte la respuesta https, en un String.
     *
     * @param connection
     * @return
     */
    private String gestionarRespuesta(HttpsURLConnection connection) {
        String result = "";
        try {
            int statusCode = connection.getResponseCode();
            Log.i("statusss","stat"+statusCode);
            if (statusCode == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;


    }




    @Override
    protected Void doInBackground(String... strings) {

        return null;
    }

    @Override
    protected void onPostExecute(Void fotos) {

    }
}