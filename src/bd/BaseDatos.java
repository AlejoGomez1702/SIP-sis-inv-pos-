package bd;

/**
 * FECHA ==> 2019-06-01.
 * Posee todos los CRUD con la base de datos.
 * @author Luis Alejandro Gómez C.
 * @version 1.0 .
 */
public class BaseDatos 
{
    /**
     * Conexión con la base de datos.
     */
    private Conexion conexion;
    
    /**
     * CRUD de la tabla proveedores.
     */
    private CrudProveedor crudProveedores;
    
    /**
     * CRUD de la tabla "categoria" de la base de datos.
     */
    private CrudCategoria crudCategorias;
    
    /**
     * CRUD de la tabla "marca" de la base de datos.
     */
    private CrudMarca crudMarcas;
    
    /**
     * CRUD de la tabla "unidad_medida" de la base de datos.
     */
    private CrudUnidadMedida crudUnidades;
    
    /**
     * CRUD de la tabla "producto" de la base de datos.
     */
    private CrudProducto crudProducto;
    
    /**
     * CRUD de la tabla "elemento" de la base de datos.
     */
    private CrudElemento crudElemento;
    
    /**
     * CRUD de la tabla "compras" de la base de datos.
     */
    private CrudCompra crudCompra;
    
    /**
     * CRUD de la tabla "elemento_comprado" de la base de datos.
     */
    private CrudElementoComprado crudElementoComprado;
    
    /**
     * CRUD de la tabla "venta" de la base de datos.
     */
    private CrudVenta crudVenta;
    
    /**
     * CRUD de la tabla "elemento_vendido" de la base de datos.
     */
    private CrudElementoVendido crudElementoVendido;
    
    /**
     * CRUD de la tabla "mapeo" de la base de datos.
     */
    private CrudScanner crudScanner;
    
    /**
     * CRUD de la tabla "michelada" de la base de datos.
     */
    private CrudMichelada crudMichelada;
    
    /**
     * Prepara todos los cruds con la base de datos.
     * @param con Conexión con la base de datos.
     */
    public BaseDatos(Conexion con)
    {
        this.conexion = con;
        this.crudProveedores = new CrudProveedor(con);
        this.crudCategorias = new CrudCategoria(con);
        this.crudMarcas = new CrudMarca(con);
        this.crudUnidades = new CrudUnidadMedida(con);
        this.crudProducto = new CrudProducto(con);
        this.crudElemento = new CrudElemento(con);
        this.crudCompra = new CrudCompra(con);
        this.crudElementoComprado = new CrudElementoComprado(con);
        this.crudVenta = new CrudVenta(con);
        this.crudElementoVendido = new CrudElementoVendido(con);
        this.crudScanner = new CrudScanner(con);
        this.crudMichelada = new CrudMichelada(con);
    }
    
    /**
     * Obtiene la clase que permite realiazr el CRUD de proveedores del negocio.
     * @return CRUD de proveedores del negocio.
     */
    public CrudProveedor getCrudProveedores() 
    {
        return crudProveedores;
    }

    /**
     * Obtiene la clase que permite realiazr el CRUD de categorias 
     * de productos del negocio.
     * @return CRUD de categorias de productos del negocio.
     */
    public CrudCategoria getCrudCategorias() 
    {
        return crudCategorias;
    }

    /**
     * Obtiene la clase que permite realiazr el CRUD de marcas de productos.
     * @return CRUD de marcas de productos del negocio.
     */
    public CrudMarca getCrudMarcas() 
    {
        return crudMarcas;
    }

    /**
     * Obtiene la clase que permite realiazr el CRUD de unidades de medidas de productos.
     * @return CRUD de unidades de medidas de productos del negocio.
     */
    public CrudUnidadMedida getCrudUnidades() 
    {
        return crudUnidades;
    }

    /**
     * Obtiene el CRUD de productos generales del negocio.
     * @return CRUD de productoss generales del negocio.
     */
    public CrudProducto getCrudProducto() 
    {
        return crudProducto;
    }
    
    /**
     * Obtiene el CRUD de elementos especificos que tiene el negocio.
     * @return CRUD de elementos que tiene el inventario del negocio.
     */
    public CrudElemento getCrudElemento() 
    {
        return crudElemento;
    }

    /**
     * Obtiene el CRUD de compras que tiene el negocio.
     * @return CRUD de compras echas en el negocio.
     */
    public CrudCompra getCrudCompra() 
    {
        return crudCompra;
    }        

    /**
     * Obtiene el CRUD de elementos comprados que tiene el negocio.
     * @return CRUD de elementos comprados del negocio.
     */
    public CrudElementoComprado getCrudElementoComprado() 
    {
        return crudElementoComprado;
    }

    /**
     * Obtiene el CRUD de ventas que tiene el negocio.
     * @return CRUD de ventas del negocio.
     */
    public CrudVenta getCrudVenta() 
    {
        return crudVenta;
    }        

    /**
     * Obtiene el CRUD de elementos vendidos que tiene el negocio.
     * @return CRUD de elementos vendidos del negocio.
     */
    public CrudElementoVendido getCrudElementoVendido() 
    {
        return crudElementoVendido;
    }        

    /**
     * Obtiene el CRUD de productos registrados con código de barras
     * que tiene el negocio.
     * @return CRUD de elementos con código de barras.
     */
    public CrudScanner getCrudScanner() 
    {
        return crudScanner;
    }    

    /**
     * Obtiene el CRUD de la tabla que permite modificar el valor de las micheladas.
     * @return CRUD de micheladas del negocio.
     */
    public CrudMichelada getCrudMichelada() 
    {
        return crudMichelada;
    }
            
    /**
     * Obtiene la conexión con la base de datos del sistema.
     * @return Conexión con la base de datos.
     */
    public Conexion getConexion() 
    {
        return conexion;
    }   
       
        
}
