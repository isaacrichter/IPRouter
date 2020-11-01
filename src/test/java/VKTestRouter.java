import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

/**
 * The test class TestRouter.

 */
public class VKTestRouter
{

    private IPRouter router;

    public VKTestRouter()
    {
    }


    @Before
    public void setUp()
    {
        this.router = new IPRouter(8,4);

        try {
            router.loadRoutes("src/test/java/routes1.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        IPAddress address = new IPAddress("85.85.0.1");
        System.out.println("---------------");
        System.out.println(this.router.trie);
        System.out.println(address);
        int res = this.router.trie.get(address);
        System.out.println(res);
        assertEquals(1, res);
    }

    /**
     * Handle an unroutable address
     */
    @Test
    public void testBadRoute()
    {
        IPAddress address = new IPAddress("73.73.0.1");
        assertEquals(-1, this.router.getRoute(address));
    }

    /**
     * Handle an address that only matches one prefix
     */
    @Test
    public void port2Test()
    {
        IPAddress address = new IPAddress("85.2.0.1");
        int res = this.router.getRoute(address);
        assertEquals(2, res);
    }

    /**
     * Handle an address that only matches multiple prefixes. Only the longest one counts
     */
    @Test
    public void port1Test()
    {
        IPAddress address = new IPAddress("85.85.85.85");
        int res = this.router.getRoute(address);
        assertEquals(1, res);
    }

    @After
    public void tearDown()
    {
    }
}

