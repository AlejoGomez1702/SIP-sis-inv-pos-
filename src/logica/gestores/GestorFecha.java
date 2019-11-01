package logica.gestores;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * FECHA ==> 2019-08-26.
 * Permite la gestion de las fechas ya que se manejan como String 
 * por facilidad con la base de datos.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0.
 */
public class GestorFecha 
{
    /**
     * Año de la fecha.
     */
    private String year;
    
    /**
     * Mes de la fecha.
     */
    private String month;
    
    /**
     * Dia del mes.
     */
    private String day;
    
    /**
     * Hora de la fecha.
     */
    private String hour;
    
    /**
     * Minutos de la fecha.
     */
    private String minute;
    
    /**
     * Franja horaria de la fecha.
     */
    private String franja;
    
    /**
     * Crea un gestor a partir una cadena con los datos de la fecha.
     * @param fecha Cadena con la fecha que será manipulada.
     */
    public GestorFecha(String fecha)
    {
        String date = fecha.replace(" ", "");
        /*Formato de la fecha para obtener la información.
            2019/08/1906:52PM
            |||| || |||| ||||
            0123 56 8901 3456
            YYYY MM DDHH MMFF
        */
        this.year = date.substring(0, 4);
        this.month = date.substring(5, 7);
        this.day = date.substring(8, 10);
        this.hour = date.substring(10, 12);
        this.minute = date.substring(13, 15);
        this.franja = date.substring(15, 17);        
    }
    
    /**
     * Constructor por defecto que no almacena ningún dato.
     */
    public GestorFecha()
    {
        this.year = "";
        this.month = "";
        this.day = "";
        this.hour = "";
        this.minute = "";
        this.franja = ""; 
    }
    
    /**
     * Me permite saber que compras y que ventas se realizan en el dia.
     * @param fechaCompra fecha de la compra o venta.
     * @param fechaDia fecha del dia.
     * @return La venta o compra si es del dia o no.
     */
    public boolean compareDates(GestorFecha fechaCompra, GestorFecha fechaDia)
    {
        boolean bandera = false;
        int hora = -1;
        try 
        {
            hora = Integer.parseInt(fechaCompra.getHour());
        } catch (NumberFormatException e) 
        {
            return false;
        }
        //Que el año, mes y dia sean igual y que sea despues de las 2 de la tarde.
        if(fechaCompra.getYear().equals(fechaDia.getYear()) && fechaCompra.getMonth().equals(fechaDia.getMonth())
            && fechaCompra.getDay().equals(fechaDia.getDay()) && fechaCompra.getFranja().equals("PM") && hora > 2)
        {
            return true;
        }

        int hour2 = -1;
        try 
        {
            hour2 = Integer.parseInt(fechaCompra.getHour());
        } catch (NumberFormatException e) 
        {
            return false;
        }
        //Si la hora esta entre las 12 y las 6 de la mañana.
        GestorFecha diaMas = this.plusDay(fechaDia);
        //System.out.println("El dia: " + diaMas.getDay());
        if(fechaCompra.getYear().equals(diaMas.getYear()) && fechaCompra.getMonth().equals(diaMas.getMonth())
            && fechaCompra.getDay().equals(diaMas.getDay()) && fechaCompra.getFranja().equals("AM") && hora <= 12)
        {
            return true;
        }
                       
        return bandera;
    }

    /**
     * Obtiene las fechas de inicio y fin en las que debe mostrar el listado
     * de ventas y compras diarias el sistema.
     * @param ldt Objeto del cual se obtendran los datos de busqueda.
     * @return Fechas de inicio y fin del dia laboral en el sistema.
     */
    public String getDateDaily(LocalDateTime ldt)
    {
        /*
        Realizo la validación para que si el usuario abre
        el sistema entre las 12 y 6 de la mañana se muestre
        el registro de las ventas del dia anterior.
        */
        //int horaEntra = ldt.getHour();
        //System.out.println("HORRAA: " + horaEntra);
//        if(horaEntra >= 0 && horaEntra <= 6)
//        {
//            ldt.minusDays(1);
//        }         
                
        //AÑO
        int yearLdt = ldt.getYear();
        String y = yearLdt + "";
        this.year = y;
        //MES
        int monthLdt = ldt.getMonthValue();
        String m = monthLdt + "";
        if(monthLdt < 10)
            m = "0" + monthLdt;
        this.month = m;
        //DIA
        int dayLdt = ldt.getDayOfMonth();
        String d = dayLdt + "";
        if(dayLdt < 10)
            d = "0" + dayLdt;  
        this.day = d;
      
        //Fecha de inicio del dia.
        String retorno = y + "/" + m + "/" + d + " 02:00 " + "PM";
        this.hour = "02";
        this.minute = "00";
        this.franja = "PM";
                
        return retorno;
    }    
    
    /**
     * Aumenta en un dia un objeto de tipo "GestorFecha".
     * @param dateDay Fecha a la cual se le desea aumentar un dia.
     * @return Fecha con un dia de mas.
     */
    public GestorFecha plusDay(GestorFecha dateDay)
    {
        String y = dateDay.getYear();
        String m = dateDay.getMonth();
        String d = dateDay.getDay();
        String date = y+"-"+m+"-"+d+ " 00:00";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
        LocalDateTime ldtInicial = LocalDateTime.parse(date, format);        
        LocalDateTime ldt = ldtInicial.plusDays(1);
        //System.out.println("BEBEBEBEBEBE: " + ldt.getDayOfMonth());
        //AÑO
        int yearLdt = ldt.getYear();
        String yy = yearLdt + "";
        //MES
        int monthLdt = ldt.getMonthValue();
        String mm = monthLdt + "";
        if(monthLdt < 10)
            mm = "0" + monthLdt;
        //DIA
        int dayLdt = ldt.getDayOfMonth();
        String dd = dayLdt + "";
        if(dayLdt < 10)
            dd = "0" + dayLdt;
        
        String laFechita = yy + "/" + mm + "/" + dd + "00:00 PM";
        GestorFecha loc = new GestorFecha(laFechita);
        
        return loc;        
    }
    
    public String convertDate(int indicador)
    {
        String fecha = "";
        if(indicador == 1)
            fecha = year + "/" + month + "/" + day + " " + "00" + ":" + minute;  
        else
            fecha = year + "/" + month + "/" + day + " " + hour + ":" + minute;
        return fecha;
    }
        
    
    //****GETERS****GETERS****GETERS****GETERS****GETERS****GETERS****GETERS****//
    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getFranja() {
        return franja;
    }
    //****FIN GETERS****FIN GETERS****FIN GETERS****FIN GETERS****FIN GETERS***//
    
    
}
