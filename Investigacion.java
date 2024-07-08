/**
 * Represents a research investigation with an ID, title, summary, and a list of authors.
 */
public class Investigacion {
    private int id;
    private String titulo;
    private Resumen resumen;
    private MyLinkedList<String> autores;

    /**
     * Constructs a new Investigacion instance with specified ID, title, summary, and authors.
     * 
     * @param id The unique identifier for the investigation. Must be a positive number.
     * @param titulo The title of the investigation. Cannot be null or empty.
     * @param resumen The summary of the investigation. Can be null.
     * @param autores A list of authors involved in the investigation. Cannot be null.
     * @throws IllegalArgumentException If the ID is negative or the title is null/empty.
     */
    public Investigacion(int id, String titulo, Resumen resumen, MyLinkedList<String> autores) {
        if (id < 0) throw new IllegalArgumentException("El ID debe ser positivo.");
        if (titulo == null || titulo.isEmpty()) throw new IllegalArgumentException("El título no puede ser nulo o vacío.");
        this.id = id;
        this.titulo = titulo;
        this.resumen = resumen;
        this.autores = autores;
    }

    /**
     * Constructs a new Investigacion instance with a specified summary. Other fields are initialized with default values.
     * 
     * @param resumen The summary of the investigation. Can be null.
     */
    public Investigacion(Resumen resumen) {
        this.resumen = resumen;
        // Initialize other fields with default values or based on the Resumen object
        this.id = 0;
        this.titulo = "";
        this.autores = new MyLinkedList<String>();
    }

    /**
     * Returns the ID of the investigation.
     * 
     * @return The ID of the investigation.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the title of the investigation.
     * 
     * @return The title of the investigation.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Returns the summary of the investigation.
     * 
     * @return The summary of the investigation.
     */
    public Resumen getResumen() {
        return resumen;
    }

    /**
     * Returns the list of authors of the investigation.
     * 
     * @return The list of authors.
     */
    public MyLinkedList<String> getAutores() {
        return autores;
    }

    /**
     * Returns a string representation of the investigation, including its ID, title, summary, and authors.
     * 
     * @return A string representation of the investigation.
     */
    @Override
    public String toString() {
        return "Investigacion{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", resumen=" + resumen +
                ", autores=" + autores +
                '}';
    }
}