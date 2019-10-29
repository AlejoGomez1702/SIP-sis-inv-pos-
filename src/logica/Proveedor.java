package logica;

/**
 * FECHA ==> 2019-07-27.
 * Proveedores de productos del negocio.
 * @author Luis Alejandro Gómez C.
 * @author Ever Peña B.
 * @version 1.0.0
 */
public class Proveedor 
{
    /**
     * Código unico con el cual se identifica un proveedor.
     */
    private String codigo;
    
    /**
     * Nombre con el que se identifica un proveedor.
     */
    private String nombre;
    
    /**
     * NIT del proveedor.
     */
    private String NIT;
    
    /**
     * Número teléfonico del proveedor.
     */
    private String telefono;

    /**
     * Construye un proveedor con todos sus datos.
     * @param codigo Código con el cual se identifica el proveedor en el negocio.
     * @param nombre Nombre que identifica al proveedor.
     * @param Nit Nit con el cuál esta registrado el proveedor ante el gobierno.
     * @param telefono Número de contacto del proveedor.
     */
    public Proveedor(String codigo, String nombre,String Nit,String telefono) 
    {
      this.NIT=Nit;
      this.codigo=codigo;
      this.nombre=nombre;
      this.telefono=telefono;
    } 

    public String getCodigo() {
        return codigo;
    }

    public String getNIT() {
        return NIT;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setNIT(String NIT) {
        this.NIT = NIT;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
}
