package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import logica.bd.Categoria;

/**
 * FECHA ==> 2019-06-04.
 * Permitira realizar el CRUD de categorias en la base de datos.
 * @author Luis Alejandro Gómez C.
 * @version 1.0 .
 */
public class CrudCategoria 
{
    /**
     * Conexión con la base de datos.
     */
    private Conexion conexion;
    
    /**
     * Inicializa un CRUD con la tabla categoria.
     * @param con Conexión con la base de datos.
     */
    public CrudCategoria(Conexion con)
    {
        this.conexion = con;        
    }
    
    /**
     * Crea una nueva categoria de productos en la base de datos.
     * @param category Categoria que se desea añadir a la base de datos.
     * @return True=>Se pudo crear la categoria, False=>No se pudo crear.
     */
    public boolean crearCategoria(Categoria category)
    {        
        String nombre = category.getNombre();
        String desc = category.getDescripcion();
        
        if(nombre.equals(""))
            return false;
        
        boolean bandera = true;
        String consulta = "INSERT INTO categoria (nombre, descripcion)"
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
    
    /**
     * Busca una categoria por su código en la base de datos.
     * @param nombre nombre con el cual se identifica la categoria.
     * @return Null=>No encuentra categoria, de lo contrario=>Categoria.
     */
    public Categoria leerCategoriaPorNombre(String nombre)
    {
        if(nombre.equals(""))
        {
            return null;
        }
        
        String consulta = "SELECT * FROM categoria WHERE nombre = ?";
        Categoria cate = null;
        int cod;
        String descripcion;
        
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, nombre);           
            ResultSet rs = ps.executeQuery();            
            while(rs.next())
            {
                cod = rs.getInt("codigo");                
                descripcion = rs.getString("descripcion");
                cate = new Categoria(cod, nombre, descripcion); 
            }                                  
        } catch (SQLException e) 
        {
            return null;
        }
        
        return cate;
    }
    
    /**
     * Actualiaza la información de una categoria.
     * @param oldCate Categoria que se quiere actualizar.
     * @param newCate Nueva categoria que reemplazara los datos de la anterior.
     * @return True=>Se modificó la categoria. False=>No se modificó.
     */
    public boolean actualizarCategoria(Categoria oldCate, Categoria newCate)
    {
        if(oldCate == null || newCate == null)
            return false;
        
        boolean bandera = true;
        int oldCode = oldCate.getCodigo();        
        //int code = newCate.getCodigo();
        String nomb = newCate.getNombre();
        String desc = newCate.getDescripcion();
        String consulta = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE codigo = ? ";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, nomb);
            ps.setString(2, desc);
            ps.setInt(3, oldCode);
            ps.executeUpdate(); 
        } catch (SQLException e) 
        {
            return false;
        }
        return bandera;
    }
    
    /**
     * Elimina una categoria que coincida con el código pasado.
     * @param cat Categoria que se desea eliminar de la base de datos.
     * @return True=>Se eliminó la categoria. False=>No se pudo eliminar.
     */
    public boolean eliminarCategoria(Categoria cat)
    {
        if(cat == null)
            return false;
        
        boolean bandera = true;
        int codigo = cat.getCodigo();
        String consulta = "DELETE FROM categoria WHERE codigo = ?";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setInt(1, codigo);
            ps.executeUpdate(); 
        } catch (SQLException e) 
        {
            return false;
        }
        
        return bandera;
    }
    
    /**
     * Obtiene la lista de todos las categorias registradas en la base de datos,
     * la utilizo para mapear los datos iniciales.
     * @return Lista con las categorias de la base de datos.
     */
    public ArrayList<Categoria> obtenerCategorias()
    {
        ArrayList<Categoria> categorias = new ArrayList<>();
        
        String consulta = "SELECT * FROM categoria";
        Categoria cate = null;
        int cod;
        String nombre;
        String desc;
        
        try 
        {
            ResultSet rs = this.conexion.getStatement().executeQuery(consulta);
            while(rs.next())
            {
                cod = rs.getInt("codigo");
                nombre = rs.getString("nombre");
                desc = rs.getString("descripcion");
                cate = new Categoria(cod, nombre, desc);
                categorias.add(cate);
            }            
        } catch (SQLException e) 
        {
            return null;
        }
        
        return categorias;
    }
    
}
