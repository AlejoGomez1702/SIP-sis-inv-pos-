package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import logica.bd.UnidadMedida;

/**
 * FECHA ==> 2019-06-07.
 * Permitira realizar el CRUD de unidades de medida de productos en la base de datos.
 * @author Luis Alejandro Gómez C.
 * @version 1.0 .
 */
public class CrudUnidadMedida 
{
    /**
     * Conexión con la base de datos.
     */
    private Conexion conexion;

    /**
     * Inicializa un CRUD con la tabla unidad_medida de productos.
     * @param con Conexión con la base de datos.
     */
    public CrudUnidadMedida(Conexion con) 
    {
        this.conexion = con;
    }
    
    /**
     * Crea una nueva unidad de medida para productos en la base de datos.
     * @param unMed Unidad de medida que se desea agregar al negocio.
     * @return True=>Se pudo crear la nueva medida, False=>No se pudo crear.
     */
    public boolean crearUnidadMedida(UnidadMedida unMed)
    {        
        if(unMed == null)
            return false;
        
        String nombre = unMed.getNombre();
        String descripcion = unMed.getDescripcion();
        boolean bandera = true;
        String consulta = "INSERT INTO unidad_medida (nombre, descripcion)"
                + " VALUES (?, ?)";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.executeUpdate();            
        } catch (SQLException e) 
        {
            bandera = false;
        }                                
        
        return bandera;  
    }
    
    /**
     * Busca una unidad de medida por su código en la base de datos.
     * @param codigo Llave primaria con la que se identifica la unidad de medida.
     * @return Null=>No encuentra la medida, de lo contrario=>UnidadMedida.
     */
    public UnidadMedida leerUnidadMedida(String codigo)
    {
        if(codigo.equals(""))
        {
            return null;
        }
        
        String consulta = "SELECT * FROM unidad_medida";
        UnidadMedida um = null;
        int cod;
        String nombre;
        String desc;
        
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
                    desc = rs.getString("descripcion");
                    
                    um = new UnidadMedida(cod, nombre, desc);
                    break;
                }
            }            
        } catch (SQLException e) 
        {
            return null;
        }
        
        return um;
    }
    
    public int getCodeFromUnit(String unitName)
    {
        if(unitName.equals(""))
            return -99;
        
        int code = -99;
        String consulta = "SELECT * FROM unidad_medida WHERE nombre = ?";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, unitName);
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
     * Actualiaza la información de una unidad de medida de productos.
     * @param oldUm Unidad de medida que se va actualizar con los datos nuevos.
     * @param newUm Unidad de medida que sustituirá los datos de la anterior.
     * @return True=>Se modificó la medida. False=>No se modificó.
     */
    public boolean actualizarUnidad(UnidadMedida oldUm, UnidadMedida newUm)
    {
        if(oldUm == null || newUm == null)
            return false;
        
        boolean bandera = true;
        int cod = oldUm.getCodigo();
        String nomb = newUm.getNombre();
        String desc = newUm.getDescripcion();
        String consulta = "UPDATE unidad_medida SET nombre = ?, descripcion = ? WHERE codigo = ? ";
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
     * Elimina una unidad de medida que coincida con el código pasado.
     * @param um Unidad de medida que se desea eliminar.
     * @return True=>Se eliminó la unidad. False=>No se pudo eliminar.
     */
    public boolean eliminarUnidadMedida(UnidadMedida um)
    {
        if(um == null)
            return false;
        
        boolean bandera = true;
        int codigo = um.getCodigo();
        String consulta = "DELETE FROM unidad_medida WHERE codigo = ?";
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
     * Obtiene la lista de todas las unidades de medidas registradas en la base de datos,
     * la utilizo para mapear los datos iniciales.
     * @return Lista con las unidades de medida de la base de datos.
     */
    public ArrayList<UnidadMedida> obtenerUnidadesMedidas()
    {
        ArrayList<UnidadMedida> uMedidas = new ArrayList<>();
        
        String consulta = "SELECT * FROM unidad_medida";
        UnidadMedida um = null;
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
                um = new UnidadMedida(cod, nombre, desc);
                uMedidas.add(um);
            }
            
        } catch (SQLException e) 
        {
            return null;
        }
        
        return uMedidas;
    }
    
}
