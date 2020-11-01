import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

/**
 * The test class TestRouter.
 */

public class CacheTest {

    String IP1s;
    String IP2s;
    String IP3s;
    String IP3bs;
    String IP4s;

    IPAddress IP1;
    IPAddress IP2;
    IPAddress IP3;
    IPAddress IP3b;
    IPAddress IP3c;
    IPAddress IP3d;
    IPAddress IP4;

    Integer port1;
    Integer port2;
    Integer port3;
    Integer port4;
    Integer port5;

    String IP1ss;
    String IP2ss;
    String IP3ss;
    String IP3bss;
    String IP4ss;


    @Before
    public void setUp() {

        IP1s = "24.0.0.0/8";
        IP2s = "243.18.3.0/24";
        IP3s = "87.5.0.0/16";
        IP3bs = "87.5.54.7/32";
        IP4s = "156.2.54.7/32";

        IP1ss = "24.0.0.0/8";
        IP2ss = "243.18.3.0/24";
        IP3ss = "87.5.0.0/16";
        IP3bss = "87.5.54.7";
        IP4ss = "156.2.54.7";

        IP1 = new IPAddress(IP1s);
        IP2 = new IPAddress(IP2s);
        IP3 = new IPAddress(IP3s);
        IP3b = new IPAddress(IP3bs);
        IP4 = new IPAddress(IP4s);

        port1 = 1;
        port2 = 2;
        port3 = 3;
        port4 = 4;
        port5 = 5;


    }

    //empty constructor
    public CacheTest() {
    }

    @Test
    public void emptyCacheWithRulesTest() {
        IPRouter router = new IPRouter(10, 10);
        router.addRule(IP1s, port1);
        router.addRule(IP2s, port2);
        router.addRule(IP3s, port3);
        router.addRule(IP4s, port4);


        String[] cacheExpected = new String[0];
        String[] cacheActual = router.dumpCache();
        assertArrayEquals(cacheActual, cacheExpected);

    }

    @Test
    public void simpleCacheTest() {
        IPRouter router = new IPRouter(10, 10);
        router.addRule(IP1s, port1);
        router.addRule(IP2s, port2);
        router.addRule(IP3s, port3);
        router.addRule(IP4s, port4);

        router.getRoute(IP1);
        router.getRoute(IP2);
        router.getRoute(IP3);
        router.getRoute(IP4);


        String[] cacheExpected = {IP4ss, IP3ss, IP2ss, IP1ss};
        String[] cacheActual = router.dumpCache();

    }


    @Test
    public void rearrangeCacheMiddleTest() {
        IPRouter router = new IPRouter(10, 10);
        router.addRule(IP1s, port1);
        router.addRule(IP2s, port2);
        router.addRule(IP3s, port3);
        router.addRule(IP4s, port4);

        router.getRoute(IP1);
        router.getRoute(IP2);
        router.getRoute(IP3);
        router.getRoute(IP4);
        router.getRoute(IP2);


        String[] cacheExpected = {IP2ss, IP4ss, IP3ss, IP1ss};
        String[] cacheActual = router.dumpCache();
        assertArrayEquals(cacheActual, cacheExpected);

    }


    @Test
    public void rearrangeCacheTailTest() {
        IPRouter router = new IPRouter(10, 10);
        router.addRule(IP1s, port1);
        router.addRule(IP2s, port2);
        router.addRule(IP3s, port3);
        router.addRule(IP4s, port4);

        router.getRoute(IP1);
        router.getRoute(IP2);
        router.getRoute(IP3);
        router.getRoute(IP4);
        router.getRoute(IP4);


        String[] cacheExpected = {IP4ss, IP3ss, IP2ss, IP1ss};
        String[] cacheActual = router.dumpCache();
        assertArrayEquals(cacheActual, cacheExpected);

    }

    @Test
    public void rearrangeCacheHeadTest() {
        IPRouter router = new IPRouter(10, 10);
        router.addRule(IP1s, port1);
        router.addRule(IP2s, port2);
        router.addRule(IP3s, port3);
        router.addRule(IP4s, port4);

        router.getRoute(IP1);
        router.getRoute(IP2);
        router.getRoute(IP3);
        router.getRoute(IP4);
        router.getRoute(IP1);


        String[] cacheExpected = {IP1ss, IP4ss, IP3ss, IP2ss};
        String[] cacheActual = router.dumpCache();
        assertArrayEquals(cacheActual, cacheExpected);

    }

    @Test
    public void cacheCapacityTest() {
        IPRouter router = new IPRouter(10, 3);
        router.addRule(IP1s, port1);
        router.addRule(IP2s, port2);
        router.addRule(IP3s, port3);
        router.addRule(IP4s, port4);

        router.getRoute(IP1);
        router.getRoute(IP2);
        router.getRoute(IP3);
        router.getRoute(IP4);


        String[] cacheExpected = {IP4ss, IP3ss, IP2ss};
        String[] cacheActual = router.dumpCache();
        assertArrayEquals(cacheActual, cacheExpected);

    }

    @Test
    public void cacheOverCapacityTwiceTest() {
        IPRouter router = new IPRouter(10, 2);
        router.addRule(IP1s, port1);
        router.addRule(IP2s, port2);
        router.addRule(IP3s, port3);
        router.addRule(IP4s, port4);

        router.getRoute(IP1);
        router.getRoute(IP2);
        router.getRoute(IP3);
        router.getRoute(IP4);


        String[] cacheExpected = {IP4ss, IP3ss};
        String[] cacheActual = router.dumpCache();
        assertArrayEquals(cacheActual, cacheExpected);

    }

}