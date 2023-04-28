package proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.proyecto.infoaugustobriga.R;

import proyecto.Interfaces.IConfigurarActividad;
import proyecto.calendarios.AcCalendarios;
import proyecto.horarios.AcHorarios;
import proyecto.miscelanea.LecturaJson;
import proyecto.miscelanea.Mensaje;
import proyecto.profesorado.AcProfesorado;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ExecutionException;

public class AcMenuPrincipal extends AppCompatActivity implements IConfigurarActividad {

    //LinearLayout de los contenedores de los iconos
    LinearLayout lyCalendario, lyHorarios, lyMapa, lyNovedades, lyCentro, lyProfesorado, lyYoutube, lyRadio;
    //LinearLayout del titulo de la cabecera para animaciones
    LinearLayout lyTitulo;
    //TextView con el titulo
    TextView txtTitulo;
    //Animaciones
    Animation animCabecera, animSubcabecera, animContenedores;
    //fichero .json con todos los enlaces
    JSONObject json = null;
    //Mensaje para casos de error
    Mensaje m = new Mensaje();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //obtenemos la configuracion del dispositivo para el modo dia-noche
        Configuration configuracion = Resources.getSystem().getConfiguration();
        //cargamos el layout que corresponde segun el modo de iluminacion del movil
        cambiarDiaNoche(configuracion);
        //identificamos lo elementos de la interfaz
        identificarElementosInterfaz(configuracion);
        cargarAnimaciones();

        //leemos el fichero raiz del cual obtendremos todos los enlaces
        leerFicheroRaiz("https://raw.githubusercontent.com/IESAugustobriga/datos_app/main/fichero_raiz.json");

        if(json==null){
            m =  new Mensaje("Error","Ha ocurrido un error al iniciar la aplicación",this);
        }else{
            activarBotones();
        }
    }

    public void leerFicheroRaiz(String url){
        LecturaJson lecturaJson= new LecturaJson(url);
        try {
            //convertimos el string a json
            json = new JSONObject((String) lecturaJson.execute().get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cambiarDiaNoche(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(tamano_pantalla()>=5.0){
            if (newConfig.uiMode == MODO_CLARO) {
                setContentView(R.layout.activity_menu_principal_claro);
            } else if (newConfig.uiMode == MODO_OSCURO){
                setContentView(R.layout.activity_menu_principal_osc);
            }
        }else if (tamano_pantalla()<5.0){
            if (newConfig.uiMode == MODO_CLARO) {
                setContentView(R.layout.activity_menu_principal_claro_p);
            } else if (newConfig.uiMode == MODO_OSCURO){
                setContentView(R.layout.activity_menu_principal_osc_p);
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
    public void identificarElementosInterfaz(Configuration newConfig){
        lyTitulo = findViewById(R.id.ly_titulo_claro);
        txtTitulo = findViewById(R.id.txt_titulo_claro);
        txtTitulo.setText(R.string.tituloAplicacion);
        if (newConfig.uiMode == MODO_CLARO) {
            lyCalendario = findViewById(R.id.ly_calendario_claro);
            lyHorarios = findViewById(R.id.ly_horario_claro);
            lyNovedades = findViewById(R.id.ly_novedades_claro);
            lyMapa = findViewById(R.id.ly_plano_claro);
            lyCentro = findViewById(R.id.ly_centro_claro);
            lyProfesorado = findViewById(R.id.ly_profesorado_claro);
            lyYoutube = findViewById(R.id.ly_youtube_claro);
            lyRadio = findViewById(R.id.ly_radio_claro);
        } else if (newConfig.uiMode == MODO_OSCURO){
            lyCalendario = findViewById(R.id.ly_calendario_osc);
            lyHorarios = findViewById(R.id.ly_horario_osc);
            lyNovedades = findViewById(R.id.ly_novedades_osc);
            lyMapa = findViewById(R.id.ly_plano_osc);
            lyCentro = findViewById(R.id.ly_centro_osc);
            lyProfesorado = findViewById(R.id.ly_profesorado_osc);
            lyYoutube = findViewById(R.id.ly_youtube_osc);
            lyRadio = findViewById(R.id.ly_radio_osc);
        }
    }

    public void cargarAnimaciones(){
        animContenedores = AnimationUtils.loadAnimation(this,R.anim.anim_contenedores);
        animCabecera = AnimationUtils.loadAnimation(this, R.anim.anim_cabecera);

        lyTitulo.startAnimation(animCabecera);

        lyCalendario.startAnimation(animContenedores);
        lyHorarios.startAnimation(animContenedores);
        lyNovedades.startAnimation(animContenedores);
        lyMapa.startAnimation(animContenedores);
        lyCentro.startAnimation(animContenedores);
        lyProfesorado.startAnimation(animContenedores);
        lyYoutube.startAnimation(animContenedores);
        lyRadio.startAnimation(animContenedores);
    }

    @Override
    public void activarBotones(){
        m.setTitulo("Error");
        m.setContenido("Ha ocurrido un error al iniciar esta opción");
        m.setContexto(this);
        lyCentro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irNuestroCentro= null;
                try {
                    irNuestroCentro = new Intent(Intent.ACTION_VIEW, Uri.parse(json.getString("Nuestro Centro")));
                    startActivity(irNuestroCentro);
                } catch (JSONException e) {
                    e.printStackTrace();
                    m.mostrarMensaje();
                }
            }
        });
        lyNovedades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irNovedades= null;
                try {
                    irNovedades = new Intent(Intent.ACTION_VIEW, Uri.parse(json.getString("Novedades")));
                    startActivity(irNovedades);
                } catch (JSONException e) {
                    e.printStackTrace();
                    m.mostrarMensaje();
                }

            }
        });
        lyMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irPlano= null;
                try {
                    irPlano = new Intent(Intent.ACTION_VIEW, Uri.parse(json.getString("Plano del centro")));
                    startActivity(irPlano);
                } catch (JSONException e) {
                    e.printStackTrace();
                    m.mostrarMensaje();
                }

            }
        });
        lyProfesorado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irProfesorado = null;
                irProfesorado = new Intent(AcMenuPrincipal.this, AcProfesorado.class);
                try {
                    irProfesorado.putExtra("listaApartados", json.getJSONArray("Profesorado").toString());
                    startActivity(irProfesorado);
                } catch (JSONException e) {
                    e.printStackTrace();
                    m.mostrarMensaje();
                }
            }
        });
        lyHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irHorarios=new Intent(AcMenuPrincipal.this, AcHorarios.class);
                try {
                    //pasamos el array como string y lo volvemos a convertir en la actividad de destino
                    irHorarios.putExtra("listaModalidades",json.getJSONArray("Horarios").toString());
                    startActivity(irHorarios);
                } catch (JSONException e) {
                    e.printStackTrace();
                    m.mostrarMensaje();
                }

            }
        });
        lyCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irCalendarios = null;
                irCalendarios = new Intent(AcMenuPrincipal.this, AcCalendarios.class);
                try {
                    irCalendarios.putExtra("listaApartados", json.getJSONArray("Calendarios").toString());
                    startActivity(irCalendarios);
                } catch (JSONException e) {
                    e.printStackTrace();
                    m.mostrarMensaje();
                }
            }
        });
        lyYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irYoutube= null;
                try {
                    irYoutube = new Intent(Intent.ACTION_VIEW, Uri.parse(json.getString("Youtube")));
                    startActivity(irYoutube);
                } catch (JSONException e) {
                    e.printStackTrace();
                    m.mostrarMensaje();
                }

            }
        });
        lyRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irRadio= null;
                try {
                    irRadio = new Intent(Intent.ACTION_VIEW, Uri.parse(json.getString("Radio")));
                    startActivity(irRadio);
                } catch (JSONException e) {
                    e.printStackTrace();
                    m.mostrarMensaje();
                }
            }
        });
    }



    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

}
