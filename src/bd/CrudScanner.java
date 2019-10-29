package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import logica.Elemento;

/**
 * FECHA ==> 2019-06-01.
 * Permite la gestion en la base de datos de la tabla "mapeo", la cual
 * se encarga de registrar los códigos de barras de los productos
 * registrados en el negocio.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0
 */
public class CrudScanner 
{
    /**
     * Conexión con la base de datos.
     */
    private Conexion conexion;

    /**
     * Inicialiaza la posibilidad de consultas con la tabla "mapeo".
     * @param con Conexión con la base de datos.
     */
    public CrudScanner(Conexion con) 
    {
        this.conexion = con;
    }
    
    /**
     * Crea un nuevo registro de un producto con su código de barras en la BD.
     * @param codeScan Código que lee el scanner.
     * @param elem Producto al que hace referencia el código
     * @return True==>Se Creo correctamente el registro, else =>False.
     */
    public boolean createMap(String codeScan, Elemento elem)
    {
        if(codeScan == null || elem == null || codeScan.equals(""))
            return false;
        
        boolean bandera = true;
        String codeElem = elem.getCodigo();
        String consulta = "INSERT INTO mapeo (codigo_elemento, codigo_scanner) "
                + "VALUES (?, ?)";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1, codeElem);
            ps.setString(2, codeScan);
            ps.executeUpdate();            
        } catch (SQLException e) 
        {
            bandera = false;
        }    

        return bandera;        
    }
    
    /**
     * Elimina un registro de código de barras.
     * @param codePro Código del producto mapeado.
     * @param codeSca Código de barras leido por el scanner.
     * @return True==>Eliminó, else==>False.
     */
    public boolean deleteMap(String codePro, String codeSca)
    {
        if(codePro.equals("") || codeSca.equals(""))
            return false;
        
        boolean bandera = true;
        String consulta = "DELETE FROM mapeo WHERE codigo_elemento = ? AND codigo_scanner = ?";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setString(1,  codePro);
            ps.setString(2,  codeSca);
            ps.executeUpdate(); 
        } catch (SQLException e) 
        {
            return false;
        }
        
        return bandera;        
    }
    
    /**
     * Obtiene todos los elementos registrados en el negocio con código de barras.
     * @return Elementos con código de barras registrados en el negocio.
     */
    public HashMap getAllMaps()
    {
        HashMap map = new HashMap();
        String codSca;
        String codProd;
        
        String consulta = "SELECT * FROM mapeo";
        try 
        {
            ResultSet rs = this.conexion.getStatement().executeQuery(consulta);
            while(rs.next())
            {
                codSca = rs.getString("codigo_elemento");
                codProd = rs.getString("codigo_scanner");
                map.put(codSca, codProd);
            }            
        } catch (SQLException e) 
        {
            return null;
        }
        
        return map;
    }
                
}
