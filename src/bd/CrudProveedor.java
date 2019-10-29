package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import logica.Proveedor;

/**
 * FECHA ==> 2019-06-01.
 * Permite realizar las consultas en la base de datos de la tabla "proveedor",
 * la cual almacena el registro de todos los proveedores de productos 
 * que se venden en el negocio.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0
 */
public class CrudProveedor 
{
    /**
     * Conexión con la base de datos.
     */
    private Conexion conexion;
    
    /**
     * Inicialiaza la posibilidad de consultas con la tabla "proveedor".
     * @param con Conexión con la base de datos.
     */
    public CrudProveedor(Conexion con)
    {
        this.conexion = con;      
    }
        
    /**
     * Registra un nuevo proveedor en la base de datos.
     * @param p Proveedor que se desea registrar en la base de datos.
     * @return True=>Se registro el proveedor False=>No se pudo registrar el proveedor.
     */
    public boolean crearProveedor(Proveedor p)
    {
        String codigo = p.getCodigo();
        String nombre = p.getNombre(); 
        String nit = p.getNIT();
        String celular = p.getTelefono();        
        
        if(codigo.equals("") || nombre.equals("") || nit.equals("") || celular.equals(""))
            return false;        
        
        boolean bandera = true;
        String consulta = "INSERT INTO proveedor (codigo, nombre, nit, celular)"
                + " VALUES (?, ?, ?, ?)"; 
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setString(3, nit);
            ps.setString(4, celular);
            ps.executeUpdate();            
        } catch (SQLException e) 
        {
            bandera = false;
        }                                
        
        return bandera;        
    }
    
    /**
     * Busca un proveedor por su código en la base de datos.
     * @param codigo Llave primaria con la que se identifica un proveedor.
     * @return Null=>No encuentra proveedor, de lo contrario=>Proveedor.
     */
    public Proveedor leerProveedor(String codigo)
    {
        if(codigo.equals(""))
        {
            return null;
        }
        
        String consulta = "SELECT * FROM proveedor";
        Proveedor prov = null;
        String cod;
        String nombre;
        String nit;
        String celular;
        
        try 
        {
            ResultSet rs = this.conexion.getStatement().executeQuery(consulta);
            while(rs.next())
            {
                cod = rs.getString("codigo");
                //Si el código del proveedor buscado es encontrado.
                if(codigo.equals(cod))
                {
                    nombre = rs.getString("nombre");
                    nit = rs.getString("nit");
                    celular = rs.getString("celular");
                    prov = new Proveedor(cod, nombre, nit, celular);
                    break;
                }
            }            
        } catch (SQLException e) 
        {
            return null;
        }
        
        return prov;
    }
    
    /**
     * Busca un proveedor por su nombre en la base de datos.
     * @param nombre nombre con el que se identifica un proveedor.
     * @return Null=>No encuentra proveedor, de lo contrario=>Proveedor.
     */
    public Proveedor getProviderFromName(String nombre)
    {
        if(nombre.equals(""))
        {
            return null;
        }
        
        String consulta = "SELECT * FROM proveedor WHERE nombre = ?";
        Proveedor prov = null;
        String cod;
        String nit;
        String celular;
        
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                cod = rs.getString("codigo");                
                nit = rs.getString("nit");
                celular = rs.getString("celular");
                prov = new Proveedor(cod, nombre, nit, celular);
                break;                
            }            
        } catch (SQLException e) 
        {
            return null;
        }
        
        return prov;
    }    
        
    /**
     * Actualiaza la información de contacto de un proveedor.
     * @param oldProveedor Proveedor al cual se le van a actualizar los datos.
     * @param newProveedor Proveedor que contiene la información para actualizar.
     * @return True=>Se modificó el proveedor. False=>No se modificó.
     */
    public boolean actualizarProveedor(Proveedor oldProveedor, Proveedor newProveedor)
    {
        boolean bandera = true;
        if(oldProveedor == null || newProveedor == null)
            return false;
        
        String oldCode = oldProveedor.getCodigo();
        String cod = newProveedor.getCodigo();
        String nomb = newProveedor.getNombre();
        String nit = newProveedor.getNIT();
        String celu = newProveedor.getTelefono();
        String consulta = "UPDATE proveedor SET codigo = ?, nombre = ?, nit = ?, celular = ? WHERE codigo = ? ";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, cod);
            ps.setString(2, nomb);
            ps.setString(3, nit);
            ps.setString(4, celu);
            ps.setString(5, oldCode);
            ps.executeUpdate(); 
        } catch (SQLException e) 
        {
            return false;
        }
        
        return bandera;
    }
    
    /**
     * Elimina un proveedore que coincida con el código pasado.
     * @param prov Proveedor que se desea eliminar de la base de datos.
     * @return True=>Se eliminó el proveedor. False=>No se pudo eliminar.
     */
    public boolean eliminarProveedor(Proveedor prov)
    {
        if(prov == null)
            return false;
        
        String codigo = prov.getCodigo();
        boolean bandera = true;
        String consulta = "DELETE FROM proveedor WHERE codigo = ?";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1,  codigo);
            ps.executeUpdate(); 
        } catch (SQLException e) 
        {
            return false;
        }
        
        return bandera;
    }
    
    /**
     * Obtiene la lista de todos los proveedores registrados en la base de datos,
     * la utilizo para mapear los datos iniciales.
     * @return Lista con los proveedores de la base de datos.
     */
    public ArrayList<Proveedor> getAllProviders()
    {
        ArrayList<Proveedor> proveedores = new ArrayList<>();
        
        String consulta = "SELECT * FROM proveedor";
        Proveedor prov = null;
        String cod;
        String nombre;
        String nit;
        String celular;        
        try 
        {
            ResultSet rs = this.conexion.getStatement().executeQuery(consulta);
            while(rs.next())
            {
                cod = rs.getString("codigo");
                nombre = rs.getString("nombre");
                nit = rs.getString("nit");
                celular = rs.getString("celular");
                prov = new Proveedor(cod, nombre, nit, celular);
                proveedores.add(prov);
            }
            
        } catch (SQLException e) 
        {
            return null;
        }
        
        return proveedores;
    }
    
}
