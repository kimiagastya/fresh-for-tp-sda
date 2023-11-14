import java.io.*;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Note:
 * 1. Mahasiswa tidak diperkenankan menggunakan data struktur dari library seperti ArrayList, LinkedList, dll.
 * 2. Mahasiswa diperkenankan membuat/mengubah/menambahkan class, class attribute, instance attribute, tipe data, dan method 
 *    yang sekiranya perlu untuk menyelesaikan permasalahan.
 * 3. Mahasiswa dapat menggunakan method {@code traverse()} dari class {@code DoublyLinkedList}
 *    untuk membantu melakukan print statement debugging.
 */
public class Lab5BIL {

  private static InputReader in;
  private static PrintWriter out;
  private static DoublyLinkedList rooms = new DoublyLinkedList();

  public static void main(String[] args) {
    InputStream inputStream = System.in;
    in = new InputReader(inputStream);
    OutputStream outputStream = System.out;
    out = new PrintWriter(outputStream);

    int N = in.nextInt();

    for (int i = 0; i < N; i++) {
      char command = in.nextChar();
      char direction;

      switch (command) {
        case 'A':
          direction = in.nextChar();
          char type = in.nextChar();
          add(type, direction);
          break;
        case 'D':
          direction = in.nextChar();
          out.println(delete(direction));
          break;
        case 'M':
          direction = in.nextChar();
          out.println(move(direction));
          break;
        case 'J':
          direction = in.nextChar();
          out.println(jump(direction));
          break;
      }
    }
    out.close();
  }

  public static void add(char type, char direction) {
    rooms.add(type, direction);
  }

  public static int delete(char direction) {
    // TODO: implement
    ListNode dibuang = rooms.delete(direction);
    return dibuang.getId();
  }

  public static int move(char direction) {
    // TODO: implement
    ListNode temp = rooms.move(direction);
    return temp.getId();
  }

  public static int jump(char direction) {
    if (rooms.current.getChar() != 'S')
    {
        return -1;
    }
    while (true)
    {
        ListNode temp = rooms.move(direction);
        if (temp.getChar() == 'S')
        {
            return temp.getId();
        }
    }
    
  }

  // taken from https://codeforces.com/submissions/Petr
  // together with PrintWriter, these input-output (IO) is much faster than the
  // usual Scanner(System.in) and System.out
  // please use these classes to avoid your fast algorithm gets Time Limit
  // Exceeded caused by slow input-output (IO)
  private static class InputReader {

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

    public long nextLong() {
      return Long.parseLong(next());
    }
  }
}

class DoublyLinkedList {

  private int nodeIdCounter = 1;
  ListNode first;
  ListNode current;
  ListNode last;
  int size = 0;

  /*
   * Method untuk menambahkan ListNode ke sisi kiri (prev) atau kanan (next) dari {@code current} ListNode
   */
  public ListNode add(char element, char direction){
    if (current == null)
    {
        ListNode newNode = new ListNode(element, nodeIdCounter);
        first = newNode;
        current = newNode;
        last = newNode;
        nodeIdCounter++;
        return null;
    }

    if (direction == 'L')
    {
        ListNode newNode = new ListNode(element, nodeIdCounter);
        if (current.prev == null)
        {
            first = newNode;
            current.prev = newNode;
            newNode.next = current;
            nodeIdCounter++;
            return null;
        }

        else
        {
            newNode.prev = current.prev;
            newNode.next = current;
            current.prev.next = newNode;
            current.prev = newNode;
            nodeIdCounter++;
        }
    }

    else if (direction == 'R')
    {
        if(current.next == null)
        {
            ListNode newNode = new ListNode(element, nodeIdCounter);
            last = newNode;
            newNode.prev = current;
            current.next = newNode;
            nodeIdCounter++;
            return null;
        }

        ListNode newNode = new ListNode(element, nodeIdCounter);
        newNode.prev = current;
        newNode.next = current.next;
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

  char element;
  ListNode next;
  ListNode prev;
  int id;

  ListNode(char element, int id) {
    this.element = element;
    this.id = id;
  }

  public String toString() {
    return String.format("(ID:%d Elem:%s)", id, element);
  }

  public int getId()
  {
    return id;
  }
  public char getChar()
  {
    return element;
  }
}