import java.util.ArrayList;
import java.util.List;

class LRUCache {

  // Node class to store key, value, and last-used timestamp
  class Node {
    int key;
    int value;
    long timestamp;

    public Node(int key, int value, long timestamp) {
      this.key = key;
      this.value = value;
      this.timestamp = timestamp;
    }
  }

  private int capacity;        // max cache size
  private List<Node> cache;    // array-based storage
  private long clock = 0;      // logical clock to track recency

  public LRUCache(int capacity) {
    this.capacity = capacity;
    this.clock = 0;
    this.cache = new ArrayList<>();
  }

  // ------------------- PUT operation -------------------
  public void put(int key, int value) {
    clock++; // every operation increases "time"

    // Step 1: Check if key already exists
    for (int i = 0; i < cache.size(); i++) {
      Node node = cache.get(i);
      if (node.key == key) {
        // if key found → update value & timestamp
        node.value = value;
        node.timestamp = clock;
        return; // no need to insert new
      }
    }

    // Step 2: If key does not exist
    if (cache.size() < capacity) {
      // cache not full → simply add new node
      cache.add(new Node(key, value, clock));
    } else {
      // cache full → find the least recently used (min timestamp)
      int lruIndex = 0;
      for (int i = 1; i < cache.size(); i++) {
        if (cache.get(i).timestamp < cache.get(lruIndex).timestamp) {
          lruIndex = i;
        }
      }
      // replace LRU node with new node
      cache.set(lruIndex, new Node(key, value, clock));
    }
  }

  // ------------------- GET operation -------------------
  public int get(int key) {
    clock++; // increase time on access
    // search through cache linearly
    for (int i = 0; i < cache.size(); i++) {
      Node node = cache.get(i);
      if (node.key == key) {
        // if found → update timestamp (most recently used now)
        node.timestamp = clock;
        return node.value;
      }
    }
    // if not found → cache miss
    return -1;
  }

}


// ------------------- MAIN -------------------
class LRUCacheDemo1 {
    
  public static void main(String[] args) {
    LRUCache cache = new LRUCache(2);

    cache.put(1, 1);                 // insert (1,1)
    cache.put(2, 2);                 // insert (2,2)
    System.out.println(cache.get(1)); // returns 1 → (1) becomes most recent
    cache.put(3, 3);                 // evicts (2), inserts (3,3)
    System.out.println(cache.get(2)); // -1 (2 was evicted)
    cache.put(4, 4);                 // evicts (1), inserts (4,4)
    System.out.println(cache.get(1)); // -1 (1 was evicted)
    System.out.println(cache.get(3)); // 3
    System.out.println(cache.get(4)); // 4
  }
}
