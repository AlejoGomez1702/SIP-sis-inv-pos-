package logica.bd;

/**
 * FECHA ==> 2019-07-13.
 * Representa las diferentes unidades de medida de los productos que maneja el negocio.
 * por ejemplo: Media, botella, cajetilla, etc.. 
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0.
 */
public class UnidadMedida extends Ajuste
{
    public UnidadMedida(int codigo, String nombre, String descripcion) 
    {
        super(codigo, nombre, descripcion);
    }
}
