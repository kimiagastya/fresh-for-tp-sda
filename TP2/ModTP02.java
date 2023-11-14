import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.HashMap;

public class ModTP02 {

    private static InputReader in;
    static PrintWriter out;

    // Menyimpan daftar Kelas di dalam Circular Doubly Linkedlist
    static CircularDoublyLL<Kelas> sekolah = new CircularDoublyLL<Kelas>(); 
    static HashMap<Integer, Siswa> mapSiswa = new HashMap<>();

    // Method main digunakan untuk mengambil input dan memprosesnya ke dalam struktur data
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // INISIALISASI INPUT
        int banyakKelas = in.nextInt();
        for (int i = 0; i < banyakKelas; i++) {
            int jumlahSiswa = in.nextInt();
            Kelas kelas = new Kelas(new AVLTree(), jumlahSiswa, 0);
            sekolah.addLast(kelas);
        }

        sekolah.initPointer = sekolah.header.next;

        while (!(sekolah.initPointer.equals(sekolah.footer))) {
            AVLTree treeKelas = sekolah.initPointer.scoreTree;
            for (int j = 0; j < sekolah.initPointer.jumlahSiswa; j++) {
                int nilai = in.nextInt();
                Siswa siswa = new Siswa(nilai, sekolah.initPointer.id);
                mapSiswa.put(siswa.id, siswa);
                treeKelas.root = treeKelas.insert(treeKelas.root, nilai);
            }
            sekolah.initPointer = sekolah.initPointer.next;
        }

        // INISISASI PAKCIL KE DALAM KELAS PALING KANAN
        sekolah.setpakcil(sekolah.header.next);        

        // MENGAMBIL QUERY
        int Q = in.nextInt();
        for(int k = 0; k < Q; k++) {
            String query = in.next();
            if (query.equals("T")) {
                T();
            } else if (query.equals("C")) {
                C();
            } else if (query.equals("G")) {
                G();
            } else if (query.equals("S")) {
                S();
            } else if (query.equals("K")) {
                K();
            } else if (query.equals("A")) {
                A();
            }
        }

        out.close();
    }


    static void T() { // Menambah poin siswa
        int poin = in.nextInt();
        int idSiswa = in.nextInt();
        Siswa takenSiswa = mapSiswa.get(idSiswa);

        if (takenSiswa == null || takenSiswa.currKelas != sekolah.pakcil.id) {
            out.println("-1");
        } else {
            long tempNilai = takenSiswa.nilai;
            AVLTree kelasTree = sekolah.pakcil.scoreTree;
            long bonus = kelasTree.countBefore(kelasTree.root, takenSiswa.nilai);
            takenSiswa.nilai += (poin + bonus);

            kelasTree.root = kelasTree.delete(kelasTree.root, tempNilai);
            kelasTree.root = kelasTree.insert(kelasTree.root, takenSiswa.nilai);
            out.println(takenSiswa.nilai);
        }
    }
    
    static void C() { // Memberikan penalty
        int idSiswa = in.nextInt();
        Siswa takenSiswa = mapSiswa.get(idSiswa);

        if (takenSiswa == null || takenSiswa.currKelas != sekolah.pakcil.id) {
            out.println("-1");
        } else {
            AVLTree kelasTree = sekolah.pakcil.scoreTree;
            takenSiswa.strikes++;
            long tempNilai = takenSiswa.nilai;
            takenSiswa.nilai = 0;
            if (takenSiswa.strikes == 1) {
                kelasTree.root = kelasTree.delete(kelasTree.root, tempNilai);
                kelasTree.root = kelasTree.insert(kelasTree.root, 0);
            } else if (takenSiswa.strikes == 2) {
                takenSiswa.currKelas = sekolah.footer.prev.id;
                kelasTree.delete(kelasTree.root, tempNilai);
                sekolah.pakcil.jumlahSiswa--;

                AVLTree treeKelasTerburuk = sekolah.footer.prev.scoreTree;
                treeKelasTerburuk.root = treeKelasTerburuk.insert(treeKelasTerburuk.root, 0);

                // HANDLE KALAU KELASNYA KURANG DARI 5 ORANG //
            }
        }
    }

    static void G() {
        String gerak = in.next();
        if (gerak.equals("L")) {
            out.println(sekolah.gerakKiri().id);
        } else {
            out.println(sekolah.gerakKanan().id);
        }
    }

    static void S() {
        String gerak = in.next();
        if (gerak.equals("L")) {
            out.println(sekolah.gerakKiri().id);
        } else {
            out.println(sekolah.gerakKanan().id);
        }
    }

    static void K() {
        String gerak = in.next();
        if (gerak.equals("L")) {
            out.println(sekolah.gerakKiri().id);
        } else {
            out.println(sekolah.gerakKanan().id);
        }
    }

    static void A() {
        int jumlahSiswaBaru = in.nextInt();
        Kelas kelas = new Kelas(new AVLTree(), jumlahSiswaBaru, 0);
        sekolah.addLast(kelas);
        sekolah.initPointer = sekolah.footer.prev;

        AVLTree treeKelas = sekolah.initPointer.scoreTree;
        for (int l = 0; l < sekolah.initPointer.jumlahSiswa; l++) {
            int nilai = 0;
            Siswa siswa = new Siswa(nilai, sekolah.initPointer.id);
            mapSiswa.put(siswa.id, siswa);
            treeKelas.root = treeKelas.insert(treeKelas.root, nilai);
        }
        
        out.println(sekolah.initPointer.id);
    }

    // Method MAIN digunakan untuk mendapatkan score yang lebih besar dari insertedKey
    static void MAIN() { 
        // Mengambil insertedKey
        int insertedKey = in.nextInt();
        // Masukkan score ke dalam AVL Tree
        AVLTree budiTree = sekolah.pakcil.scoreTree;
        budiTree.root = budiTree.insert(budiTree.root, insertedKey);
        // Ambil banyak score pada root AVL Tree & banyak score sebelum insertedKey
        long sumOfCount = budiTree.root.count;
        long sumOfBefore = budiTree.countBefore(budiTree.root, insertedKey);

        // Dapatkan score lebih besar dari insertedKey
        out.println(sumOfCount - sumOfBefore + 1);
        // Update budiTree jumlahSiswa juga
        sekolah.pakcil.jumlahSiswa += 1;
        // Insert sumScore in Kelas
        sekolah.pakcil.sumScore += insertedKey;
    }

    // Method GERAK digunakan untuk menggerakkan budi ke Kelas sebelah kiri atau kanan
    static void GERAK() {
        String arah = in.next();
        if (arah.equals("KIRI")) {
            out.println(sekolah.gerakKiri().id);
        } else {
            out.println(sekolah.gerakKanan().id);
        }
    }

    // Method HAPUS digunakan untuk menghapus score tertentu dari AVL Tree sekaligus mengembalikan jumlahnya
    static void HAPUS() {
        int X = in.nextInt();

        // Jika jumlahSiswa Kelas kurang dari atau sama dengan X maka dicek dulu
        if(sekolah.pakcil.jumlahSiswa <= X) {
            // Jika jumlahSiswa Kelas kurang dari 0 maka tidak ada score yang dihapus
            if (sekolah.pakcil.jumlahSiswa <= 0) {
                out.println("0");
                sekolah.pakcil.jumlahSiswa = 0; // Set ke 0
                sekolah.pakcil.sumScore = 0; // Set ke 0
                sekolah.pindahKelas(sekolah.pakcil); // Lakukan pindah Kelas  
            } else {
                // Jika jumlahSiswa Kelas lebih dari 0
                out.println(sekolah.pakcil.sumScore); // print sumScore dari suatu Kelas
                sekolah.pakcil.scoreTree = new AVLTree();; // Set reset to new AVLTree
                sekolah.pakcil.jumlahSiswa = 0; // Set ke 0
                sekolah.pakcil.sumScore = 0; // Set ke 0
                // Budi pindah dulu baru Kelas dipindah
                sekolah.pindahKelas(sekolah.pakcil);                
            }
        } else {
            // Update pakcil jumlahSiswa
            sekolah.pakcil.jumlahSiswa -= X;

            // Hitung jumlah node key yang akan dihapus
            long sum = 0;
            while(X > 0) {
                // Mencari nilai max dari AVL Tree
                Node maxi = sekolah.pakcil.scoreTree.findMax();
                // Menjumlahkan nilai keynya
                sum += maxi.key;
                // Menghapus node dari AVL Tree
                sekolah.pakcil.scoreTree.root = sekolah.pakcil.scoreTree.delete(sekolah.pakcil.scoreTree.root, maxi.key); // delete node
                X--;
            }
            // Update sumScore
            sekolah.pakcil.sumScore -= sum;
            // Print sum
            out.println(sum);
        }      
    }

    // Method LIHAT digunakan untuk melihat score tertentu dari AVL Tree dari L ke H
    static void LIHAT() {
        int lowkey = in.nextInt();
        int highkey = in.nextInt();
        AVLTree budiTree = sekolah.pakcil.scoreTree; // Ambil AVL Tree Kelas saat ini

        // Gunakan prefix-sum untuk mencari banyak score yang ada diantara lowkey dan highkey
        long sumOfBeforeL = budiTree.countBefore(budiTree.root, lowkey-1); // hitung banyak score sebelum lowkey-1
        long sumOfBeforeH = budiTree.countBefore(budiTree.root, highkey); // hitung banyak score sebelum highkey
        // prefix-sum
        out.println(sumOfBeforeH - sumOfBeforeL);
    }

    // Method EVALUASI digunakan untuk sorting semua Kelas saat ini agar terurut sesuai dengan jumlahSiswa
    static void EVALUASI() {
        Kelas[] arr = sekolah.sort(); // Ambil sorted data
        // Insert new data sorted to sekolah after reset the list
        sekolah.clear();
        for(int i = 0; i < arr.length; i++) {
            sekolah.addLast(arr[i]);
        }
        // Print urutan Kelas budi saat ini
        out.println(sekolah.getBudiKelasSortedNow());
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

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
}
class Siswa {
    private int siswaIdCounter = 1;
    int id;
    long nilai;
    int currKelas;
    int strikes; // jumlah penalty/kecurangan yang dilakukan

    Siswa(long nilai, int currKelas) {
        this.id = siswaIdCounter;
        siswaIdCounter++;
        this.nilai = nilai;
        this.currKelas = currKelas;
    }
    void addStrike() {
        this.strikes++;
    }
}
// ================================== LINKEDLIST THINGS ==================================

// Class Kelas digunakan untuk instance Kelas
class Kelas {
    private int kelasIdCounter = 1;
    Kelas prev, next;
    AVLTree scoreTree; // AVL Tree penyimpan score
    int jumlahSiswa; // jumlahSiswa dari suatu Kelas
    int id;
    long sumScore; // Jumlah score dari AVL Tree
    double avgScore;
    
    Kelas(AVLTree scoreTree, int jumlahSiswa, long sumScore) {
        this.id = kelasIdCounter;
        this.scoreTree = scoreTree;
        this.jumlahSiswa = jumlahSiswa;
        this.sumScore = sumScore;
        kelasIdCounter++;
    }
}

// Class sekolah digunakan untuk menyimpan semua Kelas
class CircularDoublyLL<E> {
    int size; // Jumlah Kelas
    Kelas header, footer; // null node for easier add and remove
    Kelas initPointer;
    Kelas pakcil; // untuk menyimpan lokasi pakcil
    
    // construct empty list
    CircularDoublyLL() {
        this.size = 0;
        this.header = new Kelas(null, 0, 0);
        this.footer = new Kelas(null, 0, 0);
    }

    // Method digunakan untuk menambahkan node baru di akhir linkedlist
    void addLast(Kelas kelas) {
        if (this.size == 0) { // empty
            footer.prev = kelas;
            kelas.next = footer;
            header.next = kelas;
            kelas.prev = header;

        } else { // is exist
            footer.prev.next = kelas;
            kelas.prev = footer.prev;
            kelas.next = footer;
            footer.prev = kelas;
        }

        this.size += 1;
    }

    // Method digunakan untuk remove node Kelas dari linkedlist
    Kelas remove(Kelas kelas) {
        if (this.size == 0) { // empty
            // do nothing
            throw new NullPointerException("LinkedList Size is 0");
        } else if (this.size == 1) { // tidak ada elemen kedua
            header.next = footer;
            footer.prev = header;
        } else { // saat ada lebih dari 1 node
            kelas.prev.next = kelas.next;
            kelas.next.prev = kelas.prev;
        }

        this.size -= 1;
        return kelas;
    }

    // Method digunakan untuk set Kelas depan budi saat ini
    void setpakcil(Kelas kelas) {
        pakcil = kelas;
    }

    // Method digunakan untuk mengecek letak urutan Kelas depan budi saat ini
    int getBudiKelasSortedNow() {
        Kelas check = header.next;
        int counter = 0;
        while (!check.equals(pakcil)) {
            counter++;
            check = check.next;
        }
        // return counter;
        return counter + 1;
    }

    // Menggerakan budi ke kanan
    Kelas gerakKanan() {
        if (this.size == 0) { // empty
            // do nothing
            throw new NullPointerException("LinkedList Size is 0");
        } else if (this.size == 1) { // cuma satu elemen
            // do nothing
        } else if (pakcil.next.equals(footer)) { // elemen terakhir
            pakcil = header.next;
        } else { // kasus normal
            pakcil = pakcil.next;
        }
        return pakcil;
    }

    // Menggerakan budi ke kiri
    Kelas gerakKiri() {
        if (this.size == 0) { // empty
            // do nothing
            throw new NullPointerException("LinkedList Size is 0");
        } else if (this.size == 1) { // cuma satu elemen
            // do nothing
        } else if (pakcil.prev.equals(header)) { // elemen pertama
            pakcil = footer.prev;
        } else { // kasus normal
            pakcil = pakcil.prev;
        }
        return pakcil;
    }

    // Pindah Kelas sekaligus mereturn Kelas untuk ditempati budi
    void pindahKelas(Kelas kelas) {
        if (this.size == 0) {
            // do nothing
        } else if (this.size == 1) { // cuma satu Kelas permainan
            // do nothing
        } else if (kelas.next.equals(footer)) { // Kelas berada paling kanan
            // Kelas stay
            // budi pindah ke depan
            pakcil = header.next;
        } else { // sisanya
            // budi pindah ke kanannya
            pakcil = kelas.next;
            // pindah Kelas ke pojok kanan
            Kelas kelasDipindah = remove(kelas);
            this.addLast(kelasDipindah);
        }
    }

    // Clear all Kelas
    void clear() {
        header.next = footer;
        footer.prev = header;
        this.size = 0;
    }

    // Merges two subarrays of arr[].
    // First subarray is arr[l..m]
    // Second subarray is arr[m+1..r]
    void merge(Kelas arr[], int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;
 
        /* Create temp arrays */
        Kelas L[] = new Kelas[n1];
        Kelas R[] = new Kelas[n2];
 
        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];
 
        /* Merge the temp arrays */
 
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
 
        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2) {
            
            if (L[i].jumlahSiswa > R[j].jumlahSiswa) {
                arr[k] = L[i];
                i++;
            } else if (L[i].jumlahSiswa == R[j].jumlahSiswa) {
                // dicek lagi identitynya (yg rendah di depan)
                if (L[i].id < R[j].id) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
            }

            else {
                arr[k] = R[j];
                j++;
            }

            k++;
        }
 
        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
 
        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    void mergesort(Kelas arr[], int l, int r) {
        // sort array pakai merge sort
        if (l < r) {
            // Find the middle point
            int m = l + (r - l) / 2;
 
            // Sort first and second halves
            mergesort(arr, l, m);
            mergesort(arr, m + 1, r);
 
            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    // Method yang digunakan untuk sort Kelas
    Kelas[] sort() {
        // kalo ga 0 sizenya
        // buat array kosong sesuai size of LinkedList
        Kelas[] arr = new Kelas[this.size];
        Kelas masuk = header.next;
        // Masukin semua Kelas ke array
        for(int i = 0; i < this.size; i++) {
            arr[i] = masuk;
            masuk = masuk.next;
        }
        // Sort array pakai merge sort
        mergesort(arr, 0, this.size - 1);
        return arr;
    }
}

// ====================================== AVL THINGS ====================================

// AVL Node digunakan untuk menyimpan Score
class Node { // AVL Node
    long key, height, count; // key => score, count => banyaknya node pada suatu subtree dengan root == node
    Node left, right;
    long jumlahSama; // jumlah isi key yg sama (duplicate)

    Node(long key) {
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
    Node insert(Node node, long key) {
        if (node == null) {
            return (new Node(key));
        }

        if (key < node.key) {
            node.left = insert(node.left, key);
        } else if (key > node.key) {
            node.right = insert(node.right, key);
        } else {
            // no duplication
            node.jumlahSama += 1;
            node.count += 1;
            return node;
        }

        // Update height & count
        node.height = 1 + max(getHeight(node.left), getHeight(node.right));
        node.count = node.jumlahSama + getCount(node.left) + getCount(node.right);

        // Get balance factor
        long balance = getBalance(node);

        // If this node becomes unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && key < node.left.key) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && key > node.right.key) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // Delete a node
    Node delete(Node root, long key) 
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

// References:

// Data Structure:
// 1) https://www.w3schools.com/java/java_linkedlist.asp
// 2) https://www.geeksforgeeks.org/tree-traversals-inorder-preorder-and-postorder/

// HAPUS (JANGAN LUPA DIBALANCING SETELAH DIHAPUS), 
// *OPSI 0* USE node.sum FOR DELETING SEKALIGUS (MUST LEARN REBALANCING SUBTREE TO TREE) (BETTER)
// *OPSI 1* FIND GREATEST K-TH NODE THEN DELETE NODE WITH FUNCTION DELETENODE (NO REBALANCING)
// *OPSI 2* FIND MAX -> DELETE -> FIND MAX -> DELETE ... N TIMES (LIKED)
// *OPSI 3* FIND MAX -> PREDECESSOR -> DELETE -> PREDECESSOR ... N TIMES (OVERSEARCH)
// 1) https://www.geeksforgeeks.org/kth-largest-element-bst-using-constant-extra-space/
// 2) https://www.geeksforgeeks.org/deletion-in-an-avl-tree/
// 3) https://favtutor.com/blogs/avl-tree-python#:~:text=Insertion%20and%20Deletion%20time%20complexity,tree%20and%20red%2Dblack%20tree.

// EVALUASI
// 1) https://www.geeksforgeeks.org/quick-sort/
// 2) https://visualgo.net/en/sorting
// 3) https://www.geeksforgeeks.org/quicksort-on-singly-linked-list/
// 4) https://www.geeksforgeeks.org/insertion-sort/