package logica.bd;

/**
 * FECHA ==> 2019-07-13.
 * Representa una categoria de productos del negocio. 
 * por ejemplo: licores, abarrotes, comestibles, etc.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0.
 */
public class Categoria extends Ajuste
{
    public Categoria(int codigo, String nombre, String descripcion) 
    {
        super(codigo, nombre, descripcion);
    }   
    
}
