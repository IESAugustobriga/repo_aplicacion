package proyecto.Interfaces;

import android.content.res.Configuration;

public interface IConfigurarActividad extends IModosClaroOscuro{
    void cambiarDiaNoche(Configuration newConfig);
    void identificarElementosInterfaz(Configuration newConfig);
    void activarBotones();
}
