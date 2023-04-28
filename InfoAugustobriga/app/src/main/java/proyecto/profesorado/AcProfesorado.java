package proyecto.profesorado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import proyecto.AcMenuPrincipal;
import proyecto.Interfaces.IConfigurarActividad;
import app.proyecto.infoaugustobriga.R;
import proyecto.miscelanea.AdaptadorListaGenerico;
import proyecto.miscelanea.Mensaje;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        //obtenemos la configuracion del dispositivo para el modo dia-noche
        Configuration configuracion = Resources.getSystem().getConfiguration();
        //cargamos el layout que corresponde segun el modo de iluminacion del movil
        cambiarDiaNoche(configuracion);
        recibirDatos();
        //identificamos lo elementos de la interfaz
        identificarElementosInterfaz(configuracion);
        cargarAnimaciones();
        //obtenemos la lista de modalidades del array Json que hemos pasado a esta actividad
        ArrayList<String> listaApartados = obtenerValorApartados();
        if(listaApartados.size()==0){
            m.mostrarMensaje();
        }else{
            lViewApartados.setAdapter(new AdaptadorListaGenerico(this,listaApartados,null,configuracion, tamano_pantalla()));
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

        if(tamano_pantalla()>=5.0){
            if (newConfig.uiMode == MODO_CLARO) {
                setContentView(R.layout.activity_general_claro);
            } else if (newConfig.uiMode == MODO_OSCURO){
                setContentView(R.layout.activity_general_osc);
            }
        }else if (tamano_pantalla()<5.0){
            if (newConfig.uiMode == MODO_CLARO) {
                setContentView(R.layout.activity_general_claro_p);
            } else if (newConfig.uiMode == MODO_OSCURO){
                setContentView(R.layout.activity_general_osc_p);
            }
        }
    }

    public double tamano_pantalla(){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densidadPPI = (int)(metrics.density * 160f);

        Display display = getWindowManager().getDefaultDisplay();
        Point tamano = new Point();
        display.getSize(tamano);
        int ancho = tamano.x;
        int alto = tamano.y;

        double diagonalPixels = Math.sqrt(Math.pow(ancho, 2) + Math.pow(alto, 2));
        double pulgadasDiagonal = diagonalPixels / densidadPPI;

        BigDecimal bd = new BigDecimal(pulgadasDiagonal);
        pulgadasDiagonal = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
        return pulgadasDiagonal;
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
                    //la variable "i" contiene la posicion del dato al cual se selecciona
                    //dado que los datos estan ordenados, el nombre del apartado y su correspondiende enlace coinciden
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