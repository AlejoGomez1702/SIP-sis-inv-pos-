package logica.bd;

/**
 * FECHA ==> 2019-07-13.
 * Representa las diferentes marcas de productos que maneja el negocio.
 * por ejemplo: Cristal, blanco, lucky, etc. 
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0.
 */
public class Marca extends Ajuste
{
    public Marca(int codigo, String nombre, String descripcion) 
    {
        super(codigo, nombre, descripcion);
    }      
}
