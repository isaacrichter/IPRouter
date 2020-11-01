import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * IPRouter simulates the decision process for an IP router dispatching packets according a
 * prefix trie of routing rules.
 */
public class IPRouter
{

    final int nPorts;
    final int cacheSize;
    BitVectorTrie<Integer> trie= new BitVectorTrie<Integer>();
    RouteCache cache;

    /** Router constructor
     * @param nPorts    the number of output ports, numbered 0 ... nPorts-1.  Pseudo-port -1 is
     *                  always used for errors.
     * @param cacheSize the number of IP Addresses to be kept in a cache of the most recently routed
     *                  UNIQUE IP Addresses
     */
    public IPRouter (int nPorts, int cacheSize)
    {
        this.nPorts = nPorts;
        this.cacheSize = cacheSize;
        this.cache = new RouteCache(cacheSize);
    }

    /**
     * Add a routing rule to the router. Each rule associates an IP Address prefix with an output port.
     * In case rules overlap, longest prefix wins.  If two rules specify exactly the same prefix, then
     * the second rule triggers an IllegalArgumentException.  The port must be in the permitted range
     * for this router, or an IllegalArgumentException will be triggered as well.
     *
     * @param  prefix    an IP Address prefix in CIDR (dotted decimal) notation
     * @param  port
     */
    public void addRule(String prefix, int port)
    {
        if (port > nPorts-1) throw  new IllegalArgumentException();
        if (port < 0) throw  new IllegalArgumentException();
        trie.put(new IPAddress(prefix), (Integer)port);
    }

    /**
     * Delete routing rule from the router. Removes IP Address prefix association with output port.
     */

    public void deleteRule(String prefix)
    {
        trie.delete(new IPAddress(prefix));
    }

    /**
     * Simulate routing a packet to its output port based on a binary IP Address.
     * If no rules apply to an address, route it to port -1 and log an error to System.err
     *
     * @param  address    an IP Address object
     * @return  number of output port
     */
    public int getRoute(IPAddress address)
    {
        // finish this (~6 lines)
        Integer route = cache.lookupRoute(address);
        if(route != null){
            cache.updateCache(address, route);
            return route;
        }else {

            Integer port = trie.get(address);
            if (port == null){
                cache.updateCache(address, -1);
                System.err.println();
                return -1;
            }else{
                cache.updateCache(address, port);
                return port;
            }
        }
    }

    /**
     * Is IP Address currently cached?
     *
     * @param  address    an IP Address in dotted decimal notation
     * @return  number of output port
     */
    boolean isCached(IPAddress address) {
        Integer cachedRoute = cache.lookupRoute(address);
        if (cachedRoute != null) {
            return true;
        } else {
            return false;  // just so it compiles
        }
    }
    /**
     * For testing and debugging, return the contents of the LRU queue in most-recent-first order,
     * as an array of IP Addresses.  Return a zero length array if the cache is empty
     *
     */
    String[] dumpCache()
    {
        return cache.dumpQueue();

    }

    /**
     * For testing and debugging, load a routing table from a text file
     *
     */
    public void loadRoutes(String filename) throws FileNotFoundException
    {
        Scanner sc = new Scanner(new File(filename));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("+")) {
                String[] pieces = line.substring(1).split(",");
                int port = Integer.parseInt(pieces[1]);
                this.addRule(pieces[0].trim(), port);
            }
        }
    }
}
