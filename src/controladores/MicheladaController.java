package controladores;

import dialogos.CantidadProducto;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import logica.Elemento;
import logica.Producto;
import logica.Tequilazo;

/**
 * FECHA ==> 2020-01-04.
 * Controlador para el proceso de micheladas del negocio.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0
 */
public class MicheladaController 
{
    /**
     * Permite la gestión de la mayor parte del sistema.
     */
    private Tequilazo tequilazo;

    /**
     * Crea el controlador junto con el elemento principal de gestión.
     * @param tequilazo Permite la manipulación principal del sistema.
     */
    public MicheladaController(Tequilazo tequilazo) 
    {
        this.tequilazo = tequilazo;
    }
    
    /**
     * Actualiza el precio de las cervezas micheladas del negocio.
     * @return True => Actualizó, False => No se pudo actualizar.
     */
    public boolean updatePrice()
    {
        boolean bandera = false;
        double precio;
        String ingresado = JOptionPane.showInputDialog(null, 
                "Ingrese El Precio Que Le Desea Asignar A Las Micheladas",
                (int) this.tequilazo.getBd().getCrudMichelada().readPrice() + "");
        
        if(ingresado != null)
        {
            try 
            {
                precio = Double.parseDouble(ingresado);
            } catch (NumberFormatException e) 
            {
                return false;
            }

            if(precio > 0)
            {
                boolean modifico = this.tequilazo.getBd().getCrudMichelada()
                                                               .updatePrice(precio);
                if(modifico)
                    return true;
            }
        }

        return bandera;
    }

    /**
     * Permite realizar el proceso de michelar las cervezas.
     * @param code Código del producto que se desea michelar.
     * @param elements Elementos comprometidos, se necesita para llamar
     * al método invocado del tequilazo.
     * @return Código para posteriormente mostrar mensaje al usuario.
     */
    public int michelarCerveza(String code, ArrayList<Elemento> elements)
    {
        if(code == null || elements == null)
            return 1;   //No se seleccionó ningún producto.
        
        CantidadProducto cp = new CantidadProducto(null, true, 
                                  "Seleccione Cuantas Cervezas Desea Michelar");
        cp.setVisible(true);
        
        int paraMichelar = cp.getCantidadSeleccionada();
        //Creando las micheladas que se han vendido.
        Producto p = new Producto(99999999, "Cerveza Michelada", "Licores");
        Elemento el = new Elemento("miche", p, "Michelada", "Unidad", 10, 100,
                        100, this.tequilazo.getVALOR_MICHELADA(), 
                            this.tequilazo.getVALOR_MICHELADA());
        
        boolean aumento = this.aumentarPrecioProducto
                                    (code, elements, paraMichelar, el);
        if(aumento)
            return 10;   //Cervezas micheladas correctamente.
        else
            return 2;   //Seleccionó una cantidad mayor de las agregadas.
    }
    
    /**
     * Aumenta el precio de las cervezas que se desean michelar.
     * @param code Código del producto seleccionado.
     * @param elements Elementos de la lista de resumen venta.
     * @param cantidad Cantidad de cervezas que se desean michelar.
     * @param element Simboliza un elemento michelada del negocio.
     * @return True => se aumento el precio, False => Se ingreso mayor número
     * de lo que se esta vendiendo de producto.
     */
    public boolean aumentarPrecioProducto(String code, ArrayList<Elemento> elements,
                                                    int cantidad, Elemento element)
    {
        if(code == null || elements == null)
            return false;
        boolean bandera = false;
        int num = elements.size();
        Elemento aux;
        String codeAux;
        int cantTotal;
        
        for(int i = 0; i < num; i++)
        {
            aux = elements.get(i);
            codeAux = aux.getCodigo();
            if(code.equals(codeAux))
            {                
                cantTotal = aux.getCantidadSale();
                if(cantidad > cantTotal)
                    return false;
                
                element.setCantidadSale(cantidad);
                elements.add(element);
                
                return true;
            }                        
        }
        
        return bandera;
    }
    
}
