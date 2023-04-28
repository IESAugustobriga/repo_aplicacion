package proyecto.horarios;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LecturaFicheroHorarios extends AsyncTask {
    private String url;

    public LecturaFicheroHorarios(String url) {
        this.url = url;
    }


    @Override
    protected ArrayList<String> doInBackground(Object[] objects) {
        ArrayList<String> contenido = new ArrayList<>();
        URL urlFichero = null;

        try {
            //leemos el fichero
            urlFichero = new URL(url);
            BufferedReader fichero = new BufferedReader(new InputStreamReader(urlFichero.openStream()));
            String linea;
            while((linea = fichero.readLine()) != null){
                contenido.add(linea);
            }
            fichero.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contenido;
    }

}
