package logica;
import java.util.ArrayList;

/**
 *
 * @author ever
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

    public Venta(int id, Cliente cliente, String fechaHora, String observacion, 
                    boolean fuera, double valor) 
    {
        this.id = id;
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.observacion = observacion;
        this.Fuera = fuera;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public boolean isFuera() {
        return Fuera;
    }
    
    
    
//
//    public void agregarComponente(Elemento componente)
//    {
//        componentes.add(componente);
//    }
    public ArrayList<Elemento> getElementos() {
        return this.elementos;
    }

    public void setElementos(ArrayList<Elemento> componentes) {
        this.elementos = componentes;
    }

    public String getObservacion() {
        return observacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public double getValor() {
        return valor;
    }
    
    
    

    
}
