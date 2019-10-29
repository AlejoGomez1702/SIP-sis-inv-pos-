package controladores;

import dialogos.ListaProductos;
import java.util.ArrayList;
import logica.Elemento;
import logica.Tequilazo;

/**
 * FECHA ==> 2019-09-05.
 * Controlador para la nuevas ventas del negocio.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0
 */
public class VentaController 
{
    /**
     * Permite la manipulación del sistema.
     */
    private Tequilazo tequilazo;
    
    /**
     * Crea el controlador para la manipulación de las ventas.
     * @param tequilazo Permite la gestion del sistema.
     */
    public VentaController(Tequilazo tequilazo) 
    {
        this.tequilazo = tequilazo;
    }
        
    
    
}
