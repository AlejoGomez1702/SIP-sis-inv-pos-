package logica.gestores;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import logica.Elemento;
import logica.Inventario;

/**
 * FECHA ==> 2019-08-21.
 * Permite generar diferentes reportes del negocio.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0.
 */
public class Reporte
{
    /**
     * Ruta en la cual se guardará el reporte.
     */
    private String ruta;
    
    /**
     * Permite saber si se ah generado o no un reporte en la ruta.
     */
    public Reporte()
    {
        this.ruta = null;
    }    
    
    /**
     * Permite seleccionar la ruta del pc en la cual se desea guardar 
     * el archivo de reporte creado en formato PDF.
     * @param fecha Para indicar en el archivo la fecha de generación.
     * @return Null==>No Seleccionó ruta, Else==>Ruta del archivo.
     */    
    public String seleccionarDestino(String fecha)
    {
        String ruta = null;
        JFileChooser file = new JFileChooser();
        file.setSelectedFile(new File("Inventario " + fecha + ".pdf"));
        int option = file.showSaveDialog(null);
        if(option == JFileChooser.APPROVE_OPTION)
        {
            File f = file.getSelectedFile();
            ruta = f.toString();
        }
        
        return ruta;
    }
    
    /**
     * Genera un informe en formato PDF del inventario del negocio.
     * @param inventario Inventario del negocio.
     * @return True=>Se generó reporte, Else=>False.
     */
    public boolean generarInventario(Inventario inventario)
    {
        boolean bandera = true;
        String contenido = "\n";
        ArrayList<Elemento> elementos = inventario.getComponentes();
        int numEl = elementos.size();
        Elemento el;
        for(int i = 0; i < numEl; i++) 
        {
            el = elementos.get(i);
            contenido += el.getCodigo() + "                        " + el.getProducto().getNombre() 
                + "            " + el.getMarca() + "            " + el.getUnidadMedida()
                + "           " + el.getCantidadActual() + "\n";             
        }
        
        try 
        {
            FileOutputStream archivo = new FileOutputStream(this.ruta);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            doc.add(this.getHeader("Informe Inventario"));
            Image imagen = Image.getInstance("img/tequilaLogo.png");
            imagen.scaleAbsolute(152, 87);
            imagen.setAlignment(Element.ALIGN_CENTER);
            doc.add(imagen);
            doc.add(this.getColumns());
            doc.add(new Paragraph(contenido));
            doc.close();            
        } 
        catch(Exception e) 
        {
            bandera = false;
        }        
        
        return bandera;
    }
    
    
    public Paragraph getHeader(String title)
    {
        Paragraph p = new Paragraph();
        Chunk c = new Chunk();
        p.setAlignment(Element.ALIGN_CENTER);
        c.append(title);
        Font fuente = new Font(Font.FontFamily.COURIER, 24, Font.BOLD);
        c.setFont(fuente);
        p.add(c);
        
        return p;
    }
    
    public Paragraph getColumns()
    {
        Paragraph p = new Paragraph();
        Chunk c = new Chunk();
        //p.setAlignment(Element.ALIGN_CENTER);
        String titulos = "Código   Nombre   Marca   Medida   Disponibles";
        c.append(titulos);
        Font fuente = new Font(Font.FontFamily.COURIER, 18, Font.BOLD);
        c.setFont(fuente);
        p.add(c);
        
        return p;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
    
    
       

}
