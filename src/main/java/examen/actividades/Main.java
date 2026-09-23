package examen.actividades;

import examen.actividades.controller.ActividadController;
import examen.actividades.view.VentanaActividades;
import javax.swing.SwingUtilities;

/** Arranque proporcionado en el hilo de eventos de Swing. */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaActividades ventana = new VentanaActividades();
                ActividadController controlador = new ActividadController(ventana);
                // TODO: cuando implemente iniciar(), invoque controlador.iniciar() aquí.
                ventana.setVisible(true);
            }
        });
    }
}
