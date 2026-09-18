import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeterministicSelectorTest {

    @Test
    public void testAtLeast100RandomSelections() {
        Random rand = new Random(42);
        int testCases = 100;

        for (int i = 0; i < testCases; i++) {
            int size = 50 + rand.nextInt(200);
            int[] arr = new int[size];
            for (int j = 0; j < size; j++) {
                arr[j] = rand.nextInt(1000);
            }

            int k = rand.nextInt(size);

            int[] expectedArr = arr.clone();
            Arrays.sort(expectedArr);
            int expectedValue = expectedArr[k];

            MetricsTracker metrics = new MetricsTracker();
            int[] testArr = arr.clone();
            int actualValue = DeterministicSelector.select(testArr, k, metrics);

            assertEquals(expectedValue, actualValue, "Failed on test case " + i + " for k=" + k);
            assertTrue(metrics.getMaxDepth() > 0, "Recursion depth should be tracked");
        }
    }

    @Test
    public void testEdgeCases() {
        MetricsTracker metrics = new MetricsTracker();

        int[] duplicates = {5, 5, 5, 5, 5};
        assertEquals(5, DeterministicSelector.select(duplicates.clone(), 2, metrics));

        int[] reversed = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        assertEquals(4, DeterministicSelector.select(reversed.clone(), 3, metrics));
    }
}