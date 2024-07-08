/**
 * Representa un iterador sobre una colección de elementos de tipo {@code T}. 
 * Un iterador permite al usuario recorrer la colección, elemento por elemento, sin exponer la estructura interna de la misma.
 *
 * @param <T> el tipo de elementos que este iterador puede recorrer.
 */
public interface Iterator<T> {
    /**
     * Comprueba si la colección tiene más elementos.
     * 
     * @return {@code true} si la colección tiene más elementos, {@code false} en caso contrario.
     */
    boolean hasNext();

    /**
     * Devuelve el siguiente elemento en la colección.
     * 
     * @return el siguiente elemento de tipo {@code T}.
     * @throws NoSuchElementException si la colección no tiene más elementos.
     */
    T next();
}