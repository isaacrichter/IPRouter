import java.util.HashMap;
import java.util.Map;

/**
 * Bounded cache that maintains only the most recently accessed IP Addresses
 * and their routes.  Only the least recently accessed route will be purged from the
 * cache when the cache exceeds capacity.
 */
public class RouteCache
{
    private final int capacity;
    private int nodeCount;


    /**
     * Nodes for a doubly-linked queue,
     * used to keep most-recently-used data
     */
    private class Node {
        private Node prev, next;
        private final IPAddress elem;
        private final int route;

        Node(IPAddress elem, int route) {

            prev = next = null;
            this.elem = elem;
            this.route = route;
        }

        /**
         * helper functions for updating queue
         */

        void setPrevDotNext(){
            this.prev.next =
                    this.next;              // remove from...
        }

        void setNextDotPrev(){
            System.out.println(this.next);
            this.next.prev = this.prev;    // middle of list

        }
        void setPrevHead(){
            head = head.prev;

        }
    }


    private Node head = null;
    private Node tail = null;
    private Map<IPAddress, Node> nodeMap; // the cache itself

    /**
     * Constructor for objects of class RouteCache
     */
    public RouteCache(int cacheCapacity)
    {
        this.capacity = cacheCapacity;
        nodeMap = new HashMap<>(cacheCapacity);
    }

    /**
     * Lookup the output port for an IP Address in the cache, adding it if not already there
     *
     * @param  addr   a possibly cached IP Address
     * @return     the cached route for this address, or null if not found
     */
    public Integer lookupRoute(IPAddress addr)
    {
        // your code goes here
        Node node = nodeMap.get(addr);
        if(node == null) {return  null;}
        else {return node.route;}
    }

    /**
     * Update the cache each time an element's route is looked up.
     * Make sure the element and its route is in the Map:
     * Enqueue the element at the tail of the queue if it is not already in the queue.
     * Otherwise, move it from its current position to the tail of the queue.
     * If the queue was already at capacity, remove and return the element at the head of the queue.
     *
     * @param  elem  an element to be added to the queue, which may already be in the queue
     * @return       the expired least recently used element, if any, or null
     */
    public IPAddress updateCache(IPAddress elem, int route)
    {
        Node preexistingNode = nodeMap.get(elem);
        if (preexistingNode != null){// fix potential NPE's!!!
            if(tail != preexistingNode) {
                //System.out.println(preexistingNode.prev);
                preexistingNode.setPrevDotNext();
                if(preexistingNode.next == head){
                    head = preexistingNode;
                }

                if(head != preexistingNode) {
                    preexistingNode.setNextDotPrev();
                }
                preexistingNode.prev = null; // place in
                preexistingNode.next = tail; // front of the list
                tail = preexistingNode; //it is the new tail i.e. the most recently used
            }
            return  null; // as per javadoc
        }else{
            nodeCount++;
            Node newNode = new Node(elem, route);

            nodeMap.put(elem, newNode);

            newNode.next = tail; // new node default prev is null
            if(newNode.next != null){
            newNode.next.prev = newNode;
            }else{head = newNode;}
            tail = newNode;



            if (nodeCount > capacity){
                Node nodeToRemove = head;
                head.setPrevHead();
                head.next = null;
                nodeCount--;
                return nodeToRemove.elem;
            }else{
                return null;
            }

        }
    }


    /**
     * For testing and debugging, returns the contents of the LRU queue in most-recent-first order.
     */
    String[] dumpQueue()
    {
        String[] CIDRs = new String[nodeCount];
        int index = 0;
        Node placeHolder = tail;
        while (tail != null){
            CIDRs[index] = tail.elem.toCIDR();
            tail = tail.next;
            index++;
        }
        tail = placeHolder; // to keep its previous state the same
        return CIDRs;
    }


}
