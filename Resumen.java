import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.Serializable;

/**
 * Represents a summary of a research or document, including its title, authors, body, and keywords.
 * It can be constructed either by reading from a file or by directly providing the details.
 */
public class Resumen implements Serializable {
    String titulo;
    String[] autores;
    String cuerpo;
    String[] palabrasClave;
    String rutaArchivo;

    /**
     * Constructs a Resumen object by reading the details from a specified file.
     * The file is expected to have at least four lines: title, authors (comma-separated), body, and keywords (comma-separated).
     * 
     * @param archivoRuta The path to the file from which to read the Resumen details.
     * @throws IOException If an I/O error occurs reading from the file or the file does not have enough lines.
     */
    public Resumen(String archivoRuta) throws IOException {
        this.rutaArchivo = archivoRuta;
        String contenido = new String(Files.readAllBytes(Paths.get(archivoRuta)));
        String[] lineas = contenido.split("\n");
        if (lineas.length >= 4) { // Ensure the file has at least 4 lines
            this.titulo = lineas[0];
            this.autores = lineas[1].split(",");
            // Store the entire file content in cuerpo
            this.cuerpo = contenido;
            this.palabrasClave = lineas[3].split(",");
        } else {
            throw new IOException("El archivo no tiene suficientes líneas.");
        }
    }

    /**
     * Constructs a Resumen object directly with the provided details.
     * 
     * @param titulo The title of the summary.
     * @param autores An array of authors of the summary.
     * @param cuerpo The body of the summary.
     * @param palabrasClave An array of keywords associated with the summary.
     * @param rutaArchivo The file path where the summary is stored or associated with.
     */
    public Resumen(String titulo, String[] autores, String cuerpo, String[] palabrasClave, String rutaArchivo) {
        this.titulo = titulo;
        this.autores = autores;
        this.cuerpo = cuerpo;
        this.palabrasClave = palabrasClave;
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * Returns the title of the summary.
     * 
     * @return The title of the summary.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Returns the authors of the summary.
     * 
     * @return An array of authors.
     */
    public String[] getAutores() {
        return autores;
    }

    /**
     * Returns the body of the summary.
     * 
     * @return The body of the summary.
     */
    public String getCuerpo() {
        return cuerpo;
    }

    /**
     * Returns the keywords associated with the summary.
     * 
     * @return An array of keywords.
     */
    public String[] getPalabrasClave() {
        return palabrasClave;
    }

    /**
     * Returns the file path associated with the summary.
     * 
     * @return The file path.
     */
    public String getRutaArchivo() {
        return rutaArchivo;
    }
}