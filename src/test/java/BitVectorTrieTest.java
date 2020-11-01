import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

/**
 * Comprehensive Testing of BitVectorTrie
 */

public class BitVectorTrieTest {
    IPAddress IP1;
    IPAddress IP2;
    IPAddress IP3;
    IPAddress IP3b;
    IPAddress IP4;

    Integer port1;
    Integer port2;
    Integer port3;
    Integer port4;
    Integer port5;

    BitVectorTrie<Integer> trie;




    //empty constructor
    public BitVectorTrieTest()
    {
    }


    @Before
    public void setUp(){
        trie = new BitVectorTrie<>();

         IP1 = new IPAddress("+24.0.0.0/8");
         IP2 =new IPAddress("+243.18.3.0/24");
         IP3 =new IPAddress("+87.5.0.0/16");
         IP3b =new IPAddress("+87.5.54.7/32");
         IP4 =new IPAddress("+156.2.54.7/32");

         port1 = 1;
         port2 = 2;
         port3 = 3;
         port4 = 4;
         port5 = 5;

    }

    /**
     * Simple put and get IPs
     */
    @Test
    public void simplePutAndGet1IPATest(){
        trie.put(IP1, port1);
        trie.put(IP2, port2);
        trie.put(IP3, port3);
        trie.put(IP4, port4);

        assertEquals(trie.get(IP1),port1);
        assertEquals(trie.get(IP2),port2);
        assertEquals(trie.get(IP3),port3);
        assertEquals(trie.get(IP4),port4);

    }

    /**
     * Put and get IPs that have same first octet
     */
    @Test
    public void PutAndGetSameFirstOctetTest(){
        trie.put(IP3, port3);
        trie.put(IP3b, port5);

        assertEquals(trie.get(IP3),port3);
        assertEquals(trie.get(IP3b),port5);
        assertEquals(trie.get(IP3),port3);
    }

    /**
     * Put and delete an IP
     */
    @Test
    public void simpleDeleteTest(){
        trie.put(IP3, port3);
        trie.delete(IP3);
        assertNull(trie.get(IP3));
    }

    /**
     * Put and delete IP that has another with same first octet (use put null), and then get the other IP
     */
    @Test
    public void overlapDeleteTest(){
        trie.put(IP3, port3);
        trie.put(IP3b, port5);

        trie.delete(IP3);
        assertNull(trie.get(IP3));
        assertEquals(trie.get(IP3b),port5);

    }

    /**
     * Put two identical IPs and test to see if Illegal ArgumentException is thrown
     */
    @Test
    public void directReplacePortIAExceptionTest() {
        try{trie.put(IP1, port1);
        trie.put(IP1, port2);
        fail(); // the test fails because there was no exception thrown
       }catch (IllegalArgumentException e){
            // the test passes because there was en exception thrown
        }
    }

    /**
     * Put some Ips, test the isRoutable method
     */
    @Test
    public void isRoutableTest() {
        trie.put(IP1, port1);
        trie.put(IP3b, port5);
        assertTrue(trie.isRoutable(IP1));
        assertFalse(trie.isRoutable(IP2));
        assertFalse(trie.isRoutable(IP3));
        assertTrue(trie.isRoutable(IP3b));
        assertFalse(trie.isRoutable(IP4));
    }

    /**
     * IPs that fall under prefix but are NOT the exact same
     */
    @Test
    public void PutAndGetMatchingPrefix(){
        IP3 =new IPAddress("+87.5.0.0/16");
        IPAddress IPa =new IPAddress("+87.5.54.7/32");

        trie.put(IP3, port3);

        assertEquals(trie.get(IPa),port3);
    }
}
