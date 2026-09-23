package examen.actividades.service;

import examen.actividades.model.Actividad;
import examen.actividades.repository.Repositorio;

import java.util.ArrayList;
import java.util.List;

public class ActividadService {

    private List<Actividad> actividades;
    private Repositorio<Actividad> repositorio;

    public ActividadService(Repositorio<Actividad> repositorio) {
        this.repositorio = repositorio;
        this.actividades = new ArrayList<Actividad>();
        try {
            this.actividades = repositorio.cargarTodos();
        } catch (Exception e) {
            this.actividades = null;
        }

    }

    public void registrarActividad(String codigo, String nombre, double tarifaBase, int cupoTotal, String tipoActividad) throws IllegalArgumentException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        for (Actividad actividad : actividades) {
            if (actividad.getCodigo().equalsIgnoreCase(codigo.trim()) || actividad.getNombre().equalsIgnoreCase(nombre.trim())) {
                throw new IllegalArgumentException("No se pueden registrar actividades con el mismo código o nombre.");
            }
        }

        Actividad actividad;

        if (tipoActividad.equalsIgnoreCase("PRESENCIAL")) {
            actividad = new examen.actividades.model.ActividadPresencial(codigo.trim(), nombre.trim(), tarifaBase, cupoTotal);
        } else if (tipoActividad.equalsIgnoreCase("VIRTUAL")) {
            actividad = new examen.actividades.model.ActividadVirtual(codigo.trim(), nombre.trim(), tarifaBase, cupoTotal);
        } else {
            throw new IllegalArgumentException("Tipo de actividad no válido.");
        }

        actividades.add(actividad);

        try {
            repositorio.guardarTodos(actividades);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la actividad: " + e.getMessage());
        }
    }

    public Actividad buscarPorCodigo(String codigo) {
        for (Actividad actividad : actividades) {
            if (actividad.getCodigo().equalsIgnoreCase(codigo)) {
                return actividad;
            }
        }
        return null;
    }

    public List<Actividad> listarActividades(){

        if(actividades == null) {
            return new ArrayList<>();
        }

        return actividades;
    }

    public void inscribir(String codigo) throws IllegalArgumentException {
        Actividad actividad = buscarPorCodigo(codigo);
        if (actividad == null) {
            throw new IllegalArgumentException("Actividad no encontrada.");
        }
        if (actividad.getInscritos() >= actividad.getCupoTotal()) {
            throw new IllegalArgumentException("No hay cupos disponibles para esta actividad.");
        }
        actividad.setInscritos(actividad.getInscritos() + 1);

        try {
            repositorio.guardarTodos(actividades);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la inscripción: " + e.getMessage());
        }
    }

    public void cargarDatos() throws Exception {
        this.actividades = repositorio.cargarTodos();
    }

    public void guardarDatos() throws Exception {
        repositorio.guardarTodos(actividades);
    }
















}
