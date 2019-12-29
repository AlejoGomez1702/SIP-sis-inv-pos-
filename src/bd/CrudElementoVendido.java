package bd;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import logica.Compra;
import logica.Elemento;
import logica.Venta;

/**
 *
 * @author Alejo
 */
public class CrudElementoVendido 
{    
    /**
     * Conexión con la base de datos.
     */
    private Conexion conexion;

    public CrudElementoVendido(Conexion con) 
    {
        this.conexion = con;
    }
        
    public boolean insertElementsFromSale(Venta sale)
    {
        if(sale == null)
            return false;
        
        boolean bandera = true;
        boolean fuera = sale.isFuera();
        int idPurchase = sale.getId();
        ArrayList<Elemento> elements = sale.getElementos();
        int numElem = elements.size();
        //Datos que se registraran de los elementos.
        Elemento elem;
        String codeElem;
        int cant;
        double value;
        String consulta = "INSERT INTO elemento_vendido (codigo_elemento, id_venta,"
                + " cantidad, precio_total) VALUES (?, ?, ?, ?)";
        
        for(int i = 0; i < numElem; i++) 
        {     
            elem = elements.get(i);
            codeElem = elem.getCodigo();
            cant = elem.getCantidadSale();
            if(fuera)
                value = cant*elem.getPrecioVentaFuera(); 
            else
                value = cant*elem.getPrecioVenta(); 
            
            try 
            {
                PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
                ps.setString(1, codeElem);
                ps.setInt(2, idPurchase);
                ps.setInt(3, cant);
                ps.setDouble(4, value);
                ps.executeUpdate();
            } catch (SQLException e) 
            {
                bandera = false;
            }                                   
        }
        
        return bandera;        
    }
    
    
    public boolean modifyInventorySale(ArrayList<Elemento> existentes, 
                                                ArrayList<Elemento> agregados)
    {
        if(existentes.isEmpty() || agregados.isEmpty())
            return false;
        
        int numAgregados = agregados.size();
        //Datos de los elementos de la compra.
        Elemento elAdd;
        String codeAdd;
        
        int numExistentes = existentes.size();
        //Datos de los elementos existentes en el inventario.
        Elemento elInv;
        String codeInv;
        
        boolean bandera = true;
        int cantAct;
        String consulta = "UPDATE elemento SET cantidad_actual = ? WHERE codigo = ?";
                
        for(int i = 0; i < numAgregados; i++) 
        {
            elAdd = agregados.get(i);
            codeAdd = elAdd.getCodigo();
            for(int j = 0; j < numExistentes; j++) 
            {
                elInv = existentes.get(j);
                codeInv = elInv.getCodigo();
                
                if(codeAdd.equals(codeInv+""))
                {
                    cantAct = elInv.getCantidadActual() - elAdd.getCantidadSale();
                    try 
                    {
                        PreparedStatement ps = this.conexion.getConexion().prepareStatement(consulta);
                        ps.setInt(1, cantAct);
                        ps.setString(2, elInv.getCodigo());
                        ps.executeUpdate();
                        elInv.setCantidadActual(cantAct);
                    } catch (SQLException e) 
                    {
                        bandera = false;
                    }
                    break;
                }                
            }            
        }
        
        return bandera;        
    }
    
    
}
