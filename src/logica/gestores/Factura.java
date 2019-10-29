package logica.gestores;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import logica.Elemento;
import logica.Venta;

/**
 * FECHA ==> 2019-08-07.
 * Permite la impresion de las diferentes facturas que emite el negocio.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0.
 */
public class Factura implements Printable
{
    /**
     * Tipo de factura que se desea emitir.(Venta,compra,reporte).
     */
    private char tipo;
    
    
    private Venta venta;

    /**
     * Crea el ambito de impresión, indicando el tipo de factura que se desea imprimir.
     * @param tipo Tipo de factura que se desea imprimir.
     * @param venta Por ahora para ensayar la venta
     */
    public Factura(char tipo, Venta venta) 
    {
        this.tipo = tipo;    
        this.venta = venta;
    }

    /**
     * Realiza la impresión de las diferentes facturas del negocio.
     * @param graphics Representación de la factura.
     * @param pageFormat Formato del papel de la impresora.
     * @param pageIndex Indice de la página que se desea imprimir.
     * @return Se pudo o no realizar la impresió.
     * @throws PrinterException No se pudo imprimir el ticket.
     */
    @Override
    public int print(Graphics graphics, PageFormat pageFormat,int pageIndex) throws PrinterException 
    {        
        int result = NO_SUCH_PAGE;    
        if(pageIndex == 0) 
        {                    
            Graphics2D g2d = (Graphics2D) graphics;                  
            double width = pageFormat.getImageableWidth();                  
            g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY()); 

            FontMetrics metrics = g2d.getFontMetrics(new Font("Arial",Font.BOLD,7));
            
            //Graficando la factura.
            try 
            {
                switch(this.tipo)
                {
                    case 'v':
                        printSailesInvoice(g2d);
                        break;
                        
                    case 'c':
//                        takeOutBox(g2d);
                        break;
                }                
                                
                result = PAGE_EXISTS;
            } 
            catch (NumberFormatException e) 
            {
                System.out.println("Error Imprimiendo Recibo");
            }
        }
        
        return result;
    }
    
    /**
     * Abre la caja del dinero del negocio.
     * @param pj Objeto con la información que abre la caja.
     * @return Abrio o no la caja.
     */
    public boolean takeOutBox(PrinterJob pj)
    {
        boolean bandera = true;
        byte[] open = {27, 112, 0, 55, 121};
        //byte[] open = {27,112,0,100,(byte) 250};
//      byte[] cutter = {29, 86,49};
        PrintService pservice = pj.getPrintService();
        PrintServiceLookup.lookupDefaultPrintService(); 
        DocPrintJob job = pservice.createPrintJob();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(open,flavor,null);
        PrintRequestAttributeSet aset = new 
        HashPrintRequestAttributeSet();
        try {
            job.print(doc, aset);
        } catch (PrintException ex) {
            System.out.println(ex.getMessage());
            bandera = false;
        }
        
        return bandera;
    }
    
    public void printSailesInvoice(Graphics2D g2d)
    {
        //28 caracteres por fila//
        /*Draw Header*/
        int y=20;
        int x = 0;
        int yShift = 10;
        int headerRectHeight=15;
        
        //****DATOS VENTA****DATOS VENTA****DATOS VENTA***//
        ArrayList<Elemento> elements = this.venta.getElementos();
        ArrayList<String> paintProd = this.convertString(elements, this.venta.isFuera());
        String fecha = this.venta.getFechaHora();
        String nameClient = this.venta.getCliente().getNombre();
        String cedClient = this.venta.getCliente().getCedula();
        String telClient = this.venta.getCliente().getTelefono();        
        //****DATOS VENTA****DATOS VENTA****DATOS VENTA***//        
        g2d.setFont(new Font("Monospaced",Font.PLAIN,9));
        
        //****ENCABEZADO****ENCABEZADO****ENCABEZADO****ENCABEZADO*****//
        g2d.drawString("--------------------------",x,y);y+=yShift;
        g2d.drawString(" Bar & Licores TEQUILAZO  ",x,y);y+=yShift;
        g2d.drawString("   " +     fecha   + "    ",x,y);y+=yShift;
        //Validación de los datos del cliente ==> nombre, cedula, telefono.    
        if(nameClient.length() > 0)
            g2d.drawString(" Cliente: " + nameClient,x,y);y+=yShift;
        if(cedClient.length() > 0)
            g2d.drawString("     C.C: " + cedClient,x,y);y+=yShift;
        if(telClient.length() > 0)
            g2d.drawString(" Celular: " + telClient,x,y);y+=yShift;        
        g2d.drawString("--------------------------",x,y);y+=yShift;
        //****ENCABEZADO****ENCABEZADO****ENCABEZADO****ENCABEZADO*****//

        //****CUERPO****CUERPO****CUERPO****CUERPO****CUERPO****CUERPO*****//
        g2d.drawString("--------------------------",x,y);y+=yShift;
        g2d.drawString("  Producto        Precio  ",x,y);y+=yShift;
        g2d.drawString("--------------    --------",x,y);y+=yShift;
        //Pintando cada uno de los productos en la factura.
        for (String nameProd : paintProd) 
        {
            g2d.drawString(nameProd,x,y);y+=yShift;
        }

        g2d.drawString("--------------------------",x,y);y+=yShift;
        g2d.drawString(" Valor Total: "+(int)this.venta.getValor(),x,y);y+=yShift;
        g2d.drawString("--------------------------",x,y);y+=yShift;
        //****CUERPO****CUERPO****CUERPO****CUERPO****CUERPO****CUERPO*****//
        
        //****PIE****PIE****PIE****PIE****PIE****PIE****PIE****PIE****PIE***//
        g2d.drawString("   Teléfono De Contacto   ",x,y);y+=yShift;
        g2d.drawString("       3106366850         ",x,y);y+=yShift;
        g2d.drawString("**************************",x,y);y+=yShift;
        g2d.drawString("  GRACIAS POR VISITARNOS  ",x,y);y+=yShift;
        g2d.drawString("**************************",x,y);y+=yShift;
        //****PIE****PIE****PIE****PIE****PIE****PIE****PIE****PIE****PIE***//
    }

    /**
     * Obtiene el formato de impresión, el papel para pos58 es 58 mm.
     * @param pj Trabajo de impresión.
     * @return Formato del formato de papel.
     */
    public PageFormat getPageFormat(PrinterJob pj)
    {
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();    

        double middleHeight =8.0;  
        double headerHeight = 2.0;                  
        double footerHeight = 2.0;                  
        double width = convert_CM_To_PPI(5.8);      //printer know only point per inch.default value is 72ppi
        double height = convert_CM_To_PPI(headerHeight+middleHeight+footerHeight); 
        if(this.tipo != 'c')
        {
            paper.setSize(width, height);
            paper.setImageableArea(                    
                0,
                10,
                width,            
                height - convert_CM_To_PPI(1)
            );   //define boarder size    after that print area width is about 180 points

            pf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
            pf.setPaper(paper); 
        } 
        else
        {
//            char ABRIR_GAVETA[]={(char)27,(char)112,(char)0,(char)10,(char)100};
//            pj.print(ABRIR_GAVETA);
            //imp.write(ABRIR_GAVETA);
        }

        return pf;
    }
    
    protected static double convert_CM_To_PPI(double cm) 
    {            
        return toPPI(cm * 0.393600787);            
    }
 
    protected static double toPPI(double inch) 
    {            
        return inch * 72d;            
    }
    
    public ArrayList<String> convertString(ArrayList<Elemento> elements, boolean fuera)
    {
        //Caracteres maximos para el nombre del producto ==> 16.
        int carName = 15;
        //Comodin para la cantidad vendida del producto ==> 3, maximo 4.   
        int carCant = 3;
        //Caracteres maximos para el valor del producto ==> 8.
        int carVal = 8;
        //Total Caracteres ==> 27. 
        int tamName = 5;
        int tamMark = 5;
        int tamUnit = 3;        
        //A g u a r . b l a n  c  .  b  o  t  X  2  3     1  0  0  0  0  0  0
        //1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26
        // PRODUCTO  PRODUCTO PRODUCTO  PRODUCTO|  COMODIN  | VALOR  VALOR  VALOR 
        
        ArrayList<String> products = new ArrayList<>();
        int numEl = elements.size();
        Elemento elem;        
        String name;
        String mark;
        String unit;
        int price;
        String nameComplete;
        
        for(int i = 0; i < numEl; i++) 
        {
            elem = elements.get(i);
            name = elem.getProducto().getNombre().replace(" ", "");
            mark = elem.getMarca().replace(" ", "");
            unit = elem.getUnidadMedida().replace(" ", "");          
            
            if(fuera)
                price = (int) elem.getPrecioVentaFuera();
            else
                price = (int) elem.getPrecioVenta();
            //Validaciones para la longitud de el nombre, marca, medida, cantidad.
            if(name.length() > tamName)            
                name = name.substring(0, tamName);
            if(mark.length() > tamMark)
                mark = mark.substring(0, tamMark);
            if(unit.length() > tamUnit)
                unit = unit.substring(0, tamUnit);         
            
            nameComplete = name + "." + mark + "." + unit;
            //Completando los espacios en blanco para que salga alineado.
            if(nameComplete.length() < carName)
            {
                for(int j = nameComplete.length(); j < carName; j++) 
                {
                    nameComplete += " ";                   
                }
            }       
            
            //Añadiendo la cantidad comprada del producto.
            nameComplete += "X" + elem.getCantidadSale();
            if(nameComplete.length() < (carName+carCant))
            {
                for(int j = nameComplete.length(); j < (carName+carCant); j++) 
                {
                    nameComplete += " ";                  
                }
            }
            
            //Añadiendo el precio del producto.
            nameComplete += price + "";
            
            products.add(nameComplete);                      
        }        
        
        return products;
    }
    
    
    
    
}
