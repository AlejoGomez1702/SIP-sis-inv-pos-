package logicainterfaz;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import logica.Elemento;
import logica.Proveedor;
import logica.Tequilazo;
import logica.bd.*;

/**
 * FECHA ==> 2019-07-12.
 * Permite obtener en forma de objeto lo seleccionado de alguna tabla.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0.
 */
public class GestorTablas 
{
    /**
     * Tipo de letra y tamaño para las tablas del sistema.
     */
    private Font fuenteTablas;
    
    /**
     * Tipo de letra y tamaño para los encabezados de las tablas del sistema.
     */
    private Font fuenteEncabezados;
    
    /**
     * Color de la letra de los titulos de tablas del sistema.
     */
    private Color colorLetraEncabezados;
    
    /**
     * Color de las celdas de los titulos de tablas en el sistema.
     */
    private Color colorCeldasEncabezados;
    
    /**
     * Inicializa los tipos de letras y demás caracteristicas de las tablas.
     */
    public GestorTablas()
    {
        this.fuenteTablas = new Font("Cambria", 0, 16);
        this.fuenteEncabezados = new Font("Cambria", 0, 18);
        this.colorLetraEncabezados = new Color(255,255,255);
        this.colorCeldasEncabezados = new Color(0,153,153);
    }
    
    
    //Inicializa la mayoria de modelos de tablas del sistema.
    public void startTablesModels(DefaultTableModel mp, JTable tp, DefaultTableModel mva, JTable tva,
    DefaultTableModel mi, JTable ti, DefaultTableModel mvc, JTable tvc, DefaultTableModel mcc, JTable tcc,
    DefaultTableModel mca, JTable tca, DefaultTableModel ma, JTable tc, JTable tu, JTable tm)
    {
        JTableHeader header;
        
        //***Definiendo las columnas que van a tener las diferentes tablas***//
        //Proveedores.
        mp.addColumn("CÓDIGO");
        mp.addColumn("NOMBRE");
        mp.addColumn("NIT");
        mp.addColumn("TELÉFONO");
        tp.setModel(mp);
        tp.setFont(this.fuenteTablas);    
        header = tp.getTableHeader();
        header.setFont(this.fuenteEncabezados);
        header.setForeground(this.colorLetraEncabezados);
        header.setBackground(this.colorCeldasEncabezados);
        
        //Venta actual.
        mva.addColumn("Numero");
        mva.addColumn("Producto");
        mva.addColumn("Cantidad");
        mva.addColumn("Medida");
        mva.addColumn("Precio");
        tva.setModel(mva);
        tva.setFont(this.fuenteTablas);
        header = tva.getTableHeader();
        header.setFont(this.fuenteEncabezados);
        header.setForeground(this.colorLetraEncabezados);
        header.setBackground(this.colorCeldasEncabezados);
        
        //Inventario.
        mi.addColumn("CÓDIGO");
        mi.addColumn("NOMBRE");
        mi.addColumn("MARCA");
        mi.addColumn("MEDIDA");
        mi.addColumn("STOCK");
        mi.addColumn("PRECIO COMPRA");
        mi.addColumn("PRECIO VENTA");
        mi.addColumn("PRECIO FUERA");
        mi.addColumn("CANTIDAD ACTUAL");
        ti.setModel(mi);
        ti.setFont(this.fuenteTablas); 
        header = ti.getTableHeader();
        header.setFont(this.fuenteEncabezados);
        header.setForeground(this.colorLetraEncabezados);
        header.setBackground(this.colorCeldasEncabezados);
                
        //Ventas creadas.
        mvc.addColumn("ID");
        mvc.addColumn("FECHA-HORA");
        mvc.addColumn("CLIENTE");
        mvc.addColumn("VALOR");
        mvc.addColumn("OBSERVACIONES");
        tvc.setModel(mvc);
        tvc.setFont(this.fuenteTablas); 
        header = tvc.getTableHeader();
        header.setFont(this.fuenteEncabezados);
        header.setForeground(this.colorLetraEncabezados);
        header.setBackground(this.colorCeldasEncabezados);
        
        //Resumen de las compras creadas.
        mcc.addColumn("ID");
        mcc.addColumn("FECHA-HORA");
        mcc.addColumn("PROVEEDOR");
        mcc.addColumn("VALOR");
        mcc.addColumn("OBSERVACIONES");
        tcc.setModel(mcc);
        tcc.setFont(this.fuenteTablas);
        header = tcc.getTableHeader();
        header.setFont(this.fuenteEncabezados);
        header.setForeground(this.colorLetraEncabezados);
        header.setBackground(this.colorCeldasEncabezados);
        
        //Compra actual.
        mca.addColumn("CÓDIGO");
        mca.addColumn("NOMBRE");
        mca.addColumn("MARCA");
        mca.addColumn("MEDIDA");
        mca.addColumn("CANTIDAD");
        mca.addColumn("VALOR UNI");
        mca.addColumn("VALOR TOT");
        tca.setModel(mca);
        tca.setFont(this.fuenteTablas);
        header = tca.getTableHeader();
        header.setFont(this.fuenteEncabezados);
        header.setForeground(this.colorLetraEncabezados);
        header.setBackground(this.colorCeldasEncabezados);
        
        //Unidades de medida, Categorias y marcas.
        ma.addColumn("ID");
        ma.addColumn("NOMBRE");
        ma.addColumn("DESCRIPCIÓN");
        tc.setModel(ma);
        tc.setFont(this.fuenteTablas);
        header = tc.getTableHeader();
        header.setFont(this.fuenteEncabezados);
        header.setForeground(this.colorLetraEncabezados);
        header.setBackground(this.colorCeldasEncabezados);
        
        tu.setModel(ma);
        tu.setFont(this.fuenteTablas);
        header = tu.getTableHeader();
        header.setFont(this.fuenteEncabezados);
        header.setForeground(this.colorLetraEncabezados);
        header.setBackground(this.colorCeldasEncabezados);
        
        tm.setModel(ma);
        tm.setFont(this.fuenteTablas);
        header = tm.getTableHeader();
        header.setFont(this.fuenteEncabezados);
        header.setForeground(this.colorLetraEncabezados);
        header.setBackground(this.colorCeldasEncabezados);
    }    
    
    /**
     * Inicializa la tabla de la cual se seleccionan productos para la venta.
     * @param mt Modelo de la tabla de productos.
     * @param t Tabla de productos en la que se pintarán los datos.
     * @param scanner Me indica si debo pintar los datos de registrar scanner.
     * @param c Me indica si la tabla ya esta con los datos y no los agrego. 
     * 'c' ==>True : Si esta llamandose la tabla por segunda vez(menudeo).
     */
    public void initTableProducts(DefaultTableModel mt, JTable t, boolean scanner, boolean c)
    {
        JTableHeader header;
        if(scanner)
        {
            mt.addColumn("CÓDIGO");
            mt.addColumn("NOMBRE");
            mt.addColumn("MARCA");
            mt.addColumn("MEDIDA");
        }
        else
        {
            mt.addColumn("CÓDIGO");
            mt.addColumn("NOMBRE");
            mt.addColumn("MARCA");
            mt.addColumn("MEDIDA");
            mt.addColumn("PRECIO COMPRA");
            mt.addColumn("CANTIDAD ACTUAL");
        }          
        t.setModel(mt);
        t.setFont(this.fuenteTablas);
        header = t.getTableHeader();
        header.setFont(this.fuenteEncabezados);
        header.setForeground(this.colorLetraEncabezados);
        header.setBackground(this.colorCeldasEncabezados);
    }
    
    /**
     * Inicializa los titulos de la tabla detalle de compra.
     * @param mt Modelo de la tabla.
     * @param t Tabla de detalles de compra.
     * @param daily Se utiliza cuando se van a mostrar las ventas del dia.
     */
    public void initTableDetail(DefaultTableModel mt, JTable t, boolean daily)
    {
        JTableHeader header;
        if(daily)
            mt.addColumn("FACTURA");
        mt.addColumn("CÓDIGO");
        mt.addColumn("NOMBRE");
        mt.addColumn("MARCA");
        mt.addColumn("MEDIDA");
        mt.addColumn("CANTIDAD");
        mt.addColumn("VALOR UNIT");
        mt.addColumn("VALOR TOTAL");    
        t.setModel(mt);
        t.setFont(this.fuenteTablas);
        header = t.getTableHeader();
        header.setFont(this.fuenteEncabezados);
        header.setForeground(this.colorLetraEncabezados);
        header.setBackground(this.colorCeldasEncabezados);
    }
    
    /**
     * Inicializa los titulos de la tabla de ventas del dia.
     * @param mt Modelo de la tabla.
     * @param t Tabla de ventas del dia.
     */
    public void initTableDailySales(DefaultTableModel mt, JTable t)
    {
        //JTableHeader header;
        mt.addColumn("ID");
        mt.addColumn("CLIENTE");
        mt.addColumn("VALOR");    
        t.setModel(mt);
        t.setFont(this.fuenteTablas);
//        header = t.getTableHeader();
//        header.setFont(this.fuenteEncabezados);
//        header.setForeground(this.colorLetraEncabezados);
//        header.setBackground(this.colorCeldasEncabezados);
    }
        
    /**
     * Inicializa los titulos de la tabla de compras del dia.
     * @param mt Modelo de la tabla.
     * @param t Tabla de compras del dia.
     */
    public void initTableDailyPurchases(DefaultTableModel mt, JTable t)
    {
        //JTableHeader header;
        mt.addColumn("ID");
        mt.addColumn("PROVEEDOR");
        mt.addColumn("VALOR");    
        t.setModel(mt);
        t.setFont(this.fuenteTablas);
//        header = t.getTableHeader();
//        header.setFont(this.fuenteEncabezados);
//        header.setForeground(this.colorLetraEncabezados);
//        header.setBackground(this.colorCeldasEncabezados);
    }
    
    /**
     * Inicializa los nombres de las columnas de la tabla en la que
     * se listan los productos registrados por el scanner.
     * @param mt Modelo de la tabla.
     * @param t Tabla en la que se listan los elementos.
     */
    public void initTableScanner(DefaultTableModel mt, JTable t)
    {
        JTableHeader header;
        mt.addColumn("CÓDIGO");
        mt.addColumn("C.SCANNER");
        mt.addColumn("PRODUCTO");
        t.setModel(mt);
        t.setFont(this.fuenteTablas);
        header = t.getTableHeader();
        header.setFont(this.fuenteEncabezados);
        header.setForeground(this.colorLetraEncabezados);
        header.setBackground(this.colorCeldasEncabezados);                
    }    
    
    /**
     * Obtiene el código del producto y el código de barras del producto.
     * @param tabla Tabla que lista el mapeo de código de barras.
     * @return Datos necesarios para eliminar un registro de barras.
     */
    public String[] getScannerFromTable(JTable tabla)
    {
        String[] dates = new String[2];
        dates[0] = "no";
        int row = tabla.getSelectedRow();
        if(row != -1)
        {
            //Código del producto.
            dates[0] = tabla.getModel().getValueAt(row, 0).toString();
            //Código de barras del producto.
            dates[1] = tabla.getModel().getValueAt(row, 1).toString();
        }
        
        return dates;
    }
        
    /**
     * Obtiene el proveedor que se selecciona de la tabla proveedores.
     * @param tabla Tabla en la cual se grafican los proveedores del sistema.
     * @return Null -->No se selecciono nada, Proveedor -->Proveedor seleccionado.
     */
    public Proveedor getProviderFromTable(JTable tabla)
    {
        Proveedor prov = null;
        int row = tabla.getSelectedRow();
        if(row != -1)
        {
            String code = tabla.getModel().getValueAt(row, 0).toString();
            String name = tabla.getModel().getValueAt(row, 1).toString();
            String nit = tabla.getModel().getValueAt(row, 2).toString();
            String phone = tabla.getModel().getValueAt(row, 3).toString();
            prov = new Proveedor(code, name, nit, phone);
        }
        
        return prov;
    }
    
    /**
     * Obtiene el objeto seleccionado de la tabla de ajustes.
     * @param tabla Tabla de ajustes de la cual se quiere obtener el elemento.
     * @param tipo 'c'-->Categoria, 'm'-->Marca, 'u'-->Unidad de medida.
     * @return null-->No se selecciono ningun elemento, else-->ajuste.
     */
    public Ajuste getAdjustmentFromTable(JTable tabla, char tipo)
    {
        Categoria c;
        Marca m;
        UnidadMedida um;
        
        int column = 0;
        int row = tabla.getSelectedRow();
        if(row != -1)
        {
            String codeC = tabla.getModel().getValueAt(row, 0).toString();
            int code = -1;
            try 
            {
                code = Integer.parseInt(codeC);
            } catch (NumberFormatException e) 
            {
                return null;
            }
            String name = tabla.getModel().getValueAt(row, 1).toString();
            String des = tabla.getModel().getValueAt(row, 2).toString();
            switch(tipo)
            {
                //Categoria.
                case 'c':
                    c = new Categoria(code, name, des);
                    return c;                    
                //Marca.
                case 'm':
                    m = new Marca(code, name, des);
                    return m;
                //Unidad de medida.
                case 'u':
                    um = new UnidadMedida(code, name, des);
                    return um;                
            }
        }
        
        return null;
    }
    
    public boolean deleteRowFromTable(DefaultTableModel model, JTable table)
    {
        boolean bandera = false;
        int row = table.getSelectedRow();
        if(row != -1)       
        {
            model.removeRow(row);
            bandera = true;
        }            
        
        return bandera;            
    }
    
    /**
     * Obtiene el código del elemento seleccionado en la tabla.
     * @param tabla Tabla del inventario de donde se obtendra el elemento.
     * @return null-->No se seleccionó elemento, else-->código del elemento.
     */
    public String getCodeElementFromTable(JTable tabla)
    {
        String code = null;
        int row1 = tabla.getSelectedRow();
        //int row = convertRowIndexToModel(tabla);
        int row = tabla.convertRowIndexToModel(row1);
        if(row != -1)
        {
            try 
            {
                code = tabla.getModel().getValueAt(row, 0).toString();
            } catch (Exception e) 
            {
                return null;                
            }            
        }
        
        return code;        
    }
        
    /**
     * Realiza el filtrado de datos en una tabla.
     * @param modelo Modelo de la tabla a la cual se filtrarán los datos.
     * @param tabla Tabla a la cuál se le filtrarán los datos.
     * @param txt Campo de texto donde se introduce la información.
     * @param campo Campo por el cual se desea filtrar la información.
     * @param tequila Me permite la gestion del filtrado por scanner.
     */
    public void filtrarDatos(DefaultTableModel modelo, JTable tabla, JTextField txt,
            int campo, Tequilazo tequila)
    {
        String filtro = txt.getText();
        //Realizo las validaciones para cuando se lee con el scanner.
        int tamCode = 5;
        if(filtro.length() > tamCode + 1)
        {
            //filtro = (Elemento)tequila.getMap().get(filtro).getCodigo();
            Elemento el = (Elemento) tequila.getMap().get(filtro);
            if(el != null)
            {
                filtro = el.getCodigo();
                txt.setText(filtro);
            }                
        }
        
        TableRowSorter<DefaultTableModel> trsfiltro = new TableRowSorter(modelo);
        tabla.setRowSorter(trsfiltro); 
        trsfiltro.setRowFilter(RowFilter.regexFilter(filtro, campo));               
    }   
        
    
    
}
