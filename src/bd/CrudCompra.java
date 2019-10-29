package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import logica.Compra;
import logica.Elemento;
import logica.Producto;
import logica.Proveedor;

/**
 * FECHA ==> 2019-05-09.
 * Permite realizar el CRUD con la tabla "compra" de la base de datos.
 * @author Luis Alejandro Gómez C.
 * @author Ever Peña B.
 * @version 1.0 .
 */
public class CrudCompra 
{
    /**
     * Conexión con la base de datos.
     */
    private Conexion conexion;

    /**
     * Inicializa un CRUD con la tabla "compra".
     * @param con Conexión con la base de datos.
     */
    public CrudCompra(Conexion con) 
    {
        this.conexion = con;
    }
    
    /**
     * Crea una nueva compra en la base de datos.
     * @param purchase Compra que se desea registrar en la base de datos.
     * @return True-->Se registró la compra, else-->False.
     */
    public boolean createPurchase(Compra purchase)
    {
        if(purchase == null)
            return false;
        
        boolean bandera = true;
        String date = purchase.getFechaHora();
        String obs = purchase.getObservacion();
        String providerCode = purchase.getProveedor().getCodigo();
        double value = purchase.getValor();
        String consulta = "INSERT INTO compra (fecha, observacion, codigo_proveedor, valor_total) "
                + "VALUES (?, ?, ?, ?)";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, date);
            ps.setString(2, obs);
            ps.setString(3, providerCode);
            ps.setDouble(4, value);
            ps.executeUpdate();            
        } catch (SQLException e) 
        {
            bandera = false;
        }
        
        return bandera;        
    }
    
    /**
     * Obtiene todas la compras registradas en la base de datos, que actualmente 
     * ha realizado el negocio a lo largo de todo el tiempo.
     * @return Lista de compras que ha realizado el negocio.
     */
    public ArrayList<Compra> getAllPurchases()
    {
        ArrayList<Compra> purchases = new ArrayList<>();
        String consulta = "SELECT DISTINCT c.id AS id_compra, c.fecha AS fecha_compra,"
                + " p.codigo AS codigo_proveedor, p.nombre AS nombre_proveedor, p.nit AS nit_proveedor, "
                + "p.celular AS celular_proveedor, c.valor_total AS valor_compra, c.observacion AS observacion_compra "
                + "FROM compra AS c INNER JOIN elemento_comprado AS ec ON ec.id_compra=c.id INNER JOIN proveedor AS P "
                + "ON c.codigo_proveedor = p.codigo ORDER BY c.fecha";
        
        //Datos para el proveedor.
        Proveedor provider;
        String providerCode;
        String providerName;
        String providerNit;
        String providerPhone;
        
        //Datos para la compra.
        Compra purchase;
        int id;
        String date;
        double purchaseValue;
        String purchaseObs;        
        
        try 
        {
            ResultSet rs = this.conexion.getStatement().executeQuery(consulta);
            
            while(rs.next())
            {
                //Datos para el proveedor.
                providerCode = rs.getString("codigo_proveedor");
                providerName = rs.getString("nombre_proveedor");
                providerNit = rs.getString("nit_proveedor");
                providerPhone = rs.getString("celular_proveedor");
                provider = new Proveedor(providerCode, providerName, providerNit, providerPhone);

                //Datos para la compra.
                id = rs.getInt("id_compra");
                date = rs.getString("fecha_compra");
                purchaseValue = rs.getDouble("valor_compra");
                purchaseObs = rs.getString("observacion_compra");  
                purchase = new Compra(id, date, provider, purchaseValue, purchaseObs);
                
                purchases.add(purchase);
            }               
            //Llenando las compras con sus elementos.
            this.getAllElementsFromPurchases(purchases);
        } catch (SQLException e) 
        {
            return purchases;
        }        
        
        return purchases;
    }
    
    /**
     * Obtiene los elementos involucrados en cada una de las compras del negocio.
     * @param purchases Lista de compras del negocio.
     */
    public void getAllElementsFromPurchases(ArrayList<Compra> purchases)
    {
        int numComp = purchases.size();
        ArrayList<Elemento> elements;
        Compra comp;
        
        //Información de el producto.
        Producto product;
        int codeProduct;
        String nameProduct;
        String categoryProduct;        
        
        //Información del elemento.
        Elemento element;
        String codeElement;
        String markElement;
        String unitElement;
        int stockElement;
        int cantActElement;
        double precCompElement;
        double precVentElement;
        double precVentFuerElement;
        int cantSaleElement;
        
        for(int i = 0; i < numComp; i++) 
        {
            elements = new ArrayList<>();
            comp = purchases.get(i);
            String consulta = "SELECT e.codigo AS codigo_elemento, p.codigo AS codigo_producto, p.nombre AS nombre_producto, c.nombre AS categoria_producto,\n" +
                "m.nombre AS marca_elemento, um.nombre AS medida_elemento, e.stock AS stock_elemento, e.cantidad_actual AS cantidadAct_elemento,\n" +
                "e.precio_compra AS precio_compra_elemento, e.precio_venta AS precio_venta_elemento, e.precio_venta_fuera AS precio_venta_fuera,\n" +
                "ec.cantidad AS cantidad_sale\n" +
                "FROM elemento_comprado AS ec INNER JOIN compra AS c ON ec.id_compra = c.id INNER JOIN elemento AS e ON ec.codigo_elemento = e.codigo INNER JOIN producto AS p "
                + "ON e.codigo_producto = p.codigo INNER JOIN categoria AS c ON c.codigo = p.codigo_categoria INNER JOIN marca AS m ON m.codigo = e.codigo_marca "
                + "INNER JOIN unidad_medida AS um ON um.codigo = e.codigo_medida WHERE c.id = ?";
            try 
            {
                PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
                ps.setInt(1, comp.getId());
                ResultSet rs = ps.executeQuery();
                while(rs.next())
                {
                    //Información del producto.
                    codeProduct = rs.getInt("codigo_producto");
                    nameProduct = rs.getString("nombre_producto");
                    categoryProduct = rs.getString("categoria_producto");
                    product = new Producto(codeProduct, nameProduct, categoryProduct);
                    
                    //Información del elemento.
                    codeElement = rs.getString("codigo_elemento");
                    markElement = rs.getString("marca_elemento");
                    unitElement = rs.getString("medida_elemento");
                    stockElement = rs.getInt("stock_elemento");
                    cantActElement = rs.getInt("cantidadAct_elemento");
                    precCompElement = rs.getDouble("precio_compra_elemento");
                    precVentElement = rs.getDouble("precio_venta_elemento");
                    //precVentFuerElement = rs.getDouble("precio_venta_fuera");
                    cantSaleElement = rs.getInt("cantidad_sale");
                    element = new Elemento(codeElement, product, markElement, unitElement, stockElement,
                    cantActElement, precCompElement, precVentElement, 0.0);
                    element.setCantidadSale(cantSaleElement);
                    
                    elements.add(element);
                }                
                //Añadiendo los elementos a la compra
                comp.setElementos(elements);
            } catch (SQLException e) 
            {
                System.out.println("Errroooorrr en la consulta");                
            }                        
        }        
    }
    
    /**
     * Obtiene las compras que se han echo diarias en el negocio.
     * @param dateStart Fecha de inicio del dia.
     * @param dateEnd Fecha donde termina el dia laboral, (un dia mas).
     * @return Listado con los ids de las compras del dia.
     */
    public ArrayList<Integer> getDailyPurchases(String dateStart, String dateEnd) 
    {
        ArrayList<Integer> idDailyPurch = new ArrayList<>();
        String consulta = "SELECT id FROM compra WHERE fecha BETWEEN ? AND ?";
        int id;
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, dateStart);
            ps.setString(2, dateEnd);       
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                id = rs.getInt("id");  
                idDailyPurch.add(id);
            }            
        } catch (SQLException e) 
        {
            return null;
        }
        
        return idDailyPurch;
    }
    
    /**
     * Obtiene el número de compras que se han echo en el negocio.
     * @return Número de compras realizadas.
     */
    public int getCount()
    {
        int num = 0;
        String consulta = "SELECT COUNT(*) AS n FROM compra";
        try 
        {
            ResultSet rs = this.conexion.getStatement().executeQuery(consulta);
            num = rs.getInt("n");
        } catch (SQLException e) 
        {
            return -1;
        }
        
        return num;
    }
    
}
