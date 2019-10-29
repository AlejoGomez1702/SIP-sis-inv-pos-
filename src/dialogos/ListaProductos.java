package dialogos;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import logica.Elemento;
import logica.Tequilazo;
import logicainterfaz.GestorTablas;

/**
 * FECHA ==> 2019-07-26.
 * Listado de productos del inventario para registrar en la compra.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0
 */
public class ListaProductos extends javax.swing.JDialog 
{
    /**
     * Permite toda la gestion del sistema incluida la base de datos.
     */
    private Tequilazo tequilazo;
    
    /**
     * Permite la gestión de la tabla de productos mostrada en esta ventana.
     */
    private GestorTablas gt;
    
    /**
     * Modelo de la tabla implementada en esta ventana.
     */
    private DefaultTableModel modelo; 
    
    /**
     * Indica la finalidad para la cual se creo esta ventana.
     */
    private char mostrar;
    
    /**
     * Es el elemento que se va seleccionando para posteriormente
     * añadirle una cantidad de venta o compra.
     */
    private Elemento componente;
    
    /**
     * Elementos agregados a una venta o compra con su respectiva cantidad.
     */
    private ArrayList<Elemento> productos;        
        
    /**
     * Inicializa un listado de productos del inventario del cual se seleccionarán
     * algunos de ellos para realizar una compra o una venta del negocio.
     * @param parent Ventana desde la cual es ejecutadá esta interfaz.
     * @param modal Si se puede seguir trabajando con el sistema teniendo esta ventana abierta.
     * @param tequila Gestiona todo el sistema.
     */
    public ListaProductos(java.awt.Frame parent, boolean modal, Tequilazo tequila, char mostrar, boolean fuera)
    {
        super(parent, modal);
        this.modelo = new DefaultTableModel();
        this.mostrar = mostrar;
        this.tequilazo = tequila;
        this.productos = new ArrayList<>();
        ArrayList<Elemento> productosInventario = tequila.getInventario().getComponentes();
        initComponents();
        this.setLocationRelativeTo(null);
        gt = new GestorTablas();
        //Dependiendo del fin de la ventana pinto las columnas.
        if(mostrar == 's')
            gt.initTableProducts(modelo, tablaProductos, true, false);
        else
            gt.initTableProducts(modelo, tablaProductos, false, false);        
        
        this.componente = null;
                
        int numProd = productosInventario.size();
        Elemento prod;
        for (int i = 0; i < numProd; i++) 
        {
            prod = productosInventario.get(i);
            if(mostrar == 's')
            {
                String [] fila ={prod.getCodigo()+"", prod.getProducto().getNombre(),
                prod.getMarca(), prod.getUnidadMedida()};
                modelo.addRow(fila);
            }
            
            if(mostrar == 'c')
            {
                String [] fila ={prod.getCodigo()+"", prod.getProducto().getNombre(),
                prod.getMarca(), prod.getUnidadMedida(), (int)prod.getPrecioCompra()+"",
                prod.getCantidadActual()+""};
                modelo.addRow(fila);
            }
//            if(mostrar == 'v' && fuera)
//            {
//                String [] fila ={prod.getCodigo()+"", prod.getProducto().getNombre(),
//                prod.getMarca(), prod.getUnidadMedida(), (int)prod.getPrecioVentaFuera()+"",
//                prod.getCantidadActual()+""};
//                modelo.addRow(fila);
//            }
            if(mostrar == 'v' && !fuera)
            {
                String [] fila ={prod.getCodigo()+"", prod.getProducto().getNombre(),
                prod.getMarca(), prod.getUnidadMedida(), (int)prod.getPrecioVenta()+"",
                prod.getCantidadActual()+""};
                modelo.addRow(fila);
            }
            
        }    
    }

    /**
     * Obtiene la lista de productos agregados junto con su respectiva cantidad.
     * @return Lista con los productos seleccionados junto con su cantidad.
     */
    public ArrayList<Elemento> getProductos() 
    {
        return productos;
    }    

    /**
     * Obtiene el producto que se desea registrar con el scanner.
     * @return Producto a registrar con el scanner.
     */
    public Elemento getComponente() 
    {
        return componente;
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
        txtBusqueda = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        panelEncabezado = new javax.swing.JPanel();
        txtSalir = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        panelContenido.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtBusqueda.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        txtBusqueda.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtBusquedaCaretUpdate(evt);
            }
        });
        txtBusqueda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBusquedaMouseClicked(evt);
            }
        });
        txtBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusquedaActionPerformed(evt);
            }
        });
        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyTyped(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/busqueda.png"))); // NOI18N

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProductosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaProductos);
        if (tablaProductos.getColumnModel().getColumnCount() > 0) {
            tablaProductos.getColumnModel().getColumn(0).setResizable(false);
            tablaProductos.getColumnModel().getColumn(1).setResizable(false);
            tablaProductos.getColumnModel().getColumn(2).setResizable(false);
            tablaProductos.getColumnModel().getColumn(3).setResizable(false);
        }

        panelEncabezado.setBackground(new java.awt.Color(36, 52, 81));
        panelEncabezado.setPreferredSize(new java.awt.Dimension(357, 53));

        txtSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoCerrar.png"))); // NOI18N
        txtSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSalirMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/seleccion.png"))); // NOI18N
        jLabel3.setText("      Listado Inventario");

        javax.swing.GroupLayout panelEncabezadoLayout = new javax.swing.GroupLayout(panelEncabezado);
        panelEncabezado.setLayout(panelEncabezadoLayout);
        panelEncabezadoLayout.setHorizontalGroup(
            panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEncabezadoLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnAgregar.setBackground(new java.awt.Color(55, 128, 63));
        btnAgregar.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setText("Agregar");
        btnAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarMouseClicked(evt);
            }
        });
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel2.setText("Buscar Por Código");

        javax.swing.GroupLayout panelContenidoLayout = new javax.swing.GroupLayout(panelContenido);
        panelContenido.setLayout(panelContenidoLayout);
        panelContenidoLayout.setHorizontalGroup(
            panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelEncabezado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
            .addGroup(panelContenidoLayout.createSequentialGroup()
                .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelContenidoLayout.createSequentialGroup()
                        .addGap(161, 161, 161)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelContenidoLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelContenidoLayout.createSequentialGroup()
                                .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        panelContenidoLayout.setVerticalGroup(
            panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContenidoLayout.createSequentialGroup()
                .addComponent(panelEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContenidoLayout.createSequentialGroup()
                        .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBusqueda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContenidoLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(19, 19, 19)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addComponent(panelContenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBusquedaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtBusquedaCaretUpdate

    }//GEN-LAST:event_txtBusquedaCaretUpdate

    private void txtBusquedaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBusquedaMouseClicked

    }//GEN-LAST:event_txtBusquedaMouseClicked

    private void txtBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaActionPerformed

    private void txtBusquedaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyTyped
        //this.gt.filtro(modelo, tablaProductos, txtBusqueda, 0);               
    }//GEN-LAST:event_txtBusquedaKeyTyped

    private void txtSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSalirMouseClicked
        this.dispose();
    }//GEN-LAST:event_txtSalirMouseClicked

    private void btnCancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnCancelarMouseClicked

    private void btnAgregarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarMouseClicked

    }//GEN-LAST:event_btnAgregarMouseClicked

    private void tablaProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProductosMouseClicked
        
    }//GEN-LAST:event_tablaProductosMouseClicked

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
       String codeElement = this.gt.getCodeElementFromTable(this.tablaProductos);
       Elemento el;
       if(codeElement != null)
       {
           el = this.tequilazo.getBd().getCrudElemento().getElementFromCode(codeElement);
           if(el != null)
           {
               //Lanzo el dialogo que permite ingresar la cantidad del producto.
               this.componente = el;
               if(this.mostrar == 's')
               {
                  this.dispose();
               }
               else
               {
                   String title = el.getProducto().getNombre() + " " + el.getMarca() + " Por " + el.getUnidadMedida();
                   CantidadProducto cp = new CantidadProducto(this, true, title);
                   cp.setVisible(true);
                   int cantidad = cp.getCantidadSeleccionada();
                   if(cantidad > 0)
                   {
                       el.setCantidadSale(cantidad);
                       this.productos.add(el);
                       JOptionPane.showMessageDialog(this, "         Se Agregaron " + cantidad + "\n           Unidades De \n" + title);
                   }   
               }                    
           }
           else
               JOptionPane.showMessageDialog(this, "Error Consultando El Elemento En La BD");
       }
       else
           JOptionPane.showMessageDialog(this, "No Seleccionó Ningún Producto");                 
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();   
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaKeyReleased
        this.gt.filtrarDatos(modelo, tablaProductos, txtBusqueda, 0, this.tequilazo);
    }//GEN-LAST:event_txtBusquedaKeyReleased
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelContenido;
    private javax.swing.JPanel panelEncabezado;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JLabel txtSalir;
    // End of variables declaration//GEN-END:variables
}
