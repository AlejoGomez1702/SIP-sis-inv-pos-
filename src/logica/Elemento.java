package logica;

/**
 * FECHA ==> 2019-07-24.
 * Representa un producto con todos los datos necesarios para registrarlos
 * en la base de datos.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0
 */
public class Elemento
{
    /**
     * Código de maximo 5 carácteres con el cual se identifica el producto
     * y con el que se busca para hacer compras, ventas y listar en el inventario.
     */
    private String codigo;
    
    /**
     * Es el producto general del cual esta compuesto el elemento o producto
     * especifico, por ejemplo: aguardiente, ron, cerveza, y el especifico  
     * contiene ya los demás datos como: aguardiente (cristal por botella).
     */
    private Producto producto;
    
    /**
     * Marca de la empresa fabricante del producto, ejm-->Cristal, Buchanans.
     */
    private String marca;
    
    /**
     * Unidad de medida del producto, ejm-->Media, botella, lata.
     */
    private String unidadMedida;
    
    /**
     * Cantidad minima que debe exstir en el negocio del producto.
     */
    private int stock;
    
    /**
     * Cantidad existente actualmente en el inventario del negocio.
     */
    private int cantidadActual;
    
    /**
     * Precio al que se compra en elemento.
     */
    private double precioCompra;
    
    /**
     * Precio con el cual se va vender el elemento.
     */
    private double precioVenta;   
    
    private double precioVentaFuera;
    
    /**
     * Cantidad actualmente disponible en el inventario.
     */
    private int cantidadSale;
    //private boolean fuera;

    public Elemento(String codigo, Producto producto, String marca, String um, 
                      int stock, int cantidadActual, double prC, double prV, double pvf) 
    {
        this.codigo = codigo;
        this.producto = producto;
        this.marca = marca;
        this.unidadMedida = um;
        this.stock = stock;
        this.cantidadActual = cantidadActual;
        this.precioCompra = prC;
        this.precioVenta = prV;
        this.precioVentaFuera = pvf;
        this.cantidadSale = 0;
        //this.fuera = false;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public Producto getProducto() {
        return producto;
    }

    public String getMarca() {
        return marca;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }           

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }    
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
        
    public double getPrecioCompra() {
        return precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }    

//    public double getPrecioVentaFuera() {
//        return precioVentaFuera;
//    }
//
//    public void setPrecioVentaFuera(double precioVentaFuera) {
//        this.precioVentaFuera = precioVentaFuera;
//    }    
        
    public int getStock() {
        return stock;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
        
    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public void sumarCantidadActual(int cantidad)
    {
        this.cantidadActual=this.cantidadActual +cantidad; 
    }
    public void restarCantidadActual(int cantidad)
    {
        this.cantidadActual=this.cantidadActual -cantidad; 
    }
    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setCantidadSale(int cantidadSale) {
        this.cantidadSale = cantidadSale;
    }

    public int getCantidadSale() {
        return cantidadSale;
    }

//    public void setIsFuera(boolean fuera) {
//        this.fuera = fuera;
//    }
//
//    public boolean isIsFuera() {
//        return fuera;
//    }
       
    
    
}
