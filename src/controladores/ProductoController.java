package controladores;

import dialogos.AgregarAInventario;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import logica.Elemento;
import logica.Producto;
import logica.Tequilazo;
import logicainterfaz.PintorTablas;

/**
 * FECHA ==> 2019-12-18.
 * Permite la gestión del CRUD de productos en el inventario.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0
 */
public class ProductoController 
{
    /**
     * Permite la gestión del sistema.
     */
    private Tequilazo tequilazo;

    /**
     * Inicia el controlador de productos junto con el gestor principal de este.
     * @param tequilazo Gestor principal del sistema.
     */
    public ProductoController(Tequilazo tequilazo) 
    {
        this.tequilazo = tequilazo;
    }
    
    /**
     * Permite la creación de un producto del inventario.
     * @param pintor Permite refrescar la tabla donde se pintan los datos.
     * @param inventoryModel Modelo que gestiona la tabla de los datos.
     * @return Código de control para saber que informarle al usuario.
     */
    public int createProduct(PintorTablas pintor, DefaultTableModel inventoryModel)
    {
        AgregarAInventario agregar = new AgregarAInventario(null, true, "Agregar Producto");
        agregar.initInformation(this.tequilazo.getCategorias(), 
                this.tequilazo.getMarcas(), this.tequilazo.getUnidadesMedidas());
        agregar.setVisible(true); 
        
        Elemento comp = agregar.getComponente();
        if(comp == null)
            return 1;   //Se canceló la operación de crear el producto.
        
        Producto prod = comp.getProducto();
        //Obtengo el codigo de la categoria para agregarsela al producto en la base de datos.
        int idCat = this.tequilazo.getBd().getCrudCategorias().leerCategoriaPorNombre
                                                    (prod.getCategoria()).getCodigo();
        //Validando que el producto ya este ingresado en la base de datos.
        Producto product = this.tequilazo.getBd().getCrudProducto().
                                            getProductFromName(prod.getNombre());
        //Si el producto no esta registrado en la base de datos.
        boolean ban = false;            
        if(product == null)
        {
            ban = this.tequilazo.getBd().getCrudProducto().crearProducto(prod, idCat);
        }       
        else
        {
            ban = true;
            prod = product;
            comp.setProducto(product);
        }     
        
        int codeProd = this.tequilazo.getBd().getCrudProducto().
                                                        getCodeFromProduct(prod);
        int codeMark = this.tequilazo.getBd().getCrudMarcas().
                                                getCodeFromMark(comp.getMarca());
        int codeUnit = this.tequilazo.getBd().getCrudUnidades().
                                         getCodeFromUnit(comp.getUnidadMedida());
        
        if(ban && codeProd != -99 && codeMark != -99 && codeUnit != -99)
        {
            boolean seCreo = this.tequilazo.getBd().getCrudElemento().
                                crearElemento(comp, codeProd, codeMark, codeUnit);
            if(seCreo)
            {
                //JOptionPane.showMessageDialog(this, "Producto Agregado Correctamente Al Inventario");
                tequilazo.getInventario().agregarComponente(comp);
                pintor.paintTableInventory(inventoryModel, 
                            this.tequilazo.getInventario().getComponentes(), false);
                return 10;  //Se creo el producto correctamente.
            }
            else
                return 3;  //Error creando el producto en la BD.
        }
        else
            return 2;  //Error consultando elementos en la BD.
    }
    
    /**
     * Permite la modificación de un producto del inventario.
     * @param code Código del producto que se desea modificar.
     * @param pintor Permite refrescar la tabla donde se pintan los datos.
     * @param inventoryModel Modelo que gestiona la tabla de los datos.
     * @return Código de control para saber que informarle al usuario.
     */
    public int updateProduct(String code, PintorTablas pintor, 
                                            DefaultTableModel inventoryModel)
    {        
        if(code == null) 
            return 1;   //No seleccionó ningún elemento.
        
        String password = "pensilvania";
        String passInput = JOptionPane.showInputDialog(null, "Ingrese La Contraseña: ");
        if(passInput == null)
            return 0;   //Se canceló la modificación del producto.
        
        if(password.equals(passInput))
        {
            Elemento el = this.tequilazo.getBd().getCrudElemento().getElementFromCode(code);
            if(el == null)
                return 3;   //Error consultando el producto en la BD.
            
            //Inicio el dialogo que me permite modificar el producto.
            AgregarAInventario modificar = new AgregarAInventario(null, true, "Modificar Producto");
            modificar.initInformation(this.tequilazo.getCategorias(), 
                            this.tequilazo.getMarcas(), this.tequilazo.getUnidadesMedidas()); 
            modificar.setOldElement(el);
            modificar.setVisible(true);
            
            //Operaciones en la base de datos para modificar el elemento.
            Elemento oldElement = modificar.getOldElement();
            Elemento newElement = modificar.getComponente();
            if(oldElement == null || newElement == null)
                return 4;   //Se le dio cancelar al dialogo.
            
            Producto prod = newElement.getProducto();
            int idCat = this.tequilazo.getBd().getCrudCategorias().
                        leerCategoriaPorNombre(prod.getCategoria()).getCodigo();
            Producto product = this.tequilazo.getBd().getCrudProducto().
                                        getProductFromName(prod.getNombre());
            boolean ban = false;            
            if(product == null)
                ban = this.tequilazo.getBd().getCrudProducto().crearProducto(prod, idCat);
            else
            {
                ban = true;
                prod = product;
                newElement.setProducto(product);
            }
            int codeProd = this.tequilazo.getBd().getCrudProducto().
                                                    getCodeFromProduct(prod);
            int codeMark = this.tequilazo.getBd().getCrudMarcas().
                                        getCodeFromMark(newElement.getMarca());
            int codeUnit = this.tequilazo.getBd().getCrudUnidades().
                                    getCodeFromUnit(newElement.getUnidadMedida());
            
            if(ban && codeProd != -99 && codeMark != -99 && codeUnit != -99)
            {
                //Se modifica el elemento del inventario.
                boolean modifico = this.tequilazo.getBd().getCrudElemento().
                    updateElement(oldElement,newElement,codeProd,codeMark,codeUnit);
                if(modifico)
                {
                    this.tequilazo.getInventario().updateComponent(oldElement, newElement);
                    pintor.paintTableInventory(inventoryModel, this.tequilazo.
                                        getInventario().getComponentes(), false);
                    return 10;  //Se modificó correctamente el producto.
                }
                else
                    return 6;   //Error modificando el producto en la BD.
            }
            else
                return 5; //Error consultando los elementos en la BD.
            
        }
        else
            return 2;   //Contraseña incorrecta.
    }
    
}
