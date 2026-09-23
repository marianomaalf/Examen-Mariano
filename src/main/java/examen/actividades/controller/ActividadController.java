package examen.actividades.controller;

import examen.actividades.model.Actividad;
import examen.actividades.service.ActividadService;
import examen.actividades.view.VentanaActividades;
import examen.actividades.repository.RepositorioActividadTxt;
import java.util.List;

public class ActividadController {
    private final VentanaActividades vista;
    private final ActividadService servicio;

    // Conserva un ActividadService conectado a RepositorioActividadTxt("actividades.txt").
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
        vista.getBtnBuscar().addActionListener(e -> buscar());
        vista.getBtnInscribir().addActionListener(e -> inscribir());
        vista.getBtnListar().addActionListener(e -> mostrarTodas());
    }

    public void iniciar() {
        try {
            // Dejar el selector sin selección inicial
            vista.getCmbTipo().setSelectedItem(null);
            // Solicitar carga inicial de datos desde repositorio
            servicio.cargarDatos();
            // Mostrar todas tras carga
            mostrarTodas();
            vista.setVisible(true);
        } catch (Exception e) {
            vista.mostrarResultado("Error al cargar datos iniciales:\n" + e.getMessage());
        }
    }

    // registrar(): leer formulario, convertir y registrar; si ok, mostrar todas.
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
            // Mostrar todas las actividades y anteponer mensaje de éxito
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

    // buscar(): consultar txtCodigoConsulta y mostrar la actividad o mensaje.
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

    // inscribir(): usar txtCodigoConsulta sin búsqueda previa; inscribir y actualizar listado.
    public void inscribir() {
        try {
            String codigo = vista.getTxtCodigoConsulta().getText().trim();
            if (codigo.isEmpty()) {
                vista.mostrarResultado("Debe ingresar un código para inscribir.");
                return;
            }
            servicio.inscribir(codigo);
            // Mostrar todas las actividades y anteponer mensaje de éxito
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

    // mostrarTodas(): obtener la lista del servicio y mostrar todas en orden.
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

    // limpiar(): vaciar los cinco campos de texto y dejar el tipo sin selección.
    public void limpiar() {
        vista.limpiarFormulario();
        // dejar tipo sin selección explícitamente
        vista.getCmbTipo().setSelectedItem(null);
    }

    // guardarDatos(): solicitar guardado al servicio y mostrar confirmación o error.
    public void guardarDatos() {
        try {
            servicio.guardarDatos();
            vista.mostrarResultado("Datos guardados correctamente.");
        } catch (Exception e) {
            vista.mostrarResultado("Error al guardar datos:\n" + e.getMessage());
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

