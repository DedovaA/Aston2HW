package aston2.module1;

import java.util.*;

class MyHashMap<K, V> {
    private static final int capacity = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Entry<K, V>[] table;    // бакеты
    private int size;   // кол-во заполненных элементов таблицы
    private int maxSize;    // порог (кол-во занятых ячеек), при достижении которого емкость таблицы увеличивается вдвое

    public MyHashMap() {
        table = new Entry[capacity];
        maxSize = (int)(capacity * LOAD_FACTOR);
        size = 0;
    }

    /**
     * Класс представляет элемент мапы. Содержит ключ, значение и ссылку на следующий элемент в цепочке.
     */
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

    /**
     * Метод вычисляет хэш по ключу.
     * Для null-ключа возвращает 0.
     * Взята стандартная хэш-функция использующая беззнаковый сдвиг вправо (старшие 16 бит замещают младшие).
     * Для более равномерного распределения элементов в массиве исп-м XOR^, тогда распределение "0" и "1" будет 50/50.
     */
    private int getHash(K key) {
        if (key == null) {
            return 0;
        }

        int hash = key.hashCode();
        hash = hash ^ (hash >>> 16);
        return hash;
    }

    /**
     * Метод вычисляет индекс бакета в массиве
     * @param hash хэш-код элемента
     * @param tableLength емкость массива
     * @return индекс элемента в массиве
     */
    private int getIndex(int hash, int tableLength) {
        return hash & (tableLength - 1);
    }

    /**
     * Метод для добавления элемента
     */
    public void put(K key, V value) {
        if (size >= maxSize) {
            resize();
        }
        int hash = getHash(key);
        int index = getIndex(hash, table.length);
        // Получаем текущий элемент по данному индексу
        Entry<K, V> existed = table[index];

        // Если по данному индексу отсутствуют элементы, то создаем новый
        if (existed == null) {
            Entry<K, V> newEntry = new Entry<>(key,value,null);
            table[index] = newEntry;
            size++;
        }
        // иначе перебираем всю цепочку, ищем совпадение и перезаписываем значение или добавляем в список новый элемент
        else {
            while (true) {
                if (existed.key.equals(key)) {
                    existed.value = value;
                    return;
                }
                if (existed.next == null) {
                    existed.next = new Entry<>(key,value,null);
                    size++;
                    return;
                }
                existed = existed.next;
            }
        }
    }

    /**
     * Метод возвращает значение по заданному ключу
     * @param key ключ
     * @return значение либо null, если элемент по данному ключу не найден
     */
    public V get(K key) {
        int hash = getHash(key);
        int index = getIndex(hash, table.length);
        Entry<K, V> existed = table[index];

        while (existed != null) {
            if (existed.key.equals(key)) {
                return existed.value;
            }
            existed = existed.next;
        }
        return  null;
    }

    /**
     * Метод удаляет элемент по ключу
     * @param key ключ
     * @return true, если элемент удален и false, если элемент не был найден
     */
    public boolean remove(K key) {
        int hash = getHash(key);
        int index = getIndex(hash,table.length);
        Entry<K, V> existed = table[index];
        // Если ключ совпал, то в текущую ячейку записываем значение следующей и уменьшаем размер
        if (existed != null && existed.key.equals(key)) {
            table[index] = existed.next;
            size--;
            return true;
        } else {
            // Если ключ не совпал, но ячейка не пуста, то проверяем след элемент в цепочке.
            while (existed != null) {
                // Получаем ссылку на след. элемент в цепочке
                Entry<K, V> nextElement = existed.next;
                if (nextElement == null)
                    return false;
                //Если у след. элемента в цепочке ключ совпал, то перезаписываем его на след. элемент
                if (nextElement.key.equals(key)) {
                    existed.next = nextElement.next;
                    size--;
                    return true;
                }
                // Если ни одно условие не выполнилось, то повторяем цикл со следующим элементом в цепос=чке
                existed = existed.next;
            }
        }
        // Если ячейка оказалась пуста изначально
        return false;
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
        Arrays.fill(table, null);
        size = 0;
    }

    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (Entry<K, V> entry: table) {
            Entry<K, V> currentChainElement = entry;
            while (currentChainElement != null) {
                set.add(currentChainElement.key);
                currentChainElement = currentChainElement.next;
            }
        }
        return set;
    }

    public List<V> values() {
        List<V> list = new ArrayList<>();
        for (Entry<K, V> entry: table) {
            Entry<K, V> currentChainElement = entry;
            while (currentChainElement != null) {
                list.add(currentChainElement.value);
                currentChainElement = currentChainElement.next;
            }
        }
        return list;
    }

    /**
     * Метод динамически расширяет таблицу и перезаписывает в нее существующие элементы.
     */
    private void resize() {
        int newCapacity = table.length * 2;
        Entry<K, V>[] newTable = new Entry[newCapacity];

        for ( Entry<K, V> entry: table) {
            while (entry != null) {
                // Сохраняем ссылку на след объект
                Entry<K, V> next = entry.next;
                // Вычисляем новый индекс элемента
                int newIndex = getIndex(getHash(entry.key), newCapacity);
                // Вставляем элемент в начало цепочки нового бакета
                entry.next = newTable[newIndex];
                newTable[newIndex] = entry;
                // Переходим к следующему элементу в старой цепочке
                entry = next;
            }
        }
        // Перезаписываем таблицу
        table = newTable;
        maxSize = (int)(newCapacity * LOAD_FACTOR);
    }

}
