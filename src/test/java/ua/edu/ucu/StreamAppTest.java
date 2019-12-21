package ua.edu.ucu;

import ua.edu.ucu.stream.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author andrii
 */
public class StreamAppTest {

    private IntStream intStream;

    @Before
    public void init() {
        int[] intArr = {-1, 0, 1, 2, 3};
        intStream = AsIntStream.of(intArr);
    }

    @Test
    public void testStreamOperations() {
        System.out.println("streamOperations");
        int expResult = 42;
        int result = StreamApp.streamOperations(intStream);

        assertEquals(expResult, result);
    }

    @Test
    public void testStreamToArray() {
        System.out.println("streamToArray");
        int[] expResult = {-1, 0, 1, 2, 3};
        int[] result = StreamApp.streamToArray(intStream);

        assertArrayEquals(expResult, result);
    }

    @Test
    public void testStreamForEach() {
        System.out.println("streamForEach");
        String expResult = "-10123";
        String result = StreamApp.streamForEach(intStream);

        assertEquals(expResult, result);
    }

    @Test
    public void testOriginalInstance() {
        int[] res = intStream.map(x -> x+1).toArray();
        int[] exp1 = {0,1,2,3,4};
        int[] exp2 = {-1,0,1,2,3};

        assertArrayEquals(res, exp1);
        assertArrayEquals(intStream.toArray(), exp2);
    }

    @Test
    public void testAverage() {
        double res = intStream.average();
        int exp = 1;
        assertEquals(exp, res, 0.001);

        double res2 = intStream.filter(x -> x > 1).average();
        double exp2 = 2.5;
        assertEquals(exp2, res2, 0.001);
    }

    @Test
    public void testMin() {
        int minimum1 = intStream.min();
        assertEquals(-1, minimum1);

        int minimum2 = intStream.map(x -> x * 20).filter(x -> x > 0).min();
        assertEquals(20, minimum2);
    }

    @Test
    public void testMax() {
        int maximum1 = intStream.max();
        assertEquals(3, maximum1);

        int maximum2 = intStream.map(x -> x * 20).filter(x -> x > 0).max();
        assertEquals(60, maximum2);
    }

    @Test
    public void testSum() {
        int sum1 = intStream.sum();
        assertEquals(5, sum1);

        int sum2 = intStream.map(x -> (x * 10) + 2).sum();
        assertEquals(60, sum2);

        int sum3 = intStream.map(x -> 40+x).filter(x -> x !=40).sum();
        assertEquals(165, sum3);
    }

    @Test
    public void testCount() {
        long c1 = intStream.map(x -> 40+x).filter(x -> x > 40).count();
        assertEquals(3, c1);

        long c2 = intStream.flatMap(x -> AsIntStream.of(x - 1, x, x + 1, x*x)).count();
        assertEquals(20, c2);

        long c3 = intStream.filter(x -> x > 100000).count();
        assertEquals(0, c3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testErrorMin() {
        int errMin = AsIntStream.of().min();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testErrorMax() {
        int errMax = AsIntStream.of().max();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testErrorAverage() {
        double errAvg = AsIntStream.of().average();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testErrorSum() {
        int errSum = AsIntStream.of().sum();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptiedStream() {
        int emptiedErr = intStream.filter(x -> x == 17).max();
    }

    @Test
    public void testOperationOverlap() {
        int[] stream1 = intStream.map(x -> x*100).filter(x -> x != 100).toArray();
        assertArrayEquals(new int[] {-100, 0, 200, 300}, stream1);

        int[] stream2 = intStream.filter(x -> x < 2).toArray();
        assertArrayEquals(new int[] {-1, 0, 1}, stream2);
    }
}
