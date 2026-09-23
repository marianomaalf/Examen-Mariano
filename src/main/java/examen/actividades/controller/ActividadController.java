package examen.actividades.controller;

import examen.actividades.model.Actividad;
import examen.actividades.service.ActividadService;
import examen.actividades.view.VentanaActividades;
import examen.actividades.repository.RepositorioActividadTxt;
import java.util.List;

public class ActividadController {
    private final VentanaActividades vista;
    private final ActividadService servicio;


    public ActividadController(VentanaActividades vista) {
        this(vista, new ActividadService(new RepositorioActividadTxt("actividades.txt")));
    }

    public ActividadController(VentanaActividades vista, ActividadService servicio) {
        this.vista = vista;
        this.servicio = servicio;
        configurarEventos();
    }

    private void configurarEventos() {
        vista.getBtnRegistrar().addActionListener(e -> registrar());
        vista.getBtnLimpiar().addActionListener(e -> limpiar());
        vista.getBtnGuardar().addActionListener(e -> guardarDatos());
        vista.getBtnCargar().addActionListener(e -> cargarDatos());
        vista.getBtnBuscar().addActionListener(e -> buscar());
        vista.getBtnInscribir().addActionListener(e -> inscribir());
        vista.getBtnMostrarTodas().addActionListener(e -> mostrarTodas());
    }

    public void iniciar() {
        try {

            vista.getCmbTipo().setSelectedItem(null);

            servicio.cargarDatos();

            mostrarTodas();
            vista.setVisible(true);
        } catch (Exception e) {
            vista.mostrarResultado("Error al cargar datos iniciales:\n" + e.getMessage());
        }
    }


    public void registrar() {
        try {
            String codigo = vista.getTxtCodigo().getText().trim();
            String nombre = vista.getTxtNombre().getText().trim();
            double tarifaBase = Double.parseDouble(vista.getTxtTarifaBase().getText().trim());
            int cupoTotal = Integer.parseInt(vista.getTxtCupoTotal().getText().trim());
            Object sel = vista.getCmbTipo().getSelectedItem();
            if (sel == null) {
                throw new IllegalArgumentException("Debe seleccionar un tipo de actividad.");
            }
            String tipo = sel.toString();

            servicio.registrarActividad(codigo, nombre, tarifaBase, cupoTotal, tipo);

            List<Actividad> actividades = servicio.listarActividades();
            StringBuilder sb = new StringBuilder();
            sb.append("Actividad registrada correctamente.\n\n");
            for (Actividad a : actividades) {
                sb.append(formatearActividad(a)).append("\n\n");
            }
            vista.mostrarResultado(sb.toString().trim());
            limpiar();
        } catch (Exception e) {
            vista.mostrarResultado("Error al registrar la actividad:\n" + e.getMessage());
        }
    }


    public void buscar() {
        try {
            String codigo = vista.getTxtCodigoConsulta().getText().trim();
            if (codigo.isEmpty()) {
                vista.mostrarResultado("Debe ingresar un código para consultar.");
                return;
            }
            Actividad actividad = servicio.buscarPorCodigo(codigo);
            if (actividad == null) {
                vista.mostrarResultado("Actividad no encontrada.");
                return;
            }
            vista.mostrarResultado(formatearActividad(actividad));
        } catch (Exception e) {
            vista.mostrarResultado("Error en la consulta:\n" + e.getMessage());
        }
    }


    public void inscribir() {
        try {
            String codigo = vista.getTxtCodigoConsulta().getText().trim();
            if (codigo.isEmpty()) {
                vista.mostrarResultado("Debe ingresar un código para inscribir.");
                return;
            }
            servicio.inscribir(codigo);

            List<Actividad> actividades = servicio.listarActividades();
            StringBuilder sb = new StringBuilder();
            sb.append("Inscripción realizada correctamente.\n\n");
            for (Actividad a : actividades) {
                sb.append(formatearActividad(a)).append("\n\n");
            }
            vista.mostrarResultado(sb.toString().trim());
        } catch (Exception e) {
            vista.mostrarResultado("Error al inscribir:\n" + e.getMessage());
        }
    }


    public void mostrarTodas() {
        try {
            List<Actividad> actividades = servicio.listarActividades();
            if (actividades == null || actividades.isEmpty()) {
                vista.mostrarResultado("No hay actividades registradas.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (Actividad a : actividades) {
                sb.append(formatearActividad(a)).append("\n\n");
            }
            vista.mostrarResultado(sb.toString().trim());
        } catch (Exception e) {
            vista.mostrarResultado("Error al listar actividades:\n" + e.getMessage());
        }
    }


    public void limpiar() {
        vista.limpiarFormulario();
        // dejar tipo sin selección explícitamente
        vista.getCmbTipo().setSelectedItem(null);
    }


    public void guardarDatos() {
        try {
            servicio.guardarDatos();
            vista.mostrarResultado("Datos guardados correctamente.");
        } catch (Exception e) {
            vista.mostrarResultado("Error al guardar datos:\n" + e.getMessage());
        }
    }

    public void cargarDatos() {
        try {
            servicio.cargarDatos();
            mostrarTodas();
        } catch (Exception e) {
            vista.mostrarResultado("Error al cargar datos:\n" + e.getMessage());
        }
    }

    private String formatearActividad(Actividad actividad) {
        if (actividad == null) {
            return "Actividad no encontrada.";
        }

        return "Código: " + actividad.getCodigo()
                + "\nNombre: " + actividad.getNombre()
                + "\nTarifa base: " + String.format("%.2f", actividad.getTarifaBase())
                + "\nTarifa final: " + String.format("%.2f", actividad.calcularTarifaFinal())
                + "\nCupo total: " + actividad.getCupoTotal()
                + "\nInscritos: " + actividad.getInscritos()
                + "\nTipo: " + actividad.getTipoActividad();
    }
}
