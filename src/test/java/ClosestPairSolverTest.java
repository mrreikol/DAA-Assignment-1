import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClosestPairSolverTest {

    private static final double DELTA = 1e-9;

    @Test
    public void testSmallDatasetsAgainstBruteForce() {
        int[] testSizes = {10, 50, 100, 500, 1000, 2000};
        Random rand = new Random(42);

        for (int n : testSizes) {
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                points[i] = new Point(rand.nextDouble() * 1000, rand.nextDouble() * 1000);
            }

            MetricsTracker bfMetrics = new MetricsTracker();
            double expected = ClosestPairSolver.bruteForce(points, 0, n - 1, bfMetrics);

            MetricsTracker dqMetrics = new MetricsTracker();
            double actual = ClosestPairSolver.solve(points, dqMetrics);

            assertEquals(expected, actual, DELTA, "Mismatch on size " + n);
            assertTrue(dqMetrics.getMaxDepth() > 0, "D&C recursion depth should be recorded");

            if (n > 50) {
                assertTrue(dqMetrics.getComparisons() < bfMetrics.getComparisons());
            }
        }
    }

    @Test
    public void testEdgeCases() {
        MetricsTracker metrics = new MetricsTracker();

        Point[] single = {new Point(1.0, 1.0)};
        assertEquals(Double.POSITIVE_INFINITY, ClosestPairSolver.solve(single, metrics));

        Point[] verticalLine = {
                new Point(5.0, 10.0),
                new Point(5.0, 1.0),
                new Point(5.0, 5.0)
        };
        assertEquals(4.0, ClosestPairSolver.solve(verticalLine, metrics), DELTA);

        Point[] duplicates = {
                new Point(2.0, 3.0),
                new Point(10.0, 10.0),
                new Point(2.0, 3.0)
        };
        assertEquals(0.0, ClosestPairSolver.solve(duplicates, metrics), DELTA);
    }
}