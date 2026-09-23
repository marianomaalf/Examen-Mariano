package examen.actividades.model;

public abstract class Actividad{

    private String codigo;
    private String nombre;
    private double tarifaBase;
    private int cupoTotal;
    private int inscritos;

    public Actividad(String codigo, String nombre, double tarifaBase, int cupoTotal) throws IllegalArgumentException {
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.tarifaBase = tarifaBase;
        this.inscritos = 0;
        this.cupoTotal = cupoTotal;

        if(codigo.isEmpty() || nombre.isEmpty() || tarifaBase < 0 || cupoTotal < 0){
            throw new IllegalArgumentException("Los datos de la actividad no son válidos.");
        }

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.trim();
    }

    public double getTarifaBase() {
        return tarifaBase;
    }

    public void setTarifaBase(double tarifaBase) {
        this.tarifaBase = tarifaBase;
    }

    public int getCupoTotal() {
        return cupoTotal;
    }

    public void setCupoTotal(int cupoTotal) {
        this.cupoTotal = cupoTotal;
    }

    public int getInscritos() {
        return inscritos;
    }

    public void setInscritos(int inscritos) {
        this.inscritos = inscritos;
    }

    public void inscribirParticipante() throws IllegalStateException {
        if (inscritos >= cupoTotal) {
            throw new IllegalStateException("No hay cupos disponibles para la actividad.");
        }
        inscritos++;
    }


    public abstract double calcularTarifaFinal();

    public abstract TipoActividad getTipoActividad();

}
