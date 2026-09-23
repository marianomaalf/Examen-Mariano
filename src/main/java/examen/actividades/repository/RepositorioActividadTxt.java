package examen.actividades.repository;

import examen.actividades.model.Actividad;
import examen.actividades.model.ActividadPresencial;
import examen.actividades.model.ActividadVirtual;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioActividadTxt implements Repositorio<Actividad> {

    private String rutaArchivo;

    public RepositorioActividadTxt(String rutaArchivo) throws IllegalArgumentException {
        this.rutaArchivo = rutaArchivo;

        if(rutaArchivo == null){
            throw new IllegalArgumentException("La ruta del archivo no puede ser nula");
        }

    }

    @Override
    public List<Actividad> cargarTodos() throws IOException {

        if(Files.notExists(Paths.get(rutaArchivo))){
            return new ArrayList<>();
        }

        try{
            List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo));
            
            if(lineas.isEmpty()){
                return new ArrayList<>();
            }

            List<Actividad> lista = new ArrayList<>();

            for(String linea : lineas){
                String[] datos = linea.split(",");

                if(datos.length == 6){
                    String codigo = datos[0].trim();
                    String nombre = datos[1].trim();
                    double tarifaBase = Double.parseDouble(datos[2].trim());
                    int cupoTotal = Integer.parseInt(datos[3].trim());
                    int inscritos = Integer.parseInt(datos[4].trim());
                    String tipo = datos[5].trim();

                    Actividad actividad;
                    if(tipo.equalsIgnoreCase("PRESENCIAL")){
                        actividad = new ActividadPresencial(codigo, nombre, tarifaBase, cupoTotal);
                    } else if(tipo.equalsIgnoreCase("VIRTUAL")){
                        actividad = new ActividadVirtual(codigo, nombre, tarifaBase, cupoTotal);
                    } else {
                        throw new IOException("Tipo de actividad inválido: " + tipo);
                    }
                    
                    actividad.setInscritos(inscritos);
                    lista.add(actividad);
                }
            }

            return lista;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("Error al convertir líneas a objetos Actividad", e);
        }
        
    }

    @Override
    public void guardarTodos(List<Actividad> lista) {


    }


}
