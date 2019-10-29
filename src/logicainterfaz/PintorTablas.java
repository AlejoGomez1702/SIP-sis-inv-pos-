package logicainterfaz;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import logica.Compra;
import logica.Elemento;
import logica.Producto;
import logica.Proveedor;
import logica.Venta;
import logica.bd.Categoria;
import logica.bd.Marca;
import logica.bd.UnidadMedida;

/**
 * FECHA ==> 2019-07-30.
 * Permite pintar los datos en las diferentes tablas del sistema.
 * @author Luis Alejandro Gómez C.
 * @author Ever Peña B.
 * @version 1.0 .
 */
public class PintorTablas 
{
    /**
     * Pinta los datos de los proveedores en su respectiva tabla del sistema.
     * @param providers Lista de proveedores que posee el sistema.
     * @param modelProviders Modelo de la tabla de proveedores.
     */
    public void paintTableProviders(ArrayList<Proveedor> providers, DefaultTableModel modelProviders)
    {
        this.clearDataFromTable(modelProviders);
        int numP = providers.size();
        Proveedor p;
        for(int i = 0; i < numP; i++)
        {
            p = providers.get(i);
             String [] fila ={p.getCodigo()+"", p.getNombre()+"",
                                p.getNIT()+"",p.getTelefono()+""};
             modelProviders.addRow(fila);
        }
    }    
    
    /**
     * Pinta los datos de las compras echas hasta el momento en el negocio
     * en su respectiva tabla del sistema.
     * @param purchases Lista de compras que se han echo en el negocio.
     * @param modelPurchases Modelo de la tabla de compras.
     */
    public void paintTablePurchases(ArrayList<Compra> purchases, DefaultTableModel modelPurchases)
    {
        this.clearDataFromTable(modelPurchases);
        int numPurc = purchases.size();
        Compra compra;          
        for (int i = 0; i < numPurc; i++)
        {
            compra = purchases.get(i);
            String [] fila ={compra.getId()+"", compra.getFechaHora()+"", 
                compra.getProveedor().getNombre(),(int)compra.getValor()+"", compra.getObservacion()};
            modelPurchases.addRow(fila);
        }
    }
    
    /**
     * Pinta los datos de las ventas echas hasta el momento en el negocio
     * en su respectiva tabla del sistema.
     * @param sales Lista de ventas que se han echo en el negocio.
     * @param modelTable Modelo de la tabla de ventas.
     */
    public void paintTableSales(ArrayList<Venta> sales, DefaultTableModel modelTable)
    {
        this.clearDataFromTable(modelTable);
        int numSale = sales.size();
        Venta venta;          
        for (int i = 0; i < numSale; i++)
        {
            venta = sales.get(i);
            String [] fila ={venta.getId()+"", venta.getFechaHora()+"", 
                venta.getCliente().getNombre(),(int)venta.getValor()+"", 
                venta.getObservacion()};
            modelTable.addRow(fila);
        }
    }
    
    /**
     * Pinta los datos de una compra en proceso, osea representa 
     * el resumen de productos de la compra.
     * @param modelTable Modelo de la tabla de resumen de compra.
     * @param elements Elementos que se han agregado a la compra.
     * @param campTotal Campo en el cual se muestra el valor total de la compra.
     */
    public void paintTableSumaryPurchase(DefaultTableModel modelTable, ArrayList<Elemento> elements, JLabel campTotal)
    {
        this.clearDataFromTable(modelTable);
        
        Elemento el;
        double totalUnit;
        double total = 0;
        for (int i = 0; i < elements.size(); i++)
        {
            el = elements.get(i);
            totalUnit = el.getPrecioCompra()*el.getCantidadSale();
            total += totalUnit;
            String[] datos ={el.getCodigo(), el.getProducto().getNombre(), el.getMarca(),
            el.getUnidadMedida(), el.getCantidadSale()+"", (int)el.getPrecioCompra()+"", (int)totalUnit+""};
            modelTable.addRow(datos); 
        }
        
        campTotal.setText((int)total+"");        
    }
    
    /**
     * Pinta los datos de una venta en proceso, osea representa 
     * el resumen de productos de la venta.
     * @param modelTable Modelo de la tabla de resumen de venta.
     * @param elements Elementos que se han agregado a la venta.
     * @param campTotal Campo en el cual se muestra el valor total de la venta
     * @param fuera Indica si la venta de productos es para consumo fuera
     * o dentro del negocio, ya que de esto depende el precio.
     */
    public void paintTableSumarySale(DefaultTableModel modelTable, ArrayList<Elemento>
                                            elements, JLabel campTotal, boolean fuera)
    {
        this.clearDataFromTable(modelTable);
        
        Elemento el;
        double totalUnit = 0;
        double total = 0;
        double precio;
        for (int i = 0; i < elements.size(); i++)
        {
            el = elements.get(i);
            if(fuera)  
            {
                totalUnit = el.getPrecioVentaFuera()*el.getCantidadSale();
                precio = el.getPrecioVentaFuera();
            }                
            else
            {
                totalUnit = el.getPrecioVenta()*el.getCantidadSale();  
                precio = el.getPrecioVenta();
            }
            
            JButton bot = new JButton("Eliminar");
            
            total += totalUnit;
            Object[] datos ={el.getCodigo(), el.getProducto().getNombre(), el.getMarca(),
            el.getUnidadMedida(), el.getCantidadSale()+"", (int)precio+"", (int)totalUnit+""};
            modelTable.addRow(datos); 
        }
        
        campTotal.setText((int)total+"");        
    }    
            
    /**
     * Pinta la tabla que contiene el inventario del negocio.
     * @param modelTable Modelo de la tabla del inventario.
     * @param elements Productos que pertenecen al inventario del negocio.
     * @param small Me indica si se quiere pintar la tabla reducida o completa.
     */
    public void paintTableInventory(DefaultTableModel modelTable, 
                                    ArrayList<Elemento> elements, boolean small)
    {
        this.clearDataFromTable(modelTable);
        //Pinta todos los componentes del inventario.
        int numComp = elements.size();
        Elemento comp;
        Producto prod;
        for(int i = 0; i < numComp; i++)
        {
            comp = elements.get(i);
            prod = comp.getProducto();

            if(!small)
            {
                String[] fila = {comp.getCodigo(), prod.getNombre(), comp.getMarca(),
                comp.getUnidadMedida(), comp.getStock()+"", (int)comp.getPrecioCompra()+"", 
                (int)comp.getPrecioVenta()+"", (int)comp.getPrecioVentaFuera()+"", comp.getCantidadActual()+""};

                modelTable.addRow(fila);
            }
            else
            {
                String[] fila = {comp.getCodigo(), prod.getNombre(), comp.getMarca(),
                comp.getUnidadMedida()};

                modelTable.addRow(fila);
            }
            
        }          
    }
    
    /**
     * Pinta en la tabla de ajustes los elementos correspondientes ya sean
     * Categorias, Marcas o unidades de medida.
     * @param modelTable Modelo de la tabla donde se pintarán los datos.
     * @param type Tipo de ajuse que se desea pintar: Categorias, Marcas o unidades de medida.
     * @param categories Lista de las CATEGORIAS que tiene registradas el negocio.
     * @param marks Lista de las MARCAS que tiene registradas el negocio.
     * @param units Lista de las UNIDADES DE MEDIDA que tiene registradas el negocio.
     */
    public void paintTableAdjustments(DefaultTableModel modelTable, char type,
        ArrayList<Categoria> categories, ArrayList<Marca> marks, ArrayList<UnidadMedida> units)
    {
        this.clearDataFromTable(modelTable);

        if(type == 'c')
        {
            try
            {
                int numC = categories.size();
                Categoria c;
                for(int i = 0; i < numC; i++)
                {
                    c = categories.get(i);
                     String [] fila ={c.getCodigo()+"", c.getNombre(), c.getDescripcion()};
                     modelTable.addRow(fila);
                }            
            }
            catch(Exception e) 
            {
                JOptionPane.showMessageDialog(null,"Actualmente No Hay Categorias De Productos");
            }
        }
        else if(type == 'm')
        {
            try 
            {
                int numM = marks.size();
                Marca m;
                for(int i = 0; i < numM; i++)
                {
                    m = marks.get(i);
                    String [] fila ={m.getCodigo()+"", m.getNombre(), m.getDescripcion()};
                    modelTable.addRow(fila);
                }   
            } catch (Exception e) 
            {
                JOptionPane.showMessageDialog(null, "Actualmente No Hay Marcas Registradas");
            }
        }
        else if(type == 'u')
        {
            try 
            {
                int numUm = units.size();
                UnidadMedida m;
                for(int i = 0; i < numUm; i++)
                {
                    m = units.get(i);
                    String [] fila ={m.getCodigo()+"", m.getNombre(), m.getDescripcion()};
                    modelTable.addRow(fila);
                }   
            } catch (Exception e) 
            {
                JOptionPane.showMessageDialog(null, "Actualmente No Hay Unidades De Medida Registradas");
            }
        }
    }
    
    /**
     * Pinta en la tabla de ventas diarias las ventas correspondientes.
     * @param modelTable Modelo de la tabla de ventas diarias.
     * @param sales Ventas que se desean mostrar en la tabla.
     */
    public void paintTableDailySales(DefaultTableModel modelTable, ArrayList<Venta> sales)
    {
        this.clearDataFromTable(modelTable);
        int numSal = sales.size();
        Venta venta;          
        for (int i = 0; i < numSal; i++)
        {
            venta = sales.get(i);
            String [] fila ={venta.getId()+"", venta.getCliente().getNombre(),
                                                        (int)venta.getValor()+""};
            modelTable.addRow(fila);
        }
    }    
    
    /**
     * Pinta en la tabla de compras diarias las compras correspondientes.
     * @param modelTable Modelo de la tabla de compras diarias.
     * @param purchases Compras que se desean mostrar en la tabla.
     */
    public void paintTableDailyPurchases(DefaultTableModel modelTable, ArrayList<Compra> purchases)
    {
        this.clearDataFromTable(modelTable);
        int numPurc = purchases.size();
        Compra compra;          
        for (int i = 0; i < numPurc; i++)
        {
            compra = purchases.get(i);
            String [] fila ={compra.getId()+"", compra.getProveedor().getNombre(),
                                                        (int)compra.getValor()+""};
            modelTable.addRow(fila);
        }
    }
    
    /**
     * Pinta la tabla en la cual se puede visualizar los productos registrados
     * con código de barras en el negocio.
     * @param modelTable Modelo de la tabla.
     * @param map Mapa con códigoScanner==>Producto.
     */
    public void paintTableScanner(DefaultTableModel modelTable, HashMap<String, Elemento> map)
    {
        this.clearDataFromTable(modelTable);
        Elemento el;
        String desc;
        for(String key : map.keySet()) 
        {
            el = map.get(key);
            desc = el.getProducto().getNombre() + " " + el.getMarca() + " X " + 
                    el.getUnidadMedida();
            String [] fila ={el.getCodigo()+"", key, desc};
                                                        
            modelTable.addRow(fila);                        
        }
    }
    
    /**
     * Pinta en el comboBox de una nueva compra la lista de proveedores
     * registrados actualmente en el negocio.
     * @param providers Lista de proveedores registrados en el negocio.
     * @param comb ComboBox en el cual se van a listar los proveedores.
     */
    public void paintCombProviders(ArrayList<Proveedor> providers, JComboBox comb)
    {
        String aux = "";                
        int numProviders = providers.size();
        Proveedor pro;
        for(int i = 0; i < numProviders; i++) 
        {
            pro = providers.get(i);
            aux = pro.getNombre();
            comb.addItem(aux);
        }
    }
    
    /**
     * Hace el barrido de la información contenida en una tabla.
     * @param modeloTabla Modelo de la tabla que se desea barrer.
     */
    public void clearDataFromTable(DefaultTableModel modeloTabla)
    {        
        int a =modeloTabla.getRowCount()-1;
        for(int i=a; i>=0; i--)
        {
            modeloTabla.removeRow(i);
        }            
    }
    
}
