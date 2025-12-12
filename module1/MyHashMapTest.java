package aston2.module1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyHashMapTest {
    private MyHashMap map;

    @BeforeEach
    void setUp() {
        map = new MyHashMap<>();
        map.put("Subaru Impreza", 280);
        map.put("Lada 2109", 156);
        map.put("Mitsubishi Lancer", 250);
    }

    @Test
    void put() {
        map.clear();
        map.put("Subaru Impreza", 280);
        map.put("Lada 2109", 156);
        map.put("Mitsubishi Lancer", 250);
        assertEquals(3, map.size());
    }

    @Test
    void get() {
        assertEquals(280, map.get("Subaru Impreza"));
        assertEquals(250, map.get("Mitsubishi Lancer"));
        assertEquals(156, map.get("Lada 2109"));
        assertNull(map.get("Suzuki"));
    }

    @Test
    void remove() {
        assertTrue(map.remove("Lada 2109"));
        assertFalse(map.remove("Suzuki"));
        assertEquals(2, map.size());
    }

    @Test
    void containsKey() {
        assertTrue(map.containsKey("Lada 2109"));
        assertTrue(map.containsKey("Subaru Impreza"));
        assertFalse(map.containsKey("Suzuki"));
    }

    @Test
    void size() {
        assertEquals(3, map.size());
        map.remove("Lada 2109");
        assertEquals(2, map.size());
        map.put("Lada 2109", 156);
        assertEquals(3, map.size());
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    void isEmpty() {
        assertFalse(map.isEmpty());
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    void clear() {
        assertEquals(3, map.size());
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    void keySet() {
        Set<String> set1 = new HashSet<>(map.keySet());
        assertEquals(3, set1.size());
        Set<String> set2 = new HashSet<>(List.of("Subaru Impreza", "Lada 2109", "Mitsubishi Lancer"));
        assertTrue(set1.containsAll(set2));
        assertTrue(set2.containsAll(set1));
    }

    @Test
    void values() {
        Set<Integer> set1 = new HashSet<>(map.values());
        assertEquals(3, set1.size());
        Set<Integer> set2 = new HashSet<>(List.of(280, 156, 250));
        assertTrue(set1.containsAll(set2));
        assertTrue(set2.containsAll(set1));
    }
}