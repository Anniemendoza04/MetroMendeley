/**
 * Manages the search functionality for summaries (resumenes) by keyword and author.
 * It allows adding new investigations to the search index and searching for investigations by keyword or author.
 */
public class BusquedaResumen {
    private MyMap<String, MyLinkedList<Investigacion>> resumenesPorPalabraClave;
    private MyMap<String, MyLinkedList<Investigacion>> resumenesPorAutor;

    /**
     * Initializes the search manager with empty search indexes for keywords and authors.
     */
    public BusquedaResumen() {
        if (this.resumenesPorPalabraClave == null) {
            this.resumenesPorPalabraClave = new MyMap<>();
        }
        if (this.resumenesPorAutor == null) {
            this.resumenesPorAutor = new MyMap<>();
        }
    }

    /**
     * Adds an investigation to the search indexes. It updates both the keyword and author indexes.
     * 
     * @param palabraClave The keyword associated with the investigation.
     * @param investigacion The investigation object to be added.
     */
    public void agregarInvestigacion(String palabraClave, Investigacion investigacion) {
        palabraClave = palabraClave.toLowerCase(); // Ensure consistency in keyword case
        // Add investigation by keyword
        if (!resumenesPorPalabraClave.containsKey(palabraClave)) {
            resumenesPorPalabraClave.put(palabraClave, new MyLinkedList<>());
        }
        resumenesPorPalabraClave.get(palabraClave).add(investigacion);

        // Update resumenesPorAutor
        MyLinkedList<String> autores = investigacion.getAutores();
        Iterator<String> autoresIterator = autores.iterator();
        while (autoresIterator.hasNext()) {
            String autor = autoresIterator.next().toLowerCase(); // Ensure consistency in author name case
            if (!resumenesPorAutor.containsKey(autor)) {
                resumenesPorAutor.put(autor, new MyLinkedList<>());
            }
            resumenesPorAutor.get(autor).add(investigacion);
        }
        System.out.println("Mapa actual: " + resumenesPorPalabraClave);
    }

    /**
     * Searches for investigations by a given keyword.
     * 
     * @param palabraClave The keyword to search for.
     * @return A list of investigations associated with the given keyword.
     */
    public MyLinkedList<Investigacion> buscarPorPalabraClave(String palabraClave) {
        palabraClave = palabraClave.toLowerCase(); // Ensure consistency in keyword case
        if (resumenesPorPalabraClave.containsKey(palabraClave)) {
            return resumenesPorPalabraClave.get(palabraClave);
        }
        return new MyLinkedList<>();
    }

    /**
     * Searches for investigations by a given author.
     * 
     * @param autor The author to search for.
     * @return A list of investigations associated with the given author.
     */
    public MyLinkedList<Investigacion> buscarPorAutor(String autor) {
        autor = autor.toLowerCase(); // Ensure consistency in author name case
        if (this.resumenesPorAutor.containsKey(autor)) {
            return this.resumenesPorAutor.get(autor);
        }
        return new MyLinkedList<>();
    }
}