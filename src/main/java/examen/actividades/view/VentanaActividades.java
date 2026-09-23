package examen.actividades.view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/** Ventana vacía proporcionada. Construya aquí únicamente la presentación. */
public class VentanaActividades extends JFrame {
    public VentanaActividades() {
        super("Examen de Programación III - Actividades");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(new JLabel(
                "Construya aquí el formulario indicado en el enunciado.",
                SwingConstants.CENTER), BorderLayout.CENTER);
        setContentPane(panelPrincipal);
        // TODO: sustituir el texto por los controles del examen.
        // Exponga métodos para que el controlador lea datos y conecte listeners.
    }
}
