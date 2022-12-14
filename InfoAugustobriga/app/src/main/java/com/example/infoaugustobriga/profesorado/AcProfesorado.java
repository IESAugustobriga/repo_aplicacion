package com.example.infoaugustobriga.profesorado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.infoaugustobriga.AcMenuPrincipal;
import com.example.infoaugustobriga.Interfaces.IConfigurarActividad;
import com.example.infoaugustobriga.R;
import com.example.infoaugustobriga.calendarios.AcCalendarios;
import com.example.infoaugustobriga.miscelanea.AdaptadorListaGenerico;
import com.example.infoaugustobriga.miscelanea.Mensaje;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class AcProfesorado extends AppCompatActivity implements IConfigurarActividad {

    //LinearLayout del titulo
    LinearLayout lyTituloProfesorado;
    //ImageView del titulo
    TextView txtTitulo;
    //Animaciones
    Animation animTitulo;
    //lista de modalidades
    JSONArray listaApartados;
    //listview para rellenar
    ListView lViewApartados;
    //fuente
    Typeface fuenteCabecera;
    //Mensaje para casos de error
    Mensaje m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m =  new Mensaje("Error","Ha ocurrido un error al acceder a esta opción",this);

        //TODO OBTENER TAMBIEN LA CONFIGURACION DEL IDIOMA DEL MOVIL

        //obtenemos la configuracion del dispositivo para el modo dia-noche
        Configuration configuracion = Resources.getSystem().getConfiguration();
        //cargamos el layout que corresponde segun el modo de iluminacion del movil
        cambiarDiaNoche(configuracion);
        recibirDatos();
        //identificamos lo elementos de la interfaz
        identificarElementosInterfaz(configuracion);;
        cargarAnimaciones();
        //obtenemos la lista de modalidades del array Json que hemos pasado a esta actividad
        ArrayList<String> listaApartados = obtenerValorApartados();
        if(listaApartados.size()==0){
            m.mostrarMensaje();
        }else{
            lViewApartados.setAdapter(new AdaptadorListaGenerico(this,listaApartados,null,configuracion));
            activarBotones();
        }


    }

    private ArrayList<String> obtenerValorApartados() {
        ArrayList<String> resultado = new ArrayList<>();
        for (int i = 0; i < listaApartados.length(); i++) {
            String apartado = null;
            try {
                apartado = listaApartados.getJSONObject(i).getString("apartado");
                resultado.add(apartado);
            } catch (JSONException e) {
                e.printStackTrace();
                break;
            }

        }
        return resultado;
    }

    private void recibirDatos(){
        Bundle extras = getIntent().getExtras();
        try {
            listaApartados = new JSONArray(extras.getString("listaApartados"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cambiarDiaNoche(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.uiMode == MODO_CLARO) {
            setContentView(R.layout.activity_general_claro);
        } else if (newConfig.uiMode == MODO_OSCURO){
            setContentView(R.layout.activity_general_osc);
        }
    }

    @Override
    public void identificarElementosInterfaz(Configuration newConfig) {
        lyTituloProfesorado = findViewById(R.id.ly_titulo_claro);
        txtTitulo = findViewById(R.id.txt_titulo_claro);
        txtTitulo.setText(R.string.profesorado);
        if(newConfig.uiMode == MODO_CLARO){
            lViewApartados = findViewById(R.id.listViewClaro);
        }else if (newConfig.uiMode == MODO_OSCURO){
            lViewApartados = findViewById(R.id.listViewOsc);
        }
    }

    @Override
    public void activarBotones() {
        lViewApartados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String enlaceFichero= listaApartados.getJSONObject(i).getString("enlace");

                    Intent irApartado=new Intent(Intent.ACTION_VIEW, Uri.parse(enlaceFichero));
                    startActivity(irApartado);

                } catch (JSONException e) {
                    e.printStackTrace();
                    m.mostrarMensaje();
                }
            }
        });
    }

    public void cargarAnimaciones() {
        animTitulo = AnimationUtils.loadAnimation(this,R.anim.anim_cabecera);
        lyTituloProfesorado.startAnimation(animTitulo);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(), AcMenuPrincipal.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}