package logica.gestores;

import logica.gestores.Reporte;
import java.time.LocalDateTime;
import java.util.Calendar;

/**
 *
 * @author ever
 */
public class Caja 
{
    private Calendar fechaInicio; 
    private LocalDateTime fechaHoraCierre;
    private double saldoActual;
    private final double SALDOINICIAL=0;
    private Reporte reporte;

    public Caja() 
    {
        this.fechaInicio = Calendar.getInstance();
        LocalDateTime ldt = LocalDateTime.now();
        //System.out.println("El año " + ldt.getYear());
        int year = ldt.getYear();
        int month = ldt.getMonthValue();
        int day = ldt.getDayOfMonth();
        this.fechaInicio.set(year, month, day);
        this.reporte = new Reporte();
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    public Calendar getFechaInicio() {
        return fechaInicio;
    }
        
    public LocalDateTime getFechaHoraCierre() {
        return fechaHoraCierre;
    }

    public void setFechaHoraCierre(LocalDateTime fechaHoraCierre) {
        this.fechaHoraCierre = fechaHoraCierre;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(double saldo) {
        this.saldoActual = saldo;
    }
    
    public void agregarDinero(double dinero)
    {
        this.saldoActual=saldoActual+dinero;
    }
    
    public boolean retirarDinero(double dinero)
    {
        if(saldoActual>=dinero)
        {
            saldoActual=saldoActual-dinero;
            return true;
        }
        return false;
    }
    
    
}
