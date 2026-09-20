import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

public class Experiment {
    private static final int[] SIZES = {1000, 10000, 100000};
    private static final String[] TYPES = {"Random", "Sorted", "Reverse-sorted", "Duplicate-heavy"};
    private static final Random RAND = new Random(42);

    public static void run() throws Exception {
        warmUp();

        File resultsDir = new File("results");
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new File("results/results.csv"))) {
            writer.println("Algorithm,Size,InputType,Time_ns,MaxDepth,Comparisons");

            for (int size : SIZES) {
                for (String type : TYPES) {
                    runSortingExperiments(writer, size, type);
                    runSelectExperiments(writer, size, type);
                }
                runClosestPairExperiments(writer, size);
            }
        }
    }

    private static void warmUp() {
        System.out.println("JVM Warm-up starting...");
        MetricsTracker dummyMetrics = new MetricsTracker();
        for (int i = 0; i < 50; i++) {
            int[] arr = generateArray(1000, "Random");
            MergeSorter.sort(arr.clone(), dummyMetrics);
            QuickSorter.sort(arr.clone(), dummyMetrics);
            DeterministicSelector.select(arr.clone(), 500, dummyMetrics);

            Point[] points = generatePoints(1000);
            ClosestPairSolver.solve(points, dummyMetrics);
        }
        System.out.println("JVM Warm-up completed.");
    }

    private static void runSortingExperiments(PrintWriter writer, int size, String type) {
        int[] original = generateArray(size, type);
        MetricsTracker metrics = new MetricsTracker();

        int[] arr1 = original.clone();
        MergeSorter.sort(arr1, metrics);
        writer.printf("MergeSort,%d,%s,%d,%d,%d\n", size, type, metrics.getExecutionTime(), metrics.getMaxDepth(), metrics.getComparisons());

        int[] arr2 = original.clone();
        metrics.reset();
        QuickSorter.sort(arr2, metrics);
        writer.printf("QuickSort,%d,%s,%d,%d,%d\n", size, type, metrics.getExecutionTime(), metrics.getMaxDepth(), metrics.getComparisons());
    }

    private static void runSelectExperiments(PrintWriter writer, int size, String type) {
        int[] original = generateArray(size, type);
        MetricsTracker metrics = new MetricsTracker();
        int k = size / 2;

        DeterministicSelector.select(original, k, metrics);
        writer.printf("DeterministicSelect,%d,%s,%d,%d,%d\n", size, type, metrics.getExecutionTime(), metrics.getMaxDepth(), metrics.getComparisons());
    }

    private static void runClosestPairExperiments(PrintWriter writer, int size) {
        Point[] points = generatePoints(size);
        MetricsTracker metrics = new MetricsTracker();

        ClosestPairSolver.solve(points, metrics);
        writer.printf("ClosestPair,%d,Random Points,%d,%d,%d\n", size, metrics.getExecutionTime(), metrics.getMaxDepth(), metrics.getComparisons());
    }

    private static int[] generateArray(int size, String type) {
        int[] arr = new int[size];
        switch (type) {
            case "Random":
                for (int i = 0; i < size; i++) arr[i] = RAND.nextInt(size);
                break;
            case "Sorted":
                for (int i = 0; i < size; i++) arr[i] = i;
                break;
            case "Reverse-sorted":
                for (int i = 0; i < size; i++) arr[i] = size - i;
                break;
            case "Duplicate-heavy":
                for (int i = 0; i < size; i++) arr[i] = (i % 3 == 0) ? 42 : 7;
                break;
        }
        return arr;
    }

    private static Point[] generatePoints(int size) {
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            points[i] = new Point(RAND.nextDouble() * size, RAND.nextDouble() * size);
        }
        return points;
    }
}