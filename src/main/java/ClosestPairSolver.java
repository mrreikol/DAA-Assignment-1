import java.util.Arrays;
import java.util.Comparator;

public class ClosestPairSolver {

    public static double solve(Point[] points, MetricsTracker metrics) {
        if (points == null || points.length < 2) {
            return Double.POSITIVE_INFINITY;
        }

        Point[] sortedX = points.clone();
        Arrays.sort(sortedX, Comparator.comparingDouble(Point::x));

        metrics.start();
        double minDist = closestPair(sortedX, 0, sortedX.length - 1, 1, metrics);
        metrics.stop();

        return minDist;
    }

    private static double closestPair(Point[] px, int low, int high, int depth, MetricsTracker metrics) {
        metrics.updateDepth(depth);
        int n = high - low + 1;

        if (n <= 3) {
            return bruteForce(px, low, high, metrics);
        }

        int mid = low + (high - low) / 2;
        Point midPoint = px[mid];

        double deltaLeft = closestPair(px, low, mid, depth + 1, metrics);
        double deltaRight = closestPair(px, mid + 1, high, depth + 1, metrics);
        double delta = Math.min(deltaLeft, deltaRight);

        Point[] strip = new Point[n];
        int stripSize = 0;
        for (int i = low; i <= high; i++) {
            if (Math.abs(px[i].x() - midPoint.x()) < delta) {
                strip[stripSize++] = px[i];
            }
        }

        Arrays.sort(strip, 0, stripSize, Comparator.comparingDouble(Point::y));
        return Math.min(delta, stripClosest(strip, stripSize, delta, metrics));
    }

    private static double stripClosest(Point[] strip, int size, double delta, MetricsTracker metrics) {
        double min = delta;
        for (int i = 0; i < size; ++i) {
            for (int j = i + 1; j < size && (strip[j].y() - strip[i].y()) < min; ++j) {
                metrics.incrementComparisons();
                double dist = strip[i].distanceTo(strip[j]);
                if (dist < min) {
                    min = dist;
                }
            }
        }
        return min;
    }

    public static double bruteForce(Point[] points, int low, int high, MetricsTracker metrics) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = low; i <= high; ++i) {
            for (int j = i + 1; j <= high; ++j) {
                metrics.incrementComparisons();
                double dist = points[i].distanceTo(points[j]);
                if (dist < min) {
                    min = dist;
                }
            }
        }
        return min;
    }
}