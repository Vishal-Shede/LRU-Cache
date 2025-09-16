// Naive LRU Cache implementation using Singly Linked List
// Time Complexity: get = O(n), put = O(n)
// Space Complexity: O(n)

class LRUCache {

    // ------------------- Node Class -------------------
    class Node {
        int key;
        int value;
        Node next;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
            next = null;
        }
    }

    // ------------------- Variables -------------------
    private int capacity;   // max cache size
    private int size;       // current cache size
    private Node head;      // MRU (most recently used)
    private Node tail;      // LRU (least recently used)

    // ------------------- Constructor -------------------
    public LRUCache(int capacity) {
        this.capacity = capacity;
        size = 0;
        head = null;
        tail = null;
    }

    // ------------------- GET operation -------------------
    public int get(int key) {
        Node curr = head;
        Node prev = null;

        // Traverse list to search for key
        while (curr != null && curr.key != key) {
            prev = curr;
            curr = curr.next;
        }

        if (curr == null) {
            return -1; // key not found
        }

        if (curr == head) {
            return head.value; // already MRU
        }

        // Otherwise: move node to head (make MRU)
        prev.next = curr.next;       // unlink from previous
        curr.next = head;            // attach at head
        if (curr == tail) tail = prev; // if node was tail, update tail
        head = curr;                 // set as new head

        return curr.value;
    }

    // ------------------- PUT operation -------------------
    public void put(int key, int value) {
        Node curr = head;
        Node prev = null;

        // Step 1: Check if key exists
        while (curr != null && curr.key != key) {
            prev = curr;
            curr = curr.next;
        }

        if (curr != null) {
            // Key exists → update value
            curr.value = value;

            if (curr != head) {
                // Move to head
                prev.next = curr.next;
                curr.next = head;
                if (curr == tail) tail = prev;
                head = curr;
            }
            return;
        }

        // Step 2: Key not present → create new node
        Node newNode = new Node(key, value);

        if (size < capacity) {
            // Space available → insert at head
            newNode.next = head;
            head = newNode;
            if (tail == null) tail = newNode; // first element
            size++;
        } else {
            // Cache full → remove tail (LRU)
            curr = head;
            while (curr.next != tail) {
                curr = curr.next;
            }
            curr.next = null; // unlink tail
            tail = curr;

            // Insert new node at head
            newNode.next = head;
            head = newNode;
        }
    }
}

// ------------------- DEMO -------------------
class LRUCacheDemo2 {
    public static void main(String[] args) {
        // Create cache of capacity 2
        LRUCache cache = new LRUCache(2);

        cache.put(1, 1); // Cache = {1}
        cache.put(2, 2); // Cache = {2,1}
        System.out.println(cache.get(1)); // returns 1, Cache = {1,2}

        cache.put(3, 3); // evicts key 2, Cache = {3,1}
        System.out.println(cache.get(2)); // returns -1 (not found)

        cache.put(4, 4); // evicts key 1, Cache = {4,3}
        System.out.println(cache.get(1)); // returns -1 (not found)
        System.out.println(cache.get(3)); // returns 3
        System.out.println(cache.get(4)); // returns 4
    }
}
