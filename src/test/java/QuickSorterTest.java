import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuickSorterTest {

    private void assertSorted(int[] arr) {
        int[] expected = arr.clone();
        Arrays.sort(expected);

        MetricsTracker metrics = new MetricsTracker();
        QuickSorter.sort(arr, metrics);

        assertArrayEquals(expected, arr);
        assertTrue(metrics.getMaxDepth() > 0 || arr.length <= 1);
    }

    @Test
    public void testRandomArray() {
        Random rand = new Random(42);
        int[] arr = new int[100];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(1000);
        }
        assertSorted(arr);
    }

    @Test
    public void testSortedArray() {
        int[] arr = new int[50];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        assertSorted(arr);
    }

    @Test
    public void testReverseSortedArray() {
        int[] arr = new int[50];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 50 - i;
        }
        assertSorted(arr);
    }

    @Test
    public void testDuplicateHeavyArray() {
        int[] arr = new int[50];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (i % 2 == 0) ? 5 : 10;
        }
        assertSorted(arr);
    }

    @Test
    public void testEmptyAndSingleElement() {
        assertSorted(new int[]{});
        assertSorted(new int[]{42});
    }
}