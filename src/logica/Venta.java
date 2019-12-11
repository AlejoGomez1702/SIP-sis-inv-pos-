
package logica;
import java.util.ArrayList;

/**
 * Represneta una venta del negocio.
 * @author Luis Alejandro Gomez Castaño.
 */
public class Venta
{
    private int id;
    private Cliente cliente;
    private String fechaHora;
    private String observacion;
    private boolean Fuera;
    private double valor;
    private ArrayList<Elemento> elementos;    
    
    //Sirve para sacar los informes adicionales de contabilidad en el tequilazo.
    private double valorInvertido;
    private double utilidad;

    public Venta(int id, Cliente cliente, String fechaHora, String observacion, 
                    boolean fuera, double valor) 
    {
        this.id = id;
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.observacion = observacion;
        this.Fuera = fuera;
        this.valor = valor;
        this.valorInvertido = 0.0;
        this.utilidad = 0.0;
    }

    public int getId() 
    {
        return id;
    }

    public double getValorInvertido() {
        return valorInvertido;
    }

    public double getUtilidad() {
        return utilidad;
    }
    
    

    public void setId(int id) 
    {
        this.id = id;
    }

    public String getFechaHora() 
    {
        return fechaHora;
    }

    public boolean isFuera() 
    {
        return Fuera;
    }
    
    public ArrayList<Elemento> getElementos() 
    {
        return this.elementos;
    }

    public void setElementos(ArrayList<Elemento> elements) 
    {
        this.elementos = elements;
        int numElements = this.elementos.size();
        Elemento e;
        
        //información de utilidad y valor invertido.
        double invertido = 0;
        double utilidadGanada = 0;
        
        for(int i = 0; i < numElements; i++) 
        {            
            e = elements.get(i);
            System.out.println("Elemento " + e.getProducto().getNombre() + " Cantidad: " + e.getCantidadSale());
            invertido += e.getPrecioCompra() * e.getCantidadSale();            
        }
        
       
        this.utilidad = this.valor - invertido;
        this.valorInvertido = invertido;
        System.out.println("utilidad: " + utilidad + " invertido: " + valorInvertido);
        
    }

    public String getObservacion() 
    {
        return observacion;
    }

    public Cliente getCliente() 
    {
        return cliente;
    }

    public void setCliente(Cliente cliente) 
    {
        this.cliente = cliente;
    }

    public void setObservacion(String observacion) 
    {
        this.observacion = observacion;
    }

    public double getValor() 
    {
        return valor;
    }
    
    
    

    
}
