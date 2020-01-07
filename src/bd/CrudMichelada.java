package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import logica.bd.Categoria;

/**
 * FECHA ==> 2020-01-06.
 * Permite realizar el CRUD con el valor de las cervezas micheladas del negocio.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0.
 */
public class CrudMichelada 
{
    /**
     * Conexión con la base de datos.
     */
    private Conexion conexion;

    /**
     * Inicializa un CRUD con la tabla michelada.
     * @param con Conexión con la base de datos.
     */
    public CrudMichelada(Conexion con) 
    {
        this.conexion = con;
    }
    
    /**
     * Obtiene el precio de las cervezas micheladas del negocio.
     * @return Precio de las cervezas micheladas.
     */
    public double readPrice()
    {        
        String consulta = "SELECT * FROM michelada WHERE id = 1";
        double price = -1;
        
        try 
        {
            ResultSet rs = this.conexion.getStatement().executeQuery(consulta);          
            while(rs.next())
            {
                price = rs.getDouble("valor");                 
            }                                  
        } catch (SQLException e) 
        {
            return -1;
        }
        
        return price;
    }
    
    /**
     * Actualiaza el precio de las cervezas micheladas.
     * @param price El nuevo precio que se le quiere dar a las micheladas.
     * @return True=>Se modificó la categoria. False=>No se modificó.
     */
    public boolean updatePrice(double price)
    {
        if(price < 0)
            return false;
        
        boolean bandera = true;
        String consulta = "UPDATE michelada SET valor = ? WHERE id = 1";
        try 
        {
            PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
            ps.setDouble(1, price);
            ps.executeUpdate(); 
        } catch (SQLException e) 
        {
            return false;
        }
        return bandera;
    }
}
