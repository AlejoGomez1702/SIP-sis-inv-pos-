package controladores;

import dialogos.CantidadProducto;
import logica.Elemento;
import logica.Tequilazo;

/**
 * FECHA ==> 2019-07-26.
 * Controlador de la interfaz de menudear producto.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0
 */
public class MenudearController 
{
    /**
     * Permite la gestion del sistema.
     */
    private Tequilazo tequilazo;
    
    /**
     * Inicializa el controlador de la interfaz de menudear un producto.
     * @param tequilazo Permite la gestion del sistema.
     */
    public MenudearController(Tequilazo tequilazo)
    {
        this.tequilazo = tequilazo;
    }
    
    /**
     * Permite realizar la gestion necesaria para menudear un producto.
     * @param codePadre Código del producto que se desea menudear.
     * @param codeHijo Código del producto que simboliza la unidad.
     * @return Tipo de mensaje que se debe mostrar dependiendo el proceso.
     * 0 ==> El producto se menudeo correctamente.
     * 1 ==> Debe seleccionar los dos productos involucrados en el proceso.
     * 2 ==> Error consultando los productos en la base de datos.
     * 3 ==> Error modificando la cantidad en la base de datos.
     * 4 ==> Debe seleccionar la cantidad que contiene el producto.
     * 5 ==> Debe seleccionar el producto correcto de menudeo(trago, cigarrilo).
     */
    public int menudearProducto(String codePadre, String codeHijo)
    {
        int mensaje = 0;
        Elemento elPadre;
        Elemento elHijo;
        if(codePadre != null && codeHijo != null)
        {
            elPadre = this.tequilazo.getBd().getCrudElemento().getElementFromCode(codePadre);
            elHijo = this.tequilazo.getBd().getCrudElemento().getElementFromCode(codeHijo);
            if(elPadre != null && elHijo != null)
            {
                //Cuantas/os h.unidad tiene el/la p.nombre p.marca p.unidad
                String title = "Cuantas/os " + elHijo.getUnidadMedida().toUpperCase()
                        + " Tiene el/la " + elPadre.getProducto().getNombre().toUpperCase()
                        + " " + elPadre.getMarca().toUpperCase() + " X " + 
                        elPadre.getUnidadMedida().toUpperCase();                
                //Lanzo el dialogo que permite ingresar la cantidad del producto.
                CantidadProducto cp = new CantidadProducto(null, true, title);
                cp.setVisible(true);
                int cantidad = cp.getCantidadSeleccionada();
                //System.out.println("Cantidad seleccionada: " + cantidad);
                //Si se esta seleccionando una unidad de medida erronea.
                if(elHijo.getUnidadMedida().equals(elPadre.getUnidadMedida()))
                    return 5;
                
                if(cantidad > 0)
                {
                    int cantHijo = elHijo.getCantidadActual() + cantidad;
                    int cantPadre = elPadre.getCantidadActual() - 1;
                    //Modifico la base de datos                     
                    boolean h = this.tequilazo.getBd().getCrudElemento()
                            .updateCountElement(elHijo, cantHijo);
                    //System.out.println("cantidad: " + (elHijo.getCantidadActual()+cantidad));
                    boolean p = this.tequilazo.getBd().getCrudElemento()
                            .updateCountElement(elPadre, cantPadre);
                    if(!h || !p)
                        return 3;
                    else
                    {
                        Elemento father = this.tequilazo.getElementFromCode(elPadre.getCodigo());
                        Elemento child = this.tequilazo.getElementFromCode(elHijo.getCodigo());
                        if(father != null && child != null)
                        {
                            father.setCantidadActual(cantPadre);
                            child.setCantidadActual(cantHijo);
                        }
                    }
                }
                else
                    return 4;
            }
            else
                return 2;
        }
        else
            return 1;
        
        return mensaje;
    }   
    
}
