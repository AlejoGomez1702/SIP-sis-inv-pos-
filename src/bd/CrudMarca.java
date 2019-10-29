package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import logica.bd.Marca;

/**
 * FECHA ==> 2019-06-07.
 * Permitira realizar el CRUD de marcas de productos en la base de datos.
 * @author Luis Alejandro Gómez C.
 * @version 1.0 .
 */
public class CrudMarca 
{
    /**
     * Conexión con la base de datos.
     */
    private Conexion conexion;

    /**
     * Inicializa un CRUD con la tabla marca de productos.
     * @param con Conexión con la base de datos.
     */
    public CrudMarca(Conexion con) 
    {
        this.conexion = con;
    }
    
    /**
     * Crea una nueva marca de productos en la base de datos.
     * @param marca Marca que se desea agregar a la base de datos.
     * @return True=>Se pudo crear la marca, False=>No se pudo crear.
     */
    public boolean crearMarca(Marca marca)
    {        
        if(marca == null)
            return false;
        
        boolean bandera = true;
        //int codigo = marca.getCodigo();
        String nombre = marca.getNombre();
        String desc = marca.getDescripcion();                
        
        String consulta = "INSERT INTO marca (nombre, descripcion)"
                + " VALUES (?, ?)";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, nombre);
            ps.setString(2, desc);
            ps.executeUpdate();            
        } catch (SQLException e) 
        {
            bandera = false;
        }                                
        
        return bandera;  
    }
    
    public int getCodeFromMark(String markName)
    {
        if(markName.equals(""))
            return -99;
        
        int code = -99;
        String consulta = "SELECT * FROM marca WHERE nombre = ?";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, markName);
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
     * Busca una marca por su código en la base de datos.
     * @param codigo Llave primaria con la que se identifica una marca.
     * @return Null=>No encuentra la marca, de lo contrario=>Marca.
     */
    public Marca leerMarca(String codigo)
    {
        if(codigo.equals(""))
        {
            return null;
        }
        
        String consulta = "SELECT * FROM marca";
        Marca marca = null;
        int cod;
        String nombre;
        String region;
        
        try 
        {
            ResultSet rs = this.conexion.getStatement().executeQuery(consulta);
            while(rs.next())
            {
                cod = rs.getInt("codigo");
                //Si el código del proveedor buscado es encontrado.
                if(codigo.equals(cod))
                {
                    nombre = rs.getString("nombre");
                    region = rs.getString("region");
                    
                    marca = new Marca(cod, nombre, region);
                    break;
                }
            }            
        } catch (SQLException e) 
        {
            return null;
        }
        
        return marca;
    }
    
    /**
     * Actualiaza la información de una marca de producto.
     * @param oldMark Marca que se desea actualizar su información.
     * @param newMark Contiene los datos con los que se actualizará la marca.
     * @return True=>Se modificó la marca. False=>No se modificó.
     */
    public boolean actualizarMarca(Marca oldMark, Marca newMark)
    {
        if(oldMark == null || newMark == null)
            return false;
        
        boolean bandera = true;
        int cod = oldMark.getCodigo();
        String nomb = newMark.getNombre();
        String desc = newMark.getDescripcion();
        String consulta = "UPDATE marca SET nombre = ?, descripcion = ? WHERE codigo = ? ";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, nomb);
            ps.setString(2, desc);
            ps.setInt(3, cod);
            ps.executeUpdate(); 
        } catch (SQLException e) 
        {
            return false;
        }
        return bandera;
    }
    
    /**
     * Elimina una marca que coincida con el código pasado.
     * @param mark Marca que se desea eliminar de la base de datos.
     * @return True=>Se eliminó la marca. False=>No se pudo eliminar.
     */
    public boolean eliminarMarca(Marca mark)
    {
        if(mark == null)
            return false;
        
        int codigo = mark.getCodigo();
        boolean bandera = true;
        String consulta = "DELETE FROM marca WHERE codigo = ?";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setInt(1,  codigo);
            ps.executeUpdate(); 
        } catch (SQLException e) 
        {
            return false;
        }
        
        return bandera;
    }
    
    /**
     * Obtiene la lista de todas las marcas registradas en la base de datos,
     * la utilizo para mapear los datos iniciales.
     * @return Lista con las marcas de la base de datos.
     */
    public ArrayList<Marca> obtenerMarcas()
    {
        ArrayList<Marca> marcas = new ArrayList<>();
        
        String consulta = "SELECT * FROM marca";
        Marca marca = null;
        int cod;
        String nombre;
        String des;
        
        try 
        {
            ResultSet rs = this.conexion.getStatement().executeQuery(consulta);
            while(rs.next())
            {
                cod = rs.getInt("codigo");
                nombre = rs.getString("nombre");
                des = rs.getString("descripcion");
                marca = new Marca(cod, nombre, des);
                marcas.add(marca);
            }            
        } catch (SQLException e) 
        {
            return null;
        }
        
        return marcas;
    }
    
}
