import java.util.LinkedList;

/**
 * Trie for storing bit vectors specifically designed for IP Routing.
 */
public class BitVectorTrie<Value> {

    private static final int R = 2;
    private Node root;


    public BitVectorTrie(){
        root = new Node();


    }

    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }


    /**
     * Is the key in the symbol table?
     */
    public boolean isRoutable(BitVector key) {
        return get(key) != null;
    }

    /**
     * get longest matching key in the trie
     * If no value exists for the  key, throw an IllegalArgumentException
     */
    public Value get(BitVector key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return get(root, key, 0, null);
    }

    private Value get(Node x, BitVector key, int d, Value bestSoFar) {

        if (x == null){ return bestSoFar;} //if there are no more nodes, return the the best match
        if (d == key.size()){
            return (Value)x.val; //if you get to the end of the IP Address, then that is obviously the correct port
        }
        if(x.val != null) {// if the current node stores a port, it is a better fit since it is longer
            bestSoFar = (Value) x.val;
        }

        //if there are still nodes to go, keep traversing
        int c =  Integer.parseInt(String.valueOf(key.toString().charAt(d)));
        return get(x.next[c], key, d + 1, bestSoFar);
    }

    /**
     * Insert Value value into the prefix Trie.
     * If a different value exists for the same key
     * throw an IllegalArgumentException
     */
    public void put(BitVector key, Value port) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (port == null){
            throw new IllegalArgumentException();
        }else {
            root = put(root, key, port, 0);
        }
    }

    private Node put(Node x, BitVector key, Value port, int d) {
        // finish this
        if (x == null) x = new Node();
        if (d == key.size()) {
            if(x.val != null && x.val != port ){
                throw new IllegalArgumentException();
            }

            x.val = port;
            return x;
        }
        int c = Integer.parseInt(String.valueOf(key.toString().charAt(d)));
        x.next[c] = put(x.next[c], key, port, d+1);
        return x;
    }


    /**
     * Delete the value for a key.
     * If no value exists for this key, throw IllegalArgumentException
     */
    public void delete(BitVector key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        root = delete(root, key, 0);
    }

    private Node delete(Node x, BitVector key, int d) {
        if (x == null) return null;
        if (d == key.size()) {
            x.val = null;
        } else {
            int c = Integer.parseInt(String.valueOf(key.toString().charAt(d)));
            x.next[c] = delete(x.next[c], key, d + 1);

        }

        if (x.val != null) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }

}

