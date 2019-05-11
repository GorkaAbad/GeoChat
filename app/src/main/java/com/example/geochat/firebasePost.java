package com.example.geochat;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.net.ssl.HttpsURLConnection;

public class firebasePost extends AsyncTask<String, Void, String> {
    private Context ContextoClase;
    private  HttpsURLConnection urlConnection;


    public firebasePost(Context ContextoConst) {
        this.ContextoClase = ContextoConst;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
    }

    @Override
    protected String doInBackground(String... strings) {
        String direccion;
        String respuesta=null;
        String parametros;
            String token=strings[0];
            String data=strings[1];
            direccion = "https://134.209.235.115/gabad002/WEB/enviarMensaje.php";
            parametros = "token=" + token + "&data=" + data;

        Log.i("parame",parametros);
        try {
            urlConnection= GeneradorConexionesSeguras.getInstance().crearConexionSegura(ContextoClase,direccion);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();

            int statusCode = urlConnection.getResponseCode();
            Log.i("tag", urlConnection.getURL().toString());
            Log.i("status",""+statusCode);
            if (statusCode == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();

            }

        }catch(Exception e){

        }
       // Log.i("res",respuesta[0]);
        return respuesta;
    }


}