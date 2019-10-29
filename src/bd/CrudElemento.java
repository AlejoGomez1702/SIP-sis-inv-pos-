package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import logica.Elemento;
import logica.Producto;

/**
 * FECHA ==> 2019-07-21.
 * Permitira realizar el CRUD de elementos en la base de datos.
 * @author Luis Alejandro Gómez C.
 * @author Ever Peña B.
 * @version 1.0.0
 */
public class CrudElemento 
{
    /**
     * Conexión con la base de datos.
     */
    private Conexion conexion;

    /**
     * Inicializa un CRUD con la tabla "elemento".
     * @param con Conexión con la base de datos.
     */
    public CrudElemento(Conexion con) 
    {
        this.conexion = con;
    }
    
    /**
     * Crea un elemento en la base de datos.
     * @param elem Elemento que se desea crear en la base de datos.
     * @param cp Código del producto que representa al elemeto.
     * @param cm Código de la marca del elemento.
     * @param cum Código de la unidad de medida del elemento.
     * @return Si se pudo o no crear el elemento en la base de datos.
     */
    public boolean crearElemento(Elemento elem, int cp, int cm, int cum)
    {
        if(elem == null)
            return false;
        
        String codigo = elem.getCodigo();  
        int stock = elem.getStock();
        double precComp = elem.getPrecioCompra();
        double precVent = elem.getPrecioVenta();
        double precVentFuer = elem.getPrecioVentaFuera();
        int cantAct = elem.getCantidadActual();
        
        boolean bandera = true;
        String consulta = "INSERT INTO elemento (codigo, codigo_producto, codigo_marca, "
        + "codigo_medida, stock, precio_compra, precio_venta, precio_venta_fuera, cantidad_actual) "
        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, codigo);
            ps.setInt(2, cp);
            ps.setInt(3, cm);
            ps.setInt(4, cum);
            ps.setInt(5, stock);
            ps.setDouble(6, precComp);
            ps.setDouble(7, precVent);
            ps.setDouble(8, precVentFuer);
            ps.setInt(9, cantAct);
            ps.executeUpdate();
        } catch (SQLException e) 
        {
            //System.out.println("Excepcion: " + e.getMessage());
            bandera = false;
        }        
        
        return bandera;
    }
    
    /**
     * Actualiza la cantidad actual de un elemento.
     * @param element Elemento que se desea modificar.
     * @param count Cantidad que se le va asignar.
     * @return True==>Modificó, else==>False
     */
    public boolean updateCountElement(Elemento element, int count)
    {
        if(element == null || count <0 )
            return false;
        
        boolean bandera = true;
        String codeElement = element.getCodigo();
        //System.out.println("Codigo: " + codeElement);
        String consulta = "UPDATE elemento SET cantidad_actual = ? WHERE codigo = ?";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setInt(1, count);
            ps.setString(2, codeElement);
            ps.executeUpdate();
        } catch (SQLException e) 
        {
            bandera = false;
        }        
        
        return bandera;
    }
    
    /**
     * Actualiza la información de un producto del inventario.
     * @param oldElement Producto que se desea modificar.
     * @param newElement Producto con los nuevos datos.
     * @param cp Código del producto que representa al elemeto.
     * @param cm Código de la marca del elemento.
     * @param cum Código de la unidad de medida del elemento.
     * @return True-->Se Modificó, False-->No se pudo modificar.
     */
    public boolean updateElement(Elemento oldElement, Elemento newElement, int cp, int cm, int cum)
    {
        if(oldElement == null || newElement == null)
            return false;
        
        boolean bandera = true;
        String oldCode = oldElement.getCodigo();
        String codigo = newElement.getCodigo();  
        int stock = newElement.getStock();
        double precComp = newElement.getPrecioCompra();
        double precVent = newElement.getPrecioVenta();
        double precVentFuer = newElement.getPrecioVentaFuera();
        int cantAct = newElement.getCantidadActual();
        
        String consulta = "UPDATE elemento SET codigo = ?, codigo_producto = ?, codigo_marca = ?, "
                + "codigo_medida = ?, stock = ?, precio_compra = ?, precio_venta = ?, "
                + "precio_venta_fuera = ?, cantidad_actual = ? WHERE codigo = ? ";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, codigo);
            ps.setInt(2, cp);
            ps.setInt(3, cm);
            ps.setInt(4, cum);
            ps.setInt(5, stock);
            ps.setDouble(6, precComp);
            ps.setDouble(7, precVent);
            ps.setDouble(8, precVentFuer);
            ps.setInt(9, cantAct);
            ps.setString(10, oldCode);
            ps.executeUpdate();
        } catch (SQLException e) 
        {
            //System.out.println("Excepcion: " + e.getMessage());
            bandera = false;
        }  
        
        return bandera;
    }
    
    /**
     * Elimina un producto del inventario.
     * @param code Código del producto que se desea eliminar.
     * @return True-->Eliminó, False-->No eliminó.
     */
    public boolean deleteElement(String code)
    {
        if(code.equals(""))
            return false;
        
        boolean bandera = true;
        String consulta = "DELETE FROM elemento WHERE codigo = ?";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, code);
            ps.executeUpdate();
        } catch (SQLException e) 
        {
            bandera = false;
        }
        
        return bandera;        
    }
    
    /**
     * Obtiene un elemento registrado en la base de datos dado su código.
     * @param code Código del elemento que se desea obtener de la base de datos.
     * @return Elemento de la base de datos que corresponde al código ingresado.
     */
    public Elemento getElementFromCode(String code)
    {        
        String consulta = "SELECT e.codigo AS codigo_elemento, p.codigo AS codigo_producto, p.nombre AS nombre_producto, " +
        "c.nombre AS nombre_categoria, m.nombre AS nombre_marca, um.nombre AS nombre_unidad, e.stock AS stock_elemento, " +
        "e.cantidad_actual AS elemento_cantidad, e.precio_compra AS elemento_precio_compra, e.precio_venta AS elemento_precio_venta, " +
        "e.precio_venta_fuera AS elemento_precio_venta_fuera " +
        "FROM elemento AS e INNER JOIN producto AS p " +
        "ON e.codigo_producto = p.codigo " +
        "INNER JOIN categoria AS c " +
        "ON p.codigo_categoria = c.codigo " +
        "INNER JOIN marca AS m " +
        "ON e.codigo_marca = m.codigo " +
        "INNER JOIN unidad_medida AS um " +
        "ON e.codigo_medida = um.codigo WHERE e.codigo = ?";
        
        //Datos para la creación del producto.
        Producto p;
        int codeProduct;
        String nameProduct;
        String nameCategory;        
        
        //Datos de los elementos.
        Elemento el = null;
        String codeElement;
        String markElement;
        String unitElemento;
        int stockElement;
        int cantElement;
        double precCompElement;
        double precVentElement;
        double precVentFuerElement;        
        
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                //Producto:
                codeProduct = rs.getInt("codigo_producto");
                nameProduct = rs.getString("nombre_producto");
                nameCategory = rs.getString("nombre_categoria");
                p = new Producto(codeProduct, nameProduct, nameCategory);
                
                //Elemento:
                codeElement = rs.getString("codigo_elemento");
                markElement = rs.getString("nombre_marca");
                unitElemento = rs.getString("nombre_unidad");
                stockElement = rs.getInt("stock_elemento");
                cantElement = rs.getInt("elemento_cantidad");
                precCompElement = rs.getDouble("elemento_precio_compra");
                precVentElement = rs.getDouble("elemento_precio_venta");
                precVentFuerElement = rs.getDouble("elemento_precio_venta_fuera");
                el = new Elemento(codeElement, p, markElement, unitElemento, stockElement,
                    cantElement, precCompElement, precVentElement, precVentFuerElement); 
            }
            
        } catch (SQLException e) 
        {
            return null;
        }
        
        return el;
    }
    
    
    /**
     * Obtiene todos los elementos registrados en la base de datos.
     * @return Lista con los elementos registrados en la base de datos.
     */
    public ArrayList<Elemento> getAllElements()
    {
        ArrayList<Elemento> elementos = new ArrayList<>();
        String consulta = "SELECT e.codigo AS codigo_elemento, p.codigo AS codigo_producto, p.nombre AS nombre_producto, " +
        "c.nombre AS nombre_categoria, m.nombre AS nombre_marca, um.nombre AS nombre_unidad, e.stock AS stock_elemento, " +
        "e.cantidad_actual AS elemento_cantidad, e.precio_compra AS elemento_precio_compra, e.precio_venta AS elemento_precio_venta, " +
        "e.precio_venta_fuera AS elemento_precio_venta_fuera " +
        "FROM elemento AS e INNER JOIN producto AS p " +
        "ON e.codigo_producto = p.codigo " +
        "INNER JOIN categoria AS c " +
        "ON p.codigo_categoria = c.codigo " +
        "INNER JOIN marca AS m " +
        "ON e.codigo_marca = m.codigo " +
        "INNER JOIN unidad_medida AS um " +
        "ON e.codigo_medida = um.codigo";        
        
        //Datos para la creación del producto.
        Producto p;
        int codeProduct;
        String nameProduct;
        String nameCategory;        
        
        //Datos de los elementos.
        Elemento e;
        String codeElement;
        String markElement;
        String unitElemento;
        int stockElement;
        int cantElement;
        double precCompElement;
        double precVentElement;
        double precVentFuerElement;
        
        try 
        {
            ResultSet rs = this.conexion.getStatement().executeQuery(consulta);
            while(rs.next())
            {                
                //Producto:
                codeProduct = rs.getInt("codigo_producto");
                nameProduct = rs.getString("nombre_producto");
                nameCategory = rs.getString("nombre_categoria");
                p = new Producto(codeProduct, nameProduct, nameCategory);
                
                //Elemento:
                codeElement = rs.getString("codigo_elemento");
                markElement = rs.getString("nombre_marca");
                unitElemento = rs.getString("nombre_unidad");
                stockElement = rs.getInt("stock_elemento");
                cantElement = rs.getInt("elemento_cantidad");
                precCompElement = rs.getDouble("elemento_precio_compra");
                precVentElement = rs.getDouble("elemento_precio_venta");
                precVentFuerElement = rs.getDouble("elemento_precio_venta_fuera");
                e = new Elemento(codeElement, p, markElement, unitElemento, stockElement,
                    cantElement, precCompElement, precVentElement, precVentFuerElement);
                elementos.add(e);
            }            
        } catch(SQLException ex) 
        {
            System.out.println("Error en la consulta de elementos");
            System.out.println(ex.getMessage());
            return elementos;
        }        
        
        return elementos;
    }    
    
}
