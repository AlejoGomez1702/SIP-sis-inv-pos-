package principal;
import bd.BaseDatos;
import bd.Conexion;
import java.util.ArrayList;
import java.util.HashMap;
import logica.gestores.Caja;
import logica.Compra;
import logica.Inventario;
import logica.Tequilazo;
import logica.Elemento;
import logica.Proveedor;
import logica.Venta;
import logica.bd.*;

/**
 * FECHA ==> 2019-07-24.
 * Inicializa la conexión con la base de datos junto con todos los datos
 * previos que necesita el sistema para realizar las gestiones.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0
 */
public class Main 
{
    public static void main(String[] args)
    {
        //Base de datos del sistema.
        Conexion con = new Conexion();
        BaseDatos bd = new BaseDatos(con);
        
        //Datos iniciales de la base de datos.
        ArrayList<Proveedor> prov = bd.getCrudProveedores().getAllProviders();
        ArrayList<Categoria> cate = bd.getCrudCategorias().obtenerCategorias();
        ArrayList<Marca> marc = bd.getCrudMarcas().obtenerMarcas();  
        ArrayList<UnidadMedida> unid = bd.getCrudUnidades().obtenerUnidadesMedidas();    
        ArrayList<Elemento> elementos = bd.getCrudElemento().getAllElements();
        ArrayList<Compra> compras = bd.getCrudCompra().getAllPurchases();
        ArrayList<Venta> ventas = bd.getCrudVenta().getAllSales();  
        HashMap mapaPrel = bd.getCrudScanner().getAllMaps();
  
        Caja caja = new Caja();
        Inventario inventario = new Inventario(elementos);       
        
        Tequilazo tequilazo = new Tequilazo(inventario, caja, bd); 
        tequilazo.setMap(mapaPrel);
        tequilazo.setProveedores(prov);
        tequilazo.setCategorias(cate);
        tequilazo.setMarcas(marc);
        tequilazo.setUnidadesMedidas(unid);
        tequilazo.setCompras(compras);
        tequilazo.setVentas(ventas);
        
        tequilazo.getDailySales();
        
        //inicia la vista 
        Login login = new Login(tequilazo);
        login.setVisible(true);        
    }
    
}
