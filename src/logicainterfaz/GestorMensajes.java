package logicainterfaz;

/**
 * FECHA ==> 2019-08-27.
 * Identifica el tipo de mensaje que se debe mostrar graficamente.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0.
 */
public class GestorMensajes 
{
    /**
     * Obtiene el mensaje que se debe mostrar según el estado en el cual 
     * termino el proceso de menudear un producto del negocio.
     * @param message Indicador que se envia desde la logica del error.
     * @return Mensaje que se le debe mostrar al usuario.
     */
    public String getMessageMenudeo(int message)
    {
        String muestra = "";
        switch(message)
        {            
            case (0):
                muestra = "Se Menudeo Correctamente El Producto";
                break;
                
            case (1):
                muestra = "Debe Seleccionar Los Dos Productos Involucrados"
                            + " En El Proceso";
                break;
                
            case (2):
                muestra = "Error Consultando Los Productos En La Base De Datos";
                break;
                
            case (3):
                muestra = "Error Modificando La Cantidad En La Base De Datos";
                break;
                
            case (4):
                muestra = "Debe Seleccionar La Cantidad Que Contiene El Producto \n"
                        + "Para Poder Menudearlo Correctamente";
                break;
                
            case (5):
                muestra = "Debe Seleccionar El Producto Correcto De Menudeo \n"
                        + "(Trago, Cigarrilo X Unidad)";
        }       
        
        return muestra;
    }
    
    
}
