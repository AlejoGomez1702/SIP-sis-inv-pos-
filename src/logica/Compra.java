package logica;

import java.util.ArrayList;

/**
 * FECHA ==> 2019-07-27.
 * Representa una compra que se ah echo en el negocio.
 * @author Luis Alejandro Gómez C.
 * @author Ever Peña B.
 * @version 1.0.0
 */
public class Compra 
{
    /**
     * ID de la compra, Campo autonumérico en la base de datos.
     */
    private int id;
    
    /**
     * Fecha y hora en la que se realizó la compra.
     */
    private String fechaHora;
    
    /**
     * Proveeedor de la compra.
     */
    private Proveedor proveedor;
    
    /**
     * Valor total de la compra.
     */
    private double valor;
    
    /**
     * Observaciones generales de la compra.
     */
    private String observacion;
    
    /**
     * Productos que se compraron en esta compra. 
     */
    private ArrayList<Elemento> elementos;

    /**
     * Crea una compra sin especificar aún los elementos que la componen.
     * @param id Número autonumérico que representa de manera única la compra.
     * @param fechaHora Fecha y hora en la cual se realizo la compra.
     * @param proveedor Proveedor al cual se le realizó la compra.
     * @param valor Precio total de esta compra, sumando el precio de todos los elementos.
     * @param observacion Observaciones generales que se le hacen a esta compra.
     */
    public Compra(int id, String fechaHora, Proveedor proveedor, double valor, String observacion) 
    {
        this.id = id;
        this.fechaHora = fechaHora;
        this.proveedor = proveedor;
        this.valor = valor;
        this.observacion = observacion;
        this.elementos = new ArrayList<>();
    }    
      
    public void setElementos(ArrayList<Elemento> elementos) {
        this.elementos = elementos;
    }    
    
    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    

    public ArrayList<Elemento> getElementos() {
        return elementos;
    }    

    public int getId() {
        return id;
    }
    public void agregarComponente(Elemento componente)
    {
        elementos.add(componente);
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }
        
    public void setValor(double valor) {
        this.valor = valor;
    }
}