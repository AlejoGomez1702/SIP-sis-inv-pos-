package controladores;

import javax.swing.JOptionPane;
import logica.Tequilazo;

/**
 * FECHA ==> 2019-12-18.
 * Permite la gestión del CRUD de productos en el inventario.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0
 */
public class ProductoController 
{
    /**
     * Permite la gestión del sistema.
     */
    private Tequilazo tequilazo;

    /**
     * Inicia el controlador de productos junto con el gestor principal de este.
     * @param tequilazo Gestor principal del sistema.
     */
    public ProductoController(Tequilazo tequilazo) 
    {
        this.tequilazo = tequilazo;
    }
    
    /**
     * Permite la modificación de un producto del inventario.
     * @param code Código del producto que se desea modificar.
     * @return 
     */
    public int updateProduct(String code)
    {
        String password = "pensivania";
        if(code == null) 
            return 1;   //No seleccionó ningún elemento.
        
        String passInput = JOptionPane.showInputDialog(null, "Ingrese La Contraseña: ");
        if(passInput == null)
            return 0;   //Se canceló la modificación del producto
        
        if(password.equals(passInput))
        {
            System.out.println("SONNNN IGUUUALLLLESSS");
        }
        else
        {
            System.out.println("NO SONNN IGUALLLESSS: " + passInput);
        }
        
        
        return 0;
    }
    
}
