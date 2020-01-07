package principal;
import controladores.MicheladaController;
import controladores.VentaController;
import dialogos.*;
import java.awt.Color;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import logica.Cliente;
import logicainterfaz.LogicaInterfaz;
import logica.Tequilazo;
import logica.Elemento;
import logica.Venta;
import logica.Compra;
import logica.gestores.Factura;
import logica.Proveedor;
import logica.bd.Categoria;
import logica.bd.Marca;
import logica.bd.UnidadMedida;
import logicainterfaz.GestorMensajes;
import logicainterfaz.GestorTablas;
import logicainterfaz.PintorTablas;

/**
 * FECHA ==> 2019-05-10.
 * Ventana principal del sistema.
 * @author Luis Alejandro Gómez C.
 * @version 1.0.0
 */
public class InterfazPrincipal extends javax.swing.JFrame 
{
    /**
     * Clase que controla la logica de negocio de la interfaz.
     */
    private LogicaInterfaz logica;
    
    /**
     * Gestor para la interactividad de las tablas.
     */
    private GestorTablas gt;
    
    /**
     * Permite reflejar y refrescar información de las diferentes
     * tablas del sistema.
     */
    private PintorTablas pintor;
        
    /**
     * Negocio que recibe desde el Main.
     */
    private Tequilazo tequilazo;    

    /**
     * Los componentes que va a tener la venta o compra 
     * y que se llenan con la lista de Productos.
     */
    private ArrayList<Elemento> componentesNuevos;//que se van a crear. 
    
    /**
     * Cliente que se ingresa en la venta.
     */
    private Cliente clienteVenta;
    
    /**
     * Modelo para la tabla de venta que se va a crear y a agregar en el negocio. 
     */
    private DefaultTableModel modeloVenta = new DefaultTableModel();   
      
    /**
     * Modelo para la tabla de la lista del invnetario.  
     */
    private DefaultTableModel modeloInventario = new DefaultTableModel();
    
    /**
     * Modelo para la tabla que va a guardar todas las ventas creadas en el negocio.
     */
    private DefaultTableModel modeloVentasCreadas = new DefaultTableModel();
    
    /**
     * Modelo para la tabla del Resumen de la compra que se va a realizar.
     */
    private DefaultTableModel modeloResumenCompra = new DefaultTableModel();
    
    /**
     * Modelo de la tabla que muestra todas las compras que tiene el negocio.
     */
    private DefaultTableModel modeloComprasHechas = new DefaultTableModel();
    
    /**
     * Modelo para la tabla que guarda la lista de todos los Proveedores del negocio.
     */
    private DefaultTableModel modeloListaProveedores = new DefaultTableModel();;
    
    /**
     * Modelo para las tablas de ajustes(marcas, unidadesM, categorias).
     */
    private DefaultTableModel modeloAjustes = new DefaultTableModel(); 
    
    /**
     * Modelo para la tabla donde se muestran las compras del dia.
     */
    private DefaultTableModel modeloComprasDia = new DefaultTableModel();
    
    /**
     * Modelo para la tabla que resume una venta en progreso.
     */
    private DefaultTableModel modeloResumenVenta = new DefaultTableModel();
    
    /**
     * Modelo para la tabla donde se muestran las VENTAS del dia.
     */
    private DefaultTableModel modeloVentasDia = new DefaultTableModel();
    
    /**
     * Modelo para la tabla donde se muestran los productos por scanner.
     */
    private DefaultTableModel modeloScanner = new DefaultTableModel();
    
    /**
     * Lo utilizó para saber en cual de las ventanas de ajuste estoy posicionado
     * 'c'-->Categorias, 'm'-->Marcas, 'u'-->Unidades de medida.
     */
    private char ajuste;  
    
    /**
     * Controlador para las ventas del negocio.
     */
    private VentaController saleController;
    
    /**
     * Controlador para la gestión de las cervezas micheladas.
     */
    private MicheladaController micheladaController;
    
    /**
     * Permite la gestión de los mensajes que se le muestran al usuario.
     */
    private GestorMensajes gm;
        
    
    public InterfazPrincipal(Tequilazo tequilazo) 
    {          
        initComponents();    
        this.gt = new GestorTablas();
        this.pintor = new PintorTablas();        
        this.tequilazo = tequilazo;
        this.saleController = new VentaController(tequilazo);
        
        this.logica = new LogicaInterfaz(2, Color.BLACK,tequilazo);
        this.logica.iniciarEstilosPrincipal(this, panelNav, panelEncab, panelPrincipal, panelContenido);
        this.logica.pintarComponentes(txtFecha, txtEncabezado, txtInicio);
        //seleccionados= new ArrayList<>();
        this.componentesNuevos = new ArrayList<>();
        this.initInformation();
        this.clienteVenta = new Cliente("","","");
        this.gm = new GestorMensajes();
        this.micheladaController = new MicheladaController(tequilazo);
    }
    
    /**
     * Inicializa la información de las tablas del sistema.
     */
    private void initInformation()
    {
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/iconoPrincipal.png")).getImage());
        //Inicialización de tablas.
        this.gt.startTablesModels(modeloListaProveedores, tablaListaProveedores, 
        modeloVenta, tablaDetalleVenta, modeloInventario, tablaInventario, 
        modeloVentasCreadas, tablaVentasCreadas, modeloComprasHechas, tablaComprasHechas,
        modeloResumenCompra, tablaResumenCompra, modeloAjustes, tablaCategorias, 
        tablaUnidades, tablaMarcas);
        this.gt.initTableDailyPurchases(this.modeloComprasDia, this.tablaComprasDia);
        this.gt.initTableDailySales(this.modeloVentasDia, this.tablaVentasDia);        
        this.gt.initTableDetail(modeloResumenVenta, tablaDetalleVenta, false);
        this.gt.initTableScanner(this.modeloScanner, this.tablaListaScanner);        
        this.txtTotalResumenCompra.setHorizontalAlignment(JTextField.CENTER);
        //Para el comboBox de los proveedores.
        this.pintor.paintCombProviders(this.tequilazo.getProveedores(), combProviders); 
        //Para las compras y ventas del dia.
        ArrayList<Compra> com = this.tequilazo.getDailyPurchases();
        this.pintor.paintTableDailyPurchases(modeloComprasDia, com);
        ArrayList<Venta> ven = this.tequilazo.getDailySales();
        this.pintor.paintTableDailySales(modeloVentasDia, ven);  
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelNav = new javax.swing.JPanel();
        panelLogo = new javax.swing.JPanel();
        txtLogo = new javax.swing.JLabel();
        txtInicio = new javax.swing.JLabel();
        txtInventario = new javax.swing.JLabel();
        txtVentas = new javax.swing.JLabel();
        txtCompras = new javax.swing.JLabel();
        txtSalir = new javax.swing.JLabel();
        txtProveedores = new javax.swing.JLabel();
        txtAjustes = new javax.swing.JLabel();
        txtScanner = new javax.swing.JLabel();
        panelContenido = new javax.swing.JPanel();
        panelEncab = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JLabel();
        txtNombre = new javax.swing.JLabel();
        salir = new javax.swing.JLabel();
        minim = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        txtEncabezado = new javax.swing.JLabel();
        btnCaja = new javax.swing.JButton();
        panelPrincipal = new javax.swing.JPanel();
        panelInicio = new javax.swing.JPanel();
        panelContenedorInicio = new javax.swing.JPanel();
        panelEncabVentas = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnVerVenta = new javax.swing.JLabel();
        panelVentasDia = new javax.swing.JPanel();
        scrollVentDia = new javax.swing.JScrollPane();
        tablaVentasDia = new javax.swing.JTable();
        panelEncaCompDia = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnVerCompra = new javax.swing.JLabel();
        panelComprasDia = new javax.swing.JPanel();
        scrollCompDia = new javax.swing.JScrollPane();
        tablaComprasDia = new javax.swing.JTable();
        panelNuevaVenta = new javax.swing.JPanel();
        txtNuevaVenta = new javax.swing.JLabel();
        panelNuevaCompra = new javax.swing.JPanel();
        txtNuevaCompra = new javax.swing.JLabel();
        panelNuevoProveedor = new javax.swing.JPanel();
        txtNuevoProveedor = new javax.swing.JLabel();
        panelInventario = new javax.swing.JPanel();
        panelCrudBotones = new javax.swing.JPanel();
        txtBuscarInventario = new javax.swing.JTextField();
        lblLupa = new javax.swing.JLabel();
        btnModificarComponente = new javax.swing.JButton();
        btnEliminarComponente = new javax.swing.JButton();
        btnAgregarComponente = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        lblListaI = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaInventario = new javax.swing.JTable();
        btnImprimirInventario = new javax.swing.JButton();
        lblIconoProductos1 = new javax.swing.JLabel();
        btnMenudeo = new javax.swing.JButton();
        lblIconoProductos5 = new javax.swing.JLabel();
        btnModificarMichelada = new javax.swing.JButton();
        lblIconoProductos6 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        panelVentas = new javax.swing.JPanel();
        panelCrudBotones1 = new javax.swing.JPanel();
        btnDetalleVenta = new javax.swing.JButton();
        lblListaI2 = new javax.swing.JLabel();
        lblListaI8 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnBuscarVentas = new javax.swing.JButton();
        fechaInicialVenta = new com.toedter.calendar.JDateChooser();
        fechaFinalVenta = new com.toedter.calendar.JDateChooser();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablaVentasCreadas = new javax.swing.JTable();
        jSeparator4 = new javax.swing.JSeparator();
        panelCompras = new javax.swing.JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tablaComprasHechas = new javax.swing.JTable();
        panelCrudBotones2 = new javax.swing.JPanel();
        btnDetalleCompra = new javax.swing.JButton();
        lblListaI5 = new javax.swing.JLabel();
        lblListaI9 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        panelProveedores = new javax.swing.JPanel();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel13 = new javax.swing.JPanel();
        lblListaI6 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tablaListaProveedores = new javax.swing.JTable();
        panelCrudBotones3 = new javax.swing.JPanel();
        btnModificarProveedor = new javax.swing.JButton();
        btnEliminarProveedor = new javax.swing.JButton();
        btnAgregaProveedor = new javax.swing.JButton();
        panelCrearCompra = new javax.swing.JPanel();
        panelEncabezado = new javax.swing.JPanel();
        lblIngreso = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        lblNumero = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblNumeroCompra = new javax.swing.JLabel();
        lblFechaCompra = new javax.swing.JLabel();
        lblProveedores = new javax.swing.JLabel();
        lblIconoProductos = new javax.swing.JLabel();
        lblObservaciones = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObservacionesCompra = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaResumenCompra = new javax.swing.JTable();
        lblObservaciones2 = new javax.swing.JLabel();
        lblObservaciones5 = new javax.swing.JLabel();
        txtTotalResumenCompra = new javax.swing.JLabel();
        btnLimpiarCompra = new javax.swing.JButton();
        combProviders = new javax.swing.JComboBox<>();
        btnAgregarProductosCompra = new javax.swing.JButton();
        btnCancelarCompra = new javax.swing.JButton();
        btnCrearCompra = new javax.swing.JButton();
        btnRemover2 = new javax.swing.JButton();
        panelCrearVenta = new javax.swing.JPanel();
        panelEncabezado1 = new javax.swing.JPanel();
        lblIngreso1 = new javax.swing.JLabel();
        lblTitulo1 = new javax.swing.JLabel();
        checDentro = new javax.swing.JCheckBox();
        checFuera = new javax.swing.JCheckBox();
        lblFecha2 = new javax.swing.JLabel();
        lblNumero2 = new javax.swing.JLabel();
        lblNumeroVenta = new javax.swing.JLabel();
        lblFechaVenta = new javax.swing.JLabel();
        lblProveedores1 = new javax.swing.JLabel();
        lblIconoProductos2 = new javax.swing.JLabel();
        lblObservaciones1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtObservacionesVenta = new javax.swing.JTextArea();
        lblObservaciones3 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaDetalleVenta = new javax.swing.JTable();
        lblObservaciones4 = new javax.swing.JLabel();
        txtTotalResumenVenta = new javax.swing.JLabel();
        btnLimpiarTabla = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        btnAgregarCliente = new javax.swing.JButton();
        btnAgregarProdVenta = new javax.swing.JButton();
        btnCancelarVenta = new javax.swing.JButton();
        btnCrearVenta = new javax.swing.JButton();
        btnMichelada = new javax.swing.JButton();
        lblIconoProductos3 = new javax.swing.JLabel();
        txtCode = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        panelScanner = new javax.swing.JPanel();
        jSeparator7 = new javax.swing.JSeparator();
        jPanel14 = new javax.swing.JPanel();
        lblListaI7 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tablaListaScanner = new javax.swing.JTable();
        panelCrudBotones4 = new javax.swing.JPanel();
        btnEliminarScanner = new javax.swing.JButton();
        btnAgregaScanner = new javax.swing.JButton();
        panelAjustes = new javax.swing.JPanel();
        panelEncabAjustes = new javax.swing.JPanel();
        lblCategorias = new javax.swing.JLabel();
        lblMarcas = new javax.swing.JLabel();
        lblUnidades = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        panelAjustesElementos = new javax.swing.JPanel();
        panelAjustesMarcas = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnModificarMarca = new javax.swing.JButton();
        btnEliminarMarca = new javax.swing.JButton();
        btnAgregarMarca = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        lblListaI1 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablaMarcas = new javax.swing.JTable();
        panelAjustesUnidades = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        btnModificarUnidades = new javax.swing.JButton();
        btnEliminarUnidad = new javax.swing.JButton();
        btnAgregarUnidades = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        lblListaI3 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tablaUnidades = new javax.swing.JTable();
        panelAjustesCategorias = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        btnModificarCategoria = new javax.swing.JButton();
        btnEliminarCategoria = new javax.swing.JButton();
        btnAgregarCategorias = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        lblListaI4 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tablaCategorias = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelNav.setBackground(new java.awt.Color(36, 52, 81));
        panelNav.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        panelLogo.setBackground(new java.awt.Color(0, 1, 63));
        panelLogo.setForeground(new java.awt.Color(0, 102, 153));

        txtLogo.setBackground(new java.awt.Color(0, 102, 153));
        txtLogo.setFont(new java.awt.Font("Candara", 1, 28)); // NOI18N
        txtLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/tequilaLogo.png"))); // NOI18N
        txtLogo.setText("SCI");

        javax.swing.GroupLayout panelLogoLayout = new javax.swing.GroupLayout(panelLogo);
        panelLogo.setLayout(panelLogoLayout);
        panelLogoLayout.setHorizontalGroup(
            panelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogoLayout.createSequentialGroup()
                .addComponent(txtLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelLogoLayout.setVerticalGroup(
            panelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogoLayout.createSequentialGroup()
                .addComponent(txtLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtInicio.setBackground(new java.awt.Color(255, 255, 255));
        txtInicio.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        txtInicio.setForeground(new java.awt.Color(255, 255, 255));
        txtInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/inicio.png"))); // NOI18N
        txtInicio.setText("      Inicio");
        txtInicio.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtInicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtInicioMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtInicioMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtInicioMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtInicioMouseReleased(evt);
            }
        });

        txtInventario.setBackground(new java.awt.Color(255, 255, 255));
        txtInventario.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        txtInventario.setForeground(new java.awt.Color(255, 255, 255));
        txtInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/inventario.png"))); // NOI18N
        txtInventario.setText("   Inventario");
        txtInventario.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                txtInventarioMouseDragged(evt);
            }
        });
        txtInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtInventarioMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtInventarioMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtInventarioMouseExited(evt);
            }
        });

        txtVentas.setBackground(new java.awt.Color(255, 255, 255));
        txtVentas.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        txtVentas.setForeground(new java.awt.Color(255, 255, 255));
        txtVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/venta.png"))); // NOI18N
        txtVentas.setText("      Ventas");
        txtVentas.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                txtVentasMouseDragged(evt);
            }
        });
        txtVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtVentasMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtVentasMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtVentasMouseExited(evt);
            }
        });

        txtCompras.setBackground(new java.awt.Color(255, 255, 255));
        txtCompras.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        txtCompras.setForeground(new java.awt.Color(255, 255, 255));
        txtCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compra.png"))); // NOI18N
        txtCompras.setText("     Compras");
        txtCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtComprasMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtComprasMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtComprasMouseExited(evt);
            }
        });

        txtSalir.setBackground(new java.awt.Color(255, 255, 255));
        txtSalir.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        txtSalir.setForeground(new java.awt.Color(255, 255, 255));
        txtSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.png"))); // NOI18N
        txtSalir.setText("       Salir");
        txtSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSalirMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtSalirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtSalirMouseExited(evt);
            }
        });

        txtProveedores.setBackground(new java.awt.Color(255, 255, 255));
        txtProveedores.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        txtProveedores.setForeground(new java.awt.Color(255, 255, 255));
        txtProveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/proveedor.png"))); // NOI18N
        txtProveedores.setText(" Proveedores");
        txtProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtProveedoresMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtProveedoresMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtProveedoresMouseExited(evt);
            }
        });

        txtAjustes.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        txtAjustes.setForeground(new java.awt.Color(255, 255, 255));
        txtAjustes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoAjustes.png"))); // NOI18N
        txtAjustes.setText("      Ajustes");
        txtAjustes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAjustesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtAjustesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtAjustesMouseExited(evt);
            }
        });

        txtScanner.setBackground(new java.awt.Color(255, 255, 255));
        txtScanner.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        txtScanner.setForeground(new java.awt.Color(255, 255, 255));
        txtScanner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Scanner.png"))); // NOI18N
        txtScanner.setText("     Scanner");
        txtScanner.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtScannerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtScannerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtScannerMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelNavLayout = new javax.swing.GroupLayout(panelNav);
        panelNav.setLayout(panelNavLayout);
        panelNavLayout.setHorizontalGroup(
            panelNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelNavLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(txtSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelNavLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAjustes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtProveedores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelNavLayout.createSequentialGroup()
                        .addGroup(panelNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtInventario, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                            .addComponent(txtVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCompras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtScanner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelNavLayout.setVerticalGroup(
            panelNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavLayout.createSequentialGroup()
                .addComponent(panelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(txtInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(txtInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(txtVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(txtCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtScanner, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtAjustes, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                .addComponent(txtSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        getContentPane().add(panelNav, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 160, 660));

        panelEncab.setBackground(new java.awt.Color(0, 153, 153));

        jLabel7.setFont(new java.awt.Font("Source Code Pro Semibold", 0, 22)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Bar & Licores");

        jLabel8.setFont(new java.awt.Font("Source Code Pro Semibold", 0, 22)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Tequilazo");

        txtFecha.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtFecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/calendario.png"))); // NOI18N
        txtFecha.setText("2019");

        txtNombre.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtNombre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/usuario.png"))); // NOI18N
        txtNombre.setText("Jorge Miguel Rincon Herrera");

        salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoCerrar.png"))); // NOI18N
        salir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salirMouseClicked(evt);
            }
        });

        minim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoMinimizar.png"))); // NOI18N
        minim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimMouseClicked(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono_cabecera2.png"))); // NOI18N

        javax.swing.GroupLayout panelEncabLayout = new javax.swing.GroupLayout(panelEncab);
        panelEncab.setLayout(panelEncabLayout);
        panelEncabLayout.setHorizontalGroup(
            panelEncabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEncabLayout.createSequentialGroup()
                .addContainerGap(91, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGroup(panelEncabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEncabLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7))
                    .addGroup(panelEncabLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel8)))
                .addGap(139, 139, 139)
                .addGroup(panelEncabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEncabLayout.createSequentialGroup()
                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(txtNombre)
                        .addGap(76, 76, 76))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEncabLayout.createSequentialGroup()
                        .addComponent(minim)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salir)
                        .addContainerGap())))
        );
        panelEncabLayout.setVerticalGroup(
            panelEncabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEncabLayout.createSequentialGroup()
                .addGroup(panelEncabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEncabLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelEncabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(minim)
                            .addComponent(salir)))
                    .addGroup(panelEncabLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(panelEncabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFecha)
                            .addComponent(txtNombre)))
                    .addGroup(panelEncabLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelEncabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelEncabLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator1.setForeground(new java.awt.Color(0, 204, 204));

        txtEncabezado.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        txtEncabezado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/inicio.png"))); // NOI18N
        txtEncabezado.setText("Inicio");

        btnCaja.setBackground(new java.awt.Color(51, 153, 255));
        btnCaja.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        btnCaja.setForeground(new java.awt.Color(255, 255, 255));
        btnCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono_registradora.png"))); // NOI18N
        btnCaja.setMnemonic('c');
        btnCaja.setText("Abrir Caja");
        btnCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCajaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelContenidoLayout = new javax.swing.GroupLayout(panelContenido);
        panelContenido.setLayout(panelContenidoLayout);
        panelContenidoLayout.setHorizontalGroup(
            panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelEncab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelContenidoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(258, 258, 258)
                .addComponent(btnCaja)
                .addGap(19, 19, 19))
            .addComponent(jSeparator1)
        );
        panelContenidoLayout.setVerticalGroup(
            panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContenidoLayout.createSequentialGroup()
                .addComponent(panelEncab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panelContenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, 880, 160));

        panelPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        panelPrincipal.setLayout(new java.awt.CardLayout());

        panelInicio.setBackground(new java.awt.Color(255, 255, 255));
        panelInicio.setPreferredSize(new java.awt.Dimension(880, 500));

        panelEncabVentas.setBackground(new java.awt.Color(36, 52, 81));

        jLabel5.setBackground(new java.awt.Color(51, 51, 51));
        jLabel5.setFont(new java.awt.Font("Californian FB", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoMenu.png"))); // NOI18N
        jLabel5.setText("               Ventas Del Dia");

        btnVerVenta.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnVerVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnVerVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoVer.png"))); // NOI18N
        btnVerVenta.setText("VER");
        btnVerVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVerVentaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVerVentaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVerVentaMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelEncabVentasLayout = new javax.swing.GroupLayout(panelEncabVentas);
        panelEncabVentas.setLayout(panelEncabVentasLayout);
        panelEncabVentasLayout.setHorizontalGroup(
            panelEncabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEncabVentasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVerVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelEncabVentasLayout.setVerticalGroup(
            panelEncabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panelEncabVentasLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelEncabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVerVenta)))
        );

        tablaVentasDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "PROVEEDOR", "TOTAL", "HORA"
            }
        ));
        scrollVentDia.setViewportView(tablaVentasDia);

        javax.swing.GroupLayout panelVentasDiaLayout = new javax.swing.GroupLayout(panelVentasDia);
        panelVentasDia.setLayout(panelVentasDiaLayout);
        panelVentasDiaLayout.setHorizontalGroup(
            panelVentasDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollVentDia, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
        );
        panelVentasDiaLayout.setVerticalGroup(
            panelVentasDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollVentDia, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelEncaCompDia.setBackground(new java.awt.Color(36, 52, 81));

        jLabel6.setBackground(new java.awt.Color(51, 51, 51));
        jLabel6.setFont(new java.awt.Font("Californian FB", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoMenu.png"))); // NOI18N
        jLabel6.setText("                 Compras Del Dia");

        btnVerCompra.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnVerCompra.setForeground(new java.awt.Color(255, 255, 255));
        btnVerCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoVer.png"))); // NOI18N
        btnVerCompra.setText("VER");
        btnVerCompra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVerCompraMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVerCompraMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVerCompraMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelEncaCompDiaLayout = new javax.swing.GroupLayout(panelEncaCompDia);
        panelEncaCompDia.setLayout(panelEncaCompDiaLayout);
        panelEncaCompDiaLayout.setHorizontalGroup(
            panelEncaCompDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEncaCompDiaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(btnVerCompra, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelEncaCompDiaLayout.setVerticalGroup(
            panelEncaCompDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panelEncaCompDiaLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelEncaCompDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVerCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        tablaComprasDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "PROVEEDOR", "TOTAL", "HORA"
            }
        ));
        scrollCompDia.setViewportView(tablaComprasDia);

        javax.swing.GroupLayout panelComprasDiaLayout = new javax.swing.GroupLayout(panelComprasDia);
        panelComprasDia.setLayout(panelComprasDiaLayout);
        panelComprasDiaLayout.setHorizontalGroup(
            panelComprasDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollCompDia, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        panelComprasDiaLayout.setVerticalGroup(
            panelComprasDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollCompDia, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        panelNuevaVenta.setBackground(new java.awt.Color(255, 255, 255));
        panelNuevaVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelNuevaVentaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelNuevaVentaMouseExited(evt);
            }
        });

        txtNuevaVenta.setBackground(new java.awt.Color(255, 255, 255));
        txtNuevaVenta.setFont(new java.awt.Font("Californian FB", 1, 18)); // NOI18N
        txtNuevaVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoVenta.png"))); // NOI18N
        txtNuevaVenta.setText("      VENTA");
        txtNuevaVenta.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102)));
        txtNuevaVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNuevaVentaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtNuevaVentaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtNuevaVentaMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelNuevaVentaLayout = new javax.swing.GroupLayout(panelNuevaVenta);
        panelNuevaVenta.setLayout(panelNuevaVentaLayout);
        panelNuevaVentaLayout.setHorizontalGroup(
            panelNuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtNuevaVenta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
        );
        panelNuevaVentaLayout.setVerticalGroup(
            panelNuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtNuevaVenta)
        );

        panelNuevaCompra.setBackground(new java.awt.Color(255, 255, 255));
        panelNuevaCompra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelNuevaCompraMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelNuevaCompraMouseEntered(evt);
            }
        });

        txtNuevaCompra.setFont(new java.awt.Font("Californian FB", 1, 18)); // NOI18N
        txtNuevaCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoCompra.png"))); // NOI18N
        txtNuevaCompra.setText("    COMPRA");
        txtNuevaCompra.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102)));
        txtNuevaCompra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNuevaCompraMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtNuevaCompraMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtNuevaCompraMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelNuevaCompraLayout = new javax.swing.GroupLayout(panelNuevaCompra);
        panelNuevaCompra.setLayout(panelNuevaCompraLayout);
        panelNuevaCompraLayout.setHorizontalGroup(
            panelNuevaCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtNuevaCompra, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
        );
        panelNuevaCompraLayout.setVerticalGroup(
            panelNuevaCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNuevaCompraLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txtNuevaCompra))
        );

        panelNuevoProveedor.setBackground(new java.awt.Color(255, 255, 255));

        txtNuevoProveedor.setFont(new java.awt.Font("Californian FB", 1, 18)); // NOI18N
        txtNuevoProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevoProveedor.png"))); // NOI18N
        txtNuevoProveedor.setText("PROVEEDOR");
        txtNuevoProveedor.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102)));
        txtNuevoProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNuevoProveedorMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtNuevoProveedorMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtNuevoProveedorMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelNuevoProveedorLayout = new javax.swing.GroupLayout(panelNuevoProveedor);
        panelNuevoProveedor.setLayout(panelNuevoProveedorLayout);
        panelNuevoProveedorLayout.setHorizontalGroup(
            panelNuevoProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtNuevoProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelNuevoProveedorLayout.setVerticalGroup(
            panelNuevoProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNuevoProveedorLayout.createSequentialGroup()
                .addComponent(txtNuevoProveedor)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelContenedorInicioLayout = new javax.swing.GroupLayout(panelContenedorInicio);
        panelContenedorInicio.setLayout(panelContenedorInicioLayout);
        panelContenedorInicioLayout.setHorizontalGroup(
            panelContenedorInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContenedorInicioLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(panelContenedorInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelVentasDia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelEncabVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(44, 44, 44)
                .addGroup(panelContenedorInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelComprasDia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelEncaCompDia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(64, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContenedorInicioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelNuevaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(panelNuevaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(panelNuevoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        panelContenedorInicioLayout.setVerticalGroup(
            panelContenedorInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContenedorInicioLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panelContenedorInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelNuevaVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelNuevaCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelNuevoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(panelContenedorInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelEncabVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelEncaCompDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelContenedorInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelVentasDia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelComprasDia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelInicioLayout = new javax.swing.GroupLayout(panelInicio);
        panelInicio.setLayout(panelInicioLayout);
        panelInicioLayout.setHorizontalGroup(
            panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelContenedorInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelInicioLayout.setVerticalGroup(
            panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicioLayout.createSequentialGroup()
                .addComponent(panelContenedorInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelPrincipal.add(panelInicio, "card2");

        txtBuscarInventario.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        txtBuscarInventario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarInventarioKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarInventarioKeyReleased(evt);
            }
        });

        lblLupa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/busqueda.png"))); // NOI18N
        lblLupa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLupaMouseClicked(evt);
            }
        });

        btnModificarComponente.setBackground(new java.awt.Color(53, 144, 197));
        btnModificarComponente.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnModificarComponente.setForeground(new java.awt.Color(255, 255, 255));
        btnModificarComponente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoEditar.png"))); // NOI18N
        btnModificarComponente.setText("Modificar ");
        btnModificarComponente.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnModificarComponente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarComponenteActionPerformed(evt);
            }
        });

        btnEliminarComponente.setBackground(new java.awt.Color(176, 95, 95));
        btnEliminarComponente.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnEliminarComponente.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarComponente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoEliminar.png"))); // NOI18N
        btnEliminarComponente.setText("Eliminar");
        btnEliminarComponente.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminarComponente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarComponenteActionPerformed(evt);
            }
        });

        btnAgregarComponente.setBackground(new java.awt.Color(93, 160, 115));
        btnAgregarComponente.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnAgregarComponente.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarComponente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        btnAgregarComponente.setText("Agregar");
        btnAgregarComponente.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregarComponente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarComponenteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCrudBotonesLayout = new javax.swing.GroupLayout(panelCrudBotones);
        panelCrudBotones.setLayout(panelCrudBotonesLayout);
        panelCrudBotonesLayout.setHorizontalGroup(
            panelCrudBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrudBotonesLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(txtBuscarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLupa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAgregarComponente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnModificarComponente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminarComponente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        panelCrudBotonesLayout.setVerticalGroup(
            panelCrudBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCrudBotonesLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(panelCrudBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCrudBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnModificarComponente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminarComponente)
                        .addComponent(btnAgregarComponente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelCrudBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtBuscarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblLupa))))
        );

        lblListaI.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblListaI.setText("Inventario De Productos");

        tablaInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "CÓDIGO", "NOMBRE", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tablaInventario);

        btnImprimirInventario.setBackground(new java.awt.Color(64, 154, 102));
        btnImprimirInventario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnImprimirInventario.setForeground(new java.awt.Color(255, 255, 255));
        btnImprimirInventario.setText("Imprimir Lista");
        btnImprimirInventario.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnImprimirInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirInventarioActionPerformed(evt);
            }
        });

        lblIconoProductos1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono_impresora.png"))); // NOI18N

        btnMenudeo.setBackground(new java.awt.Color(64, 154, 102));
        btnMenudeo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnMenudeo.setForeground(new java.awt.Color(255, 255, 255));
        btnMenudeo.setText("Menudear");
        btnMenudeo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenudeoActionPerformed(evt);
            }
        });

        lblIconoProductos5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono_menudeo.png"))); // NOI18N

        btnModificarMichelada.setBackground(new java.awt.Color(64, 154, 102));
        btnModificarMichelada.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnModificarMichelada.setForeground(new java.awt.Color(255, 255, 255));
        btnModificarMichelada.setText("Modificar Michelada");
        btnModificarMichelada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarMicheladaActionPerformed(evt);
            }
        });

        lblIconoProductos6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono-cerveza.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 818, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(25, 25, 25))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addComponent(lblListaI)
                            .addGap(317, 317, 317)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lblIconoProductos1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnImprimirInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addComponent(lblIconoProductos5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMenudeo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addComponent(lblIconoProductos6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificarMichelada)
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(lblListaI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblIconoProductos1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImprimirInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMenudeo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblIconoProductos5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnModificarMichelada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblIconoProductos6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelInventarioLayout = new javax.swing.GroupLayout(panelInventario);
        panelInventario.setLayout(panelInventarioLayout);
        panelInventarioLayout.setHorizontalGroup(
            panelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCrudBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator3)
        );
        panelInventarioLayout.setVerticalGroup(
            panelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInventarioLayout.createSequentialGroup()
                .addComponent(panelCrudBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelPrincipal.add(panelInventario, "card2");

        btnDetalleVenta.setBackground(new java.awt.Color(53, 144, 197));
        btnDetalleVenta.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnDetalleVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnDetalleVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/busqueda.png"))); // NOI18N
        btnDetalleVenta.setText("Detalle Venta");
        btnDetalleVenta.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDetalleVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalleVentaActionPerformed(evt);
            }
        });

        lblListaI2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblListaI2.setText("Listado De Ventas");

        lblListaI8.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        lblListaI8.setText("Últimos 10 dias");

        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel2.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel2.setText("Buscar Por Fecha:");

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel3.setText("Inicial:");

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel4.setText("Final:");

        btnBuscarVentas.setBackground(new java.awt.Color(53, 144, 197));
        btnBuscarVentas.setText("ACEPTAR");
        btnBuscarVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarVentasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCrudBotones1Layout = new javax.swing.GroupLayout(panelCrudBotones1);
        panelCrudBotones1.setLayout(panelCrudBotones1Layout);
        panelCrudBotones1Layout.setHorizontalGroup(
            panelCrudBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrudBotones1Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(panelCrudBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCrudBotones1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaInicialVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                        .addComponent(jLabel4))
                    .addGroup(panelCrudBotones1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCrudBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fechaFinalVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarVentas))
                .addGap(74, 74, 74)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(panelCrudBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCrudBotones1Layout.createSequentialGroup()
                        .addComponent(lblListaI2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCrudBotones1Layout.createSequentialGroup()
                        .addComponent(lblListaI8, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)))
                .addComponent(btnDetalleVenta)
                .addGap(58, 58, 58))
        );
        panelCrudBotones1Layout.setVerticalGroup(
            panelCrudBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator8)
            .addGroup(panelCrudBotones1Layout.createSequentialGroup()
                .addGroup(panelCrudBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCrudBotones1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(btnDetalleVenta))
                    .addGroup(panelCrudBotones1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblListaI2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblListaI8))
                    .addGroup(panelCrudBotones1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(panelCrudBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(btnBuscarVentas))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCrudBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fechaInicialVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fechaFinalVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelCrudBotones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaVentasCreadas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "CÓDIGO", "NOMBRE", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(tablaVentasCreadas);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 818, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelVentasLayout = new javax.swing.GroupLayout(panelVentas);
        panelVentas.setLayout(panelVentasLayout);
        panelVentasLayout.setHorizontalGroup(
            panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCrudBotones1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelVentasLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jSeparator4)
        );
        panelVentasLayout.setVerticalGroup(
            panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVentasLayout.createSequentialGroup()
                .addComponent(panelCrudBotones1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        panelPrincipal.add(panelVentas, "card2");

        tablaComprasHechas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "CÓDIGO", "NOMBRE", "Title 3", "Title 4"
            }
        ));
        jScrollPane10.setViewportView(tablaComprasHechas);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 818, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnDetalleCompra.setBackground(new java.awt.Color(53, 144, 197));
        btnDetalleCompra.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnDetalleCompra.setForeground(new java.awt.Color(255, 255, 255));
        btnDetalleCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/busqueda.png"))); // NOI18N
        btnDetalleCompra.setText("Detalle Compra");
        btnDetalleCompra.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDetalleCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalleCompraActionPerformed(evt);
            }
        });

        lblListaI5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblListaI5.setText("Listado De Compras");

        lblListaI9.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        lblListaI9.setText("Últimos 10 Dias");

        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton2.setBackground(new java.awt.Color(53, 144, 197));
        jButton2.setText("ACEPTAR");

        jLabel9.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel9.setText("Final:");

        jLabel10.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel10.setText("Inicial:");

        jLabel11.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel11.setText("Buscar Por Fecha:");

        javax.swing.GroupLayout panelCrudBotones2Layout = new javax.swing.GroupLayout(panelCrudBotones2);
        panelCrudBotones2.setLayout(panelCrudBotones2Layout);
        panelCrudBotones2Layout.setHorizontalGroup(
            panelCrudBotones2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrudBotones2Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(panelCrudBotones2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCrudBotones2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9))
                    .addGroup(panelCrudBotones2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCrudBotones2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(22, 22, 22)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelCrudBotones2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCrudBotones2Layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(lblListaI5)
                        .addGap(46, 46, 46))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCrudBotones2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblListaI9)
                        .addGap(116, 116, 116)))
                .addComponent(btnDetalleCompra)
                .addGap(31, 31, 31))
        );
        panelCrudBotones2Layout.setVerticalGroup(
            panelCrudBotones2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCrudBotones2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDetalleCompra)
                .addGap(24, 24, 24))
            .addComponent(jSeparator9)
            .addGroup(panelCrudBotones2Layout.createSequentialGroup()
                .addGroup(panelCrudBotones2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCrudBotones2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblListaI5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblListaI9))
                    .addGroup(panelCrudBotones2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(panelCrudBotones2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCrudBotones2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelCrudBotones2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(jLabel9)))))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelComprasLayout = new javax.swing.GroupLayout(panelCompras);
        panelCompras.setLayout(panelComprasLayout);
        panelComprasLayout.setHorizontalGroup(
            panelComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCrudBotones2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator5)
        );
        panelComprasLayout.setVerticalGroup(
            panelComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelComprasLayout.createSequentialGroup()
                .addComponent(panelCrudBotones2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelPrincipal.add(panelCompras, "card2");

        lblListaI6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblListaI6.setText("Listado De Proveedores");

        tablaListaProveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "NOMBRE", "NIT", "TELÉFONO"
            }
        ));
        jScrollPane11.setViewportView(tablaListaProveedores);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 818, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(lblListaI6)
                        .addGap(327, 327, 327))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(lblListaI6)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        btnModificarProveedor.setBackground(new java.awt.Color(53, 144, 197));
        btnModificarProveedor.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnModificarProveedor.setForeground(new java.awt.Color(255, 255, 255));
        btnModificarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoEditar.png"))); // NOI18N
        btnModificarProveedor.setText("Modificar ");
        btnModificarProveedor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnModificarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarProveedorActionPerformed(evt);
            }
        });

        btnEliminarProveedor.setBackground(new java.awt.Color(176, 95, 95));
        btnEliminarProveedor.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnEliminarProveedor.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoEliminar.png"))); // NOI18N
        btnEliminarProveedor.setText("Eliminar");
        btnEliminarProveedor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProveedorActionPerformed(evt);
            }
        });

        btnAgregaProveedor.setBackground(new java.awt.Color(93, 160, 115));
        btnAgregaProveedor.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnAgregaProveedor.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregaProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        btnAgregaProveedor.setText("Agregar");
        btnAgregaProveedor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregaProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregaProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCrudBotones3Layout = new javax.swing.GroupLayout(panelCrudBotones3);
        panelCrudBotones3.setLayout(panelCrudBotones3Layout);
        panelCrudBotones3Layout.setHorizontalGroup(
            panelCrudBotones3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrudBotones3Layout.createSequentialGroup()
                .addContainerGap(85, Short.MAX_VALUE)
                .addComponent(btnAgregaProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnModificarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        panelCrudBotones3Layout.setVerticalGroup(
            panelCrudBotones3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrudBotones3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(panelCrudBotones3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificarProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminarProveedor)
                    .addComponent(btnAgregaProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout panelProveedoresLayout = new javax.swing.GroupLayout(panelProveedores);
        panelProveedores.setLayout(panelProveedoresLayout);
        panelProveedoresLayout.setHorizontalGroup(
            panelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator6)
            .addGroup(panelProveedoresLayout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(panelCrudBotones3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelProveedoresLayout.setVerticalGroup(
            panelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProveedoresLayout.createSequentialGroup()
                .addComponent(panelCrudBotones3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelPrincipal.add(panelProveedores, "card2");

        panelEncabezado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblIngreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono_surtir_negocio.png"))); // NOI18N

        lblTitulo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitulo.setText("NUEVA COMPRA");

        lblNumero.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNumero.setText("N° :");

        lblFecha.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblFecha.setText("Fecha :");

        lblNumeroCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNumeroCompra.setText("Error");

        lblFechaCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblFechaCompra.setText("Error");

        javax.swing.GroupLayout panelEncabezadoLayout = new javax.swing.GroupLayout(panelEncabezado);
        panelEncabezado.setLayout(panelEncabezadoLayout);
        panelEncabezadoLayout.setHorizontalGroup(
            panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEncabezadoLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(lblIngreso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblFecha)
                    .addComponent(lblNumero))
                .addGap(18, 18, 18)
                .addGroup(panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFechaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNumeroCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55))
        );
        panelEncabezadoLayout.setVerticalGroup(
            panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEncabezadoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(panelEncabezadoLayout.createSequentialGroup()
                        .addGroup(panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNumero)
                            .addComponent(lblNumeroCompra))
                        .addGap(19, 19, 19)
                        .addGroup(panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFecha)
                            .addComponent(lblFechaCompra))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addComponent(lblIngreso, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
        );

        lblProveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono_usuario_corbata.png"))); // NOI18N

        lblIconoProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono_alcohol.png"))); // NOI18N

        lblObservaciones.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblObservaciones.setText("Observaciones:");

        txtObservacionesCompra.setColumns(20);
        txtObservacionesCompra.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        txtObservacionesCompra.setRows(5);
        jScrollPane1.setViewportView(txtObservacionesCompra);

        tablaResumenCompra.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tablaResumenCompra);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        lblObservaciones2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblObservaciones2.setText("Resumen De La Compra");

        lblObservaciones5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblObservaciones5.setText("TOTAL:");

        txtTotalResumenCompra.setBackground(new java.awt.Color(255, 255, 255));
        txtTotalResumenCompra.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        txtTotalResumenCompra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnLimpiarCompra.setBackground(new java.awt.Color(68, 151, 144));
        btnLimpiarCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLimpiarCompra.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarCompra.setText("Limpiar Tabla");
        btnLimpiarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarCompraActionPerformed(evt);
            }
        });

        combProviders.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        combProviders.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione el proveedor..." }));

        btnAgregarProductosCompra.setBackground(new java.awt.Color(20, 60, 108));
        btnAgregarProductosCompra.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        btnAgregarProductosCompra.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProductosCompra.setText("Agregar Productos");
        btnAgregarProductosCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductosCompraActionPerformed(evt);
            }
        });

        btnCancelarCompra.setBackground(new java.awt.Color(238, 91, 86));
        btnCancelarCompra.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        btnCancelarCompra.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelarCompra.setText("CANCELAR");
        btnCancelarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarCompraActionPerformed(evt);
            }
        });

        btnCrearCompra.setBackground(new java.awt.Color(55, 128, 63));
        btnCrearCompra.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        btnCrearCompra.setForeground(new java.awt.Color(255, 255, 255));
        btnCrearCompra.setText("CREAR COMPRA");
        btnCrearCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearCompraActionPerformed(evt);
            }
        });

        btnRemover2.setBackground(new java.awt.Color(238, 91, 86));
        btnRemover2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRemover2.setForeground(new java.awt.Color(255, 255, 255));
        btnRemover2.setText("Remover");
        btnRemover2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemover2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCrearCompraLayout = new javax.swing.GroupLayout(panelCrearCompra);
        panelCrearCompra.setLayout(panelCrearCompraLayout);
        panelCrearCompraLayout.setHorizontalGroup(
            panelCrearCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrearCompraLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(panelCrearCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCrearCompraLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(panelCrearCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelCrearCompraLayout.createSequentialGroup()
                                .addGroup(panelCrearCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblObservaciones5)
                                    .addGroup(panelCrearCompraLayout.createSequentialGroup()
                                        .addComponent(btnCancelarCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(64, 64, 64)
                                        .addComponent(btnCrearCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(31, 31, 31)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotalResumenCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelCrearCompraLayout.createSequentialGroup()
                        .addComponent(lblObservaciones)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelCrearCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCrearCompraLayout.createSequentialGroup()
                                .addGap(142, 142, 142)
                                .addComponent(lblObservaciones2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRemover2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLimpiarCompra))
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCrearCompraLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblProveedores)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(combProviders, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(lblIconoProductos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAgregarProductosCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(panelEncabezado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(34, 34, 34))
        );
        panelCrearCompraLayout.setVerticalGroup(
            panelCrearCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrearCompraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(panelCrearCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblIconoProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblProveedores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(combProviders)
                    .addComponent(btnAgregarProductosCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelCrearCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCrearCompraLayout.createSequentialGroup()
                        .addComponent(lblObservaciones)
                        .addGap(39, 39, 39))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(panelCrearCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblObservaciones2)
                    .addComponent(btnLimpiarCompra)
                    .addComponent(btnRemover2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(panelCrearCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalResumenCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblObservaciones5))
                .addGap(18, 18, 18)
                .addGroup(panelCrearCompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCrearCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelarCompra))
                .addGap(31, 31, 31))
        );

        panelPrincipal.add(panelCrearCompra, "card7");

        panelEncabezado1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblIngreso1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono_carrito_compras2.png"))); // NOI18N

        lblTitulo1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitulo1.setText("NUEVA VENTA");

        checDentro.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        checDentro.setText("Venta Dentro");

        checFuera.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        checFuera.setText("Venta Fuera");

        lblFecha2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblFecha2.setText("Fecha :");

        lblNumero2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNumero2.setText("N° :");

        lblNumeroVenta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNumeroVenta.setText("Error");

        lblFechaVenta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblFechaVenta.setText("Error");

        javax.swing.GroupLayout panelEncabezado1Layout = new javax.swing.GroupLayout(panelEncabezado1);
        panelEncabezado1.setLayout(panelEncabezado1Layout);
        panelEncabezado1Layout.setHorizontalGroup(
            panelEncabezado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEncabezado1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(lblIngreso1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelEncabezado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checDentro)
                    .addComponent(checFuera))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addGroup(panelEncabezado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblFecha2)
                    .addComponent(lblNumero2))
                .addGap(18, 18, 18)
                .addGroup(panelEncabezado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNumeroVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44))
        );
        panelEncabezado1Layout.setVerticalGroup(
            panelEncabezado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEncabezado1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addGroup(panelEncabezado1Layout.createSequentialGroup()
                .addGroup(panelEncabezado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelEncabezado1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblIngreso1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(panelEncabezado1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(panelEncabezado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelEncabezado1Layout.createSequentialGroup()
                                .addGroup(panelEncabezado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblNumero2)
                                    .addComponent(lblNumeroVenta)
                                    .addComponent(checDentro))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelEncabezado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblFecha2)
                                    .addComponent(lblFechaVenta)))
                            .addGroup(panelEncabezado1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(checFuera)))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        lblProveedores1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono_usuario_corbata.png"))); // NOI18N

        lblIconoProductos2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono_alcohol.png"))); // NOI18N

        lblObservaciones1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblObservaciones1.setText("Observaciones:");

        txtObservacionesVenta.setColumns(20);
        txtObservacionesVenta.setRows(5);
        jScrollPane4.setViewportView(txtObservacionesVenta);

        lblObservaciones3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblObservaciones3.setText("TOTAL:");

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
        jScrollPane5.setViewportView(tablaDetalleVenta);

        lblObservaciones4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblObservaciones4.setText("Resumen De La Venta");

        txtTotalResumenVenta.setBackground(new java.awt.Color(255, 255, 255));
        txtTotalResumenVenta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnLimpiarTabla.setBackground(new java.awt.Color(68, 151, 144));
        btnLimpiarTabla.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLimpiarTabla.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarTabla.setText("Limpiar Tabla");
        btnLimpiarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarTablaActionPerformed(evt);
            }
        });

        btnRemover.setBackground(new java.awt.Color(238, 91, 86));
        btnRemover.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRemover.setForeground(new java.awt.Color(255, 255, 255));
        btnRemover.setText("Remover");
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        btnAgregarCliente.setBackground(new java.awt.Color(20, 60, 108));
        btnAgregarCliente.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        btnAgregarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarCliente.setText("Agregar Cliente");
        btnAgregarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarClienteActionPerformed(evt);
            }
        });

        btnAgregarProdVenta.setBackground(new java.awt.Color(20, 60, 108));
        btnAgregarProdVenta.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        btnAgregarProdVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProdVenta.setText("Agregar Productos");
        btnAgregarProdVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProdVentaActionPerformed(evt);
            }
        });

        btnCancelarVenta.setBackground(new java.awt.Color(238, 91, 86));
        btnCancelarVenta.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        btnCancelarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelarVenta.setText("CANCELAR");
        btnCancelarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarVentaActionPerformed(evt);
            }
        });

        btnCrearVenta.setBackground(new java.awt.Color(55, 128, 63));
        btnCrearVenta.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        btnCrearVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnCrearVenta.setText("CREAR VENTA");
        btnCrearVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearVentaActionPerformed(evt);
            }
        });

        btnMichelada.setBackground(new java.awt.Color(102, 102, 255));
        btnMichelada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnMichelada.setForeground(new java.awt.Color(255, 255, 255));
        btnMichelada.setText("Michelada");
        btnMichelada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMicheladaActionPerformed(evt);
            }
        });

        lblIconoProductos3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Scanner.png"))); // NOI18N

        txtCode.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        txtCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodeKeyReleased(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 51, 51));
        jButton1.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Borrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel12.setText("Escanear Código");

        javax.swing.GroupLayout panelCrearVentaLayout = new javax.swing.GroupLayout(panelCrearVenta);
        panelCrearVenta.setLayout(panelCrearVentaLayout);
        panelCrearVentaLayout.setHorizontalGroup(
            panelCrearVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrearVentaLayout.createSequentialGroup()
                .addGap(250, 250, 250)
                .addComponent(btnCancelarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(btnCrearVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelCrearVentaLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(panelCrearVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelCrearVentaLayout.createSequentialGroup()
                        .addGroup(panelCrearVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelCrearVentaLayout.createSequentialGroup()
                                .addComponent(btnMichelada)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblObservaciones4)
                                .addGap(87, 87, 87)
                                .addComponent(btnRemover)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLimpiarTabla))
                            .addComponent(jScrollPane5))
                        .addGap(48, 48, 48))
                    .addGroup(panelCrearVentaLayout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addGroup(panelCrearVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelEncabezado1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelCrearVentaLayout.createSequentialGroup()
                                .addComponent(lblProveedores1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAgregarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(lblIconoProductos2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAgregarProdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(lblIconoProductos3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(47, 47, 47))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCrearVentaLayout.createSequentialGroup()
                        .addComponent(lblObservaciones1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 49, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCrearVentaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelCrearVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCrearVentaLayout.createSequentialGroup()
                        .addComponent(lblObservaciones3)
                        .addGap(18, 18, 18)
                        .addComponent(txtTotalResumenVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCrearVentaLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(156, 156, 156))))
        );
        panelCrearVentaLayout.setVerticalGroup(
            panelCrearVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrearVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelEncabezado1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addGap(5, 5, 5)
                .addGroup(panelCrearVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelCrearVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblIconoProductos2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAgregarProdVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAgregarCliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblIconoProductos3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblProveedores1)
                    .addGroup(panelCrearVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)))
                .addGap(18, 18, 18)
                .addGroup(panelCrearVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCrearVentaLayout.createSequentialGroup()
                        .addComponent(lblObservaciones1)
                        .addGap(41, 41, 41))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(panelCrearVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimpiarTabla)
                    .addComponent(lblObservaciones4)
                    .addComponent(btnRemover)
                    .addComponent(btnMichelada))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCrearVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTotalResumenVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblObservaciones3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelCrearVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancelarVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCrearVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32))
        );

        panelPrincipal.add(panelCrearVenta, "card2");

        lblListaI7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblListaI7.setText("Productos Registrados Con Scanner");

        tablaListaScanner.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "NOMBRE", "NIT", "TELÉFONO"
            }
        ));
        jScrollPane12.setViewportView(tablaListaScanner);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 818, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(lblListaI7)
                        .addGap(250, 250, 250))))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(lblListaI7)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        btnEliminarScanner.setBackground(new java.awt.Color(176, 95, 95));
        btnEliminarScanner.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnEliminarScanner.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarScanner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoEliminar.png"))); // NOI18N
        btnEliminarScanner.setText("Eliminar");
        btnEliminarScanner.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminarScanner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarScannerActionPerformed(evt);
            }
        });

        btnAgregaScanner.setBackground(new java.awt.Color(93, 160, 115));
        btnAgregaScanner.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnAgregaScanner.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregaScanner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        btnAgregaScanner.setText("Agregar");
        btnAgregaScanner.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregaScanner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregaScannerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCrudBotones4Layout = new javax.swing.GroupLayout(panelCrudBotones4);
        panelCrudBotones4.setLayout(panelCrudBotones4Layout);
        panelCrudBotones4Layout.setHorizontalGroup(
            panelCrudBotones4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrudBotones4Layout.createSequentialGroup()
                .addContainerGap(161, Short.MAX_VALUE)
                .addComponent(btnAgregaScanner, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(btnEliminarScanner, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79))
        );
        panelCrudBotones4Layout.setVerticalGroup(
            panelCrudBotones4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCrudBotones4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(panelCrudBotones4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminarScanner)
                    .addComponent(btnAgregaScanner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout panelScannerLayout = new javax.swing.GroupLayout(panelScanner);
        panelScanner.setLayout(panelScannerLayout);
        panelScannerLayout.setHorizontalGroup(
            panelScannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator7)
            .addGroup(panelScannerLayout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(panelCrudBotones4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelScannerLayout.setVerticalGroup(
            panelScannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelScannerLayout.createSequentialGroup()
                .addComponent(panelCrudBotones4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelPrincipal.add(panelScanner, "card2");

        lblCategorias.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        lblCategorias.setText("  Categorías");
        lblCategorias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCategoriasMouseClicked(evt);
            }
        });

        lblMarcas.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        lblMarcas.setText("       Marcas");
        lblMarcas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMarcasMouseClicked(evt);
            }
        });

        lblUnidades.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        lblUnidades.setText("    Unidades");
        lblUnidades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblUnidadesMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelEncabAjustesLayout = new javax.swing.GroupLayout(panelEncabAjustes);
        panelEncabAjustes.setLayout(panelEncabAjustesLayout);
        panelEncabAjustesLayout.setHorizontalGroup(
            panelEncabAjustesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEncabAjustesLayout.createSequentialGroup()
                .addGap(225, 225, 225)
                .addComponent(lblCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(lblMarcas, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelEncabAjustesLayout.setVerticalGroup(
            panelEncabAjustesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEncabAjustesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblCategorias, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addComponent(lblMarcas, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addComponent(lblUnidades, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
        );

        panelAjustesElementos.setLayout(new java.awt.CardLayout());

        btnModificarMarca.setBackground(new java.awt.Color(53, 144, 197));
        btnModificarMarca.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnModificarMarca.setForeground(new java.awt.Color(255, 255, 255));
        btnModificarMarca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoEditar.png"))); // NOI18N
        btnModificarMarca.setText("Modificar ");
        btnModificarMarca.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnModificarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarMarcaActionPerformed(evt);
            }
        });

        btnEliminarMarca.setBackground(new java.awt.Color(176, 95, 95));
        btnEliminarMarca.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnEliminarMarca.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarMarca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoEliminar.png"))); // NOI18N
        btnEliminarMarca.setText("Eliminar");
        btnEliminarMarca.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarMarcaActionPerformed(evt);
            }
        });

        btnAgregarMarca.setBackground(new java.awt.Color(93, 160, 115));
        btnAgregarMarca.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnAgregarMarca.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarMarca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        btnAgregarMarca.setText("Agregar");
        btnAgregarMarca.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarMarcaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(btnAgregarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnModificarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminarMarca)
                    .addComponent(btnAgregarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lblListaI1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblListaI1.setText("Listado De Marcas");

        tablaMarcas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "CÓDIGO", "NOMBRE", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(tablaMarcas);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 811, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(370, 370, 370)
                .addComponent(lblListaI1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(lblListaI1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelAjustesMarcasLayout = new javax.swing.GroupLayout(panelAjustesMarcas);
        panelAjustesMarcas.setLayout(panelAjustesMarcasLayout);
        panelAjustesMarcasLayout.setHorizontalGroup(
            panelAjustesMarcasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAjustesMarcasLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAjustesMarcasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(132, 132, 132))
        );
        panelAjustesMarcasLayout.setVerticalGroup(
            panelAjustesMarcasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAjustesMarcasLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelAjustesElementos.add(panelAjustesMarcas, "card2");

        btnModificarUnidades.setBackground(new java.awt.Color(53, 144, 197));
        btnModificarUnidades.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnModificarUnidades.setForeground(new java.awt.Color(255, 255, 255));
        btnModificarUnidades.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoEditar.png"))); // NOI18N
        btnModificarUnidades.setText("Modificar ");
        btnModificarUnidades.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnModificarUnidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarUnidadesActionPerformed(evt);
            }
        });

        btnEliminarUnidad.setBackground(new java.awt.Color(176, 95, 95));
        btnEliminarUnidad.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnEliminarUnidad.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarUnidad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoEliminar.png"))); // NOI18N
        btnEliminarUnidad.setText("Eliminar");
        btnEliminarUnidad.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminarUnidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarUnidadActionPerformed(evt);
            }
        });

        btnAgregarUnidades.setBackground(new java.awt.Color(93, 160, 115));
        btnAgregarUnidades.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnAgregarUnidades.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarUnidades.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        btnAgregarUnidades.setText("Agregar");
        btnAgregarUnidades.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregarUnidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarUnidadesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(btnAgregarUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnModificarUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminarUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificarUnidades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminarUnidad)
                    .addComponent(btnAgregarUnidades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        lblListaI3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblListaI3.setText("Listado De Unidades De Medida");

        tablaUnidades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "CÓDIGO", "NOMBRE", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(tablaUnidades);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 811, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(289, 289, 289)
                .addComponent(lblListaI3, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(lblListaI3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelAjustesUnidadesLayout = new javax.swing.GroupLayout(panelAjustesUnidades);
        panelAjustesUnidades.setLayout(panelAjustesUnidadesLayout);
        panelAjustesUnidadesLayout.setHorizontalGroup(
            panelAjustesUnidadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAjustesUnidadesLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAjustesUnidadesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(136, 136, 136))
        );
        panelAjustesUnidadesLayout.setVerticalGroup(
            panelAjustesUnidadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAjustesUnidadesLayout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelAjustesElementos.add(panelAjustesUnidades, "card3");

        btnModificarCategoria.setBackground(new java.awt.Color(53, 144, 197));
        btnModificarCategoria.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnModificarCategoria.setForeground(new java.awt.Color(255, 255, 255));
        btnModificarCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoEditar.png"))); // NOI18N
        btnModificarCategoria.setText("Modificar ");
        btnModificarCategoria.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnModificarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarCategoriaActionPerformed(evt);
            }
        });

        btnEliminarCategoria.setBackground(new java.awt.Color(176, 95, 95));
        btnEliminarCategoria.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnEliminarCategoria.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoEliminar.png"))); // NOI18N
        btnEliminarCategoria.setText("Eliminar");
        btnEliminarCategoria.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarCategoriaActionPerformed(evt);
            }
        });

        btnAgregarCategorias.setBackground(new java.awt.Color(93, 160, 115));
        btnAgregarCategorias.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        btnAgregarCategorias.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarCategorias.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        btnAgregarCategorias.setText("Agregar");
        btnAgregarCategorias.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregarCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCategoriasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(btnAgregarCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnModificarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificarCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminarCategoria)
                    .addComponent(btnAgregarCategorias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lblListaI4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblListaI4.setText("Listado De Categorías");

        tablaCategorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "CÓDIGO", "NOMBRE", "Title 3", "Title 4"
            }
        ));
        jScrollPane9.setViewportView(tablaCategorias);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 811, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addComponent(lblListaI4)
                        .addGap(327, 327, 327))))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(lblListaI4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelAjustesCategoriasLayout = new javax.swing.GroupLayout(panelAjustesCategorias);
        panelAjustesCategorias.setLayout(panelAjustesCategoriasLayout);
        panelAjustesCategoriasLayout.setHorizontalGroup(
            panelAjustesCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAjustesCategoriasLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelAjustesCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelAjustesCategoriasLayout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        panelAjustesCategoriasLayout.setVerticalGroup(
            panelAjustesCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAjustesCategoriasLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelAjustesElementos.add(panelAjustesCategorias, "card4");

        javax.swing.GroupLayout panelAjustesLayout = new javax.swing.GroupLayout(panelAjustes);
        panelAjustes.setLayout(panelAjustesLayout);
        panelAjustesLayout.setHorizontalGroup(
            panelAjustesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelEncabAjustes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAjustesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelAjustesElementos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelAjustesLayout.setVerticalGroup(
            panelAjustesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAjustesLayout.createSequentialGroup()
                .addComponent(panelEncabAjustes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAjustesElementos, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelPrincipal.add(panelAjustes, "card2");

        getContentPane().add(panelPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 880, 500));

        setBounds(0, 0, 1040, 660);
    }// </editor-fold>//GEN-END:initComponents

    private void txtInicioMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtInicioMouseReleased
        
    }//GEN-LAST:event_txtInicioMouseReleased

    private void txtInicioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtInicioMouseEntered
        this.logica.crearInteractividadBotones(this.txtInicio, true);
    }//GEN-LAST:event_txtInicioMouseEntered

    private void txtInicioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtInicioMouseExited
        this.logica.crearInteractividadBotones(this.txtInicio, false);
    }//GEN-LAST:event_txtInicioMouseExited

    private void txtInventarioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtInventarioMouseEntered
        this.logica.crearInteractividadBotones(this.txtInventario, true);
    }//GEN-LAST:event_txtInventarioMouseEntered

    private void txtInventarioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtInventarioMouseExited
        this.logica.crearInteractividadBotones(this.txtInventario, false);
    }//GEN-LAST:event_txtInventarioMouseExited

    private void txtVentasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtVentasMouseEntered
        this.logica.crearInteractividadBotones(this.txtVentas, true);
    }//GEN-LAST:event_txtVentasMouseEntered

    private void txtVentasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtVentasMouseExited
        this.logica.crearInteractividadBotones(this.txtVentas, false);
    }//GEN-LAST:event_txtVentasMouseExited

    private void txtComprasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtComprasMouseEntered
        this.logica.crearInteractividadBotones(this.txtCompras, true);
    }//GEN-LAST:event_txtComprasMouseEntered

    private void txtComprasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtComprasMouseExited
        this.logica.crearInteractividadBotones(this.txtCompras, false);
    }//GEN-LAST:event_txtComprasMouseExited

    private void txtSalirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSalirMouseEntered
        this.logica.crearInteractividadBotones(this.txtSalir, true);
    }//GEN-LAST:event_txtSalirMouseEntered

    private void txtSalirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSalirMouseExited
        this.logica.crearInteractividadBotones(this.txtSalir, false);
    }//GEN-LAST:event_txtSalirMouseExited

    private void txtInicioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtInicioMouseClicked
        this.logica.modificarEncabezado(this.txtInicio, 1, this.txtEncabezado);  
        this.logica.refrescarContenido(this.panelPrincipal, this.panelInicio);     
    }//GEN-LAST:event_txtInicioMouseClicked

    private void minimMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimMouseClicked
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_minimMouseClicked

    private void salirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salirMouseClicked
        this.logica.comprobarSalida();        
    }//GEN-LAST:event_salirMouseClicked

    private void txtInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtInventarioMouseClicked
        this.logica.modificarEncabezado(this.txtInventario, 2, this.txtEncabezado);        
//        this.paintTableInventario();   
        this.pintor.paintTableInventory(this.modeloInventario, 
                            this.tequilazo.getInventario().getComponentes(), false);
        this.logica.refrescarContenido(this.panelPrincipal, this.panelInventario);
    }//GEN-LAST:event_txtInventarioMouseClicked

    private void txtVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtVentasMouseClicked
        this.logica.modificarEncabezado(this.txtVentas, 3, this.txtEncabezado);
        this.logica.refrescarContenido(this.panelPrincipal, this.panelVentas);
        this.pintor.paintTableSales(this.tequilazo.getVentas(), this.modeloVentasCreadas);        
    }//GEN-LAST:event_txtVentasMouseClicked

    private void txtComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtComprasMouseClicked
        this.logica.modificarEncabezado(this.txtCompras, 4, this.txtEncabezado);
        this.logica.refrescarContenido(this.panelPrincipal, this.panelCompras);     
        this.pintor.paintTablePurchases(this.tequilazo.getCompras(), this.modeloComprasHechas);
    }//GEN-LAST:event_txtComprasMouseClicked

    private void txtSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSalirMouseClicked
        this.logica.comprobarSalida();
    }//GEN-LAST:event_txtSalirMouseClicked

    private void txtProveedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtProveedoresMouseClicked
        this.pintor.paintTableProviders(this.tequilazo.getProveedores(), this.modeloListaProveedores);
        this.logica.modificarEncabezado(this.txtProveedores, 5, this.txtEncabezado);
        this.logica.refrescarContenido(this.panelPrincipal, this.panelProveedores);
    }//GEN-LAST:event_txtProveedoresMouseClicked

    private void txtProveedoresMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtProveedoresMouseEntered
        this.logica.crearInteractividadBotones(this.txtProveedores, true);
    }//GEN-LAST:event_txtProveedoresMouseEntered

    private void txtProveedoresMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtProveedoresMouseExited
        this.logica.crearInteractividadBotones(this.txtProveedores, false);
    }//GEN-LAST:event_txtProveedoresMouseExited

    private void txtNuevaCompraMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNuevaCompraMouseEntered
	this.logica.crearInteractividadBotones(this.panelNuevaCompra, this.txtNuevaCompra, true);
	}//GEN-LAST:event_txtNuevaCompraMouseEntered

    private void panelNuevaVentaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelNuevaVentaMouseEntered
        this.logica.crearInteractividadBotones(this.panelNuevaVenta, this.txtNuevaVenta, true);
    }//GEN-LAST:event_panelNuevaVentaMouseEntered

    private void panelNuevaVentaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelNuevaVentaMouseExited
        this.logica.crearInteractividadBotones(this.panelNuevaVenta, this.txtNuevaVenta, false);
    }//GEN-LAST:event_panelNuevaVentaMouseExited

    private void panelNuevaCompraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelNuevaCompraMouseClicked
    }//GEN-LAST:event_panelNuevaCompraMouseClicked

    private void panelNuevaCompraMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelNuevaCompraMouseEntered
        this.logica.crearInteractividadBotones(this.panelNuevaCompra, this.txtNuevaCompra, true);
    }//GEN-LAST:event_panelNuevaCompraMouseEntered

    private void txtNuevaCompraMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNuevaCompraMouseExited
        this.logica.crearInteractividadBotones(this.panelNuevaCompra, this.txtNuevaCompra, false);
    }//GEN-LAST:event_txtNuevaCompraMouseExited

    private void txtNuevoProveedorMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNuevoProveedorMouseEntered
        this.logica.crearInteractividadBotones(this.panelNuevoProveedor, this.txtNuevoProveedor, true);
    }//GEN-LAST:event_txtNuevoProveedorMouseEntered

    private void txtNuevoProveedorMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNuevoProveedorMouseExited
        this.logica.crearInteractividadBotones(this.panelNuevoProveedor, this.txtNuevoProveedor, false);
    }//GEN-LAST:event_txtNuevoProveedorMouseExited

    private void txtNuevoProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNuevoProveedorMouseClicked
       this.logica.crearInteractividadBotones(this.panelNuevoProveedor, this.txtNuevoProveedor, false);
       this.addProveedor();
    }//GEN-LAST:event_txtNuevoProveedorMouseClicked

    private void lblLupaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLupaMouseClicked
        //System.out.println("Metodo Buscar");
    }//GEN-LAST:event_lblLupaMouseClicked

    private void btnModificarComponenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarComponenteActionPerformed
        String codeElement = this.gt.getCodeElementFromTable(this.tablaInventario);
        int control = this.logica.productController.updateProduct
                                (codeElement, this.pintor, this.modeloInventario);
        //GestorMensajes mensaje = new GestorMensajes();
        String alerta = this.gm.getMessageUpdateProduct(control);
        if(!alerta.equals(""))
            JOptionPane.showMessageDialog(this, alerta);        
    }//GEN-LAST:event_btnModificarComponenteActionPerformed

    private void btnImprimirInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirInventarioActionPerformed
        String fechaArch = this.tequilazo.getLdt().getYear() + "-" + 
                this.tequilazo.getLdt().getMonthValue() + "-" + this.tequilazo.getLdt().getDayOfMonth();
        String ruta = this.tequilazo.getReporte().seleccionarDestino(fechaArch);
        if(ruta != null)
        {
            this.tequilazo.getReporte().setRuta(ruta);
            boolean b = this.tequilazo.getReporte().generarInventario
                        (this.tequilazo.getInventario()); 
            if(b)
                JOptionPane.showMessageDialog(this, "Reporte Generado Correctamente");
            else
                JOptionPane.showMessageDialog(this, "No Se Ha Podido Generar El Reporte");
        }
        
    }//GEN-LAST:event_btnImprimirInventarioActionPerformed

    private void txtNuevaCompraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNuevaCompraMouseClicked
        this.logica.crearInteractividadBotones(this.panelNuevaCompra, this.txtNuevaCompra, false);
        //Para Limpiar la tabla
        this.componentesNuevos.clear();
        this.txtTotalResumenCompra.setText("0");
        this.pintor.clearDataFromTable(this.modeloResumenCompra);
        //Modificar el número de la factura y la fecha y hora.
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MM/dd '-' hh:mm a");
        LocalDateTime fechaHora = LocalDateTime.now();
        String fh = formato.format(fechaHora);
        this.lblFechaCompra.setText(fh);
        int numCompras = this.tequilazo.getBd().getCrudCompra().getCount() + 1;
        if(numCompras == 0)
            JOptionPane.showMessageDialog(this, "Error Consultando El Número De Compras Del Negocio");
        else        
            this.lblNumeroCompra.setText(numCompras + "");
        
        this.logica.refrescarContenido(this.panelPrincipal, this.panelCrearCompra);
    }//GEN-LAST:event_txtNuevaCompraMouseClicked

    
    private void txtNuevaVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNuevaVentaMouseClicked
        this.logica.crearInteractividadBotones(this.panelNuevaVenta, this.txtNuevaVenta, false);
        //Para Limpiar la tabla
        this.componentesNuevos.clear();
        this.txtTotalResumenVenta.setText("0");
        this.pintor.clearDataFromTable(this.modeloResumenVenta);           
        //Modificar el número de la factura y la fecha y hora.
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MM/dd '-' hh:mm a");
        LocalDateTime fechaHora = LocalDateTime.now();
        String fh = formato.format(fechaHora);
        this.lblFechaVenta.setText(fh);
        int numVentas = this.tequilazo.getBd().getCrudVenta().getCount() + 1;
        if(numVentas == 0)
            JOptionPane.showMessageDialog(this, "Error Consultando El Número De Ventas Del Negocio");
        else
        {
            this.lblNumeroVenta.setText(numVentas + "");
        }
        
        this.logica.refrescarContenido(this.panelPrincipal, this.panelCrearVenta);//Panel para introducir Venta Nueva.
    }//GEN-LAST:event_txtNuevaVentaMouseClicked

    private void txtNuevaVentaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNuevaVentaMouseEntered
        this.logica.crearInteractividadBotones(this.panelNuevaVenta, this.txtNuevaVenta, true);
    }//GEN-LAST:event_txtNuevaVentaMouseEntered

    private void txtNuevaVentaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNuevaVentaMouseExited
        this.logica.crearInteractividadBotones(this.panelNuevaVenta, this.txtNuevaVenta, false);
    }//GEN-LAST:event_txtNuevaVentaMouseExited

    private void btnAgregarComponenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarComponenteActionPerformed
        int control = this.logica.productController.createProduct
                                            (this.pintor, this.modeloInventario);
        String rpta = new GestorMensajes().getMessageCreateProduct(control);
        if(!rpta.equals(""))
            JOptionPane.showMessageDialog(this, rpta);
    }//GEN-LAST:event_btnAgregarComponenteActionPerformed

    private void txtAjustesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAjustesMouseEntered
        this.logica.crearInteractividadBotones(this.txtAjustes, true);
    }//GEN-LAST:event_txtAjustesMouseEntered

    private void txtAjustesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAjustesMouseExited
        this.logica.crearInteractividadBotones(this.txtAjustes, false);
    }//GEN-LAST:event_txtAjustesMouseExited

    private void txtAjustesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAjustesMouseClicked
        this.ajuste = 'c';
        this.logica.modificarEncabezado(this.txtAjustes, 6, this.txtEncabezado);
        this.logica.refrescarContenido(this.panelPrincipal, this.panelAjustes);
        this.logica.crearInteractividadBotones(this.lblCategorias, true);
        this.logica.crearInteractividadBotones(this.lblMarcas, false);
        this.logica.crearInteractividadBotones(this.lblUnidades, false);
        //Iniciando el tablero de listado de categorías.
        this.logica.refrescarContenido(this.panelAjustesElementos, this.panelAjustesCategorias);
        
        this.pintor.paintTableAdjustments(modeloAjustes, ajuste,this.tequilazo.getCategorias(),
                                this.tequilazo.getMarcas(), this.tequilazo.getUnidadesMedidas());        
    }//GEN-LAST:event_txtAjustesMouseClicked

    private void lblCategoriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCategoriasMouseClicked
        this.ajuste = 'c';
        this.logica.crearInteractividadBotones(this.lblCategorias, true);
        this.logica.crearInteractividadBotones(this.lblMarcas, false);
        this.logica.crearInteractividadBotones(this.lblUnidades, false);        
        this.logica.refrescarContenido(this.panelAjustesElementos, this.panelAjustesCategorias);
        this.pintor.paintTableAdjustments(modeloAjustes, ajuste,this.tequilazo.getCategorias(),
                                this.tequilazo.getMarcas(), this.tequilazo.getUnidadesMedidas());  
    }//GEN-LAST:event_lblCategoriasMouseClicked

    private void lblMarcasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMarcasMouseClicked
        this.ajuste = 'm';
        this.logica.crearInteractividadBotones(this.lblCategorias, false);
        this.logica.crearInteractividadBotones(this.lblMarcas, true);
        this.logica.crearInteractividadBotones(this.lblUnidades, false);               
        this.logica.refrescarContenido(this.panelAjustesElementos, this.panelAjustesMarcas);
        this.pintor.paintTableAdjustments(modeloAjustes, ajuste,this.tequilazo.getCategorias(),
                                this.tequilazo.getMarcas(), this.tequilazo.getUnidadesMedidas());          
    }//GEN-LAST:event_lblMarcasMouseClicked

    private void lblUnidadesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblUnidadesMouseClicked
        this.ajuste = 'u';
        this.logica.crearInteractividadBotones(this.lblCategorias, false);
        this.logica.crearInteractividadBotones(this.lblMarcas, false);
        this.logica.crearInteractividadBotones(this.lblUnidades, true);        
        this.logica.refrescarContenido(this.panelAjustesElementos, this.panelAjustesUnidades);
        this.pintor.paintTableAdjustments(modeloAjustes, ajuste,this.tequilazo.getCategorias(),
                                this.tequilazo.getMarcas(), this.tequilazo.getUnidadesMedidas());  
        
    }//GEN-LAST:event_lblUnidadesMouseClicked

    private void btnModificarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarMarcaActionPerformed
        Marca marca = (Marca)this.gt.getAdjustmentFromTable(tablaMarcas, ajuste);
        if(marca == null)
            JOptionPane.showMessageDialog(this, "No Seleccionó Nungún Elemento");
        else
        {
            DialogoAjuste nuevaMarc = new DialogoAjuste(this, true, false, 'm');
            nuevaMarc.setTxtNombre(marca.getNombre());
            nuevaMarc.setTxtDescripcion(marca.getDescripcion());
            nuevaMarc.setVisible(true);
            Marca marca2 = nuevaMarc.getMarca();
            if(marca2 != null)
            {
                boolean ban = this.tequilazo.getBd().getCrudMarcas().actualizarMarca(marca, marca2);
                if(ban)
                {
                    JOptionPane.showMessageDialog(this, "Marca Actualizada Correctamente");
                    this.tequilazo.updateDataBase();
                    this.pintor.paintTableAdjustments(this.modeloAjustes, this.ajuste,
                            this.tequilazo.getCategorias(), this.tequilazo.getMarcas(), 
                            this.tequilazo.getUnidadesMedidas());
                }
                else
                    JOptionPane.showMessageDialog(this, "No Fue Posible Actualizar La Marca"); 
            }            
        }     
                
    }//GEN-LAST:event_btnModificarMarcaActionPerformed

    private void btnAgregarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarMarcaActionPerformed
        DialogoAjuste da = new DialogoAjuste(this, true, true, 'm');
        da.setVisible(true);        
        Marca marc = da.getMarca();
        if(marc != null)
        {
            boolean ban = this.tequilazo.getBd().getCrudMarcas().crearMarca(marc);
            if(ban)
            {
                this.tequilazo.updateDataBase();
                JOptionPane.showMessageDialog(this, "Marca Creada Correctamente");
                this.pintor.paintTableAdjustments(this.modeloAjustes, this.ajuste,
                            this.tequilazo.getCategorias(), this.tequilazo.getMarcas(), 
                            this.tequilazo.getUnidadesMedidas());
            }
            else
                JOptionPane.showMessageDialog(this, "No Se Pudo Crear La Marca En La Base De Datos");
        }       
        
    }//GEN-LAST:event_btnAgregarMarcaActionPerformed

    private void btnModificarUnidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarUnidadesActionPerformed
        UnidadMedida um = (UnidadMedida)this.gt.getAdjustmentFromTable(tablaUnidades, ajuste);
        if(um == null)
            JOptionPane.showMessageDialog(this, "No Seleccionó Nungún Elemento");
        else
        {
            DialogoAjuste nuevaUm = new DialogoAjuste(this, true, false, 'u');
            nuevaUm.setTxtNombre(um.getNombre());
            nuevaUm.setTxtDescripcion(um.getDescripcion());
            nuevaUm.setVisible(true);
            UnidadMedida um2 = nuevaUm.getUnidadMedida();
            if(um2 != null)
            {
                boolean ban = this.tequilazo.getBd().getCrudUnidades().actualizarUnidad(um, um2);
                if(ban)
                {
                    JOptionPane.showMessageDialog(this, "Unidad De Medida Actualizada Correctamente");
                    this.tequilazo.updateDataBase();
                    this.pintor.paintTableAdjustments(this.modeloAjustes, this.ajuste,
                            this.tequilazo.getCategorias(), this.tequilazo.getMarcas(), 
                            this.tequilazo.getUnidadesMedidas());
                }
                else
                    JOptionPane.showMessageDialog(this, "No Fue Posible Actualizar La Unidad De Medida"); 
            }
        }        
    }//GEN-LAST:event_btnModificarUnidadesActionPerformed

    private void btnAgregarUnidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarUnidadesActionPerformed
        DialogoAjuste da = new DialogoAjuste(this, true, true, 'u');
        da.setVisible(true);
        UnidadMedida um = da.getUnidadMedida();
        if(um != null)
        {
            boolean ban = this.tequilazo.getBd().getCrudUnidades().crearUnidadMedida(um);
            if(ban)
            {
                this.tequilazo.updateDataBase();
                JOptionPane.showMessageDialog(this, "Unidad De Medida Creada Correctamente");
                this.pintor.paintTableAdjustments(this.modeloAjustes, this.ajuste,
                            this.tequilazo.getCategorias(), this.tequilazo.getMarcas(), 
                            this.tequilazo.getUnidadesMedidas());
            }
            else
                JOptionPane.showMessageDialog(this, "No Se Pudo Crear La Unidad De Medida En La Base De Datos");
            
        }
      
    }//GEN-LAST:event_btnAgregarUnidadesActionPerformed

    private void btnModificarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarCategoriaActionPerformed
        Categoria cat = (Categoria)this.gt.getAdjustmentFromTable(this.tablaCategorias, ajuste);
        if(cat == null)
            JOptionPane.showMessageDialog(this, "No Seleccionó Nungún Elemento");
        else
        {
            DialogoAjuste nuevaCat = new DialogoAjuste(this, true, false, 'c');
            nuevaCat.setTxtNombre(cat.getNombre());
            nuevaCat.setTxtDescripcion(cat.getDescripcion());
            nuevaCat.setVisible(true);
            Categoria cat2 = nuevaCat.getCategoria();
            if(cat2 != null)
            {
                boolean ban = this.tequilazo.getBd().getCrudCategorias().actualizarCategoria(cat, cat2);
                if(ban)
                {
                    JOptionPane.showMessageDialog(this, "Categoria Actualizada Correctamente"); 
                    this.tequilazo.updateDataBase();
                    this.pintor.paintTableAdjustments(this.modeloAjustes, this.ajuste,
                            this.tequilazo.getCategorias(), this.tequilazo.getMarcas(), 
                            this.tequilazo.getUnidadesMedidas());
                }
                else
                    JOptionPane.showMessageDialog(this, "No Fue Posible Actualizar La Categoria"); 
            }            
        }                
    }//GEN-LAST:event_btnModificarCategoriaActionPerformed

    private void btnAgregarCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCategoriasActionPerformed
        DialogoAjuste nuevaCat = new DialogoAjuste(this, true, true, 'c');
        nuevaCat.setVisible(true);
        Categoria cat = nuevaCat.getCategoria();
        //Si no le di al boton de cancelar en la ventana nuevaCat.
        if(cat != null)
        {
            boolean ban = this.tequilazo.getBd().getCrudCategorias().crearCategoria(cat);
            if(ban)
            {
                this.tequilazo.updateDataBase();
                JOptionPane.showMessageDialog(this, "Categoria Creada Correctamente");
                this.pintor.paintTableAdjustments(this.modeloAjustes, this.ajuste,
                            this.tequilazo.getCategorias(), this.tequilazo.getMarcas(), 
                            this.tequilazo.getUnidadesMedidas());
            }                
            else
                JOptionPane.showMessageDialog(this, "No Se Pudo Crear La Categoria En La Base De Datos");
        }
    }//GEN-LAST:event_btnAgregarCategoriasActionPerformed

    private void btnVerVentaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVerVentaMouseEntered
        this.logica.crearInteractividadBotones(this.btnVerVenta, true);
    }//GEN-LAST:event_btnVerVentaMouseEntered

    private void btnVerVentaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVerVentaMouseExited
        this.logica.crearInteractividadBotones(this.btnVerVenta, false);
    }//GEN-LAST:event_btnVerVentaMouseExited

    private void btnVerCompraMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVerCompraMouseEntered
        this.logica.crearInteractividadBotones(this.btnVerCompra, true);
    }//GEN-LAST:event_btnVerCompraMouseEntered

    private void btnVerCompraMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVerCompraMouseExited
        this.logica.crearInteractividadBotones(this.btnVerCompra, false);
    }//GEN-LAST:event_btnVerCompraMouseExited

    private void btnDetalleVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalleVentaActionPerformed
        String codeSale = this.gt.getCodeElementFromTable(this.tablaVentasCreadas);
        if(codeSale != null)
        {
            int idSale = -1;
            try 
            {
                idSale = Integer.parseInt(codeSale);
            } catch (NumberFormatException e) 
            {
                JOptionPane.showMessageDialog(this, "Error Con El ID De la Venta");
            }
            
            Venta vent = this.tequilazo.getSaleFromId(idSale);
            if(vent != null)
            {
                DetalleVenta detVen = new DetalleVenta(this, true, vent, null);
                detVen.setVisible(true);
            }
            else
                JOptionPane.showMessageDialog(this, "Error Consultando La Venta");
        }
        else
            JOptionPane.showMessageDialog(this, "No Se Seleccionó Ningún Elemento");
    }//GEN-LAST:event_btnDetalleVentaActionPerformed

    private void btnDetalleCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalleCompraActionPerformed
        String codePurchase = this.gt.getCodeElementFromTable(this.tablaComprasHechas);
        if(codePurchase != null)
        {
            int idPurchase = -1;
            try 
            {
                idPurchase = Integer.parseInt(codePurchase);
            } catch (NumberFormatException e) 
            {
                JOptionPane.showMessageDialog(this, "Error Con El ID De la Compra");
            }
            
            Compra comp = this.tequilazo.getPurchaseFromId(idPurchase);
            if(comp != null)
            {
                DetalleCompra dc = new DetalleCompra(this, true, comp);
                dc.setVisible(true);
            }
            else
                JOptionPane.showMessageDialog(this, "Error Consultando La Compra");
        }
        else
            JOptionPane.showMessageDialog(this, "No Se Seleccionó Ningún Elemento");
    }//GEN-LAST:event_btnDetalleCompraActionPerformed

    private void btnModificarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarProveedorActionPerformed
        Proveedor prov = this.gt.getProviderFromTable(this.tablaListaProveedores);
        Proveedor nProv = null;

        if(prov == null)
            JOptionPane.showMessageDialog(this, "No Se ha Seleccionado Ningún Proveedor");
        else
        {
            ModificarProveedor mp = new ModificarProveedor(this, true, prov);
            mp.setVisible(true);
            nProv = mp.getProvider();
            if(nProv == null){}
                //JOptionPane.showMessageDialog(this, "Los Datos Ingresados No Son Correctos");
            else
            {
                boolean ban = this.tequilazo.getBd().getCrudProveedores().actualizarProveedor(prov, nProv);
                //Si se hace el registro correctamente en la base de datos.
                if(ban)
                {
                    JOptionPane.showMessageDialog(this, "Proveedor Actualizado Correctamente");
                    this.tequilazo.updateDataBase();
                    //this.paintTableProveedores();
                    this.pintor.paintTableProviders(this.tequilazo.getProveedores(), this.modeloListaProveedores);
                }
                else //No se modificó el proveedor.
                {
                    JOptionPane.showMessageDialog(this, "No Se Pudo Actualizar El Proveedor En La BD");
                }
            }
            
        }
   
    }//GEN-LAST:event_btnModificarProveedorActionPerformed

    private void btnAgregaProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregaProveedorActionPerformed
        this.addProveedor();
    }//GEN-LAST:event_btnAgregaProveedorActionPerformed

    private void btnVerVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVerVentaMouseClicked
        ArrayList<Venta> ven = this.tequilazo.getDailySales();
        if(ven == null || ven.isEmpty())
            JOptionPane.showMessageDialog(this, "No Hay Ventas Registradas Hoy");
        else
        {
            DetalleVenta dv = new DetalleVenta(this, true, null, ven);
            dv.setVisible(true);
        }        
    }//GEN-LAST:event_btnVerVentaMouseClicked

    private void btnVerCompraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVerCompraMouseClicked
        String codePurchase = this.gt.getCodeElementFromTable(this.tablaComprasDia);
        if(codePurchase != null)
        {
            int idPurchase = -1;
            try 
            {
                idPurchase = Integer.parseInt(codePurchase);
            } catch (NumberFormatException e) 
            {
                JOptionPane.showMessageDialog(this, "Error Con El ID De la Compra");
            }
            
            Compra comp = this.tequilazo.getPurchaseFromId(idPurchase);
            if(comp != null)
            {
                DetalleCompra dc = new DetalleCompra(this, true, comp);
                dc.setVisible(true);
            }
            else
                JOptionPane.showMessageDialog(this, "Error Consultando La Compra");
        }
        else
            JOptionPane.showMessageDialog(this, "No Se Seleccionó Ningún Elemento");
    }//GEN-LAST:event_btnVerCompraMouseClicked

    
    private void txtInventarioMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtInventarioMouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInventarioMouseDragged

    private void btnLimpiarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarTablaActionPerformed
        //Para Limpiar la tabla
        this.componentesNuevos.clear();
        this.txtTotalResumenVenta.setText("0");
        this.pintor.clearDataFromTable(this.modeloResumenVenta);        
    }//GEN-LAST:event_btnLimpiarTablaActionPerformed

    private void btnEliminarComponenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarComponenteActionPerformed
        String codeElement = this.gt.getCodeElementFromTable(this.tablaInventario);
        if(codeElement != null)
        {
            int resp = JOptionPane.showConfirmDialog(null, "Esta Seguro De Eliminar El Producto?",
                                                    "Eliminar Componente", JOptionPane.YES_NO_OPTION);    
            if(resp == 0)
            {
                boolean elim = this.tequilazo.getInventario().deleteComponent(codeElement);
                boolean elim2 = this.tequilazo.getBd().getCrudElemento().deleteElement(codeElement);
                if(elim && elim2)
                {
                    JOptionPane.showMessageDialog(this, "Producto Eliminado Correctamente Del Inventario");
                    //this.paintTableInventario();
                    this.pintor.paintTableInventory(this.modeloInventario, 
                                this.tequilazo.getInventario().getComponentes(), false);
                }
            }
        }
        else
            JOptionPane.showMessageDialog(this, "No Seleccionó Ningún Elemento");
    }//GEN-LAST:event_btnEliminarComponenteActionPerformed

    private void txtVentasMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtVentasMouseDragged
       
    }//GEN-LAST:event_txtVentasMouseDragged

    private void btnLimpiarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarCompraActionPerformed
        //Para Limpiar la tabla
        this.componentesNuevos.clear();
        this.txtTotalResumenCompra.setText("0");
        this.pintor.clearDataFromTable(this.modeloResumenCompra);
    }//GEN-LAST:event_btnLimpiarCompraActionPerformed

    private void btnEliminarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProveedorActionPerformed
        Proveedor prov = this.gt.getProviderFromTable(this.tablaListaProveedores);
        if(prov == null)
            JOptionPane.showMessageDialog(this, "No Se Seleccionó Ningún Proveedor");
        else
        {            
            String name = prov.getNombre();
            int opc = JOptionPane.showConfirmDialog(null, "Realmente Desea Eliminar El Proveedor " + name.toUpperCase(),
                    "Eliminar Proveedor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            //0 para si || 1 para no
            if (opc == 0)
            {
                boolean ban = this.tequilazo.getBd().getCrudProveedores().eliminarProveedor(prov);
                //Si se elimino el proveedor de la base de datos.
                if(ban)
                {
                    JOptionPane.showMessageDialog(this, "Proveedor " + name.toUpperCase() + " Eliminado Correctamente");
                    this.tequilazo.updateDataBase();
                    //this.paintTableProveedores();
                    this.pintor.paintTableProviders(this.tequilazo.getProveedores(), this.modeloListaProveedores);
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "No Se Pudo Eliminar El Proveedor De La Base De Datos");
                }
            }            
        }        
    }//GEN-LAST:event_btnEliminarProveedorActionPerformed

    private void btnEliminarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarCategoriaActionPerformed
        Categoria cat = (Categoria)this.gt.getAdjustmentFromTable(tablaCategorias, 'c');
        if(cat == null)
            JOptionPane.showMessageDialog(this, "No Se Seleccionó Ningún Elemento");
        else
        {
            int opc = JOptionPane.showConfirmDialog(null, "Realmente Desea Eliminar La Categoría " + cat.getNombre(),
                    "Eliminar Categoría", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            //0 para si || 1 para no
            if(opc == 0)
            {
                boolean ban = this.tequilazo.getBd().getCrudCategorias().eliminarCategoria(cat);
                if(ban)
                {
                    JOptionPane.showMessageDialog(this, "Categoría Eliminada Correctamente");
                    this.tequilazo.updateDataBase();
                    this.pintor.paintTableAdjustments(this.modeloAjustes, this.ajuste,
                            this.tequilazo.getCategorias(), this.tequilazo.getMarcas(), 
                            this.tequilazo.getUnidadesMedidas());
                }
                else
                    JOptionPane.showMessageDialog(this, "No Fue Posible Eliminar La Categoría");
            }            
        }
    }//GEN-LAST:event_btnEliminarCategoriaActionPerformed

    private void btnEliminarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarMarcaActionPerformed
        Marca marc = (Marca)this.gt.getAdjustmentFromTable(tablaMarcas, 'm');
        if(marc == null)
            JOptionPane.showMessageDialog(this, "No Se Seleccionó Ningún Elemento");
        else
        {
            int opc = JOptionPane.showConfirmDialog(null, "Realmente Desea Eliminar La Marca " + marc.getNombre(),
                    "Eliminar Marca", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(opc == 0)
            {
                boolean ban = this.tequilazo.getBd().getCrudMarcas().eliminarMarca(marc);
                if(ban)
                {
                    JOptionPane.showMessageDialog(this, "Marca Eliminada Correctamente");
                    this.tequilazo.updateDataBase();
                    this.pintor.paintTableAdjustments(this.modeloAjustes, this.ajuste,
                            this.tequilazo.getCategorias(), this.tequilazo.getMarcas(), 
                            this.tequilazo.getUnidadesMedidas());
                }
                else
                    JOptionPane.showMessageDialog(this, "No Fue Posible Eliminar La Marca");
            }
        }
    }//GEN-LAST:event_btnEliminarMarcaActionPerformed

    private void btnEliminarUnidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarUnidadActionPerformed
        UnidadMedida um = (UnidadMedida)this.gt.getAdjustmentFromTable(tablaUnidades, ajuste);
        if(um == null)
            JOptionPane.showMessageDialog(this, "No Se Seleccionó Ningún Elemento");
        else
        {
            int opc = JOptionPane.showConfirmDialog(null, "Realmente Desea Eliminar La Unidad De Medida " + um.getNombre(),
                    "Eliminar Unidad De Medida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            //0 para si || 1 para no
            if(opc == 0)
            {
                boolean ban = this.tequilazo.getBd().getCrudUnidades().eliminarUnidadMedida(um);
                if(ban)
                {
                    JOptionPane.showMessageDialog(this, "Unidad De Medida Eliminada Correctamente");
                    this.tequilazo.updateDataBase();
                    //this.paintTableAdjustments();
                    this.pintor.paintTableAdjustments(this.modeloAjustes, this.ajuste,
                            this.tequilazo.getCategorias(), this.tequilazo.getMarcas(), 
                            this.tequilazo.getUnidadesMedidas());
                }
                else
                    JOptionPane.showMessageDialog(this, "No Fue Posible Eliminar La Unidad De Medida");
            }
        }
        
    }//GEN-LAST:event_btnEliminarUnidadActionPerformed

    private void btnCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCajaActionPerformed
        boolean bandera = true;
        PrinterJob pj = PrinterJob.getPrinterJob();
        
        Factura fac = new Factura('c', null);
        bandera = fac.takeOutBox(pj);
        
        if(bandera)
            JOptionPane.showMessageDialog(this, "              Caja Abierta");
        else
            JOptionPane.showMessageDialog(this, "        Error Abriendo La Caja");
    }//GEN-LAST:event_btnCajaActionPerformed

    private void txtBuscarInventarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarInventarioKeyPressed
        
    }//GEN-LAST:event_txtBuscarInventarioKeyPressed

    private void txtBuscarInventarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarInventarioKeyReleased
        this.gt.filtrarDatos(modeloInventario, tablaInventario, 
                txtBuscarInventario, 0, this.tequilazo);
    }//GEN-LAST:event_txtBuscarInventarioKeyReleased

    private void txtScannerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtScannerMouseClicked
        this.pintor.paintTableScanner(modeloScanner, this.tequilazo.getMap());
        this.logica.modificarEncabezado(this.txtScanner, 7, this.txtEncabezado);
        this.logica.refrescarContenido(this.panelPrincipal, this.panelScanner);
    }//GEN-LAST:event_txtScannerMouseClicked

    private void txtScannerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtScannerMouseEntered
        this.logica.crearInteractividadBotones(this.txtScanner, true);
    }//GEN-LAST:event_txtScannerMouseEntered

    private void txtScannerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtScannerMouseExited
        this.logica.crearInteractividadBotones(this.txtScanner, false);
    }//GEN-LAST:event_txtScannerMouseExited

    private void btnEliminarScannerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarScannerActionPerformed
        String[] dates = this.gt.getScannerFromTable(this.tablaListaScanner);
        if(dates[0].equals("no"))
            JOptionPane.showMessageDialog(this, "No Se Seleccionó Ningún Elemento");
        else
        {
            int opc = JOptionPane.showConfirmDialog(null, "Realmente desea salir del TEQUILAZO?",
                "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            //0 para si || 1 para no
            if (opc == 0)
            {
                boolean ban = this.tequilazo.getBd().getCrudScanner().deleteMap(dates[0], dates[1]);
                if(ban)
                {
                    this.tequilazo.getMap().remove(dates[1]);
                    this.pintor.paintTableScanner(modeloScanner, this.tequilazo.getMap());
                    JOptionPane.showMessageDialog(this, "Código De Barras Eliminado Correctamente");
                }
                else
                    JOptionPane.showMessageDialog(this, "No Fue Posible Eliminar El Código De Barras");
            }            
        } 
    }//GEN-LAST:event_btnEliminarScannerActionPerformed

    private void btnAgregaScannerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregaScannerActionPerformed
        RegistrarScanner rs = new RegistrarScanner(this, true, this.tequilazo);
        rs.setVisible(true);
        this.pintor.paintTableScanner(modeloScanner, this.tequilazo.getMap());
    }//GEN-LAST:event_btnAgregaScannerActionPerformed

    private void btnMenudeoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenudeoActionPerformed
        MenudearProducto menud = new MenudearProducto(this, true, this.tequilazo);
        menud.setVisible(true);
        this.pintor.paintTableInventory(this.modeloInventario, this.tequilazo.
                                        getInventario().getComponentes(), false);
    }//GEN-LAST:event_btnMenudeoActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        String codeEl = this.gt.getCodeElementFromTable(this.tablaDetalleVenta);
        if(codeEl != null)
        {
            this.tequilazo.verifyExistDatail(codeEl, componentesNuevos, 1);
            boolean b = this.gt.deleteRowFromTable(this.modeloResumenVenta, this.tablaDetalleVenta);
            this.pintor.paintTableSumarySale(this.modeloResumenVenta, 
                        this.componentesNuevos, this.txtTotalResumenVenta,checFuera.isSelected());
            if(!b)
                JOptionPane.showMessageDialog(this, "No Selecciono Ningún Elemento");
        }
        else
            JOptionPane.showMessageDialog(this, "No Selecciono Ningún Elemento");
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnAgregarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarClienteActionPerformed
        IngresarCliente ingCli = new IngresarCliente(this, true);
        ingCli.setVisible(true);  
        this.clienteVenta = ingCli.getCliente();
    }//GEN-LAST:event_btnAgregarClienteActionPerformed

    private void btnAgregarProdVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProdVentaActionPerformed
        boolean dentro = this.checDentro.isSelected();
        boolean fuera = this.checFuera.isSelected();
        //VentaController vc = new VentaController(this.tequilazo);        
        //////////////////////////////////////
        if((dentro && fuera) || (!dentro && !fuera))
        {
            JOptionPane.showMessageDialog(this, "Debe Seleccionar Si La Venta Es Dentro O Fuera");            
        }
        else
        {
            ListaProductos lista = new ListaProductos(this, true, tequilazo, 'v', this.checFuera.isSelected());
            lista.setVisible(true);
            ArrayList<Elemento> elements = lista.getProductos();
            this.tequilazo.verifyNewsElements(this.componentesNuevos, elements);
            //this.paintTableSumaryPurchase(this.componentesNuevos); 
            this.pintor.paintTableSumarySale(this.modeloResumenVenta, 
                    this.componentesNuevos, this.txtTotalResumenVenta, fuera);
        }                       
    }//GEN-LAST:event_btnAgregarProdVentaActionPerformed

    private void btnCancelarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarVentaActionPerformed
        this.logica.refrescarContenido(this.panelPrincipal, this.panelInicio);
    }//GEN-LAST:event_btnCancelarVentaActionPerformed

    private void btnCrearVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearVentaActionPerformed
        int numVenta = -1;
        String fecha = this.lblFechaVenta.getText().replace("- ", "");
        String observ = this.txtObservacionesVenta.getText();
        Cliente cliente = this.clienteVenta;
        boolean fuera = checFuera.isSelected();
        double total = -1;
        
        try 
        {
            numVenta = Integer.parseInt(this.lblNumeroVenta.getText()); 
            total = Double.parseDouble(this.txtTotalResumenVenta.getText());
        } catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Error Con La Venta: " + e.getMessage());
        }
        
        int msj = this.saleController.createSale(numVenta, fecha, observ, cliente, fuera, 
                                                total, this.componentesNuevos);
        String mostrar = this.gm.getMessageCreateSale(msj);
        JOptionPane.showMessageDialog(this, mostrar);
        
        
//        
//        Venta vent;
//        if(numVenta != -1 && !fecha.equals("Error") && total != -1)
//        {
//            //Se crea la venta y se gestiona la base de datos.
//            vent = new Venta(numVenta, cliente, fecha, observ, fuera, total);
//            if(!this.componentesNuevos.isEmpty())
//            {
//                ArrayList<Elemento> newElements = new ArrayList<>();
//                newElements.addAll(this.componentesNuevos);
//                vent.setElementos(newElements);
//                boolean add = this.tequilazo.addSale(vent);
//                if(add)
//                {
//                    //Modifico el inventario, dismuniyo la cantidad a los productos vendido.
//                    boolean ban = this.tequilazo.getBd().getCrudElementoVendido().modifyInventorySale
//                            (this.tequilazo.getInventario().getComponentes(), componentesNuevos);
//                    if(ban)
//                    {
//                        this.tequilazo.getVentas().add(vent);
//                        this.clienteVenta = new Cliente("","","");
//                        //////////////////////////////////
//                        int opc = JOptionPane.showConfirmDialog(null, "Desea Imprimir Factura?",
//                                "Imprimir Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                        //0 para si || 1 para no
//                        if (opc == 0)
//                        {
//                            PrinterJob pj = PrinterJob.getPrinterJob();
//                            Factura fac = new Factura('v', vent);
//                            pj.setPrintable(fac,fac.getPageFormat(pj));
//                            try 
//                            {
//                                pj.print();
//                            } catch (PrinterException e) 
//                            {
//                                JOptionPane.showMessageDialog(this, "Error Con La Impresión");
//                            }  
//                            
//                            int opc2 = JOptionPane.showConfirmDialog(null, "Desea Imprimir Copia?",
//                                "Imprimir Copia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                            if (opc2 == 0)
//                            {
//                                PrinterJob pj2 = PrinterJob.getPrinterJob();
//                                Factura fac2 = new Factura('v', vent);
//                                pj.setPrintable(fac2,fac.getPageFormat(pj2));
//                                try 
//                                {
//                                    //pj.printDialog();
//                                    pj.print();
//                                } catch (PrinterException e) 
//                                {
//                                    JOptionPane.showMessageDialog(this, "Error Con La Impresión");
//                                }
//                            }                        
//                        }    
//                        else
//                        {
//                            PrinterJob pj = PrinterJob.getPrinterJob();
//                            Factura fac = new Factura('c', null);
//                            fac.takeOutBox(pj);
//                        }
//                        ///////////////////////////////////
//                        this.pintor.paintTableInventory(this.modeloInventario, 
//                                this.tequilazo.getInventario().getComponentes(), false);
//                        JOptionPane.showMessageDialog(this, "Venta Registrada Correctamente");
//                        this.componentesNuevos.clear();
//                        this.pintor.clearDataFromTable(this.modeloResumenVenta);
//                        this.txtObservacionesVenta.setText("");
//                        this.txtTotalResumenVenta.setText("0");
//                        ArrayList<Venta> ven = this.tequilazo.getDailySales();
//                        this.pintor.paintTableDailySales(modeloVentasDia, ven);  
//                    }                            
//                    else
//                        JOptionPane.showMessageDialog(this, "No Fue Posible Registrar La Venta: "
//                                + "ERROR HACIENDO LA MODIFICACIÓN EN LA BASE DE DATOS");
//                }
//                else
//                    JOptionPane.showMessageDialog(this, "No Se Pudo Registrar La Venta: "
//                            + "nO SE PUDO AÑADIR LA VENTA A LA LÓGICA");
//            }
//            else
//                JOptionPane.showMessageDialog(this, "No Ha Agregado Productos A La Venta");
//        }
//        else
//            JOptionPane.showMessageDialog(this, "Error Intentando Registrar La Venta: "
//                    + "ERROR EN LA VALIDACIÓN PRINCIPAL");
        
        ///////////////////////////////////
        this.pintor.paintTableInventory(this.modeloInventario, 
                this.tequilazo.getInventario().getComponentes(), false);
        //JOptionPane.showMessageDialog(this, "Venta Registrada Correctamente");
        this.componentesNuevos.clear();
        this.pintor.clearDataFromTable(this.modeloResumenVenta);
        this.txtObservacionesVenta.setText("");
        this.txtTotalResumenVenta.setText("0");
        ArrayList<Venta> ven = this.tequilazo.getDailySales();
        this.pintor.paintTableDailySales(modeloVentasDia, ven); 
        
        this.logica.crearInteractividadBotones(this.panelNuevaVenta, this.txtNuevaVenta, false);
        this.logica.refrescarContenido(this.panelPrincipal, this.panelInicio);
    }//GEN-LAST:event_btnCrearVentaActionPerformed

    private void btnAgregarProductosCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductosCompraActionPerformed
        ListaProductos lista = new ListaProductos(this, true, tequilazo, 'c', false);
        lista.setVisible(true);
        ArrayList<Elemento> elements = lista.getProductos();
        this.tequilazo.verifyNewsElements(this.componentesNuevos, elements);
        //this.paintTableSumaryPurchase(this.componentesNuevos); 
        this.pintor.paintTableSumaryPurchase(this.modeloResumenCompra, 
                            this.componentesNuevos, this.txtTotalResumenCompra);
    }//GEN-LAST:event_btnAgregarProductosCompraActionPerformed

    private void btnCancelarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarCompraActionPerformed
        this.logica.refrescarContenido(this.panelPrincipal, this.panelInicio);
    }//GEN-LAST:event_btnCancelarCompraActionPerformed

    private void btnCrearCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearCompraActionPerformed
        int numCompra = -1;        
        String fecha = this.lblFechaCompra.getText().replace("- ", "");

        String observ = this.txtObservacionesCompra.getText();
        String prov = (String)this.combProviders.getSelectedItem();
        double total = -1;
        try 
        {
            numCompra = Integer.parseInt(this.lblNumeroCompra.getText()); 
            total = Double.parseDouble(this.txtTotalResumenCompra.getText());
        } catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Error Con La Compra: " + e.getMessage());
        }
        
        Compra com;
        if(numCompra != -1 && !fecha.equals("Error") && total != -1 && !prov.equals("Seleccione el proveedor..."))
        {            
            Proveedor prove = this.tequilazo.getBd().getCrudProveedores().getProviderFromName(prov);
            if(prove != null)
            {
                //Se crea la compra y además se gestiona la base de datos.
                com = new Compra(numCompra, fecha, prove, total, observ);
                if(!this.componentesNuevos.isEmpty())
                {
                    ArrayList<Elemento> newElements = new ArrayList<>();
                    newElements.addAll(this.componentesNuevos);
                    com.setElementos(newElements);
                    boolean add = this.tequilazo.addPurchase(com);
                    if(add)
                    {
                        //Modifico el inventario, aumento la cantidad a los productos comprados.
                        boolean ban = this.tequilazo.getBd().getCrudElementoComprado().modifyInventoryPurchase
                                (this.tequilazo.getInventario().getComponentes(), componentesNuevos);

                        if(ban)
                        {
                            this.tequilazo.getCompras().add(com);
                            this.pintor.paintTableInventory(this.modeloInventario, 
                                    this.tequilazo.getInventario().getComponentes(), false);
                            JOptionPane.showMessageDialog(this, "Compra Registrada Correctamente");
                            this.componentesNuevos.clear();
                            this.pintor.clearDataFromTable(this.modeloResumenCompra);
                            this.txtObservacionesCompra.setText("");
                            this.combProviders.setSelectedIndex(0);
                            this.txtTotalResumenCompra.setText("0");
                            ArrayList<Compra> comp = this.tequilazo.getDailyPurchases();
                                this.pintor.paintTableDailyPurchases(modeloComprasDia, comp);
                        }                            
                        else
                            JOptionPane.showMessageDialog(this, "No Fue Posible "
                                    + "Registrar La Compra: ERROR REGISTRANDO EN BASE DE DATOS");
                    }
                    else
                        JOptionPane.showMessageDialog(this, "No Se Pudo Registrar"
                                + " La Compra: ERROR AÑADIENDO LA COMPRA A LA LÓGICA");
                }
                else
                    JOptionPane.showMessageDialog(this, "No Agregó Productos A La Compra");                
            }
            else
                JOptionPane.showMessageDialog(this, "Error Con El Proveedor Seleccionado");
        }
        else
            JOptionPane.showMessageDialog(this, "Error Al Intentar Registrar La Compra:"
                    + " ERROR EN LA VALIDACIÓN PRINCIPAL");
        
        this.logica.crearInteractividadBotones(this.panelNuevaCompra, this.txtNuevaCompra, false);
        this.logica.refrescarContenido(this.panelPrincipal, this.panelInicio);     
    }//GEN-LAST:event_btnCrearCompraActionPerformed

    private void btnRemover2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemover2ActionPerformed
        String codeEl = this.gt.getCodeElementFromTable(this.tablaResumenCompra);
        if(codeEl != null)
        {
            this.tequilazo.verifyExistDatail(codeEl, componentesNuevos, 1);
            boolean b = this.gt.deleteRowFromTable(this.modeloResumenCompra, this.tablaResumenCompra);
            this.pintor.paintTableSumaryPurchase(this.modeloResumenCompra, 
                            this.componentesNuevos, this.txtTotalResumenCompra);
            if(!b)
                JOptionPane.showMessageDialog(this, "No Selecciono Ningún Elemento");
        }
        else
            JOptionPane.showMessageDialog(this, "No Selecciono Ningún Elemento");
    }//GEN-LAST:event_btnRemover2ActionPerformed

    private void btnMicheladaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMicheladaActionPerformed
        String codeEl = this.gt.getCodeElementFromTable(this.tablaDetalleVenta);
        int codeMsj = this.micheladaController.michelarCerveza(codeEl, componentesNuevos);
        String msj = this.gm.getMessageMichelar(codeMsj);
        if(!msj.equals("ok"))
        {
            JOptionPane.showMessageDialog(this, msj);
        }
        
        this.pintor.paintTableSumarySale(this.modeloResumenVenta, 
                this.componentesNuevos, this.txtTotalResumenVenta,checFuera.isSelected());
        
    }//GEN-LAST:event_btnMicheladaActionPerformed

    private void btnBuscarVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarVentasActionPerformed
        String initialDate = null;
        String finishDate = null;
        
        try 
        {
            Calendar fechaInicial = fechaInicialVenta.getCalendar();
            Date dateInitial = fechaInicial.getTime();          
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");  
            initialDate = format1.format(dateInitial);
            
            Calendar fechaFinal = fechaFinalVenta.getCalendar();
            Date dateFinal = fechaFinal.getTime();          
            finishDate = format1.format(dateFinal);
            
            initialDate += " 00:00";
            finishDate += " 12:00";

        }catch (Exception e1) 
        {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(this, "ERROR, Se Debe Seleccionar El Rango De Fechas");
        }
        
        //Si se seleccionan correctamente el intervalo de fechas
        if(initialDate != null && finishDate != null)
        {
            ArrayList<Venta> sales = this.tequilazo.getBd().getCrudVenta().
                                    getAllSalesFromDates(initialDate, finishDate);
            
            this.pintor.paintTableSales(sales, this.modeloVentasCreadas);             
        }
    }//GEN-LAST:event_btnBuscarVentasActionPerformed

    private void txtCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodeKeyReleased
        String code = this.txtCode.getText().toLowerCase();
        if(code.length() > 6)
        {
            Elemento el = (Elemento) this.tequilazo.getMap().get(code);
            if(el != null)
            {
                //System.out.println("EL CÓDIIGOOOO ESSS: "+ el.getCodigo());
                this.saleController.addProduct(el.getCodigo());
                this.txtCode.setText("");
            }            
        }
        
        this.componentesNuevos = this.saleController.getElementsSale();
        if(this.componentesNuevos != null)
            this.pintor.paintTableSumarySale(this.modeloResumenVenta, 
                    this.componentesNuevos, this.txtTotalResumenVenta, false);
    }//GEN-LAST:event_txtCodeKeyReleased

    private void txtCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodeKeyPressed
        String code = this.txtCode.getText();
        if(code.length() > 6)
        {
            Elemento el = (Elemento) this.tequilazo.getMap().get(code);
            //System.out.println("EL CÓDIIGOOOO ESSS: "+ el.getCodigo());
            if(el != null)   
            {
                this.saleController.addProduct(el.getCodigo()); 
                this.txtCode.setText(""); 
            }
                                           
//            else            
//                JOptionPane.showMessageDialog(this, "Código No Registrado");
//            
//            this.txtCode.setText("");    
        }
        
        this.componentesNuevos = this.saleController.getElementsSale();
        if(this.componentesNuevos != null)        
            this.pintor.paintTableSumarySale(this.modeloResumenVenta, 
                    this.componentesNuevos, this.txtTotalResumenVenta, false);
    }//GEN-LAST:event_txtCodeKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.txtCode.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnModificarMicheladaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarMicheladaActionPerformed
        boolean modifico = this.micheladaController.updatePrice();
        if(modifico)
            JOptionPane.showMessageDialog(this, "Valor Modificado Correctamente");
    }//GEN-LAST:event_btnModificarMicheladaActionPerformed
    
    /**
     * Hace todo el proceso para agregar un proveedor al hacer click en nuevo.
     */
    public void addProveedor()
    {
        NuevoProveedor nuevo = new NuevoProveedor(this, true);
        nuevo.setVisible(true);
        Proveedor proveedor = nuevo.getProveedor();
        if(proveedor != null)
        {
            boolean ban = this.tequilazo.getBd().getCrudProveedores().crearProveedor(proveedor);
            if(ban)
            {
                //Se Registro Correctamente El Proveedor.
                tequilazo.agregarProveedor(proveedor);
                JOptionPane.showMessageDialog(this ,"Proveedor Registrado Correctamente");
                //this.paintTableProveedores();
                this.pintor.paintTableProviders(this.tequilazo.getProveedores(), this.modeloListaProveedores);
            }   
            else
                JOptionPane.showMessageDialog(this ,"No Se Pudo Registrar El Proveedor");
        }
    }   
                
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregaProveedor;
    private javax.swing.JButton btnAgregaScanner;
    private javax.swing.JButton btnAgregarCategorias;
    private javax.swing.JButton btnAgregarCliente;
    private javax.swing.JButton btnAgregarComponente;
    private javax.swing.JButton btnAgregarMarca;
    private javax.swing.JButton btnAgregarProdVenta;
    private javax.swing.JButton btnAgregarProductosCompra;
    private javax.swing.JButton btnAgregarUnidades;
    private javax.swing.JButton btnBuscarVentas;
    private javax.swing.JButton btnCaja;
    private javax.swing.JButton btnCancelarCompra;
    private javax.swing.JButton btnCancelarVenta;
    private javax.swing.JButton btnCrearCompra;
    private javax.swing.JButton btnCrearVenta;
    private javax.swing.JButton btnDetalleCompra;
    private javax.swing.JButton btnDetalleVenta;
    private javax.swing.JButton btnEliminarCategoria;
    private javax.swing.JButton btnEliminarComponente;
    private javax.swing.JButton btnEliminarMarca;
    private javax.swing.JButton btnEliminarProveedor;
    private javax.swing.JButton btnEliminarScanner;
    private javax.swing.JButton btnEliminarUnidad;
    private javax.swing.JButton btnImprimirInventario;
    private javax.swing.JButton btnLimpiarCompra;
    private javax.swing.JButton btnLimpiarTabla;
    private javax.swing.JButton btnMenudeo;
    private javax.swing.JButton btnMichelada;
    private javax.swing.JButton btnModificarCategoria;
    private javax.swing.JButton btnModificarComponente;
    private javax.swing.JButton btnModificarMarca;
    private javax.swing.JButton btnModificarMichelada;
    private javax.swing.JButton btnModificarProveedor;
    private javax.swing.JButton btnModificarUnidades;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnRemover2;
    private javax.swing.JLabel btnVerCompra;
    private javax.swing.JLabel btnVerVenta;
    private javax.swing.JCheckBox checDentro;
    private javax.swing.JCheckBox checFuera;
    private javax.swing.JComboBox<String> combProviders;
    private com.toedter.calendar.JDateChooser fechaFinalVenta;
    private com.toedter.calendar.JDateChooser fechaInicialVenta;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lblCategorias;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFecha2;
    private javax.swing.JLabel lblFechaCompra;
    private javax.swing.JLabel lblFechaVenta;
    private javax.swing.JLabel lblIconoProductos;
    private javax.swing.JLabel lblIconoProductos1;
    private javax.swing.JLabel lblIconoProductos2;
    private javax.swing.JLabel lblIconoProductos3;
    private javax.swing.JLabel lblIconoProductos5;
    private javax.swing.JLabel lblIconoProductos6;
    private javax.swing.JLabel lblIngreso;
    private javax.swing.JLabel lblIngreso1;
    private javax.swing.JLabel lblListaI;
    private javax.swing.JLabel lblListaI1;
    private javax.swing.JLabel lblListaI2;
    private javax.swing.JLabel lblListaI3;
    private javax.swing.JLabel lblListaI4;
    private javax.swing.JLabel lblListaI5;
    private javax.swing.JLabel lblListaI6;
    private javax.swing.JLabel lblListaI7;
    private javax.swing.JLabel lblListaI8;
    private javax.swing.JLabel lblListaI9;
    private javax.swing.JLabel lblLupa;
    private javax.swing.JLabel lblMarcas;
    private javax.swing.JLabel lblNumero;
    private javax.swing.JLabel lblNumero2;
    private javax.swing.JLabel lblNumeroCompra;
    private javax.swing.JLabel lblNumeroVenta;
    private javax.swing.JLabel lblObservaciones;
    private javax.swing.JLabel lblObservaciones1;
    private javax.swing.JLabel lblObservaciones2;
    private javax.swing.JLabel lblObservaciones3;
    private javax.swing.JLabel lblObservaciones4;
    private javax.swing.JLabel lblObservaciones5;
    private javax.swing.JLabel lblProveedores;
    private javax.swing.JLabel lblProveedores1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JLabel lblUnidades;
    private javax.swing.JLabel minim;
    private javax.swing.JPanel panelAjustes;
    private javax.swing.JPanel panelAjustesCategorias;
    private javax.swing.JPanel panelAjustesElementos;
    private javax.swing.JPanel panelAjustesMarcas;
    private javax.swing.JPanel panelAjustesUnidades;
    private javax.swing.JPanel panelCompras;
    private javax.swing.JPanel panelComprasDia;
    private javax.swing.JPanel panelContenedorInicio;
    private javax.swing.JPanel panelContenido;
    private javax.swing.JPanel panelCrearCompra;
    private javax.swing.JPanel panelCrearVenta;
    private javax.swing.JPanel panelCrudBotones;
    private javax.swing.JPanel panelCrudBotones1;
    private javax.swing.JPanel panelCrudBotones2;
    private javax.swing.JPanel panelCrudBotones3;
    private javax.swing.JPanel panelCrudBotones4;
    private javax.swing.JPanel panelEncaCompDia;
    private javax.swing.JPanel panelEncab;
    private javax.swing.JPanel panelEncabAjustes;
    private javax.swing.JPanel panelEncabVentas;
    private javax.swing.JPanel panelEncabezado;
    private javax.swing.JPanel panelEncabezado1;
    private javax.swing.JPanel panelInicio;
    private javax.swing.JPanel panelInventario;
    private javax.swing.JPanel panelLogo;
    private javax.swing.JPanel panelNav;
    private javax.swing.JPanel panelNuevaCompra;
    private javax.swing.JPanel panelNuevaVenta;
    private javax.swing.JPanel panelNuevoProveedor;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelProveedores;
    private javax.swing.JPanel panelScanner;
    private javax.swing.JPanel panelVentas;
    private javax.swing.JPanel panelVentasDia;
    private javax.swing.JLabel salir;
    private javax.swing.JScrollPane scrollCompDia;
    private javax.swing.JScrollPane scrollVentDia;
    private javax.swing.JTable tablaCategorias;
    private javax.swing.JTable tablaComprasDia;
    private javax.swing.JTable tablaComprasHechas;
    private javax.swing.JTable tablaDetalleVenta;
    private javax.swing.JTable tablaInventario;
    private javax.swing.JTable tablaListaProveedores;
    private javax.swing.JTable tablaListaScanner;
    private javax.swing.JTable tablaMarcas;
    private javax.swing.JTable tablaResumenCompra;
    private javax.swing.JTable tablaUnidades;
    private javax.swing.JTable tablaVentasCreadas;
    private javax.swing.JTable tablaVentasDia;
    private javax.swing.JLabel txtAjustes;
    private javax.swing.JTextField txtBuscarInventario;
    private javax.swing.JTextField txtCode;
    private javax.swing.JLabel txtCompras;
    private javax.swing.JLabel txtEncabezado;
    private javax.swing.JLabel txtFecha;
    private javax.swing.JLabel txtInicio;
    private javax.swing.JLabel txtInventario;
    private javax.swing.JLabel txtLogo;
    private javax.swing.JLabel txtNombre;
    private javax.swing.JLabel txtNuevaCompra;
    private javax.swing.JLabel txtNuevaVenta;
    private javax.swing.JLabel txtNuevoProveedor;
    private javax.swing.JTextArea txtObservacionesCompra;
    private javax.swing.JTextArea txtObservacionesVenta;
    private javax.swing.JLabel txtProveedores;
    private javax.swing.JLabel txtSalir;
    private javax.swing.JLabel txtScanner;
    private javax.swing.JLabel txtTotalResumenCompra;
    private javax.swing.JLabel txtTotalResumenVenta;
    private javax.swing.JLabel txtVentas;
    // End of variables declaration//GEN-END:variables
}
