package logica;

import java.util.ArrayList;

/**
 * FECHA ==> 2019-07-24.
 * Inventario de productos que tiene el negocio.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0.
 */
public class Inventario
{
    /**
     * Listado de productos que tiene el negocio.
     */
    private ArrayList<Elemento> componentes;
    
    /**
     * Crea el inventario del negocio con sus respectivos productos.
     * @param componentes Listado de productos que contiene el negocio.
     */
    public Inventario(ArrayList<Elemento> componentes) 
    {        
        this.componentes = componentes;
    }
   
    /**
     * Obtiene la lista de productos que tiene el inventario del negocio.
     * @return Lista de productos inventariados.
     */
    public ArrayList<Elemento> getComponentes()
    {
        return componentes;
    }
    
    public void agregarComponente(Elemento compo)
    {
        this.componentes.add(compo);
//        if(componentes.contains(compo))//si en el inventario existe ese componente
//        {
//        //agrega los componentes de esa compra a los componentes del inventario
//            for (int i = 0; i < componentes.size(); i++)
//            {
//                Componente componente=componentes.get(i);//saca cada componente de la compra
//                if(componente==compo)
//                {
//                    //componente.modificarCantidadActual(compo.getCantidadActual());
//                }     
//            }  
//        }
//        else if(compo!=null)
//        {
//            componentes.add(compo);
//        } 
    }
    
    /**
     * Elimina un componente del inventario por su respectivo código.
     * @param code Código del componente que se desea eliminar.
     * @return True-->Se eliminó, False-->No se pudo eliminar.
     */
    public boolean deleteComponent(String code)
    {
        boolean bandera = false;
        int numCom = this.componentes.size();
        Elemento el;
        String aux;
        for(int i = 0; i < numCom; i++) 
        {
            el = this.componentes.get(i);
            aux = el.getCodigo();
            if(code.equals(aux))
            {
                bandera = true;
                this.componentes.remove(i);
                break;
            }            
        }
        
        return bandera;        
    }
    
    /**
     * Actualiza un elemento del inventario.
     * @param oldElement Elemento que se desea actualizar.
     * @param newElement Elemento que sustituirá los datos del anterior producto.
     * @return True-->Se modificó, False-->No se modificó.
     */
    public boolean updateComponent(Elemento oldElement, Elemento newElement)
    {
        if(oldElement == null || newElement == null)
            return false;
        
        boolean bandera = false;
        int numCom = this.componentes.size();
        String oldCode = oldElement.getCodigo();
        String auxCode;
        for(int i = 0; i < numCom; i++) 
        {
            auxCode = this.componentes.get(i).getCodigo();
            if(oldCode.equals(auxCode))
            {
                bandera = true;
                this.componentes.remove(i);
                this.componentes.add(newElement);
                break;
            }            
        }       
                
        return bandera;        
    }
    
    public void eliminarComponente(Elemento componente)
    {
        componentes.remove(componente);
    }

    public void setComponentes(ArrayList<Elemento> componentes) {
        this.componentes = componentes;
    }
    
}
