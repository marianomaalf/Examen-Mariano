package examen.actividades.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VentanaActividades extends JFrame {
    private final JTextField txtCodigo = new JTextField(18);
    private final JTextField txtNombre = new JTextField(18);
    private final JTextField txtTarifaBase = new JTextField(18);
    private final JTextField txtCupoTotal = new JTextField(18);
    private final JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"PRESENCIAL", "VIRTUAL"});
    private final JTextField txtCodigoConsulta = new JTextField(18);
    private final JButton btnRegistrar = new JButton("Registrar");
    private final JButton btnLimpiar = new JButton("Limpiar");
    private final JButton btnGuardar = new JButton("Guardar datos");
    private final JButton btnCargar = new JButton("Cargar datos");
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnMostrarTodas = new JButton("Mostrar todas");
    private final JButton btnInscribir = new JButton("Inscribir");
    private final JTextArea txtResultado = new JTextArea(12, 38);

    public VentanaActividades() {
        setTitle("Actividades");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 520);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Formulario"));

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila1.add(new JLabel("Código:"));
        fila1.add(txtCodigo);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila2.add(new JLabel("Nombre:"));
        fila2.add(txtNombre);

        JPanel fila3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila3.add(new JLabel("Tarifa base:"));
        fila3.add(txtTarifaBase);

        JPanel fila4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila4.add(new JLabel("Cupo total:"));
        fila4.add(txtCupoTotal);

        JPanel fila5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila5.add(new JLabel("Tipo:"));
        fila5.add(cmbTipo);

        JPanel fila6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila6.add(btnRegistrar);
        fila6.add(btnLimpiar);

        panelFormulario.add(fila1);
        panelFormulario.add(fila2);
        panelFormulario.add(fila3);
        panelFormulario.add(fila4);
        panelFormulario.add(fila5);
        panelFormulario.add(fila6);

        JPanel panelConsulta = new JPanel();
        panelConsulta.setLayout(new BoxLayout(panelConsulta, BoxLayout.Y_AXIS));
        panelConsulta.setBorder(BorderFactory.createTitledBorder("Consulta y resultados"));

        JPanel filaConsulta = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaConsulta.add(new JLabel("Código:"));
        filaConsulta.add(txtCodigoConsulta);
        filaConsulta.add(btnBuscar);
        filaConsulta.add(btnInscribir);
        filaConsulta.add(btnMostrarTodas);
        filaConsulta.add(btnGuardar);
        filaConsulta.add(btnCargar);

        txtResultado.setEditable(false);
        txtResultado.setText("Resultados aparecerán aquí.");

        JScrollPane scroll = new JScrollPane(txtResultado);
        scroll.setPreferredSize(new Dimension(700, 220));

        panelConsulta.add(filaConsulta);
        panelConsulta.add(Box.createVerticalStrut(8));
        panelConsulta.add(scroll);

        panelPrincipal.add(panelFormulario);
        panelPrincipal.add(Box.createVerticalStrut(10));
        panelPrincipal.add(panelConsulta);

        setContentPane(panelPrincipal);
    }

    public JTextField getTxtCodigo() {
        return txtCodigo;

    }

    public JTextField getTxtNombre(){
        return txtNombre;
    }

    public JTextField getTxtTarifaBase() {
        return txtTarifaBase;
    }

    public JTextField getTxtCupoTotal() {
        return txtCupoTotal;
    }

    public JComboBox<String> getCmbTipo() {
        return cmbTipo;
    }

    public JTextField getTxtCodigoConsulta() {
        return txtCodigoConsulta;
    }

    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnCargar() {
        return btnCargar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnMostrarTodas() {
        return btnMostrarTodas;
    }

    public JButton getBtnInscribir() {
        return btnInscribir;

    }

    public JTextArea getTxtResultado() { return txtResultado; }

    public void mostrarResultado(String texto) { txtResultado.setText(texRto); }

    public void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtTarifaBase.setText("");
        txtCupoTotal.setText("");
        cmbTipo.setSelectedIndex(0);
    }
}
