package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import logica.Producto;

/**
 * FECHA ==> 2019-07-21.
 * Permitira realizar el CRUD de proveedores en la base de datos.
 * @author Luis Alejandro Gómez C.
 * @author Ever Peña B.
 * @version 1.0.0
 */
public class CrudProducto 
{
    /**
     * Conexión con la base de datos.
     */
    private Conexion conexion;

    /**
     * Inicialiaza un CRUD con la tabla productos.
     * @param con Conexión con la base de datos.
     */
    public CrudProducto(Conexion con) 
    {
        this.conexion = con;
    }
    
    /**
     * Crea un nuevo producto en la base de datos, por ejemplo: aguardiente, ron, etc.
     * @param p Producto que se desea agregar a la base de datos.
     * @param idCategoria Llave primaria de la categoria del producto.
     * @return Si se pudo o no agregar el producto a la base de datos.
     */
    public boolean crearProducto(Producto p, int idCategoria)
    {
        if(p == null)
            return false;
        
        boolean bandera = true;        
        String nombre = p.getNombre();        
        String consulta = "INSERT INTO producto (nombre, codigo_categoria) VALUES (?, ?)";
        
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, nombre);
            ps.setInt(2, idCategoria);
            ps.executeUpdate();
        } catch (SQLException e) 
        {
            bandera = false;
        }
        
        return bandera;        
    }
    
    /**
     * Obtiene el código que le corresponde al producto pasado.
     * @param p Producto del cual se quiere saber el código.
     * @return -99-->No se pudo obtener, else-->Código.
     */
    public int getCodeFromProduct(Producto p)
    {
        if(p == null)
            return -99;
        
        int code = -99;
        String nombre = p.getNombre();
        String consulta = "SELECT * FROM producto WHERE nombre = ?";
        
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery(); 
            while(rs.next())
            {
                code = rs.getInt("codigo");
            }            
        } catch (SQLException e) 
        {
            return -99;
        }
        
        return code;
    }
    
    /**
     * Obtiene un producto dado su nombre.
     * @param name Nombre del producto buscado.
     * @return null-->no esta registrado, else-->Producto.
     */
    public Producto getProductFromName(String name)
    {
        if(name.equals(""))
            return null;
        
        Producto p = null;
        int code;
        String catNam;
        
        String consulta = "SELECT p.codigo AS code, c.nombre AS category FROM producto AS p "
                + "INNER JOIN categoria AS c ON p.codigo_categoria = c.codigo WHERE p.nombre = ?";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery(); 
            while(rs.next())
            {
                code = rs.getInt("code");
                catNam = rs.getString("category");
                p = new Producto(code, name, catNam);
            }             
        } catch (SQLException e) 
        {
            return null;
        }
        
        return p;
    }
            
    
}
