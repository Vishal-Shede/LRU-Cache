# 📘 LRU Cache Notes

---

## 1. What is a Cache?
- A **cache** is a temporary storage where we store frequently accessed data for faster access.  
- Since cache size is limited, we need a way to decide which data to remove when new data comes in. This is handled by **cache eviction policies**.  

---

## 2. What is LRU (Least Recently Used)?
- **LRU** is one of the most popular cache eviction policies.  
- When the cache is full and new data arrives, it removes the **least recently used** element first.  

---

## 3. Real-World Examples
- **Browser Cache** → Stores recently visited pages for faster reload.  
- **Mobile Apps** → OS evicts least recently used apps from memory when RAM is full.  
- **Databases** → MySQL, Redis, and other systems use LRU for query/result caching.  

---

## 4. Operations in LRU Cache
Cache stores data in the form of **key–value pairs**. We define two operations on cache:

- **get(key)** → returns the value if present in cache, else `-1`.  
- **put(key, value)** → inserts the key–value pair into cache.  
  - If the key exists → update it.  
  - If capacity is full → evict the least recently used element.  

---

## 5. Approaches to Implement LRU Cache
When designing LRU Cache, we need to solve two main problems:
1. **Where to store the key–value data.**  
2. **How to track the least recently used element.**

Based on this, there are three approaches:

---

### 🔹 Naive Approach 1: Array of Nodes
- **Storage**: Keep an array of nodes, each storing `(key, value, timeStamp)`.  
- **Tracking LRU**: Use a `timeStamp` to track when each element was last accessed.  

**Operations**  
- `get(key)` → Scan array for key, find the node.  
  - Return value if found.  
  - Else `-1`.  
- `put(key, value)` →  
  - If cache is not full → insert new node at end with current `timeStamp`.  
  - If cache is full → find the node with the **oldest timeStamp** (least recently used), replace it with new key–value.  

**Analysis**  
- ✅ Easy to implement.  
- ❌ Every operation requires linear search.  
- **Time Complexity**: `get = O(n)`, `put = O(n)`  
- **Space Complexity**: `O(n)`  

---

### 🔹 Naive Approach 2: Singly Linked List
- **Storage**: Store cache entries in a **singly linked list**.  
- **Tracking LRU**:  
  - Head = most recently used (MRU).  
  - Tail = least recently used (LRU).  

**Operations**  
- `get(key)` → Traverse list for key, move node to head if found. **O(n)**  
- `put(key, value)` →  
  - If key exists → update value and move node to head.  
  - If new and space available → insert at head. **O(1)**  
  - If full → remove tail, then insert at head.  

**Analysis**  
- ✅ Insertions at head are O(1).  
- ❌ Lookup is O(n) since traversal is needed.  
- ❌ Tail removal also requires traversal.  
- **Time Complexity**: `get = O(n)`, `put = O(n)`  
- **Space Complexity**: `O(n)`  

---

### 🔹 Optimal Approach: HashMap + Doubly Linked List
- **Storage**:  
  - **HashMap** → stores `(key → pointer to node)` for O(1) access.  
  - **Doubly Linked List (DLL)** → maintains usage order.  
- **Tracking LRU**:  
  - Head = most recently used (MRU).  
  - Tail = least recently used (LRU).  

**Operations**  
- `get(key)` → If key exists in HashMap, fetch node from DLL, move it to head (MRU), return value.  
- `put(key, value)` →  
  - If key exists → update value, move to head.  
  - If not present →  
    - If space available → insert at head + map entry.  
    - If full → remove tail node (LRU) from DLL + map, then insert at head.  

**Analysis**  
- ✅ Both lookup and insert/remove are O(1).  
- ✅ This is the standard approach used in real-world systems and interviews.  
- **Time Complexity**: `get = O(1)`, `put = O(1)`  
- **Space Complexity**: `O(n)`  

---

## 6. Complexity Comparison

| Approach             | get() | put() | Space | Notes                              |
|----------------------|-------|-------|-------|------------------------------------|
| Array of Nodes       | O(n)  | O(n)  | O(n)  | Timestamp tracking.                |
| Singly Linked List   | O(n)  | O(n)  | O(n)  | Head = MRU, Tail = LRU.            |
| HashMap + DLL        | O(1)  | O(1)  | O(n)  | Head = MRU, Tail = LRU, HashMap.   |
