package bd;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import logica.Compra;
import logica.Elemento;

/**
 * FECHA ==> 2019-07-29.
 * Permitira realizar el CRUD de "elemento_comprado" en la base de datos.
 * @author Luis Alejandro Gómez C.
 * @author Ever Peña B.
 * @version 1.0.0
 */
public class CrudElementoComprado 
{
    /**
     * Conexión con la base de datos.
     */
    private Conexion conexion;

    /**
     * Inicialiaza un CRUD con la tabla elemento_comprado(Entidad Débil).
     * @param con Conexión con la base de datos.
     */
    public CrudElementoComprado(Conexion con) 
    {
        this.conexion = con;
    }
    
    /**
     * Inserta en la entidad débil los productos registrados en una compra.
     * @param purchase Compra de la cual se desea registrar sus elementos.
     * @return True-->Inserto elementos, else-->False.
     */
    public boolean insertElementsFromPurchase(Compra purchase)
    {
        if(purchase == null)
            return false;
        
        boolean bandera = true;
        int idPurchase = purchase.getId();
        ArrayList<Elemento> elements = purchase.getElementos();
        int numElem = elements.size();
        //Datos que se registraran de los elementos.
        Elemento elem;
        String codeElem;
        int cant;
        double value;
        String consulta = "INSERT INTO elemento_comprado (codigo_elemento, id_compra,"
                + " cantidad, precio_total) VALUES (?, ?, ?, ?)";
        
        for(int i = 0; i < numElem; i++) 
        {     
            elem = elements.get(i);
            codeElem = elem.getCodigo();
            cant = elem.getCantidadSale();
            value = cant*elem.getPrecioCompra(); 
            
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
    
    /**
     * Modifica el inventario después de realizada una compra.
     * @param existentes Productos existentes en el inventario.
     * @param agregados Productos de la compra.
     * @return True-->Se Modificó, else-->False.
     */
    public boolean modifyInventoryPurchase(ArrayList<Elemento> existentes, ArrayList<Elemento> agregados)
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
                    cantAct = elInv.getCantidadActual() + elAdd.getCantidadSale();
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
