package examen.actividades.model;

public class ActividadVirtual extends Actividad{

    public ActividadVirtual(String codigo, String nombre, double tarifaBase, int cupoTotal) {
        super(codigo, nombre, tarifaBase, cupoTotal);
    }

    @Override
    public TipoActividad getTipoActividad() {
        return TipoActividad.VIRTUAL;
    }

    @Override
    public double calcularTarifaFinal() {
        return getTarifaBase();
    }

    @Override
    public String toString() {
        return "ActividadVirtual{" +
                "codigo='" + getCodigo() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", tarifaBase=" + getTarifaBase() +
                ", tarifaFinal=" + calcularTarifaFinal() +
                ", cupoTotal=" + getCupoTotal() +
                ", inscritos=" + getInscritos() +
                '}';
    }


}






