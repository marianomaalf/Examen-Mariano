package examen.actividades;

import examen.actividades.controller.ActividadController;
import examen.actividades.repository.RepositorioActividadTxt;
import examen.actividades.service.ActividadService;
import examen.actividades.view.VentanaActividades;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    RepositorioActividadTxt repositorio = new RepositorioActividadTxt("datos/actividades.txt");
                    ActividadService servicio = new ActividadService(repositorio);
                    VentanaActividades ventana = new VentanaActividades();
                    ActividadController controlador = new ActividadController(ventana, servicio);
                    controlador.iniciar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
