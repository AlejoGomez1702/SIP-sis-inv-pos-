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
    
    /**
     * Obtiene el mensaje que se debe mostrar según el estado en el cual 
     * termino el proceso de actualizar un producto del inventario.
     * @param message Indicador que se envia desde la logica del error.
     * @return Mensaje que se le debe mostrar al usuario.
     */
    public String getMessageUpdateProduct(int message)
    {
        String muestra = "";
        switch(message)
        {            
            case (1):
                muestra = "No Seleccionó Ningún Producto Del Inventario";
                break;
                
            case (2):
                muestra = "La Contraseña Ingresada Es Incorrecta";
                break;
                
            case (3):
                muestra = "Error Consultando El Producto En La Base De Datos";
                break;
                
            case (5):
                muestra = "Error Consultando Elementos En La Base De Datos";
                break;
                
            case (6):
                muestra = "Error Modificando Producto En La Base De Datos";
                break;
                
            //Caso Válido.
            case (10):
                muestra = "!Producto Actualizado Correctamente!";
                break;      
        }       
        
        return muestra;
    }
    
    /**
     * Obtiene el mensaje que se debe mostrar según el estado en el cual 
     * termino el proceso de crear un producto del inventario.
     * @param message Indicador que se envia desde la logica del error.
     * @return Mensaje que se le debe mostrar al usuario.
     */
    public String getMessageCreateProduct(int message)
    {
        String muestra = "";
        switch(message)
        {            
            case (2):
                muestra = "Error Consultando Elementos En La Base De Datos";
                break;
                
            case (3):
                muestra = "Error Creando Producto En La Base De Datos";
                break;
               
            //Caso Válido.
            case (10):
                muestra = "!Producto Creado Correctamente!";
                break;      
        }       
        
        return muestra;
    }
    
    /**
     * Obtiene el mensaje que se debe mostrar según el estado en el cual 
     * termino el proceso de crear una nueva venta en el negocio.
     * @param message Indicador que se envia desde la logica del error.
     * @return Mensaje que se le debe mostrar al usuario.
     */
    public String getMessageCreateSale(int message)
    {
        String muestra = "";
        switch(message)
        {            
            case (0):
                muestra = "ERROR, No Se Han Agregado Productos A La Venta";
                break;
                
            case (1):
                muestra = "ERROR, Problemas Con El Número De La Venta";
                break;
                
            case (2):
                muestra = "ERROR, Problemas Con La Fecha De La Venta";
                break;
                
            case (3):
                muestra = "ERROR, Problemas Con El Valor Total La Venta";
                break;
                
            case (4):
                muestra = "ERROR, Problemas Añadiendo La Venta A La Base De Datos";
                break;
                
            case (5):
                muestra = "ERROR, Problemas Disminuyendo La Cantidad Del Inventario";
                break;
               
            //Caso Válido.
            case (10):
                muestra = "!Venta Registrada Correctamente!";
                break;      
        }       
        
        return muestra;
    }
    
    /**
     * Obtiene el mensaje que se debe mostrar según el estado en el cual 
     * termino el proceso de michelar cervezas en el negocio.
     * @param message Indicador que se envia desde la logica del error.
     * @return Mensaje que se le debe mostrar al usuario.
     */
    public String getMessageMichelar(int message)
    {
        String muestra = "";
        switch(message)
        {  
            case (1):
                muestra = "ERROR, No Seleccionó Ningúna Cerveza Para Michelar";
                break;
                
            case (2):
                muestra = "ERROR, Debe Seleccionar Máximo La Cantidad De "
                        + "Cervezas Agregadas En La Venta";
                break;
                               
            //Caso Válido.
            case (10):
                muestra = "ok";
                break;      
        }       
        
        return muestra;
    }
    
}
