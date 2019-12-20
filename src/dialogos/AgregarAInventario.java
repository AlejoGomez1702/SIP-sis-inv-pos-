package dialogos;

import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import logica.Elemento;
import logica.Producto;
import logica.bd.*;

/**
 * FECHA ==> 2019-07-24.
 * Permitira realizar el CRUD de elementos en la base de datos.
 * @author Luis Alejandro Gómez C.
 * @author Ever Peña B.
 * @version 1.0.0
 */
public class AgregarAInventario extends javax.swing.JDialog 
{
    /**
     * Elemento que se crea con los datos ingresados en esta ventana.
     */
    private Elemento componente;
    
    /**
     * Elemento antiguo, se utilizá para cuando se va modificar un elemento.
     */
    private Elemento oldElement;
    
    /**
     * Listado con las categorias de productos registrados en el sistema.
     */
    private ArrayList<Categoria> categorias;
    
    /**
     * Listado con las marcas de los productos registrados en el sistema.
     */
    private ArrayList<Marca> marcas;
    
    /**
     * Listado con las unidades de medida de los productos registrados en el sistema.
     */
    private ArrayList<UnidadMedida> unidadMedidas;    
    
    /**
     * Titulo de la ventana(agregar o modificar).
     */
    private String titulo;
    
    /**
     * Inicializa la ventana que agrega o modifica un producto del inventario.
     * @param parent Ventana de la cual se abre esta.
     * @param modal Indica la modalidad de la ventana.
     * @param titulo Titulo que se le asigna a la ventana.
     */
    public AgregarAInventario(java.awt.Frame parent, boolean modal,String titulo)
    {
        super(parent, modal);
        initComponents();
        this.titulo = titulo;
        this.lblTitulo.setText(titulo);
        this.setLocationRelativeTo(null);
        this.componente = null;
        this.categorias = new ArrayList<>();
        this.marcas = new ArrayList<>();
        this.unidadMedidas = new ArrayList<>();
        this.txtPrecioCompra.setHorizontalAlignment(JTextField.CENTER);
        this.txtPrecioVenta.setHorizontalAlignment(JTextField.CENTER);
        this.txtPrecioVentaFuera.setHorizontalAlignment(JTextField.CENTER);
    }

    /**
     * Obtiene el producto que se crea al ingresar todos los datos 
     * correctamente en esta ventana.
     * @return Elemento creado desde esta ventana.
     */
    public Elemento getComponente() 
    {
        return componente;
    }

    /**
     * Modifica el elemento que posee esta ventana.
     * @param componete Elemento que va reemplazar al existente.
     */
    public void setComponete(Elemento componete) 
    {
        this.componente = componete;
    }

    /**
     * Modifica el elemento que proporciona los datos de modificación.
     * @param oldElement Elemento al cual se le van a modificar los datos.
     */
    public void setOldElement(Elemento oldElement) 
    {
        this.oldElement = oldElement;
        this.setCode(oldElement.getCodigo());
        this.setNombre(oldElement.getProducto().getNombre());
        this.setCategory(oldElement.getProducto().getCategoria());
        this.setMark(oldElement.getMarca());
        this.setUnitMed(oldElement.getUnidadMedida());
        this.setStock(oldElement.getStock());
        this.setPrecioCompra(oldElement.getPrecioCompra());
        this.setPrecioVenta(oldElement.getPrecioVenta());
        this.setPrecioVentaFuera(oldElement.getPrecioVentaFuera());
        this.setCantAct(oldElement.getCantidadActual());
    }    
    
    public Elemento getOldElement()
    {
        return this.oldElement;
    }
        
    /**
     * Inicializa los ComboBox que tiene esta ventana para seleccionar datos.
     * @param cat Datos de las categorias existentes.
     * @param marc Datos de las marcas de productos existentes.
     * @param um Datos de las unidades de medidas existentes.
     */
    public void initInformation(ArrayList<Categoria> cat, ArrayList<Marca> marc, ArrayList<UnidadMedida> um)
    {
        this.categorias = cat;
        this.marcas = marc;
        this.unidadMedidas = um;
        int numCat = cat.size();
        int numMarc = marc.size();
        int numUm = um.size();
        
        String aux = "";
        //Llenando las categorias.
        for(int i = 0; i < numCat; i++) 
        {
            aux = cat.get(i).getNombre();
            this.combCategorias.addItem(aux);
        }
        //Llenando las marcas.
        for(int i = 0; i < numMarc; i++) 
        {
            aux = marc.get(i).getNombre();
            this.combMarcas.addItem(aux);
        }
        //Llenando las unidades de medida.
        for(int i = 0; i < numUm; i++) 
        {
            aux = um.get(i).getNombre();
            this.combMedidas.addItem(aux);
        }
    }
    
    /**
     * Valida que los datos ingresados sean correctos.
     * @return 1-->Cógigo incorrecto, 2-->Nombre incorrecto, 3-->No seleccionó categoria...
     * 4-->No seleccionó marca, 5-->No selecciono unidad de medida, 6-->Stock negativo...
     * 7-->Precio compra negativo, 8-->Precio compra texto, 9-->Precio venta negativo...
     * 10-->Precio venta texto, 11-->Cantidad actual negativa.
     */
    public int validateComponent()
    {
        String code = this.txtCodigo.getText().replace(" ", "");
        //El código debe ser de máximo 5 dígitos.
        if(code.length() > 5)
            return 1;
        
        String name = this.txtNombre.getText();
        //El nombre no puede estar vacio.
        if(name.equals(""))
            return 2;
        
        String category = (String)this.combCategorias.getSelectedItem();
        //Se debe seleccionar alguna categoria disponible.
        if(category.equals("Seleccione una opción..."))
            return 3;
        
        String mark = (String)this.combMarcas.getSelectedItem();
        //Se debe seleccionar una marca disponible.
        if(mark.equals("Seleccione una opción..."))
            return 4;
        
        String unitMed = (String)this.combMedidas.getSelectedItem();
        //Se debe seleccionar una unidad de medida disponible.
        if(unitMed.equals("Seleccione una opción..."))
            return 5;
        
        int stock = (int)this.spinStock.getValue();
        //El stock debe ser positivo.
        if(stock <= 0)
            return 6;
        
        double precioComp = -99;
        try 
        {
            precioComp = Double.parseDouble(this.txtPrecioCompra.getText().replace(".",""));
            //Precio de compra negativo.
            if(precioComp < 0)
                return 7;
        } catch (NumberFormatException e) 
        {
            //Precio de compra no es numérico.
            return 8;
        }
        
        double precioVent = -99;
        try 
        {
            precioVent = Double.parseDouble(this.txtPrecioVenta.getText().replace(".",""));
            //Precio de venta negativo.
            if(precioVent < 0)
                return 9;
        } catch (NumberFormatException e) 
        {
            //Precio de venta no es numérico.
            return 10;
        }
        
        int cantAct = (int)this.spinCant.getValue();
        //Si la cantidad es negativa.
        if(cantAct < 0)
            return 11;        
        
        double precioVentaFuera = -99;
        try 
        {
            precioVentaFuera = Double.parseDouble(this.txtPrecioVentaFuera.getText().replace(".",""));
            //Precio de venta fuera negativo.
            if(precioVentaFuera < 0)
                return 9;
        } catch (NumberFormatException e) 
        {
            //Precio de venta fuera no es numérico.
            return 10;
        }        
                
        //Si avanza hasta acá es porque los datos son validos.
        Producto prod = new Producto(-1, name, category);
        this.componente = new Elemento(code, prod, mark, unitMed, stock,
                        cantAct, precioComp, precioVent, precioVentaFuera);
        
        return -1;
    }    
    
    /**
     * Pone los puntos cada miles en el ingreso de un valor.
     * @param txt Campo al cual se le aplican los puntos.
     */
    public void validatePoints(JTextField txt)
    {
        DecimalFormat df = new DecimalFormat("#,###"); 
        if(txt.getText().length() >= 1) 
        { 
            try 
            {
                txt.setText( df.format(Integer.valueOf
                    (txt.getText().replace(".", "").replace(",", ""))) );
            } catch (NumberFormatException e) 
            {
                JOptionPane.showMessageDialog(this, "Error, Compruebe La Cifra Ingresada");
            }            
        }
    }
    
    //***************SETERS***************SETERS***************SETERS***************//
    public void setCode(String code)
    {
        this.txtCodigo.setText(code);        
    }
    public void setNombre(String name)
    {
        this.txtNombre.setText(name);
    }
    public void setCategory(String category)
    {
        this.combCategorias.setSelectedItem(category);
    }
    public void setMark(String mark)
    {
        this.combMarcas.setSelectedItem(mark);
    }
    public void setUnitMed(String unitMed)
    {
        this.combMedidas.setSelectedItem(unitMed);
    }
    public void setStock(int stock)
    {
        this.spinStock.setValue(stock);        
    }    
    public void setPrecioCompra(double precioCompra)
    {
        this.txtPrecioCompra.setText((int)precioCompra+"");
    }
    public void setPrecioVenta(double precioVenta)
    {
        this.txtPrecioVenta.setText((int)precioVenta+"");
    }
    public void setPrecioVentaFuera(double precioVentaFuera)
    {
        this.txtPrecioVentaFuera.setText((int)precioVentaFuera+"");
    }
    public void setCantAct(int cant)
    {
        this.spinCant.setValue(cant);
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lblCategoria = new javax.swing.JLabel();
        txtPrecioVenta = new javax.swing.JTextField();
        spinStock = new javax.swing.JSpinner();
        btnAceptar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        txtNombre = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        combCategorias = new javax.swing.JComboBox<>();
        lblCodigo = new javax.swing.JLabel();
        lblPrecio = new javax.swing.JLabel();
        combMedidas = new javax.swing.JComboBox<>();
        txtPrecioCompra = new javax.swing.JTextField();
        spinCant = new javax.swing.JSpinner();
        lblMedida = new javax.swing.JLabel();
        lblCantidad = new javax.swing.JLabel();
        lblStock = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblMarca = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        txtSalir = new javax.swing.JLabel();
        combMarcas = new javax.swing.JComboBox<>();
        lblPrecio1 = new javax.swing.JLabel();
        lblPrecio2 = new javax.swing.JLabel();
        txtPrecioVentaFuera = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCategoria.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        lblCategoria.setText("Categoria:");

        txtPrecioVenta.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtPrecioVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtPrecioVentaMouseReleased(evt);
            }
        });
        txtPrecioVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecioVentaKeyReleased(evt);
            }
        });

        spinStock.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        btnAceptar.setBackground(new java.awt.Color(52, 208, 142));
        btnAceptar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAceptar.setForeground(new java.awt.Color(255, 255, 255));
        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        jLabel1.setText("Máximo 5 Dígitos");

        btnCancelar.setBackground(new java.awt.Color(240, 80, 80));
        btnCancelar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        txtNombre.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        jSeparator1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        combCategorias.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        combCategorias.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una opción..." }));

        lblCodigo.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        lblCodigo.setText("Código:");

        lblPrecio.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        lblPrecio.setText("Precio Compra");

        combMedidas.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        combMedidas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una opción..." }));

        txtPrecioCompra.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtPrecioCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecioCompraKeyReleased(evt);
            }
        });

        spinCant.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        lblMedida.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        lblMedida.setText("Medida:");

        lblCantidad.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        lblCantidad.setText("Cantidad Actual");

        lblStock.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        lblStock.setText("Stock:");

        lblNombre.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        lblNombre.setText("Nombre:");

        lblMarca.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        lblMarca.setText("Marca:");

        txtCodigo.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(36, 52, 81));

        lblTitulo.setFont(new java.awt.Font("Baskerville Old Face", 1, 24)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoProductos.png"))); // NOI18N
        lblTitulo.setText("Agregar Producto");

        txtSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iconoCerrar.png"))); // NOI18N
        txtSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSalirMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSalir)
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSalir)
                    .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        combMarcas.setFont(new java.awt.Font("Cambria", 0, 16)); // NOI18N
        combMarcas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una opción..." }));

        lblPrecio1.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        lblPrecio1.setText("Precio Venta");

        lblPrecio2.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        lblPrecio2.setText("Precio Venta Fuera");

        txtPrecioVentaFuera.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        txtPrecioVentaFuera.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecioVentaFueraKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(lblCodigo)
                        .addGap(18, 18, 18)
                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblPrecio)
                .addGap(138, 138, 138))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addComponent(lblPrecio1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(lblCantidad)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(lblPrecio2)
                        .addGap(121, 121, 121))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(spinCant, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(169, 169, 169))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(lblNombre)
                                .addGap(18, 18, 18)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblCategoria)
                                .addGap(18, 18, 18)
                                .addComponent(combCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(lblMarca)
                                .addGap(18, 18, 18)
                                .addComponent(combMarcas, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(lblStock)
                                    .addGap(20, 20, 20)
                                    .addComponent(spinStock, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(164, 164, 164))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addGap(14, 14, 14)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtPrecioCompra, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(lblMedida)
                                                .addGap(22, 22, 22)
                                                .addComponent(combMedidas, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addComponent(txtPrecioVentaFuera, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCodigo)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblNombre))
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblCategoria))
                    .addComponent(combCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblMarca))
                    .addComponent(combMarcas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMedida)
                    .addComponent(combMedidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStock)
                    .addComponent(spinStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblPrecio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(lblPrecio1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblPrecio2)
                .addGap(4, 4, 4)
                .addComponent(txtPrecioVentaFuera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblCantidad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(spinCant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 710));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        int msj = this.validateComponent();
        switch(msj)
        {
            case 1:
                JOptionPane.showMessageDialog(this, "ERROR, El Código Ingresado Debe Tener Máximo 5 Dígitos");
                break;
                
            case 2:
                JOptionPane.showMessageDialog(this, "ERROR, Verifique El Nombre Ingresado");
                break;
                
            case 3:
                JOptionPane.showMessageDialog(this, "ERROR, Debe Seleccionar Una Categoría");
                break;
                
            case 4:
                JOptionPane.showMessageDialog(this, "ERROR, Debe Seleccionar Una Marca");
                break;
                
            case 5:
                JOptionPane.showMessageDialog(this, "ERROR, Debe Seleccionar Una Unidad De Medida");
                break;
                
            case 6:
                JOptionPane.showMessageDialog(this, "ERROR, El Stock Debe Ser Positivo");
                break;
                
            case 7:
                JOptionPane.showMessageDialog(this, "ERROR, El Precio De Compra Debe Ser Positivo");
                break;
                
            case 8:
                JOptionPane.showMessageDialog(this, "ERROR, Verifique El Precio De Compra Ingresado");
                break;
                
            case 9:
                JOptionPane.showMessageDialog(this, "ERROR, El Precio De Venta Debe Ser Positivo");
                break;
                
            case 10:
                JOptionPane.showMessageDialog(this, "ERROR, Verifique El Precio De Venta Ingresado");
                break;
                
            case 11:
                JOptionPane.showMessageDialog(this, "ERROR, La Cantidad Actual Debe Ser Positiva");
                break;
                
            default:
                //JOptionPane.showMessageDialog(this, "Producto Agregado Correctamente Al Inventario");
                this.dispose();
                break;
        }        
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSalirMouseClicked
        this.dispose();
    }//GEN-LAST:event_txtSalirMouseClicked

    private void txtPrecioCompraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioCompraKeyReleased
        this.validatePoints(this.txtPrecioCompra);
    }//GEN-LAST:event_txtPrecioCompraKeyReleased

    private void txtPrecioVentaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPrecioVentaMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioVentaMouseReleased

    private void txtPrecioVentaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioVentaKeyReleased
        this.validatePoints(this.txtPrecioVenta);
    }//GEN-LAST:event_txtPrecioVentaKeyReleased

    private void txtPrecioVentaFueraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioVentaFueraKeyReleased
        this.validatePoints(this.txtPrecioVentaFuera);
    }//GEN-LAST:event_txtPrecioVentaFueraKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<String> combCategorias;
    private javax.swing.JComboBox<String> combMarcas;
    private javax.swing.JComboBox<String> combMedidas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblCategoria;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JLabel lblMedida;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblPrecio1;
    private javax.swing.JLabel lblPrecio2;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JSpinner spinCant;
    private javax.swing.JSpinner spinStock;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecioCompra;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtPrecioVentaFuera;
    private javax.swing.JLabel txtSalir;
    // End of variables declaration//GEN-END:variables
}
