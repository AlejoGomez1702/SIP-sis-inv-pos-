package controladores;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import logica.Cliente;
import logica.Elemento;
import logica.Tequilazo;
import logica.Venta;
import logica.gestores.Factura;

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
        this.tequilazo = tequilazo;
        this.elementsSale = new ArrayList<>();
    }
    
    /**
     * Permite la creación de una nueva venta en el negocio.
     * @param num Número de factura que se desea crear.
     * @param fecha Fecha en la cual se esta realizando la venta.
     * @param obs Observaciones que tiene la venta.
     * @param cliente Cliente al cual se le esta registrando la venta.
     * @param fuera Indica si es una venta por fuera o dentro del negocio.
     * @param total Valor total de la venta.
     * @param products Productos que estan involucrados en la venta.
     * @return Número que indica que mensaje se desea mostrar al usuario.
     */
    public int createSale(int num, String fecha, String obs, Cliente cliente,
                        boolean fuera, double total, ArrayList<Elemento> products)
    {
        Venta venta;
        if(num == -1)
            return 1;   //Error con el número de la venta.
        
        if(fecha.equals("Error"))
            return 2;   //Error con la fecha de la venta.
        
        if(total == -1)
            return 3;   //Error con el valor total de la venta.
        
        venta = new Venta(num, cliente, fecha, obs, fuera, total);
        if(!products.isEmpty()) //si no se han agregado productos aún.
        {
            ArrayList<Elemento> newElements = new ArrayList<>();
            newElements.addAll(products);
            venta.setElementos(newElements);
            boolean add = this.tequilazo.addSale(venta);
            if(add)
            {
                //Modifico el inventario, dismuniyo la cantidad a los productos vendido.
                boolean ban = this.tequilazo.getBd().getCrudElementoVendido().modifyInventorySale
                            (this.tequilazo.getInventario().getComponentes(), products);
                if(ban)
                {
                    this.tequilazo.getVentas().add(venta);
                    cliente = new Cliente("","","");
                    //Antes de mostrar la confirmación de la venta, imprimir factura.
                    boolean imprimio = this.imprimirFactura(venta, 'p');
                    if(imprimio)
                    {
                        this.imprimirFactura(venta, 'c');
                        return 10;  //Se realizó la venta correctamente.
                    }                        
                    else
                    {
                        PrinterJob pj = PrinterJob.getPrinterJob();
                        Factura fac = new Factura('c', null);
                        fac.takeOutBox(pj);        
                        return 10;   //Se realizó la venta correctamente.
                    }                    
                }
                else
                    return 5;  //No se pudo disminuir la cantidad del inventario.
            }
            else
                return 4;  //No se pudo agregar la venta al negocio.                    
        }
        
        return 0;  //No se han agregado productos a la venta.
    }
    
    /**
     * Permite la gestión para imprimir la factura de la venta.
     * @param vent Venta que se desea imprimir.
     * @param cual Permite decidir si es la factura principal('p') o copia('c').
     * @return Indica si se salta la impresion de la copia.
     */
    public boolean imprimirFactura(Venta vent, char cual)
    {
        boolean bandera = false;
        String msj = "";        
        if(cual == 'p')
            msj = "Desea Imprimir Factura?";
        if(cual == 'c')
            msj = "Desea Imprimir Copia?";             
        
        int opc = JOptionPane.showConfirmDialog(null, msj,
                "Imprimir Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        //0 para si || 1 para no
        if(opc == 0)
        {
            bandera = true;
            PrinterJob pj = PrinterJob.getPrinterJob();
            Factura fac = new Factura('v', vent);
            pj.setPrintable(fac,fac.getPageFormat(pj));
            try 
            {
                pj.print();
            } catch (PrinterException e) 
            {
                JOptionPane.showMessageDialog(null, "Error Con La Impresión");
            }
        }
        
        return bandera;
    }
    
    /**
     * Agrega un producto de manera ágil con el código de barras a la venta.
     * @param code Código del producto que se desea añadir.
     */
    public void addProduct(String code)
    {
        Elemento element;
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
        else
            JOptionPane.showMessageDialog(null, "jajajajja");
        
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
    
    public ArrayList<Elemento> getElementsSale() 
    {
        return elementsSale;
    } 
           
}
