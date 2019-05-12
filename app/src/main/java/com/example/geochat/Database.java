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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import javax.net.ssl.HttpsURLConnection;


public class Database extends AsyncTask<String, String, ArrayList<Grupo>> {
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
     * @return
     */
    public String[] getTokens(String id) {
        String[] tokens = null;
        HttpsURLConnection connection = getUrlConnection();
        Uri.Builder builder = new Uri.Builder().appendQueryParameter("group",id)
                .appendQueryParameter("case","2");


        String result = hacerConexion(builder, connection);
        try {

            if (result.length() > 0) {
                JSONArray jsonArr = new JSONArray(result);
                tokens = new String[jsonArr.length()];
                for (int i = 0; i < jsonArr.length(); i++) {
                    org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);
                    tokens[i] = jsonObj.getString("token");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    /**
     * Ver si el token de ahora esta ya registrado para ese usuario
     *
     * @return
     * @parans nick
     */
    public boolean isTokenUser(String nick) {
        String[] tokens = null;
        HttpsURLConnection connection = getUrlConnection();
        Uri.Builder builder = new Uri.Builder().appendQueryParameter("nick", nick)
                .appendQueryParameter("case", "3");


        String result = hacerConexion(builder, connection);
        try {

            if (result.length() > 0) {
                JSONArray jsonArr = new JSONArray(result);
                tokens = new String[jsonArr.length()];
                for (int i = 0; i < jsonArr.length(); i++) {
                    org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);
                    tokens[i] = jsonObj.getString("token");
                }
            }
            List<String> list = Arrays.asList(tokens);
            if (list.contains(firebase.getToken())) {
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void anadirUsuarioAGrupo(String nick, String id) {
        HttpsURLConnection connection = getUrlConnection();

        Uri.Builder builder = new Uri.Builder().appendQueryParameter("nick", nick)
                .appendQueryParameter("case", "0")
                .appendQueryParameter("groupId", id);

        hacerConexion(builder, connection);


    }

    public ArrayList<Grupo> getGrupos(String nick) {

        ArrayList<Grupo> grupos = new ArrayList<>();
        ArrayList<String> gruposTemp = null;
        HttpsURLConnection connection = getUrlConnection();

        Uri.Builder builder = new Uri.Builder().appendQueryParameter("nick", nick)
                .appendQueryParameter("case", "3");


        String result = hacerConexion(builder, connection);

        Log.i("Resultado", result);
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(result);
            org.json.simple.JSONArray aux = (org.json.simple.JSONArray) json.get("grupos");


            for (int i = 0; i < aux.size(); i++) {
                org.json.simple.JSONObject auxObject = (JSONObject) aux.get(i);

                grupos.add(new Grupo(auxObject.get("id").toString(), auxObject.get("name").toString(), null));
            }

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return grupos;

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
    public void actualizarLatLong(String nick, String lat, String longitu) {


        HttpsURLConnection connection = getUrlConnection();

        User user = null;
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("nick", nick)
                .appendQueryParameter("lat", lat)
                .appendQueryParameter("long", longitu)
                .appendQueryParameter("case", "0");


        String result = hacerConexion(builder, connection);


    }

    /**
     * Conseguir longitud y la latitud de los usuarios, por grupo
     *
     * @return
     */
    public String getLatLong(String grupo) {


        HttpsURLConnection connection = getUrlConnection();

        User user = null;
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("group",grupo)
                .appendQueryParameter("case", "1");


        String result = hacerConexion(builder, connection);
        Log.i("result", result);
        return result;


    }
    /**
     * Conseguir mensaje de un grpo
     *
     * @return
     */
    public String getMessages(String id) {


        HttpsURLConnection connection = getUrlConnection();

        User user = null;
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("group", id)
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
     * Insertar mensaje.
     *
     *
     * @return
     */
    public void insertarMensaje(String nick, String mensaje, String grupoId) {
        HttpsURLConnection connection = getUrlConnection();
        String date =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("user", nick)
                .appendQueryParameter("mensaje", mensaje)
                .appendQueryParameter("group",grupoId)
                .appendQueryParameter("fecha", date)
                .appendQueryParameter("case", "0");


        String result = hacerConexion(builder, connection);



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
            Log.i("statusss", "stat" + statusCode);
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

    /**
     * Crea un grupo y añade automaticamente a el usuario que lo ha creado
     * @param nick
     * @param nombreGrupo
     */
    private void crearGrupo(String nick, String nombreGrupo){
        HttpsURLConnection connection = getUrlConnection();

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("nick", nick)
                .appendQueryParameter("groupName", nombreGrupo)
                .appendQueryParameter("case", "2");


        String result = hacerConexion(builder, connection);

    }

    private void borrarUserDeGrupo(String nick, String id){
        HttpsURLConnection connection = getUrlConnection();

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("nick", nick)
                .appendQueryParameter("groupId",id)
                .appendQueryParameter("case", "4");


        String result = hacerConexion(builder, connection);
    }

    @Override
    protected ArrayList<Grupo> doInBackground(String... strings) {
        if(strings[0].equals("delete")){
            borrarUserDeGrupo(strings[1], strings[2]);
        }else if(strings[0].equals("update")){
            actualizarLatLong(strings[1],strings[2],strings[3]);
        }else if(strings[0].equals("create")){
            crearGrupo(strings[1], strings[2]);
        }else if (strings[0].equals("add")) {
            anadirUsuarioAGrupo(strings[1], strings[2]);
        } else {
            return getGrupos(strings[0]);
        }
        return null;

    }

    @Override
    protected void onPostExecute(ArrayList<Grupo> grupos) {
        super.onPostExecute(grupos);
    }
}