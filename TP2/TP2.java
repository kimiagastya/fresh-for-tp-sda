import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.HashMap;

public class TP2 {
    private static InputReader in;
    private static PrintWriter out;
    static DoublyLinkedList sekolah;
    static HashMap<Integer, Siswa> mapSiswa;
    static ArrayList<Siswa> arrSiswa;
    static ListNode pakcil;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int jumlahKelas = in.nextInt();
        for (int i = 0; i < jumlahKelas; i++) {
            sekolah.add(new AVLTree(), in.nextInt());
        }

        sekolah.current = sekolah.first;
        while (sekolah.current != null) {
            for (int j = 0; j < sekolah.current.initCapacity; j++) {
                Siswa siswa = new Siswa();
                arrSiswa.add(siswa);
                sekolah.current.getAVLTree().insert(new Node(in.nextInt(), j+1));
            }
            sekolah.current = sekolah.current.next;
        }


        out.close();
    }
    static void T() {
        int poin = in.nextInt();
        int id = in.nextInt();
        Siswa siswaTugas = mapSiswa.get(id);
        
    }
    static void C() {
        int id = in.nextInt();
    }
    static void G() {
        char move = in.nextChar();
    }
    static void S() {

    }

    // taken from https://codeforces.com/submissions/Petr
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public char nextChar() {
            return next().charAt(0);
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}

class Siswa implements Comparable<Siswa> {
    private int siswaIdCounter = 1;
    int id;
    int nilai;
    int strikes; // jumlah penalty/kecurangan yang dilakukan

    Siswa() {
        this.id = siswaIdCounter;
        siswaIdCounter++;
    }
    void addStrike() {
        this.strikes++;
    }
    @Override
    public int compareTo(Siswa that) {
        if (this.nilai - that.nilai != 0) {
            return this.nilai < that.nilai ? -1 : 1;
        } else {
            return this.id < that.id ? -1 : 1;
        }
    }
}

class DoublyLinkedList {

  private int nodeIdCounter = 1;
  ListNode first;
  ListNode current;
  ListNode last;

  public ListNode add(AVLTree element, int initCapacity){

    if (current == null) {
        ListNode newNode = new ListNode(element, nodeIdCounter, initCapacity);
        first = newNode;
        current = newNode;
        last = newNode;
        nodeIdCounter++;
        return null;
    }

    else {
        ListNode newNode = new ListNode(element, nodeIdCounter, initCapacity);
        newNode.prev = last;
        newNode.next = null;
        newNode.prev.next = newNode;
        newNode.next.prev = newNode;
        nodeIdCounter++;
    }
    return null;
  }

  /**
   * Method untuk menghapus ListNode di sisi kiri (prev) atau kanan (next) dari {@code current} ListNode
   */
  public ListNode delete(char direction) {
    if (direction == 'L')
    {
        if (current.prev == null)
        {
            ListNode terakhir = last;
            last = last.prev;
            last.next = null;
            return terakhir;
        }

        if (current.prev.prev == null)
        {  
            ListNode temp = first;
            first = current;
            first.prev = null;
            return temp;
        }
        ListNode temp = current.prev;
        current.prev = current.prev.prev;
        current.prev.next = current;
        return temp;
    }

    else
    {
        if (current.next == null)
        {
            ListNode temp = first;
            first = first.next;
            first.prev = null;
            return temp;
            

        }

        if (current.next.next == null)
        {
            ListNode temp = last;
            last = current;
            last.next = null;
            return temp;
        }
        ListNode temp = current.next;
        current.next = current.next.next;
        current.next.prev = current;
        return temp;
    }
  }

  /*
   * Method untuk berpindah ke kiri (prev) atau kanan (next) dari {@code current} ListNode
   */
  public ListNode move(char direction) {
    if (direction == 'L')
    {
        if(current.prev == null)
        {
            current = last;
            return current;
        }
        current = current.prev;
    }

    else
    {
        if (current.next == null)
        {
            current = first;
            return current;
        }
        current = current.next;
    }
    return current;
  }

  /**
   * Method untuk mengunjungi setiap ListNode pada DoublyLinkedList
   */
  public String traverse() {
    ListNode traverseNode = first;
    StringBuilder result = new StringBuilder();
    do {
      result.append(traverseNode + ((traverseNode.next != first) ? " | " : ""));
      traverseNode = traverseNode.next;
    } while (traverseNode != first);

    return result.toString();
  }
}

class ListNode {

  AVLTree element;
  ListNode next;
  ListNode prev;
  int id;
  double avgScore;
  int initCapacity;

  ListNode(AVLTree element, int id, int initCapacity) {
    this.element = element;
    this.id = id;
    this.initCapacity = initCapacity;
  }
  public int getId() {
    return id;
  }
  public AVLTree getAVLTree() {
    return element;
  }
  public void setAvg() {
    this.avgScore = element.calculateAverage(element.root);
  }
  public void setId(int id) {
    this.id = id;
  }
    //   public String toString() {
    //     return String.format("(ID:%d Elem:%s)", id, element);
    //   }
}


// AVL Node digunakan untuk menyimpan Score
class Node { // AVL Node
    Siswa key;
    long height, count; // key => score, count => banyaknya node pada suatu subtree dengan root == node
    Node left, right;
    long jumlahSama; // jumlah isi key yg sama (duplicate)

    Node(Siswa key) {
        this.key = key;
        this.height = 1;
        this.count = 1;
        this.jumlahSama = 1;
    }
}

class AVLTree {

    Node root;

    // Implement right rotate
    Node rightRotate(Node y) {
        Node x = y.left; 
        Node T2 = x.right; 
  
        // Perform rotation 
        x.right = y; 
        y.left = T2; 
  
        // Update heights & count
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1; 
        y.count = y.jumlahSama + getCount(y.left) + getCount(y.right);

        x.height = max(getHeight(x.left), getHeight(x.right)) + 1; 
        x.count = x.jumlahSama + getCount(x.left) + getCount(x.right);

        // Return new root 
        return x; 
    }

    // Implement left rotate
    Node leftRotate(Node y) {
        Node x = y.right; 
        Node T2 = x.left; 
  
        // Perform rotation 
        x.left = y; 
        y.right = T2;   
  
        // Update heights & count
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1; 
        y.count = y.jumlahSama + getCount(y.left) + getCount(y.right);

        x.height = max(getHeight(x.left), getHeight(x.right)) + 1; 
        x.count = x.jumlahSama + getCount(x.left) + getCount(x.right);
  
        // Return new root 
        return x; 
    }

    // Implement insert node to AVL Tree
    Node insert(Node node, Siswa key) {
        if (node == null) {
            return (new Node(key));
        }

        // if (key < node.key) {
        //     node.left = insert(node.left, key);
        // } else if (key > node.key) {
        //     node.right = insert(node.right, key);
        // } else {
        //     // no duplication
        //     node.jumlahSama += 1;
        //     node.count += 1;
        //     return node;
        // }
        if (key.compareTo(node.key) < 0) {
            node.left = insert(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            node.right = insert(node.right, key);
        } else {
            // Handle duplicate keys if needed
            return node;
        }

        // Update height & count
        node.height = 1 + max(getHeight(node.left), getHeight(node.right));
        node.count = node.jumlahSama + getCount(node.left) + getCount(node.right);

        // Get balance factor
        long balance = getBalance(node);

        // If this node becomes unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && key.compareTo(node.left.key) < 0) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && key.compareTo(node.right.key) > 0) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // Delete a node
    Node delete(Node root, Siswa key) 
    { 
        // STEP 1: PERFORM STANDARD BST DELETE 
        if (root == null) 
            return root; 
  
        // If the key to be deleted is smaller than 
        // the root's key, then it lies in left subtree 
        if (key < root.key) 
            root.left = delete(root.left, key); 
  
        // If the key to be deleted is greater than the 
        // root's key, then it lies in right subtree 
        else if (key > root.key) 
            root.right = delete(root.right, key); 
  
        // if key is same as root's key, then this is the node 
        // to be deleted 
        else
        { 
            // if jumlah sama masih ada rootnya jangan diilangin dulu gan, duplicatenya urusin
            if (root.jumlahSama > 1) {
                root.jumlahSama -= 1;
                root.count -= 1;
            } else {
                // node with only one child or no child 
                if ((root.left == null) || (root.right == null)) { 
                    root = (root.left == null) ? root.right : root.left;
                } else {
                    // node with two children: Get the inorder 
                    // successor (smallest in the right subtree) 
                    Node temp = lowerBound(root.right); 
    
                    // Copy the inorder successor's data to this node 
                    root.key = temp.key; 
                    // fixing yg keupdate ga cuma key doang, ada count juga
                    root.jumlahSama = temp.jumlahSama;
                    root.count = temp.count;
                    // Delete the inorder successor 
                    root.right = delete(root.right, temp.key); 
                } 
            }
        } 
  
        // If the tree had only one node then return 
        if (root == null) 
            return root; 
  
        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE 
        root.height = max(getHeight(root.left), getHeight(root.right)) + 1; 
        root.count = root.jumlahSama + getCount(root.left) + getCount(root.right);
  
        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether 
        // this node became unbalanced) 
        long balance = getBalance(root); 
  
        // If this node becomes unbalanced, then there are 4 cases 
        // Left Left Case 
        if (balance > 1 && getBalance(root.left) >= 0) 
            return rightRotate(root); 
  
        // Left Right Case 
        if (balance > 1 && getBalance(root.left) < 0) 
        { 
            root.left = leftRotate(root.left); 
            return rightRotate(root); 
        } 
  
        // Right Right Case 
        if (balance < -1 && getBalance(root.right) <= 0) 
            return leftRotate(root); 
  
        // Right Left Case 
        if (balance < -1 && getBalance(root.right) > 0) 
        { 
            root.right = rightRotate(root.right); 
            return leftRotate(root); 
        } 
  
        return root; 
    } 

    // Mencari lowerBound dari suatu node
    Node lowerBound(Node node) {
        // Return node with the lowest from this node
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // Mencari upperBound dari suatu node
    Node upperBound(Node node) {
        // Return node with the greatest from this node
        Node current = node;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }


    // Utility function to get height of node
    long getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Utility function to get num of peoples
    long getCount(Node node) {
        if (node == null) {
            return 0;
        }
        return node.count;
    }

    // Utility function to get balance factor of node
    long getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }
    
    // Utility function to get max of 2 longs
    long max(long a, long b) {
        return (a > b) ? a : b;
    }   

    // QUERY MAIN, LIHAT
    // Method digunakan untuk mencari jumlah score yang kurang dari inserted key
    long countBefore(Node node, long insertedKey) {
        if (node == null) { // Jika node kosong, return 0
            return 0;
        }
        // Jika sudah didapat insertedKey sama dengan key node, maka cari count di subtree kiri dulu
        if (node.key == insertedKey) { 
            return node.jumlahSama + getCount(node.left);
        }
        // Jika insertedKey lebih kecil dari key node, maka cari di subtree kiri
        if (node.key < insertedKey) {
            // Cek kiri lalu, ke kanan
            if (node.left != null) {
                // Jika ada node di subtree kiri, maka cari count di subtree kiri + duplicatenya
                return node.jumlahSama + node.left.count + countBefore(node.right, insertedKey);
            } else {
                return node.jumlahSama + countBefore(node.right, insertedKey);
            }
        }
        // Ke kiri untuk cari key yang cocok
        return countBefore(node.left, insertedKey);
    }

    // Method digunakan untuk mencari score max
    Node findMax() {
        Node temp = root;
        while (temp.right != null) {
            temp = temp.right;
        }
        return temp;
    }
}