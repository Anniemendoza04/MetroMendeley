import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream; 
import java.io.ObjectInputStream;

/**
 * Manages summaries (Resumenes) including their storage, retrieval, and analysis.
 */
public class ResumenManager {
    /**
     * A map of summaries indexed by a hash key generated from their titles.
     */
    MyMap<String, Resumen> resumenes = new MyMap<>();
    private BusquedaResumen busquedaResumen;

    /**
     * Reads the content of a file and returns it as a String.
     * 
     * @param rutaArchivo The path of the file to read.
     * @return The content of the file as a String.
     * @throws IOException If an I/O error occurs reading from the file.
     */
    public String leerContenidoArchivo(String rutaArchivo) throws IOException {
        return new String(Files.readAllBytes(Paths.get(rutaArchivo)));
    }

    /**
     * Default constructor. Initializes the manager and loads summaries from a JSON file.
     */
    public ResumenManager() {
        if (this.resumenes == null) {
            this.resumenes = new MyMap<>();
        }
        cargarResumenesDesdeJson();
    }

/**
     * Constructs a ResumenManager with a list of summaries.
     * 
     * @param resumenesList A list of summaries to initialize the manager with.
     */
    public ResumenManager(MyLinkedList<Resumen> resumenesList) {
        if (this.resumenes == null) {
            this.resumenes = new MyMap<>();
        }
        for (int i = 0; i < resumenesList.size(); i++) {
            Resumen resumen = resumenesList.get(i);
            String clave = generarClaveHash(resumen.getTitulo());
            resumenes.put(clave, resumen);
        }
    }
/**
     * Adds a summary to the manager.
     * 
     * @param titulo The title of the summary.
     * @param autores An array of authors of the summary.
     * @param cuerpo The body of the summary.
     * @param rutaArchivo The file path where the summary is stored.
     * @param palabrasClave An array of keywords associated with the summary.
     * @return The newly added summary, or null if a summary with the same title already exists.
     */
    public Resumen agregarResumen(String titulo, String[] autores, String cuerpo, String rutaArchivo, String[] palabrasClave) {
        String clave = generarClaveHash(titulo);
        if (!resumenes.containsKey(clave)) {
            Resumen nuevoResumen = new Resumen(titulo, autores, cuerpo, palabrasClave, rutaArchivo);
            resumenes.put(clave, nuevoResumen);
            System.out.println("Resumen agregado exitosamente.");
            guardarResumenesComoJson(); // Guardar cambios después de agregar un resumen
            
            // Actualizar BusquedaResumen
            Investigacion nuevaInvestigacion = new Investigacion(nuevoResumen);
            for (String palabraClave : palabrasClave) {
                busquedaResumen.agregarInvestigacion(palabraClave, nuevaInvestigacion);
            }
            
            return nuevoResumen; // Devolver el nuevo Resumen si se agregó exitosamente
        } else {
            System.out.println("El resumen ya existe y no se agregará de nuevo.");
            return null; // O considerar lanzar una excepción si el resumen ya existe
        }
    }

    // Method to list all summary titles
    public String[] listarTitulosResumenes() {
        MyLinkedList<MyMap.Entry<String, Resumen>> entries = resumenes.entryList();
        String[] titulos = new String[entries.size()];
        for (int i = 0; i < entries.size(); i++) {
            // Assuming Resumen class has a getTitulo() method
            titulos[i] = entries.get(i).getValue().getTitulo();
        }
        return titulos;
    }

    public void guardarResumenesComoJson() {
        try (FileOutputStream fileOutputStream = new FileOutputStream("resumenes.dat");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(resumenes);
            System.out.println("Resúmenes guardados exitosamente en resumenes.dat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarResumenesDesdeJson() {
        try {
            File file = new File("resumenes.dat");
            if (file.exists()) {
                try (FileInputStream fileInputStream = new FileInputStream("resumenes.dat");
                     ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                    resumenes = (MyMap<String, Resumen>) objectInputStream.readObject();
                    System.out.println("Resúmenes cargados exitosamente desde resumenes.dat.");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No se encontró el archivo resumenes.dat.");
                // Inicializar el mapa si el archivo no existe para evitar NullPointerException
                if (this.resumenes == null) {
                    this.resumenes = new MyMap<>();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String analizarResumen(int indiceResumenSeleccionado) {
        MyLinkedList<Resumen> listaResumenes = resumenes.values();
        
        if (indiceResumenSeleccionado < 0 || indiceResumenSeleccionado >= listaResumenes.size()) {
            StringBuilder listaTitulos = new StringBuilder("Índice fuera de rango. Resúmenes disponibles:\n");
            for (int i = 0; i < listaResumenes.size(); i++) {
                listaTitulos.append(i).append(": ").append(listaResumenes.get(i).getTitulo()).append("\n");
            }
            return listaTitulos.toString();
        } else {
            Resumen resumenSeleccionado = listaResumenes.get(indiceResumenSeleccionado);
            StringBuilder infoResumen = new StringBuilder();
            infoResumen.append("Nombre del trabajo: ").append(resumenSeleccionado.getTitulo()).append("\nAutores: ");
            for (String autor : resumenSeleccionado.getAutores()) {
                infoResumen.append(autor).append(", ");
            }
            infoResumen.append("\nCuerpo: ").append(resumenSeleccionado.getCuerpo()).append("\nPalabras Clave: ");
            for (String palabraClave : resumenSeleccionado.getPalabrasClave()) {
                infoResumen.append(palabraClave).append(", ");
            }
            return infoResumen.toString();
        }
    }

    private String generarClaveHash(String titulo) {
        return titulo.replaceAll("\\s+", "").toLowerCase();
    }

    private int calcularFrecuencia(String palabra, String texto) {
        String[] palabras = texto.split("\\s+");
        int contador = 0;
        for (String p : palabras) {
            if (p.equalsIgnoreCase(palabra)) {
                contador++;
            }
        }
        return contador;
    }
}