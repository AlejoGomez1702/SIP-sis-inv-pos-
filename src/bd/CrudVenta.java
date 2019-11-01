package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import logica.Cliente;
import logica.Elemento;
import logica.Producto;
import logica.Venta;

/**
 * FECHA ==> 2019-06-08.
 * Permitirá realizar el CRUD de ventas en la base de datos.
 * @author Luis Alejandro Gómez C.
 * @version 1.0 .
 */
public class CrudVenta 
{
    /**
     * Conexión con la base de datos.
     */
    private Conexion conexion;

    /**
     * Inicializa un CRUD con la tabla venta.
     * @param con Conexión con la base de datos.
     */
    public CrudVenta(Conexion con) 
    {
        this.conexion = con;
    }
    
    /**
     * 
     * @param venta
     * @return 
     */
    public boolean crearVenta(Venta venta)
    {
        if(venta == null)
            return false;    
        
        Cliente c = venta.getCliente();
        String cliente = c.getNombre() + "," + c.getCedula() + "," + c.getTelefono();
        String fecha = venta.getFechaHora();
        String obs = venta.getObservacion();
        boolean fuera = venta.isFuera();
        double valor = venta.getValor();
                
        boolean bandera = true;
        String consulta = "INSERT INTO venta (cliente, fecha, observacion, is_fuera, valor_total)"
                            + " VALUES (?, ?, ?, ?, ?)"; 
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, cliente);
            ps.setString(2, fecha);
            ps.setString(3, obs);
            ps.setBoolean(4, false);
            ps.setDouble(5, valor);
            ps.executeUpdate();            
        } catch (SQLException e) 
        {
            bandera = false;
        }                                
        
        return bandera;        
    }
    
    /**
     * Busca una venta por su código en la base de datos.
     * @param codigo Llave primaria con la que se identifica una venta.
     * @return Null=>No encuentra la venta, de lo contrario=>Venta.
     */
    public Venta leerVenta(String codigo)
    {
        if(codigo.equals(""))
        {
            return null;
        }
        
        String consulta = "SELECT * FROM venta";
        Venta venta = null;
        String cod;
        String cliente;
        String fecha;
        String observacion;
        
        try 
        {
            ResultSet rs = this.conexion.getStatement().executeQuery(consulta);
            while(rs.next())
            {
                cod = rs.getString("codigo");
                //Si el código del proveedor buscado es encontrado.
                if(codigo.equals(cod))
                {
                    cliente = rs.getString("cliente");
                    fecha = rs.getString("fecha");
                    observacion = rs.getString("observacion");
                    //venta = new Venta(fecha, cliente, observacion);
                    break;
                }
            }
            
        } catch (SQLException e) 
        {
            return null;
        }
        
        return venta;
    }
    
    /**
     * Obtiene todas las ventas que se han echo en el negocio a lo largo del tiempo.
     * @return Lista con las ventas realizadas por el negocio.
     */
    public ArrayList<Venta> getAllSales()
    {
        ArrayList<Venta> sales = new ArrayList<>();
        String consulta = "SELECT * FROM venta ORDER BY fecha";
        
        //Datos de la venta.
        Venta v;
        int id;
        Cliente cliente;
        String fecha;
        String obs;
        boolean fuera;
        double valor;
        
        try 
        {
            ResultSet rs = this.conexion.getStatement().executeQuery(consulta);
            while(rs.next())
            {
                id = rs.getInt("id");
                cliente = convertClient(rs.getString("cliente"));
                fecha = rs.getString("fecha");
                obs = rs.getString("observacion");
                fuera = rs.getInt("is_fuera") == 1;
                //System.out.println("FUERAAA: " + fuera);
                valor = rs.getDouble("valor_total");
                v = new Venta(id, cliente, fecha, obs, fuera, valor);
                sales.add(v);
            }
            //Obtener los elementos de la venta.
            this.getAllElementsFromSales(sales);
        } catch (SQLException e) 
        {
            return sales;
        }
        
        return sales;
    }
    
    /**
     * Obtiene todas las ventas realizadas por el negocio en un rango de fechas.
     * @param initialDate Fecha inicial de consulta.
     * @param finishDate Fecha final de consulta.
     * @return Listado de ventas, else => null.
     */
    public ArrayList<Venta> getAllSalesFromDates(String initialDate, String finishDate)
    {
        ArrayList<Venta> sales = new ArrayList<>();
        String consulta = "SELECT * FROM venta WHERE fecha BETWEEN ? AND ? ORDER BY fecha";
        
        //Datos de la venta.
        Venta v;
        int id;
        Cliente cliente;
        String fecha;
        String obs;
        boolean fuera;
        double valor;
        
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, initialDate);
            ps.setString(2, finishDate);            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next())
            {
                id = rs.getInt("id");
                cliente = convertClient(rs.getString("cliente"));
                fecha = rs.getString("fecha");
                obs = rs.getString("observacion");
                fuera = rs.getInt("is_fuera") == 1;
                //System.out.println("FUERAAA: " + fuera);
                valor = rs.getDouble("valor_total");
                v = new Venta(id, cliente, fecha, obs, fuera, valor);
                sales.add(v);
            }
            //Obtener los elementos de la venta.
            this.getAllElementsFromSales(sales);
        } catch (SQLException e) 
        {
            return sales;
        }
        
        return sales;
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    public Venta getSaleFromId(int id)
    {
        ArrayList<Venta> sales = new ArrayList<>();
        Venta venta = null;
        String consulta = "SELECT * FROM venta WHERE id = ? ORDER BY fecha";
        
        //Datos de la venta.
        Venta v;
        Cliente cliente;
        String fecha;
        String obs;
        boolean fuera;
        double valor;
        
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setInt(1, id);          
            ResultSet rs = ps.executeQuery();
            
            while(rs.next())
            {
                id = rs.getInt("id");
                cliente = convertClient(rs.getString("cliente"));
                fecha = rs.getString("fecha");
                obs = rs.getString("observacion");
                fuera = rs.getInt("is_fuera") == 1;
                //System.out.println("FUERAAA: " + fuera);
                valor = rs.getDouble("valor_total");
                v = new Venta(id, cliente, fecha, obs, fuera, valor);
                venta = v;
                sales.add(v);
                break;
            }
            //Obtener los elementos de la venta.
            this.getAllElementsFromSales(sales);
        } catch (SQLException e) 
        {
            return venta;
        }
        
        return venta;
    }
    
    /**
     * 
     * @param sales 
     */
    public void getAllElementsFromSales(ArrayList<Venta> sales)
    {
        int numComp = sales.size();
        ArrayList<Elemento> elements;
        Venta vent;
        
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
        //double precVentFuerElement;
        int cantSaleElement;
        //boolean fuera;
        
        for(int i = 0; i < numComp; i++) 
        {
            elements = new ArrayList<>();
            vent = sales.get(i);
            String consulta = "SELECT e.codigo AS codigo_elemento, p.codigo AS codigo_producto,"
                    + " p.nombre AS nombre_producto, c.nombre AS categoria_producto, m.nombre AS "
                    + "marca_elemento, um.nombre AS medida_elemento, e.stock AS stock_elemento, "
                    + "e.cantidad_actual AS cantidadAct_elemento, e.precio_compra AS precio_compra_elemento,"
                    + " e.precio_venta AS precio_venta_elemento, e.precio_venta_fuera AS precio_venta_fuera,"
                    + " ec.cantidad AS cantidad_sale \n" +
                    "FROM elemento_vendido AS ec INNER JOIN venta AS c ON ec.id_venta = c.id INNER JOIN elemento "
                    + "AS e ON ec.codigo_elemento = e.codigo INNER JOIN producto AS p ON e.codigo_producto = p.codigo "
                    + "INNER JOIN categoria AS c ON c.codigo = p.codigo_categoria INNER JOIN marca AS m ON m.codigo = "
                    + "e.codigo_marca INNER JOIN unidad_medida AS um ON um.codigo = e.codigo_medida WHERE c.id = ?";
            try 
            {
                PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
                ps.setInt(1, vent.getId());
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
                vent.setElementos(elements);
            } catch (SQLException e) 
            {
                System.out.println("Errroooorrr en la consulta");                
            }                        
        }               
        
    }
    
    /**
     * Obtiene las ventas que se han echo diarias en el negocio.
     * @param dateStart Fecha de inicio del dia.
     * @param dateEnd Fecha donde termina el dia laboral, (un dia mas).
     * @return Listado con los ids de las ventas del dia.
     */
    public ArrayList<Integer> getDailySales(String dateStart, String dateEnd) 
    {
        ArrayList<Integer> idDailySale = new ArrayList<>();
        String consulta = "SELECT id FROM venta WHERE fecha BETWEEN ? AND ?";
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
                idDailySale.add(id);
            }            
        } catch (SQLException e) 
        {
            return null;
        }
        
        return idDailySale;
    }
    
    /**
     * Mapea de una cadena separada por , a un objeto cliente.
     * @param client Datos del cliente separados por ",".
     * @return Cliente como objeto.
     */
    public Cliente convertClient(String client)
    {
        if(client == null)
            return null;
        
        Cliente c = null;
        String[] data = client.split(",");
        //System.out.println("Tama: " + data.length);
        if(data.length >= 0 && data.length < 3)            
            c = new Cliente("", "", "");     
        else
            c = new Cliente(data[0], data[1], data[2]);
            
        return c;
    }
    
    /**
     * Obtiene el número de ventas que se han echo en el negocio.
     * @return Número de ventas realizadas.
     */
    public int getCount()
    {
        int num = 0;
        String consulta = "SELECT COUNT(*) AS n FROM venta";
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
