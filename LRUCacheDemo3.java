// Optimal LRU Cache implementation using HashMap + Doubly Linked List
// get() and put() both work in O(1) time
// Space Complexity: O(n)

import java.util.HashMap;

class LRUCacheDemo3 {

    // ------------------- Node Class -------------------
    class Node {
        int key, value;
        Node prev, next; // pointers for doubly linked list

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    // ------------------- Variables -------------------
    private HashMap<Integer, Node> map; // stores key -> Node mapping for O(1) lookup
    private int capacity, size;         // cache capacity and current size
    private Node head, tail;            // head = MRU, tail = LRU

    // ------------------- Constructor -------------------
    public LRUCacheDemo3(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        map = new HashMap<>();

        // Initially cache is empty
        head = null;
        tail = null;
    }

    // ------------------- GET operation -------------------
    public int get(int key) {
        if (!map.containsKey(key)) return -1; // if not found, return -1

        Node node = map.get(key);
        moveToHead(node); // accessed node becomes MRU
        return node.value;
    }

    // ------------------- PUT operation -------------------
    public void put(int key, int value) {
        if (map.containsKey(key)) {
            // Case 1: Key already exists
            Node node = map.get(key);
            node.value = value;   // update value
            moveToHead(node);     // move this node to MRU position
        } else {
            // Case 2: New key
            Node newNode = new Node(key, value);
            map.put(key, newNode);
            addNode(newNode); // add to MRU position
            size++;

            // If capacity exceeded, evict LRU
            if (size > capacity) {
                Node lru = popTail(); // remove tail node
                map.remove(lru.key);  // also remove from HashMap
                size--;
            }
        }
    }

    // ------------------- Helper: Add node next to head -------------------
    private void addNode(Node node) {
     if (head == null) {
        // first node in the cache
        head = node;
        tail = node;
        return;
     }

     // normal case: insert at front (MRU)
     node.next = head;
     head.prev = node;
     head = node;
    }

    // ------------------- Helper: Remove node from DLL -------------------
   private void removeNode(Node node) {
    if (node.prev != null) {
        node.prev.next = node.next;   // detach from left
    } else {
        head = node.next;             // if no prev, this was head
    }

    if (node.next != null) {
        node.next.prev = node.prev;   // detach from right
    } else {
        tail = node.prev;             // if no next, this was tail
    }
}


    // ------------------- Helper: Move existing node to MRU -------------------
    private void moveToHead(Node node) {
        removeNode(node);
        addNode(node);
    }

    // ------------------- Helper: Pop the LRU node -------------------
    private Node popTail() {
        Node res = tail.prev; // LRU node is before tail
        removeNode(res);
        return res;
    }


    // ------------------- MAIN (Testing) -------------------
    public static void main(String[] args) {
        LRUCacheDemo3 cache = new LRUCacheDemo3(2);

        cache.put(1, 1);               // Cache = {1}
        cache.put(2, 2);               // Cache = {2,1}
        System.out.println(cache.get(1)); // returns 1, Cache = {1,2}

        cache.put(3, 3);               // evicts key 2, Cache = {3,1}
        System.out.println(cache.get(2)); // returns -1 (not found)

        cache.put(4, 4);               // evicts key 1, Cache = {4,3}
        System.out.println(cache.get(1)); // returns -1 (not found)
        System.out.println(cache.get(3)); // returns 3
        System.out.println(cache.get(4)); // returns 4
    }
}
