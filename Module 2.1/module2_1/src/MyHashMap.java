import java.util.Objects;

// реализация без использования деревьев, надеюсь в рамках этого дз можно

public class MyHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;        
    private static final float DEFAULT_LOAD_FACTOR = 0.75f; 

    private Node<K, V>[] table; 
    private int size; 
    private int threshold; 
    private final float loadFactor; 

    @SuppressWarnings("unchecked")
    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 1) initialCapacity = 1;
        this.loadFactor = loadFactor <= 0 ? DEFAULT_LOAD_FACTOR : loadFactor;
        int cap = 1;    
        while (cap < initialCapacity) cap <<= 1;  
        this.table = (Node<K, V>[]) new Node[cap];
        this.threshold = (int) (cap * this.loadFactor);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    private static final class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;
        
        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        return h ^ (h >>> 16);
    }

    private int index(int hash, int length) {
        return hash & (length - 1);
    }

    public int size() {
        return size;
    }

    public V get(Object key) {
        int h = hash(key);
        Node<K, V> e = table[index(h, table.length)];
        while (e != null) {
            if (e.hash == h && Objects.equals(e.key, key)) {
                return e.value;
            }
            e = e.next;
        }
        return null;
    }


    public V put(K key, V value) {
        int h = hash(key);
        int i = index(h, table.length);
        for (Node<K, V> e = table[i]; e != null; e = e.next) {
            if (e.hash == h && Objects.equals(e.key, key)) {
                V old = e.value;
                e.value = value;
                return old;
            }
        }

        table[i] = new Node<>(h, key, value, table[i]);
        size++;
        if (size > threshold) resize();
        return null;
    }


    public V remove(Object key) {
        int h = hash(key);
        int i = index(h, table.length);
        Node<K, V> prev = null;
        Node<K, V> e = table[i];
        while (e != null) {
            if (e.hash == h && Objects.equals(e.key, key)) {
                if (prev == null) {
                    table[i] = e.next;
                } else {
                    prev.next = e.next;
                }
                size--;
                return e.value;
            }
            prev = e;
            e = e.next;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCap = table.length << 1; 
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];

        for (Node<K, V> head : table) {
            Node<K, V> e = head;
            while (e != null) {
                Node<K, V> next = e.next;
                int newIndex = index(e.hash, newCap);
                e.next = newTab[newIndex];
                newTab[newIndex] = e;
                e = next;
            }
        }
        table = newTab;
        threshold = (int) (newCap * loadFactor);
    }

    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        System.out.println(map.get("b"));   // 2
        map.put(null, 100);   

        map.put("b", 22);           

        System.out.println(map.get("a"));   // 1
        System.out.println(map.get("b"));   // 22
        System.out.println(map.get(null));  // 100
        System.out.println(map.size());     // 3

        System.out.println(map.remove("a")); // 1
        System.out.println(map.get("a"));    // null
        System.out.println(map.size());      // 2
    }
}
