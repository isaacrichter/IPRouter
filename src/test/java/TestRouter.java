import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

/**
 * The test class TestRouter.
 * PutAndGet4PrefixOverlapTest is the most important one,
 * as it comprehensively deals with prefixes
 * as opposed to only dealing with exact matches
 * However, it is very comprehensive and does the job
*/

 public class TestRouter
{

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


    @Before
    public void setUp(){
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

        IP1s = "+24.0.0.0/8";
        IP2s = "+243.18.3.0/24";
        IP3s = "+87.5.0.0/16";
        IP3bs = "+87.5.54.7/32";
        IP4s = "+156.2.54.7/32";

    }

    /**
     * empty constructor
     */
    public TestRouter()
    {
    }

    @Test
    public void simplePutAndGet1IPATest(){
         IPRouter router = new IPRouter(10, 10);
        router.addRule(IP1s, port1);
        router.addRule(IP2s, port2);
        router.addRule(IP3s, port3);
        router.addRule(IP4s, port4);

        assertEquals((Integer) router.getRoute(IP1), port1);
        assertEquals((Integer) router.getRoute(IP2),port2);
        assertEquals((Integer) router.getRoute(IP3),port3);
        assertEquals((Integer) router.getRoute(IP4),port4);

    }

    @Test
    public void PutAndGetSameFirstOctetTest(){
        IPRouter router = new IPRouter(10, 10);
        router.addRule(IP3s, port3);
        router.addRule(IP3bs, port5);

        assertEquals((Integer)router.getRoute(IP3),port3);
        assertEquals((Integer)router.getRoute(IP3b),port5);
        assertEquals((Integer)router.getRoute(IP3),port3);
    }

    @Test
    public void PutAndGet4PrefixOverlapTest(){
        String IP3s ="+87.5.0.0/16";
        String IP3bs = "+87.5.28.0/22";
        String IP3cs = "+87.5.250.0/24";
        String IP3ds ="+87.5.250.7/31";

        IPAddress IP3a =new IPAddress(IP3s);
        IP3b = new IPAddress(IP3bs);
        IP3c = new IPAddress(IP3cs);
        IP3d =new IPAddress(IP3ds);
        IPAddress IPbeforeA= new IPAddress("+87.0.0.0/8"); // should map to -1
        IPAddress IPAtoB= new IPAddress("+87.5.23.0/24");
        IPAddress IPBtoC= new IPAddress("+87.5.29.0/24");
        IPAddress IPCtoD= new IPAddress("+87.5.250.2/32");
        IPAddress IPofD= new IPAddress("+87.5.250.6/32");

        IPAddress noMatch1= new IPAddress("+87.165.250.6/14");
        IPAddress noMatch2= new IPAddress("+96.0.0.0/3");



        IPRouter router = new IPRouter(10, 10);
        router.addRule(IP3s, port1);
        router.addRule(IP3bs, port2);
        router.addRule(IP3cs, port3);
        router.addRule(IP3ds, port4);

        //test routes that lie in all five possible "sections"

        assertEquals(router.getRoute(IPbeforeA),-1);
        assertEquals((Integer)router.getRoute(IPAtoB),port1);
        assertEquals((Integer)router.getRoute(IPBtoC),port2);
        assertEquals((Integer)router.getRoute(IPCtoD),port3);
        assertEquals((Integer)router.getRoute(IPofD),port4);
        assertEquals(router.getRoute(noMatch1),-1);
        assertEquals(router.getRoute(noMatch2),-1);


    }

    @Test
    public void simpleDeleteTest(){
        IPRouter router = new IPRouter(10, 10);
        router.addRule(IP3s, port3);
        router.deleteRule(IP3s);
        assertEquals(-1,router.getRoute(IP3));
    }

    @Test
    public void overlapDeleteTest(){
        IPRouter router = new IPRouter(10, 10);
        router.addRule(IP3s, port3);
        router.addRule(IP3bs, port5);

        router.deleteRule(IP3s);
        assertEquals(-1,router.getRoute(IP3));
        assertEquals((Integer) router.getRoute(IP3b),port5);

    }

    @Test
    public void directReplacePortIAExceptionTest() {
        IPRouter router = new IPRouter(10, 10);

        try{router.addRule(IP1s, port1);
            router.addRule(IP1s, port2);
            fail(); // the test fails because there was no exception thrown
            }catch (IllegalArgumentException e){
            // the test passes because there was en exception thrown
        }
    }
}