package dialogos;

import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import logica.Venta;
import logica.Elemento;
import logicainterfaz.GestorTablas;

/**
 * FECHA ==> 2019-07-13.
 * Muestra la información detallada de una venta, es decir, muestra 
 * los respectivos productos de la venta.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0.
 */
public class DetalleVenta extends javax.swing.JDialog 
{    
    /**
     * Modelo de la tabla donde se listan los productos.
     */
    private DefaultTableModel modeloDetalleVenta;
    
    /**
     * Venta que se desea mostrar graficamente.
     */
    private Venta venta;
    
    /**
     * Ventas que se han realizado en el dia.
     */
    private ArrayList<Venta> sales;
    
    /**
     * Permite la gestion de las diferentes tablas del sistema.
     */
    private GestorTablas gt;

    /**
     * Crea una lista con los detalles de una venta, o una lista con todos 
     * los productos vendidos en el dia dependiendo que se le envie.
     * @param parent Ventana padre.
     * @param modal Modalidad del dialogo.
     * @param venta Venta que se desea detallar en esta ventana.
     * @param sales Cuando se desea detallar los productos vendidos en el dia.
     */
    public DetalleVenta(java.awt.Frame parent, boolean modal, Venta venta, ArrayList<Venta> sales) 
    {
        super(parent, modal);
        initComponents();
        this.gt = new GestorTablas();
        modeloDetalleVenta = new DefaultTableModel();   
        
        this.venta = venta;
        this.sales = sales;
        this.setLocationRelativeTo(null);
        if(venta != null)
        {            
            this.gt.initTableDetail(modeloDetalleVenta, tablaDetalleVenta, false);
            this.initDetailSale();
        }            
        else
        {
            this.gt.initTableDetail(modeloDetalleVenta, tablaDetalleVenta, true);
            this.initDailySales();
        }            
                
    }
    
    /**
     * Muestra el listado de productos que se han vendido en el dia.
     */
    public void initDailySales()
    {
//        boolean fuera = venta.isFuera();
        this.txtEcabezado.setText("Productos Vendidos En El Dia");
        this.txtTotal.setHorizontalAlignment(JTextField.CENTER);
        this.txtTitleId.setText("");
        this.txtId.setText("Ventas Del Dia");
        this.txtFecha.setText(this.sales.get(0).getFechaHora().substring(0, 10));
        this.checFuera.setSelected(true);
        this.checDentro.setSelected(true);
        
        int a = modeloDetalleVenta.getRowCount()-1;
        for(int i=a; i>=0; i--)        
            modeloDetalleVenta.removeRow(i); 
        
        //Para cada una de las ventas se sacan los productos correspondientes.
        int numSales = this.sales.size();
        ArrayList<Elemento> elements;
        int numEl;
        Elemento elem;
        double total = 0;
        double unitario = 0;
        boolean fuera;
        for(int i = 0; i < numSales; i++) 
        {
            elements = sales.get(i).getElementos();
            numEl = elements.size();
            for(int j = 0; j < numEl; j++) 
            {   
                elem = elements.get(j);
                fuera = sales.get(i).isFuera();
                if(fuera)            
                    unitario = elem.getPrecioVentaFuera();
                else
                    unitario = elem.getPrecioVenta();

                double precTotal = elem.getCantidadSale()*unitario;

                String [] fila ={sales.get(i).getId()+"", elem.getCodigo(),
                elem.getProducto().getNombre(), elem.getMarca(),
                elem.getUnidadMedida(), elem.getCantidadSale()+"", (int)unitario+"",
                (int) precTotal+""}; 
                total += precTotal;
                modeloDetalleVenta.addRow(fila);                               
            }          
        }
        this.txtTotal.setText((int) total + "");        
    }
    
    /**
     * Gráfica en la ventana los datos de una venta en el encabezado
     * y la lista de sus productos correspondiente.
     */
    public void initDetailSale()
    {
        this.txtTotal.setHorizontalAlignment(JTextField.CENTER);
        this.txtId.setText(venta.getId()+"");
        this.txtFecha.setText(venta.getFechaHora());
        boolean fuera = venta.isFuera();
        this.checFuera.setSelected(fuera);
        this.checDentro.setSelected(!fuera);
        //Muestra los reportes contables
        String inv = "";
        String gan = "";
        try {            
            inv = (int) venta.getValorInvertido() + "";
            gan = (int) venta.getUtilidad() + "";
        } catch (Exception e) {
        }
        this.txtInvertido.setText(inv);
        this.txtUtilidad.setText(gan);
       
        int a = modeloDetalleVenta.getRowCount()-1;
        for(int i=a; i>=0; i--)        
            modeloDetalleVenta.removeRow(i);        

        int numVent = this.venta.getElementos().size();
        Elemento elem;
        double total = 0;
        double unitario = 0;
        for (int i = 0; i < numVent; i++)
        {
            elem = venta.getElementos().get(i);
            if(fuera)            
                unitario = elem.getPrecioVentaFuera();
            else
                unitario = elem.getPrecioVenta();
            
            double precTotal = elem.getCantidadSale()*unitario;
            
            String [] fila ={elem.getCodigo(), elem.getProducto().getNombre(), elem.getMarca(),
            elem.getUnidadMedida(), elem.getCantidadSale()+"", (int)unitario+"",
            (int) precTotal+""}; 
            total += precTotal;
            modeloDetalleVenta.addRow(fila);
        }        
        this.txtTotal.setText((int) total + "");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelContenido = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDetalleVenta = new javax.swing.JTable();
        panelEncabezado = new javax.swing.JPanel();
        txtSalir = new javax.swing.JLabel();
        txtEcabezado = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtTitleId = new javax.swing.JLabel();
        txtId = new javax.swing.JLabel();
        txtFecha = new javax.swing.JLabel();
        checDentro = new javax.swing.JCheckBox();
        checFuera = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtUtilidad = new javax.swing.JLabel();
        txtInvertido = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        panelContenido.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tablaDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaDetalleVenta);

        panelEncabezado.setBackground(new java.awt.Color(36, 52, 81));
        panelEncabezado.setPreferredSize(new java.awt.Dimension(357, 53));

        txtSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoCerrar.png"))); // NOI18N
        txtSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSalirMouseClicked(evt);
            }
        });

        txtEcabezado.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtEcabezado.setForeground(new java.awt.Color(255, 255, 255));
        txtEcabezado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/seleccion.png"))); // NOI18N
        txtEcabezado.setText("      Detalle De Venta");

        javax.swing.GroupLayout panelEncabezadoLayout = new javax.swing.GroupLayout(panelEncabezado);
        panelEncabezado.setLayout(panelEncabezadoLayout);
        panelEncabezadoLayout.setHorizontalGroup(
            panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEncabezadoLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(txtEcabezado, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSalir)
                .addContainerGap())
        );
        panelEncabezadoLayout.setVerticalGroup(
            panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(txtEcabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btnCancelar.setBackground(new java.awt.Color(176, 95, 95));
        btnCancelar.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Atras");
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel2.setText("Fecha:");

        txtTitleId.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        txtTitleId.setText("Id: ");

        txtId.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N

        txtFecha.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N

        checDentro.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        checDentro.setText("Venta Dentro");

        checFuera.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        checFuera.setText("Venta Fuera");

        jLabel5.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel5.setText("TOTAL:");

        txtTotal.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        txtTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel6.setText("UTILIDAD:");

        txtUtilidad.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        txtUtilidad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtInvertido.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        txtInvertido.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel7.setText("INVERTIDO:");

        javax.swing.GroupLayout panelContenidoLayout = new javax.swing.GroupLayout(panelContenido);
        panelContenido.setLayout(panelContenidoLayout);
        panelContenidoLayout.setHorizontalGroup(
            panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelEncabezado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 939, Short.MAX_VALUE)
            .addGroup(panelContenidoLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(panelContenidoLayout.createSequentialGroup()
                .addGap(414, 414, 414)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContenidoLayout.createSequentialGroup()
                .addContainerGap(108, Short.MAX_VALUE)
                .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContenidoLayout.createSequentialGroup()
                        .addComponent(checDentro)
                        .addGap(49, 49, 49)
                        .addComponent(checFuera)
                        .addGap(105, 105, 105)
                        .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(txtTitleId))
                        .addGap(18, 18, 18)
                        .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(100, 100, 100))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContenidoLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtUtilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtInvertido, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panelContenidoLayout.setVerticalGroup(
            panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContenidoLayout.createSequentialGroup()
                .addComponent(panelEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelContenidoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtTitleId, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18))
                    .addGroup(panelContenidoLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checDentro)
                            .addComponent(checFuera))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5))
                    .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel6)
                        .addComponent(txtUtilidad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel7)
                    .addComponent(txtInvertido, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelContenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelContenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSalirMouseClicked
        this.dispose();
    }//GEN-LAST:event_txtSalirMouseClicked

    private void btnCancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnCancelarMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JCheckBox checDentro;
    private javax.swing.JCheckBox checFuera;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelContenido;
    private javax.swing.JPanel panelEncabezado;
    private javax.swing.JTable tablaDetalleVenta;
    private javax.swing.JLabel txtEcabezado;
    private javax.swing.JLabel txtFecha;
    private javax.swing.JLabel txtId;
    private javax.swing.JLabel txtInvertido;
    private javax.swing.JLabel txtSalir;
    private javax.swing.JLabel txtTitleId;
    private javax.swing.JLabel txtTotal;
    private javax.swing.JLabel txtUtilidad;
    // End of variables declaration//GEN-END:variables
}
