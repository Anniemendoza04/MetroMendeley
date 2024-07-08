import java.io.Serializable;

/**
 * A custom implementation of a map data structure that associates keys with values.
 * It is not based on the standard Java Map interface but provides similar functionality.
 * This implementation uses a linked list to store entries, making it suitable for small datasets.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
class MyMap<K, V> implements Iterable<V>, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Represents a key-value pair in the map.
     *
     * @param <K> the type of the key
     * @param <V> the type of the value
     */
    public static class Entry<K, V> {
        K key;
        V value;

        /**
         * Constructs an Entry with the specified key and value.
         *
         * @param key   the key associated with the entry
         * @param value the value associated with the entry
         */
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Returns the key associated with this entry.
         *
         * @return the key of this entry
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the value associated with this entry.
         *
         * @return the value of this entry
         */
        public V getValue() {
            return value;
        }
    }

    private MyLinkedList<Entry<K, V>> entries;

    /**
     * Constructs an empty MyMap.
     */
    public MyMap() {
        entries = new MyLinkedList<>();
    }

    public MyLinkedList<Entry<K, V>> entryList() {
        return entries;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value is replaced.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     */
    public void put(K key, V value) {
        for (int i = 0; i < entries.size(); i++) {
            Entry<K, V> entry = entries.get(i);
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        entries.add(new Entry<>(key, value));
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the key
     */
    public V get(K key) {
        for (int i = 0; i < entries.size(); i++) {
            Entry<K, V> entry = entries.get(i);
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key.
     *
     * @param key key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified key
     */
    public boolean containsKey(K key) {
        for (int i = 0; i < entries.size(); i++) {
            Entry<K, V> entry = entries.get(i);
            if (entry.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a MyLinkedList containing the values in this map.
     * The list is not backed by the map, so changes to the map are not reflected in the list, and vice-versa.
     *
     * @return a list of the values in this map
     */
    public MyLinkedList<V> values() {
        MyLinkedList<V> valuesList = new MyLinkedList<>();
        for (int i = 0; i < entries.size(); i++) {
            valuesList.add(entries.get(i).value);
        }
        return valuesList;
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return entries.size();
    }

    /**
     * Returns an iterator over the values in this map.
     *
     * @return an Iterator over the values in this map
     */
    @Override
    public Iterator<V> iterator() {
        return new Iterator<V>() {
            private final Iterator<Entry<K, V>> entryIterator = entries.iterator();

            @Override
            public boolean hasNext() {
                return entryIterator.hasNext();
            }

            @Override
            public V next() {
                return entryIterator.next().value;
            }
        };
    }
}