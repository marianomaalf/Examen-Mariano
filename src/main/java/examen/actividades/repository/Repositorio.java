package examen.actividades.repository;

import java.io.IOException;
import java.util.List;

public interface Repositorio<T> {

     public List<T> cargarTodos() throws IOException;

     public void guardarTodos(List<T> lista) throws IOException;

}
