/*
MATERIAL DE APOYO: PERSISTENCIA TXT
Programación III — Referencia del laboratorio de flota vehicular

CÓMO USAR ESTE ARCHIVO
Coloque este archivo en material_apoyo, junto a pom.xml y fuera de src.
Ábralo en IntelliJ para consultar los ejemplos. No necesita ejecutarlo.
Todo el contenido está comentado: reúne fragmentos de varias clases del
laboratorio, no una clase lista para incorporarse al proyecto del examen.
Los fragmentos dependen del modelo de vehículos y de su interfaz de repositorio.

ORDEN DE LECTURA
1. Recorrido de los datos.
2. Repositorio: leer, escribir y convertir objetos.
3. Servicio: mantener la lista y solicitar la persistencia.
4. Controlador: conectar las clases y atender una acción.
5. Diferencias que debe considerar al adaptar el ejemplo al examen.

1. RECORRIDO DE LOS DATOS

AL GUARDAR:
El controlador llama al servicio. El servicio entrega su lista al repositorio.
El repositorio convierte cada vehículo en texto y escribe el archivo.

AL CARGAR:
El repositorio lee las líneas y reconstruye los vehículos. Devuelve una lista.
El servicio conserva esa lista en memoria. El controlador pide los datos al
servicio para mostrarlos en la interfaz.

La lista y el TXT tienen funciones diferentes: la lista es el estado de trabajo
mientras el programa está abierto; el TXT conserva lo guardado entre ejecuciones.
Cambiar un objeto en memoria no cambia automáticamente el archivo.

2. REPOSITORIO DEL LABORATORIO

Ejemplo basado en RepositorioVehiculoTxt, con comentarios ampliados.
Se conserva el formato y el manejo de errores del laboratorio.
RepositorioVehiculo declara cargarTodos() y guardarTodos(List<Vehiculo>).
PersistenciaException es la excepción propia utilizada en ese laboratorio.
No copie esas dependencias al examen: revise la sección 5.

*/

package lab.flotavehicular.repository;

import lab.flotavehicular.model.EstadoVehiculo;
import lab.flotavehicular.model.TipoCarga;
import lab.flotavehicular.model.TipoVehiculo;
import lab.flotavehicular.model.Vehiculo;
import lab.flotavehicular.model.VehiculoCombustion;
import lab.flotavehicular.model.VehiculoElectrico;
import lab.flotavehicular.model.VehiculoPesado;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class RepositorioVehiculoTxt implements RepositorioVehiculo {

    // El laboratorio tiene diez columnas y escribe un encabezado.
    // Su orden debe coincidir al guardar y al leer.
    private static final String ENCABEZADO =
            "tipo;placa;marca;kilometraje;estado;energia;ciclos;tonelaje;carga;tipoCarga";

    private final Path archivo;

    public RepositorioVehiculoTxt(Path archivo) {
        // Conserva la ubicación. Aquí todavía no se lee ni se escribe.
        this.archivo = archivo;
    }

    @Override
    public List<Vehiculo> cargarTodos() {
        // Es normal que todavía no exista el archivo en la primera ejecución.
        if (Files.notExists(archivo)) {
            return new ArrayList<>();
        }

        try {
            // La lista se arma aquí y se devuelve cuando termina la lectura.
            List<Vehiculo> vehiculos = new ArrayList<>();

            // readAllLines devuelve las líneas del archivo como List<String>.
            // UTF-8 permite conservar caracteres como tildes y ñ.
            for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
                // El encabezado describe columnas, no representa un vehículo.
                if (linea.isBlank() || linea.equals(ENCABEZADO)) {
                    continue;
                }
                // Cada línea de datos se convierte en un objeto.
                vehiculos.add(convertirDesdeLinea(linea));
            }

            // Si el archivo está vacío, la lista permanece vacía.
            return vehiculos;
        } catch (IOException | IllegalArgumentException e) {
            // IOException: problema de acceso o lectura.
            // IllegalArgumentException: datos que no se pueden interpretar.
            // El laboratorio envuelve ambos en su excepción de persistencia.
            throw new PersistenciaException(
                    "No se pudieron cargar los vehículos desde " + archivo + ".", e
            );
        }
    }

    @Override
    public void guardarTodos(List<Vehiculo> vehiculos) {
        try {
            // Para datos/vehiculos.txt, la carpeta padre es datos.
            // Para un nombre sin carpeta, getParent() puede devolver null.
            Path carpeta = archivo.getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }

            // Primero se prepara el contenido completo en memoria.
            List<String> lineas = new ArrayList<>();
            lineas.add(ENCABEZADO);
            for (Vehiculo vehiculo : vehiculos) {
                lineas.add(convertirALinea(vehiculo));
            }

            // Files.write escribe cada elemento de la lista como una línea.
            // CREATE: crea el archivo si no existe.
            // TRUNCATE_EXISTING: borra el contenido anterior para reemplazarlo.
            // No se usa APPEND: guardar dos veces no debe acumular copias.
            Files.write(
                    archivo,
                    lineas,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new PersistenciaException(
                    "No se pudieron guardar los vehículos en " + archivo + ".", e
            );
        }
    }

    private String convertirALinea(Vehiculo vehiculo) {
        // Los tres tipos comparten columnas, pero algunos campos no aplican.
        String energia = "";
        String ciclos = "";
        String tonelaje = "";
        String carga = "";
        String tipoCarga = "";

        // Pesado se revisa primero porque también es un VehiculoCombustion.
        // instanceof identifica el subtipo; pesado/electrico/combustion son
        // variables que permiten consultar sus métodos específicos.
        if (vehiculo instanceof VehiculoPesado pesado) {
            energia = String.valueOf(pesado.getNivelCombustible());
            tonelaje = String.valueOf(pesado.getTonelajeMaximo());
            carga = String.valueOf(pesado.getCargaActual());
            tipoCarga = pesado.getTipoCarga().name();
        } else if (vehiculo instanceof VehiculoElectrico electrico) {
            energia = String.valueOf(electrico.getPorcentajeBateria());
            ciclos = String.valueOf(electrico.getCiclosDeCarga());
        } else if (vehiculo instanceof VehiculoCombustion combustion) {
            energia = String.valueOf(combustion.getNivelCombustible());
        }

        // String.valueOf convierte los números en texto.
        // name() obtiene el nombre exacto de una constante enum.
        // String.join une los campos e intercala el separador indicado.
        return String.join(";",
                vehiculo.getTipo().name(),
                vehiculo.getPlaca(),
                vehiculo.getMarca(),
                String.valueOf(vehiculo.getKilometraje()),
                vehiculo.getEstado().name(),
                energia,
                ciclos,
                tonelaje,
                carga,
                tipoCarga
        );
    }

    private Vehiculo convertirDesdeLinea(String linea) {
        // split divide la línea por el separador.
        // -1 evita descartar campos vacíos al FINAL de la línea.
        // Los índices empiezan en cero: datos[0] es la primera columna.
        String[] datos = linea.split(";", -1);
        if (datos.length != 10) {
            throw new IllegalArgumentException("Línea inválida: " + linea);
        }

        // Se invierte la conversión realizada al guardar.
        TipoVehiculo tipo = TipoVehiculo.valueOf(datos[0]);
        String placa = datos[1];
        String marca = datos[2];
        int kilometraje = Integer.parseInt(datos[3]);
        EstadoVehiculo estado = EstadoVehiculo.valueOf(datos[4]);
        double energia = Double.parseDouble(datos[5]);

        // No basta con recuperar los textos: hay que construir objetos.
        // El tipo almacenado permite decidir qué constructor utilizar.
        // Este switch devuelve un objeto; también se podría usar if/else.
        Vehiculo vehiculo = switch (tipo) {
            case COMBUSTION -> new VehiculoCombustion(
                    placa, marca, kilometraje, energia
            );
            case ELECTRICO -> new VehiculoElectrico(
                    placa, marca, kilometraje, energia,
                    Integer.parseInt(datos[6])
            );
            case PESADO -> {
                VehiculoPesado pesado = new VehiculoPesado(
                        placa, marca, kilometraje, energia,
                        Double.parseDouble(datos[7]),
                        TipoCarga.valueOf(datos[9])
                );
                double cargaActual = Double.parseDouble(datos[8]);
                // El constructor comienza sin carga; se restaura la guardada.
                if (cargaActual > 0) {
                    pesado.cargarMercancia(cargaActual);
                }
                // yield indica el objeto que devuelve esta rama del switch.
                yield pesado;
            }
        };

        // El constructor comienza DISPONIBLE; se recupera el estado del TXT.
        vehiculo.setEstado(estado);
        return vehiculo;
    }
}

/*

EJEMPLO DE LÍNEA DEL LABORATORIO
COMBUSTION;ABC123;Toyota;1200;DISPONIBLE;50.0;;;;

Las cuatro columnas finales están vacías porque no corresponden a ese subtipo.
convertirALinea y convertirDesdeLinea deben coincidir en el orden de campos.
Una lista vacía en ESTE laboratorio deja el encabezado; en el examen debe
quedar un archivo completamente vacío porque no se utiliza encabezado.

3. SERVICIO: FRAGMENTOS DE FlotaService

Se omiten registro, edición y eliminación para concentrarse en persistencia.
En el laboratorio esas operaciones llaman guardarCambios() después de modificar
la lista. No necesita copiar esas operaciones para estudiar lectura y escritura.

*/
// Imports del fragmento:
 import lab.flotavehicular.model.Vehiculo;
 import lab.flotavehicular.repository.RepositorioVehiculo;
 import java.util.ArrayList;
 import java.util.List;

public class FlotaService {
    private final List<Vehiculo> vehiculos;
    private final RepositorioVehiculo repositorio;

    public FlotaService(RepositorioVehiculo repositorio) {
        this.repositorio = repositorio;
        // En el LABORATORIO se carga durante la construcción del servicio.
        // cargarTodos devuelve objetos ya reconstruidos por el repositorio.
        this.vehiculos = new ArrayList<>(repositorio.cargarTodos());
    }

    public List<Vehiculo> obtenerVehiculos() {
        // Devuelve otra lista para mostrar los datos sin exponer la lista interna.
        return new ArrayList<>(vehiculos);
    }

    public void guardarCambios() {
        // El servicio pasa su lista, no una lista nueva vacía.
        // No necesita conocer separadores, columnas ni operaciones de Files.
        repositorio.guardarTodos(vehiculos);
    }
}

/* 
4. CONTROLADOR: CONEXIÓN Y USO

FRAGMENTO DE CONSTRUCCIÓN DEL CONTROLADOR DEL LABORATORIO
Se muestran solo las conexiones relacionadas con persistencia.
Se omiten la cola de mantenimiento y la configuración de los controles.

*/

private final FlotaService flota;

public FlotaController() {
    RepositorioVehiculo repositorio = new RepositorioVehiculoTxt(
            Path.of("datos", "vehiculos.txt")
    );
    this.flota = new FlotaService(repositorio);
    // En este laboratorio, la construcción del servicio ya solicitó la carga.
}

// FRAGMENTO DE UNA ACCIÓN DEL LABORATORIO
// Después de comprobar que hay un vehículo seleccionado, iniciarRuta realiza:

try {
        seleccionado.iniciarRuta();       // Modifica el objeto en memoria.
    flota.guardarCambios();           // Servicio -> repositorio -> TXT.
actualizarTabla();               // Muestra el estado actualizado.
mostrarMensaje("Ruta iniciada", "La operación terminó correctamente.");
} catch (IllegalStateException | PersistenciaException e) {
mostrarError(e.getMessage());
        }

/*
actualizarTabla, mostrarMensaje y mostrarError son métodos auxiliares del
controlador del laboratorio; no son métodos incorporados al lenguaje Java.
seleccionado es el Vehiculo obtenido de la selección de la tabla.
La confirmación se muestra después del guardado. Si guardarCambios falla,
se pasa al catch y no se anuncia que el guardado terminó correctamente.
Este ejemplo no revierte automáticamente los cambios que ya se hicieron
en memoria si después falla la escritura del archivo.

5. CÓMO ADAPTAR EL EJEMPLO A LOS REQUISITOS DEL EXAMEN

A. MANTENGA LOS CONTRATOS DEL ENUNCIADO
El examen solicita Repositorio<T> y una implementación para Actividad.
El modelo del laboratorio es una referencia; no agregue vehículos a su examen.
Utilice los nombres de clases, métodos y parámetros solicitados en el enunciado.

B. RUTA DEL ARCHIVO
El laboratorio recibe Path y utiliza datos/vehiculos.txt.
El examen recibe String rutaArchivo y utiliza actividades.txt en la raíz.
Path.of(rutaArchivo) permite convertir ese String en un Path para usar Files.
La ruta relativa se resuelve desde el directorio de ejecución. Ejecute desde
la carpeta raíz del proyecto, donde se encuentra pom.xml.

C. FORMATO Y CONVERSIÓN
El examen utiliza seis campos, en el orden exacto indicado en su enunciado.
No escriba ENCABEZADO ni agregue una línea con nombres de columnas.
Adapte convertirALinea y convertirDesdeLinea al modelo de actividades.
Reconstruya la subclase correspondiente y conserve el contador almacenado;
cargar información no equivale a registrar una actividad nueva con contador cero.
No guarde datos calculados que el enunciado indica obtener mediante métodos.
En las pruebas del examen se usarán archivos con formato válido.

D. DIFERENCIA ENTRE CARGA INICIAL Y CONSTRUCTOR
En el examen, el constructor del servicio deja la lista vacía.
El controlador solicita la carga UNA VEZ cuando la vista está disponible.
El servicio pide los datos al repositorio y sustituye su lista solo después de
una lectura exitosa. No añada otra copia de lo cargado a una lista ya poblada.
Si la lectura falla, debe conservarse la lista anterior.
JavaFX: haga esa solicitud cuando FXMLLoader ya haya inyectado los controles,
por ejemplo llamando iniciar() desde initialize().
Swing: haga esa solicitud después de construir la vista y conectar el servicio.

E. GUARDADO MANUAL
El laboratorio guarda después de operaciones como agregar o iniciar una ruta.
El examen guarda únicamente cuando se pulsa el botón Guardar datos.
Por eso NO copie las llamadas de autoguardado del laboratorio en las acciones
de registrar o inscribir. Conecte el botón al controlador y delegue al servicio.
El servicio debe entregar al repositorio toda su lista actual.

F. ERRORES: IOException EN EL EXAMEN
El laboratorio captura IOException y la envuelve en PersistenciaException.
El examen pide declarar throws IOException en los métodos de carga y guardado
del repositorio y del servicio. No necesita crear PersistenciaException.

Declarar throws IOException significa que el método permite que ese error
llegue al método que lo llamó. Si no necesita manejarlo allí, puede dejar que
la operación de Files lo propague sin envolverla en otra excepción.
El controlador sí lo captura para comunicar el problema en la interfaz.

*/

// EJEMPLO ADAPTADO DE MANEJO DE ERROR (solo referencia, nombres ilustrativos):

// En el servicio, un método de persistencia declara throws IOException.
// En una acción del controlador que lo llama:
        try {
        servicio.guardarDatos();
    lblMensaje.setText("Datos guardados correctamente.");
} catch (IOException e) {
        lblMensaje.setText("No se pudieron guardar los datos. Revise la ruta y los permisos.");
}
/*
Se necesita importar java.io.IOException.
servicio representa el servicio que conserva el controlador.
lblMensaje debe ser el control ya creado o inyectado en la vista.
Label de JavaFX y JLabel de Swing tienen el método setText mostrado.
Para la carga, aplique el mismo criterio: capture el error y comuníquelo;
no anuncie una carga exitosa cuando haya fallado.

G. COMPROBACIONES DE PERSISTENCIA
- Al iniciar sin archivo, la aplicación debe quedar operativa con lista vacía.
- Al guardar dos veces la misma lista, no deben duplicarse líneas.
- Al guardar una lista vacía, el archivo debe quedar vacío.
- Después de guardar, cerrar y abrir, deben recuperarse los datos persistidos.
- Un error de acceso debe mostrarse; no debe confundirse con un archivo vacío.

El ejemplo del laboratorio sirve para comprender y adaptar la persistencia.
Debe implementar e integrar las clases requeridas por el enunciado del examen.
*/