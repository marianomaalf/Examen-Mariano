package examen.actividades.model;

public class ActividadPresencial extends Actividad{

    private final double RECARGO_PRESENCIAL = 1500;

    public ActividadPresencial(String codigo, String nombre, double tarifaBase, int cupoTotal) {
        super(codigo, nombre, tarifaBase, cupoTotal);
    }

    public int getCuposDisponibles() {
        return getCupoTotal() - getInscritos();
    }

    @Override
   public TipoActividad getTipoActividad() {

    return TipoActividad.PRESENCIAL;

    }

    @Override
    public double calcularTarifaFinal() {
        return getTarifaBase() + RECARGO_PRESENCIAL;
    }


    @Override
    public String toString() {
        return "ActividadPresencial{" +
                "codigo='" + getCodigo() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", tarifaBase=" + getTarifaBase() +
                ", tarifaFinal=" + calcularTarifaFinal() +
                ", cupoTotal=" + getCupoTotal() +
                ", inscritos=" + getInscritos() +
                '}';
    }





}
