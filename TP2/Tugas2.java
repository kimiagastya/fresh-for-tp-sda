import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.PrintWriter;

class Tugas2 {
    private static InputReader in;
    static PrintWriter out;
    static DoublyLinkedList funZone;
    static DoublyLinkedList.Mesin budi;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        funZone = new DoublyLinkedList();

        int N = in.nextInt(); //machines
        for (int i = 1; i <= N; i++) {   // iterate through each machine
            DoublyLinkedList.Mesin mesin = new DoublyLinkedList.Mesin(i);
            funZone.push(mesin);
            int banyakSkor = in.nextInt();
            mesin.jumlahSkor = banyakSkor;

            // insert scores to each machine
            for(int j = 0; j < banyakSkor; j++) { // iterate through each score
                int M = in.nextInt();
                mesin.skor.root = mesin.skor.insert(mesin.skor.root, M);
                mesin.totalSum += M;
            }            
        }

        // set budi to the most popular machine (linkedlist head)
        budi = funZone.head;

        int Q = in.nextInt(); //queries

        for (int i = 0; i < Q; i++) { // iterate through each query
            String query = in.next();
            if(query.equals("MAIN")) {
                int Y = in.nextInt();
                bermain(Y);
            } else if(query.equals("GERAK")) {
                String arah = in.next();
                gerak(arah);
            } else if(query.equals("HAPUS")) {
                int X = in.nextInt();
                hapus(X);
            } else if(query.equals("LIHAT")) {
                int L = in.nextInt();
                int H = in.nextInt();
                lihat(L, H, budi);
            } else { //eval
                evaluasi();
            }
        }
        out.close();
    }

    static void bermain(int skor) {
        // insert budi's score to machine
        budi.skor.root = budi.skor.insert(budi.skor.root, skor);

        // add sum & count 
        budi.totalSum += skor;
        budi.jumlahSkor++;

        int posisiSkor = budi.skor.countGreaterScore(budi.skor.root, skor) + 1;
        out.println(posisiSkor);
    }

    static void gerak(String arah) {
        if (arah.equals("KANAN")) {
            // budi is in front of tail
            if (budi.next == null) {
                budi = funZone.head;
            } 
            else {
                budi = budi.next;
            }
        } else { // KIRI
            // budi is in front of head
            if (budi.prev == null) {
                budi = funZone.tail;
            }
            else{
                budi = budi.prev;
            }
        }
        out.println(budi.nomorMesin);
    }
    
    static void hapus(int X) {
        if(budi.jumlahSkor <= X) {
            // print sum
            out.println(budi.totalSum);
            
            // delete all scores
            budi.skor = new AVLTree();
            budi.jumlahSkor = 0;
            budi.totalSum = 0;

            // if machine is tail
            if(budi.nomorMesin == funZone.tail.nomorMesin) {
                budi = funZone.head;
            } else if (budi.next == null && budi.prev == null) { // count machine == 1
                //do nothing
            } else { // move broken machine
                DoublyLinkedList.Mesin temp = budi;

                // machine is head
                if(budi.prev == null) { 
                    budi = temp.next;
                    funZone.tail.next = temp;
                    temp.prev = funZone.tail;
                    temp.next = null;
                    budi.prev = null;
                    funZone.tail = temp;
                    funZone.head = budi;
                } else {
                    budi = temp.next;
                    temp.prev.next = budi;
                    budi.prev = temp.prev;
                    funZone.tail.next = temp;
                    temp.prev = funZone.tail;
                    temp.next = null;
                    funZone.tail = temp;
                }


            }

        } else {
            long sum = 0;
            
            // delete X highest score
            while(X > 0) {
                Node maxNode = budi.skor.maxValueNode(budi.skor.root);

                if(maxNode != null) {
                    sum += maxNode.key;
                    budi.totalSum -= maxNode.key;
                    budi.jumlahSkor--;
                    X--;
                    
                    budi.skor.root = budi.skor.deleteNode(budi.skor.root, maxNode.key);  
                }
            }

            out.println(sum);            
        }
    }

    static void lihat(int start, int end, DoublyLinkedList.Mesin mesin) {
        int countScore = mesin.skor.countInRange(mesin.skor.root, start, end);
        out.println(countScore);
    }

    static void evaluasi() {
        // sort machines
        funZone.head = funZone.mergeSort(funZone.head);

        DoublyLinkedList.Mesin pointer = funZone.head;
        int urutanMesin = 0;

        // find index of budi (starts with 1 NOT 0)
        while(pointer != null) {
            urutanMesin++;

            if(pointer.nomorMesin == budi.nomorMesin) {
                out.println(urutanMesin);
                break;
            } 

            pointer = pointer.next;
        }

        // update tail
        while(pointer != null) {
            funZone.tail = pointer;
            pointer = pointer.next;
        }
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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

// Referensi: https://www.geeksforgeeks.org/doubly-linked-list/
class DoublyLinkedList {
    Mesin head;
    Mesin current;
    Mesin tail;

    static class Mesin {
        int nomorMesin;
        AVLTree skor = new AVLTree();
        int jumlahSkor;
        long totalSum;
        Mesin prev;
        Mesin next;
    
        Mesin(int nomorMesin) {
            this.nomorMesin = nomorMesin;
        }  
    }

    // Adding a node at the front of the list
    void push(Mesin mesin) {
        if(head == null) {
            head = mesin;
            current = mesin;
            tail = mesin;
        } else if (head != null) {
            current.next = mesin;
            mesin.prev = current;
            current = mesin;
            tail = current;
        }
    }

    // Referensi: https://www.geeksforgeeks.org/merge-sort-for-doubly-linked-list/
    // Split a doubly linked list (DLL) into 2 DLLs of
    // half sizes
    Mesin split(Mesin head) {
        Mesin fast = head, slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        Mesin temp = slow.next;
        slow.next = null;
        return temp;
    }
  
    Mesin mergeSort(Mesin node) {
        if (node == null || node.next == null) {
            return node;
        }
        Mesin second = split(node);
  
        // Recur for left and right halves
        node = mergeSort(node);
        second = mergeSort(second);
  
        // Merge the two sorted halves
        return merge(node, second);
    }
  
    // Function to merge two linked lists
    // return new head
    Mesin merge(Mesin first, Mesin second) {
        // If first linked list is empty
        if (first == null) {
            return second;
        }
  
        // If second linked list is empty
        if (second == null) {
            return first;
        }
  
        // Pick the higher jumlah skor
        if (first.jumlahSkor > second.jumlahSkor || 
            (first.jumlahSkor == second.jumlahSkor && first.nomorMesin < second.nomorMesin)) {
            first.next = merge(first.next, second);
            first.next.prev = first;
            first.prev = null;
            return first;
        } else {
            second.next = merge(first, second.next);
            second.next.prev = second;
            second.prev = null;
            return second;
        }
    }
}

// blueprint for score in AVLTree
class Node {
    int key; //score
    Node left;
    Node right;
    int count;
    int height;
    int greaterEqual; //count score that greater or equal to key

    Node(int key) {
        this.key = key;
        this.height = 1;
        this.count = 1;
        this.greaterEqual = 1; //equal to itself
    }
}

// Referensi: https://www.geeksforgeeks.org/avl-with-duplicate-keys/
class AVLTree {
    Node root;

    // A utility function to right rotate subtree rooted with y
    // See the diagram given above.
    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
 
        // Perform rotation
        x.right = y;
        y.left = T2;
 
        // Update heights
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
 
        y.greaterEqual = countGreaterEqual(y.left) + countGreaterEqual(y.right) + y.count;
        x.greaterEqual = countGreaterEqual(x.left) + countGreaterEqual(x.right) + x.count;

        // Return new root
        return x;
    }
 
    // A utility function to left rotate subtree rooted with x
    // See the diagram given above.
    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
 
        // Perform rotation
        y.left = x;
        x.right = T2;
 
        // Update heights
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;

        x.greaterEqual = countGreaterEqual(x.left) + countGreaterEqual(x.right) + x.count;
        y.greaterEqual = countGreaterEqual(y.left) + countGreaterEqual(y.right) + y.count;

        // Return new root
        return y;
    }

    Node insert(Node node, int key) {
        /*1.  Perform the normal BST rotation */
        if (node == null) {
            Node newNode = new Node(key);
            if(this.root == null)
                this.root = newNode;
            return newNode;
        }
            
        // If key already exists in BST, increment count and return
        else if (key == node.key) {
            (node.count)++;
            (node.greaterEqual)++; //increment greaterEqual
            
            return node;
        }
 
        /* Otherwise, recur down the tree */
        else if (key < node.key) 
            node.left = insert(node.left, key);
        else
            node.right = insert(node.right, key);
 
        /* 2. Update height of this ancestor node */
        node.greaterEqual = countGreaterEqual(node.left) + countGreaterEqual(node.right) + node.count;
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
 
        /* 3. Get the balance factor of this ancestor node to check whether
       this node became unbalanced */
        int balance = getBalance(node);
 
        // If this node becomes unbalanced, then there are 4 cases
 
        // Left Left Case (case 1)
        if (balance > 1 && key < node.left.key){
            node = rightRotate(node);
            return node;
        }
 
        // Right Right Case (case 4)
        if (balance < -1 && key > node.right.key) {
            node = leftRotate(node);
            return node;
        }
            
 
        // Left Right Case (case 2)
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            node = rightRotate(node);
            return node;
        }
 
        // Right Left Case (case 3)
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            node = leftRotate(node);
            return node;
        }
 
        /* return the (unchanged) node pointer */
        return node;
    }

    /* Given a non-empty binary search tree, return the node with minimum
    key value found in that tree. Note that the entire tree does not
    need to be searched. */
    Node minValueNode(Node node) {
       Node current = node;

       /* loop down to find the leftmost leaf */
       while (current.left != null)
           current = current.left;

       return current;
    }

    Node deleteNode(Node node, int key) {
        // STEP 1: PERFORM STANDARD BST DELETE
 
        if (node == null)
            return node;
 
        // If the key to be deleted is smaller than the root's key,
        // then it lies in left subtree
        if (key < node.key)
            node.left = deleteNode(node.left, key);
 
        // If the key to be deleted is greater than the root's key,
        // then it lies in right subtree
        else if (key > node.key)
            node.right = deleteNode(node.right, key);
 
        // if key is same as root's key, then This is the node
        // to be deleted
        else {
            // If key is present more than once, simply decrement
            // count and return
            if (node.count > 1) {
                (node.count)--;
                node.greaterEqual = countGreaterEqual(node.left) + countGreaterEqual(node.right) + node.count;
                return node;
            }
            // ElSE, delete the node
 
            // node with only one child or no child
            if ((node.left == null) || (node.right == null)) {
                Node temp = node.left != null ? node.left : node.right;
 
                // No child case
                if (temp == null) {
                    temp = node;
                    node = null;
                }
                else // One child case
                    node = temp; // Copy the contents of the non-empty child
            }
            else {
                // node with two children: Get the inorder successor (smallest
                // in the right subtree)
                Node temp = minValueNode(node.right);
 
                // Copy the inorder successor's data to this node and update the count
                node.key = temp.key;
                node.count = temp.count;
                temp.count = 1;
 
                // Delete the inorder successor
                node.right = deleteNode(node.right, temp.key);
            }
        }
 
        // If the tree had only one node then return
        if (node == null)
            return node;
 
        // STEP 2: UPDATE HEIGHT & greaterEqual OF THE CURRENT NODE
        node.greaterEqual = countGreaterEqual(node.left) + countGreaterEqual(node.right) + node.count;
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
 
        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
        // this node became unbalanced)
        int balance = getBalance(node);
 
        // If this node becomes unbalanced, then there are 4 cases
 
        // Left Left Case (case 1)
        if (balance > 1 && getBalance(node.left) >= 0){
            node = rightRotate(node);
            return node;
        }
 
        // Right Right Case (case 4)
        if (balance < -1 && getBalance(node.right) <= 0) {
            node = leftRotate(node);
            return node;
        }
            
 
        // Left Right Case (case 2)
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            node = rightRotate(node);
            return node;
        }
 
        // Right Left Case (case 3)
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            node = leftRotate(node);
            return node;
        }
 
        return node;
    }


    void preOrder(Node root)
    {
        if (root != null) {
            Tugas2.out.printf("%d(%d) ", root.key, root.count);
            preOrder(root.left);
            preOrder(root.right);
        }
    }

    // count scores that greater than score(key)
    int countGreaterScore(Node node, int score){
        if(node == null){
            return 0;
        }

        if(node.key == score){
            if(node.right == null) return 0;
            return node.right.greaterEqual;
        }else if(node.key > score){
            int sum = 0;
            if(node.right != null) sum = node.right.greaterEqual;
            return sum + node.count + countGreaterScore(node.left, score);
        } else {
            return countGreaterScore(node.right, score);
        }
    }

    // count scores that greater than or equal to score(key)
    int countGreaterEqualScore(Node node, int score){
        if(node == null){
            return 0;
        }

        if(node.key == score){
            int sum = node.count;
            if(node.right != null) {
                sum += node.right.greaterEqual;
            }
            return sum;
        }else if(node.key > score){
            int sum = 0;
            if(node.right != null) sum = node.right.greaterEqual;
            return sum + node.count + countGreaterEqualScore(node.left, score);
        } else {
            return countGreaterEqualScore(node.right, score);
        }
    }

    // count scores that greater than or equal to score(key)
    // used for updating greaterEqual
    int countGreaterEqual(Node node){
        int left = 0;
        int right = 0;

        if(node == null){
            return 0;
        }

        if(node.left != null) {
            left = node.left.greaterEqual;
        }

        if(node.right != null) {
            right = node.right.greaterEqual;
        }

        return right + left + node.count; 
    }

    // count scores in range [start, end]
    int countInRange(Node node, int start, int end) {
        int greaterEqualThanL = countGreaterEqualScore(node, start);
        int greaterThanH = countGreaterScore(node, end);
        return greaterEqualThanL - greaterThanH;
    }

    // Utility function to get height of node
    int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Utility function to get balance factor of node
    int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    // return node with max key 
    Node maxValueNode(Node node){
        if(node == null) 
            return null;

        Node pointerMax = node;
 
        /* loop down to find the rightmost leaf */
        while (pointerMax.right != null)
            pointerMax = pointerMax.right;
 
        return pointerMax;
    }
}