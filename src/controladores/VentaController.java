package controladores;

import java.util.ArrayList;
import logica.Elemento;
import logica.Tequilazo;

/**
 * FECHA ==> 2019-09-05.
 * Controlador para la nuevas ventas del negocio.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0
 */
public class VentaController 
{
    /**
     * Permite la manipulación del sistema.
     */
    private Tequilazo tequilazo;
    
    /**
     * Lista con los productos de una venta rápida.
     */
    private ArrayList<Elemento> elementsSale; 
    
    /**
     * Crea el controlador para la manipulación de las ventas.
     * @param tequilazo Permite la gestion del sistema.
     */
    public VentaController(Tequilazo tequilazo) 
    {
        this.elementsSale = new ArrayList<>();
        this.tequilazo = tequilazo;
    }
    
    /**
     * 
     * @param code 
     */
    public void addProduct(String code)
    {
        Elemento element = null;
        element = this.tequilazo.getBd().getCrudElemento().getElementFromCode(code);
        if(element != null)
        {
            Elemento band = this.verifyElement(code);
            if(band != null)//Si el producto ya habia sido escaneado
            {
                band.setCantidadSale(band.getCantidadSale() + 1);
            }
            else //Si el producto se va agregar por primera vez a la venta.
            {
                element.setCantidadSale(1);
                this.elementsSale.add(element);
            }            
        }       
        
    }
    
    public Elemento verifyElement(String code)
    {
        int numElements = this.elementsSale.size();
        String codeAux;
        for(int i = 0; i < numElements; i++) 
        {
            codeAux = this.elementsSale.get(i).getCodigo();
            if(code.equals(codeAux+""))
                return this.elementsSale.get(i);            
        }
                
        return null;
    }

    public ArrayList<Elemento> getElementsSale() {
        return elementsSale;
    }
        
    
    
}
