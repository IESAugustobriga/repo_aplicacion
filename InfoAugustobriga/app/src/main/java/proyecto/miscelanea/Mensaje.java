package proyecto.miscelanea;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class Mensaje {

    private String contenido;
    private String titulo;
    private Context context;
    AlertDialog.Builder mensj;

    public Mensaje(){

    }

    public Mensaje(String titulo, String contenido, Context contexto){
        this.titulo = titulo;
        this.contenido = contenido;
        this.context = contexto;
        mensj = new AlertDialog.Builder(this.context);
    }

    public void mostrarMensaje(){
        if(this.contenido != null && this.titulo != null && this.context != null) {
            mensj = new AlertDialog.Builder(this.context);
            mensj.setTitle(titulo);
            mensj.setMessage(contenido);
            mensj.setCancelable(false);
            mensj.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            mensj.show();
        }
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setContexto(Context context) {
        this.context = context;
    }

    public String getContenido() {
        return contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public Context getContexto() {
        return context;
    }
}
