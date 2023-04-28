package proyecto.miscelanea;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import proyecto.Interfaces.IModosClaroOscuro;
import app.proyecto.infoaugustobriga.R;
import java.util.ArrayList;

public class AdaptadorListaGenerico extends BaseAdapter implements IModosClaroOscuro {

    ArrayList<String> filas;
    Context contexto;
    Typeface fuente;
    Configuration config;
    double pulgadas;

    public AdaptadorListaGenerico(Context c,ArrayList<String> datos, Typeface fuenteContenedores, Configuration newConfig, double pulgadas){
        this.contexto=c;
        this.filas=new ArrayList<>();
        this.fuente = fuenteContenedores;
        this.config = newConfig;
        this.pulgadas = pulgadas;

        //se utiliza un for y no un foreach debido a que este ultimo no coge los datos en orden
        for(int i=0;i<datos.size();i++){
            filas.add(datos.get(i));
        }

    }

    @Override
    public int getCount() {
        return filas.size();
    }

    @Override
    public Object getItem(int i) {
        return filas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //preparamos los elementos visuales de la lista
        LayoutInflater inflar=(LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View list = null;
        TextView curso = null;

        if(pulgadas>=5.0){
            if(config.uiMode == MODO_CLARO){
                list=inflar.inflate(R.layout.lista_generica_claro, viewGroup, false);
                //disponemos las variables de los elementos gráficos
                curso = (TextView) list.findViewById(R.id.txtListaClaro);
            }else if(config.uiMode == MODO_OSCURO){
                list=inflar.inflate(R.layout.lista_generica_osc, viewGroup, false);
                //disponemos las variables de los elementos gráficos
                curso = (TextView) list.findViewById(R.id.txtListaOsc);
            }
        }else if (pulgadas<5.0){
            if(config.uiMode == MODO_CLARO){
                list=inflar.inflate(R.layout.lista_generica_claro_p, viewGroup, false);
                //disponemos las variables de los elementos gráficos
                curso = (TextView) list.findViewById(R.id.txtListaClaro);
            }else if(config.uiMode == MODO_OSCURO){
                list=inflar.inflate(R.layout.lista_generica_osc_p, viewGroup, false);
                //disponemos las variables de los elementos gráficos
                curso = (TextView) list.findViewById(R.id.txtListaOsc);
            }
        }



        //rellenamos las variables con los datos de la lista
        curso.setText(filas.get(i).toString());
        //se añade la fuente (puede ser opcional)
        if(fuente!=null){
            curso.setTypeface(fuente);
        }

        //Anadimos una animacion
        Animation animacion;
        animacion = AnimationUtils.loadAnimation(contexto,R.anim.anim_lista_desl);
        list.startAnimation(animacion);

        return list;
    }
}
