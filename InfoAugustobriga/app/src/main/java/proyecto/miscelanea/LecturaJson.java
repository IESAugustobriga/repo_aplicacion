package proyecto.miscelanea;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class LecturaJson extends AsyncTask {

    private String url;

    public LecturaJson(String url){
        this.url = url;
    }

    @Override
    protected String doInBackground(Object[] objects) {
        String ficheroJson="";
        URL urlFichero = null;
        try {

            urlFichero = new URL(url);
            BufferedReader fichero = new BufferedReader(new InputStreamReader(urlFichero.openStream()));
            StringBuffer buffer = new StringBuffer();
            String linea = "";
            while((linea = fichero.readLine()) != null){
                buffer.append(linea+"\n");
            }
            ficheroJson = buffer.toString();
            fichero.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ficheroJson;
    }
}
