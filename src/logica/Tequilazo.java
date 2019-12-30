package logica;

import logica.gestores.Caja;
import logica.gestores.Reporte;
import bd.BaseDatos;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import logica.bd.*;
import logica.gestores.GestorFecha;

/**
 * FECHA ==> 2019-07-27.
 * Representa el negocio en general, contiene toda la información de este y permite
 * casi la total gestion del sistema al tener todos los datos.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0
 */
public class Tequilazo 
{
    /**
     * Posee la conexión y los CRUDS con la base de datos del sistema.
     */
    private BaseDatos bd;
    
    /**
     * Inventario de productos que existen actualmente en el negocio.
     */
    private Inventario inventario;
    
    /**
     * Mapa con los elementos registrados por el scanner.
     */
    private HashMap<String, Elemento> map;
    
    /**
     * Almacena la fecha y hora en la que se inicia el sistema.
     */
    private LocalDateTime ldt;
    
    /**
     * Para esta version no se utiliza.
     */
    private Caja caja;
    
    /**
     * Listado de compras que se han realizado en el negocio.
     */
    private ArrayList<Compra> compras;
    
    /**
     * Listado de ventas que se han realizado en el negocio.
     */
    private ArrayList<Venta> ventas;
    
    /**
     * Genera los reportes del negocio.
     */
    private Reporte reporte;
    
    /**
     * Listado de proveedores de productos que tiene el negocio.
     */
    private ArrayList<Proveedor> proveedores;
    
    /**
     * Listado de categorias de productos que tiene el negocio.
     */
    private ArrayList<Categoria> categorias;
    
    /**
     * Listado de marcas de productos que se comercializan en el negocio.
     */
    private ArrayList<Marca> marcas;
    
    /**
     * Valor adicional que se le aumenta por estar michelado.
     */
    private final double VALOR_MICHELADA = 700;
    
    /**
     * Listado de unidades de medida que se comercializan en el negocio.
     */
    private ArrayList<UnidadMedida> unidadesMedidas;
    
    /**
     * Inicializa la información preliminar del negocio para su gestión.
     * @param inventario Inventario de productos del negocio.
     * @param caja Para esta version no se utiliza.
     * @param bd Base de datos del sistema en general.
     */
    public Tequilazo(Inventario inventario, Caja caja, BaseDatos bd) 
    {
        this.bd = bd;
        this.inventario = inventario;
        this.caja = caja;
        this.compras=new ArrayList();
        this.ventas=new ArrayList();
        this.ldt = LocalDateTime.now();
        this.reporte = new Reporte();
    }
    
    /**
     * Obtiene un elemento del inventario del negocio mediante su código.
     * @param code Código del elemento que se desea buscar.
     * @return Elemento al que corresponde el código.
     */
    public Elemento getElementFromCode(String code)
    {
        Elemento elem = null;
        int numElem = this.inventario.getComponentes().size();
        String aux;
        for(int i = 0; i < numElem; i++) 
        {
            elem = this.inventario.getComponentes().get(i);
            aux = elem.getCodigo();
            if(code.equals(aux))
                return elem;                                    
        }        
        return elem;
    }
    
    
    /**
     * Obtiene una compra echa en el negocio mediante sus código.
     * @param idPurchase Id de la compra que se desea buscar.
     * @return Compra a la cual corresponde el id.
     */
    public Compra getPurchaseFromId(int idPurchase)
    {
        Compra comp = null;
        int numComp = this.compras.size();
        int aux;
        for(int i = 0; i < numComp; i++) 
        {
            comp = this.compras.get(i);
            aux = comp.getId();
            if(idPurchase == aux)
                return comp;                                    
        }        
        return comp;
    }
    
    /**
     * Obtiene una determinada venta del negocio a partir de su ID.0
     * @param idSale Id de la venta que se desea encontrar.
     * @return Venta que corresponde al id pasado.
     */
    public Venta getSaleFromId(int idSale)
    {
        Venta vent;
        int numVent = this.ventas.size();
        int aux;
        for(int i = 0; i < numVent; i++) 
        {
            vent = this.ventas.get(i);
            aux = vent.getId();
            if(idSale == aux)
                return vent;
        }
        
        return null;
    }
    
    /**
     * Realiza las modificación en la cantidad actual de los productos 
     * del inventario que estan incluidos en una compra.
     * @param existentes Productos existentes en el inventario.
     * @param nuevos Productos que se compraron.
     */
    public void verifyNewsElements(ArrayList<Elemento> existentes, ArrayList<Elemento> nuevos)
    {
        int numExistentes = existentes.size();
        int numNuevos = nuevos.size();
        boolean existe = false;
        String codeExis;
        Elemento auxExis;
        String codeNew;
        Elemento auxNew;
        for(int i = 0; i < numNuevos; i++) 
        {
            auxNew = nuevos.get(i);
            codeNew = auxNew.getCodigo();            
            for(int j = 0; j < numExistentes; j++) 
            {
                auxExis = existentes.get(j);
                codeExis = auxExis.getCodigo(); 
                if(codeExis.equals(codeNew+""))
                {
                    int cant = auxExis.getCantidadSale() + auxNew.getCantidadSale();
                    auxExis.setCantidadSale(cant);
                    existe = true;
                    break;
                }                
            }
            if(!existe)
            {
                existe = false;
                existentes.add(auxNew);
            }            
        }
        
    }    
    
    /**
     * Obtiene las compras que se hicieron durante el dia.
     * @return Compras realizadas duerante el dia.
     */
    public ArrayList<Compra> getDailyPurchases()
    {
        ArrayList<Compra> dailyPurchases = new ArrayList<>();
        GestorFecha gf = new GestorFecha();
        gf.getDateDaily(ldt);
        String initialDate = gf.convertDate(1); //Fecha inicial
        GestorFecha gf2 = gf.plusDay(gf);
        String finishDate = gf2.convertDate(0); //Fecha final
        System.out.println("Ventas ini: " + initialDate);
        System.out.println("Ventas fin: " + finishDate);
        ArrayList<Integer> purchasesId = this.bd.getCrudCompra().
                                    getDailyPurchases(initialDate, finishDate);
        
        int countDailySales = purchasesId.size();
        Compra auxCompra;
        for (int i = 0; i < countDailySales; i++) 
        {
            auxCompra = this.getPurchaseFromId(purchasesId.get(i));
            dailyPurchases.add(auxCompra);
            //System.out.println("Compra Del dia: " + purchasesId.get(i));            
        }
        //System.out.println("FECHHHHHAAAAA = " + this.);
        
        
//        gf.getDateDaily(this.ldt); 
//        
//        int numCom = this.compras.size();
//        Compra auxCompra;
//        GestorFecha auxFechComp;
//        boolean ban = false;
//        for(int i = 0; i < numCom; i++) 
//        {
//            auxCompra = this.compras.get(i);
//            auxFechComp = new GestorFecha(auxCompra.getFechaHora());
//            ban = gf.compareDates(auxFechComp, gf);
//            if(ban)
//            {
//                dailyPurchases.add(auxCompra);
//            }                
//        }                 
        
        return dailyPurchases;
    }        
    
    public String[] getDatesFromOperations()
    {
        String[] fechas = new String[2];
        //Sacando las compras y ventas del ultimo mes.     
        LocalDateTime fechaActual = this.getLdt();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(fechaActual.getYear(), fechaActual.getMonthValue()-1, 
                                                fechaActual.getDayOfMonth());
        
        //calendar.add(Calendar.DATE, 1);
        Date dateInitial = calendar.getTime();          
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");          
        String initialDate = null;
        String finishDate = null;
        try 
        {
            finishDate = format1.format(dateInitial);
            
            calendar.add(Calendar.DATE, -10);
            Date dateFinish = calendar.getTime();
            initialDate = format1.format(dateFinish);
        }catch (Exception e1) 
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        fechas[0] = initialDate;
        fechas[1] = finishDate;
        //initialDate += " 00:00";
        //finishDate += " 12:59";
        return fechas;
    }
    
    
    /**
     * Obtiene el listado de ventas diarias que se realizan en el negocio.
     * @return Listado de ventas registradas en el dia actual.
     */
    public ArrayList<Venta> getDailySales()
    {
        ArrayList<Venta> dailySales = new ArrayList<>();
        //ArrayList<Compra> dailyPurchases = new ArrayList<>();
        GestorFecha gf = new GestorFecha();
        gf.getDateDaily(ldt);
        String initialDate = gf.convertDate(1); //Fecha inicial
        GestorFecha gf2 = gf.plusDay(gf);
        String finishDate = gf2.convertDate(0); //Fecha final
        System.out.println("Fecha inicial: " + initialDate);
        System.out.println("Fecha final: " + finishDate);
        
        ArrayList<Integer> salesId = this.bd.getCrudVenta().
                                    getDailySales(initialDate, finishDate);
        
        System.out.println("SACO " + salesId.size() + " Ventas");
        
        int countDailySales = salesId.size();
        Venta auxVenta;
        int auxId;
        for (int i = 0; i < countDailySales; i++) 
        {
            auxId = salesId.get(i);
            auxVenta = this.bd.getCrudVenta().getSaleFromId(auxId);
            if(auxVenta != null)
                dailySales.add(auxVenta);
            //System.out.println("Venta del dia: " + salesId.get(i));            
        }
               
        return dailySales;
    }
        
    /**
     * Obtiene el listado de todas las compras del negocio.
     * @return Listado de compras que ha realizado el negocio.
     */
    public ArrayList<Compra> getCompras() 
    {
        return compras;
    }

    /**
     * Modifica la lista de compras del negocio.
     * @param compras Lista de compras realizadas por el negocio.
     */
    public void setCompras(ArrayList<Compra> compras) 
    {
        this.compras = compras;
    }

    /**
     * Obtiene el inventario de productos del negocio.
     * @return Inventario del negocio.
     */
    public Inventario getInventario() 
    {
        return inventario;
    }

    /**
     * Obtiene la base de datos del negocio.
     * @return Base de datos del negocio.
     */
    public BaseDatos getBd() 
    {
        return bd;
    }    

    /**
     * Obtiene el listado de categoria de productos del negocio.
     * @return Listado de categorias de productos del negocio.
     */
    public ArrayList<Categoria> getCategorias() 
    {
        return categorias;
    }

    /**
     * Modifica la lista de categorias de productos del negocio.
     * @param categorias Lista de categoria de productos.
     */
    public void setCategorias(ArrayList<Categoria> categorias) 
    {
        this.categorias = categorias;
    }    

    /**
     * Obtiene la lista de marcas de productos del sistema.
     * @return Lista de marcas de productos que tiene el negocio.
     */
    public ArrayList<Marca> getMarcas() 
    {
        return marcas;
    }

    public ArrayList<UnidadMedida> getUnidadesMedidas() {
        return unidadesMedidas;
    }

    public void setMarcas(ArrayList<Marca> marcas) {
        this.marcas = marcas;
    }

    public void setUnidadesMedidas(ArrayList<UnidadMedida> unidadesMedidas) {
        this.unidadesMedidas = unidadesMedidas;
    }
   
    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public Caja getCaja() {
        return caja;
    }

    public ArrayList<Venta> getVentas() {
        return ventas;
    }

    public void setProveedores(ArrayList<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    /**
     * Modifica el mapa que registra los codigos de barras 
     * con el respectivo producto.
     * @param mapPrel Mapa preliminar donde solo registra el codigo del producto.
     */
    public void setMap(HashMap mapPrel) 
    {
        HashMap<String, Elemento> mapComplete = new HashMap();
        int numMap = mapPrel.size();
        Elemento el;
        String codElement;
        //iterando solo sobre valores
        for(Object key : mapPrel.keySet()) 
        {
            codElement = (String) key;
            el = this.bd.getCrudElemento().getElementFromCode(codElement);
            mapComplete.put((String)mapPrel.get(key), el);
//            System.out.println("Value = " + (String)mapPrel.get(key));
//            System.out.println("producto = " + el.getCodigo());
        }

        this.map = mapComplete;
    }

    public HashMap getMap() {
        return map;
    }
    
    
    
    public ArrayList<Proveedor> getProveedores() {
        return proveedores;
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }
    
    public void agregarProveedor(Proveedor proveedor)
    {
        proveedores.add(proveedor);
    }
    
    /**
     * Refresca la base de datos cuando se hace un CRUD de algun elemento
     * menor perteneciente al sistema.
     */
    public void updateDataBase()
    {
        this.proveedores = this.bd.getCrudProveedores().getAllProviders();
        this.categorias = this.bd.getCrudCategorias().obtenerCategorias();
        this.marcas = this.bd.getCrudMarcas().obtenerMarcas();
        this.unidadesMedidas = this.bd.getCrudUnidades().obtenerUnidadesMedidas();
    }  
    
    /**
     * Realiza la gestion para que se pueda registrar una venta en el negocio.
     * @param sale Venta que se desea registrar en el negocio.
     * @return True==>Se registró la venta, False==>No se pudo registrar venta.
     */
    public boolean addSale(Venta sale)
    {
        if(sale == null)
            return false;
        boolean bandera = this.bd.getCrudVenta().crearVenta(sale);
        if(!bandera)
            return false;
        
        bandera = this.bd.getCrudElementoVendido().insertElementsFromSale(sale);
        if(!bandera)
            return false;
                        
        return bandera;
    }
    
    /**
     * Verifica si una venta o compra está, y la elimina o agrega michelada. 
     * (Se usa para el boton (remover/michelada)de la tabla de resumen de ventas o compras).
     * @param code Código del elemento que se desea remover o michelar.
     * @param elements Lista en donde se buscará el elemento y se removerá.
     * @param indice 1=>remover, 2=>michelada.
     * @return True==>Se (removio, michelo), else==>False.
     */
    public boolean verifyExistDatail(String code, ArrayList<Elemento> elements, int indice)
    {
        if(code == null || elements == null)
            return false;
        boolean bandera = false;
        int num = elements.size();
        Elemento aux;
        String codeAux;
        for(int i = 0; i < num; i++)
        {
            aux = elements.get(i);
            codeAux = aux.getCodigo();
            if(code.equals(codeAux))
            {
                if(indice == 1)
                    elements.remove(i);
                if(indice == 2)
                {
                    aux.setPrecioVenta(aux.getPrecioVenta() + this.VALOR_MICHELADA);
                    //aux.setPrecioVentaFuera(aux.getPrecioVentaFuera() + this.VALOR_MICHELADA);
                }
                return true;
            }                        
        }
        
        return bandera;
    }
    
    /**
     * Añade una compra a la base de datos y al sistema en general.
     * @param purchase Compra que se desea agregar al negocio.
     * @return True-->Se agregó, False-->No se pudo agregar la compra.
     */
    public boolean addPurchase(Compra purchase)
    {
        if(purchase == null)
            return false;
        boolean bandera;
        bandera = this.bd.getCrudCompra().createPurchase(purchase);
        if(!bandera)
            return false;
        
        bandera = this.bd.getCrudElementoComprado().insertElementsFromPurchase(purchase);
        if(!bandera)
            return false;
                        
        return bandera;
    }

    public void setVentas(ArrayList<Venta> ventas) {
        this.ventas = ventas;
    }

    public LocalDateTime getLdt() {
        return ldt;
    }
        
}
