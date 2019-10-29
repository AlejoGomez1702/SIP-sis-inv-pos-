package logica;

/**
 * FECHA ==> 2019-07-24.
 * Representa un producto general del cual se desprenden los elemento
 * por ejemplo: cerveza, aguardiente y ron.
 * @author Luis Alejandro Gómez C.
 * @author Ever Peña B.
 * @version 1.0.0
 */
public class Producto 
{
    /**
     * Código con el cual se identifica de manera unica un producto general
     * en la base de datos es un campo autonumérico.
     */
    private int codigo;
    
    /**
     * Nombre del producto general, por ejemplo: aguardiente, ron y cerveza.
     */
    private String nombre;
    
    /**
     * Categoría a la cual pertenece el producto, por ejemplo: Abarrotes,Licores,Otras Bebidas.
     */
    private String categoria;

    /**
     * Inicializa un producto general con todos sus datos.
     * @param codigo Código del producto que se desea crear.
     * @param nombre Nombre unico que representa al producto.
     * @param categoria Categoría a la cual pertenece, ejm:licores, abarrotes.
     */
    public Producto(int codigo, String nombre, String categoria)
    {
        this.nombre = nombre;
        this.categoria = categoria;
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
        
}
