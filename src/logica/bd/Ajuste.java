package logica.bd;

/**
 * FECHA ==> 2019-07-13.
 * Clase padre de categoria, marca y unidades de medida, los cuales
 * son atributos que tienen los diferentes productos del inventario.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0.
 */
public abstract class Ajuste 
{
    /**
     * Código con el que esta identificado en la base de datos el ajuste.
     */
    private int codigo;
    
    /**
     * Nombre que describe el ajuste.
     */
    private String nombre;
    
    /**
     * Descripción opcional que se le brinda a los ajustes.
     */
    private String descripcion;

    /**
     * Crea un ajuste de tipo categoria, marca o unidad de medida.
     * @param codigo Código con el que se identifica el ajuste.
     * @param nombre Nombre asignado al ajuste.
     * @param descripcion Descripción corta del ajuste.
     */
    public Ajuste(int codigo, String nombre, String descripcion) 
    {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el código con el que esta identificado el ajuste.
     * @return Código del ajuste.
     */
    public int getCodigo() 
    {
        return codigo;
    }

    /**
     * Obtiene el nombre que identifica al ajuste.
     * @return Nombre que identifica el ajuste.
     */
    public String getNombre() 
    {
        return nombre;
    }

    /**
     * Obtiene la descripción que se le hace a un ajuste.
     * @return Descripción que posee el ajuste.
     */
    public String getDescripcion() 
    {
        return descripcion;
    }
    
    
    
    
}
