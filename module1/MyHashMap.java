package aston2.module1;

class MyHashMap<K, V> {
    private static final int capacity = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Entry<K, V>[] table;
    private int size;
    private int maxSize;

    public MyHashMap() {
        table = new Entry[capacity];
        maxSize = (int)(capacity * LOAD_FACTOR);
        size = 0;
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        hash = hash ^ (hash >>> 16);
        return hash;
    }

    private int index(int hash, int length) {
        return hash & (length - 1);
    }

    private static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

    public V put(K key, V value) {
        if (size >= maxSize) {
            resize();
        }
        int hash = hash(key);
        int index = index(hash, table.length);

        Entry<K, V> entry = table[index];
        while (entry != null) {
            if (entry.key == key || (entry.key != null && entry.key.equals(key))) {
                V oldValue = entry.value;
                entry.value = value;
                return  oldValue;
            }
            entry = entry.next;
        }
        Entry<K, V> newEntry = new Entry<>(key,value,table[index]);
        table[index] = newEntry;
        size++;

        return null;
    }

    public V get(K key) {
        int hash = hash(key);
        int index = index(hash, table.length);

        Entry<K, V> entry = table[index];
        while (entry != null) {
            if (entry.key == key || (entry.key != null && entry.key.equals(key))) {
                return entry.value;
            }
            entry = entry.next;
        }

        return  null;
    }

    public V remove(K key) {
        int hash = hash(key);
        int index = index(hash,table.length);

        Entry<K, V> prev = null;
        Entry<K, V> current = table[index];

        while (current != null) {
            if (current.key == key || (current.key != null && current.key.equals(key))) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Entry<K, V>[] newTable = new Entry[newCapacity];

        for ( Entry<K, V> entry: table) {
            while (entry != null) {
                Entry<K, V> next = entry.next;

                int newIndex = index(hash(entry.key), newCapacity);

                entry.next = newTable[newIndex];
                newTable[newIndex] = entry;

                entry = next;
            }
        }

        table = newTable;
        maxSize = (int)(newCapacity * LOAD_FACTOR);
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }


}
