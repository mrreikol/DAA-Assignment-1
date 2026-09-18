import java.util.Random;

public class QuickSorter {
    private static final Random RAND = new Random();

    public static void sort(int[] arr, MetricsTracker metrics) {
        if (arr == null || arr.length <= 1) return;

        metrics.start();
        sort(arr, 0, arr.length - 1, 1, metrics);
        metrics.stop();
    }

    private static void sort(int[] arr, int low, int high, int currentDepth, MetricsTracker metrics) {
        while (low < high) {
            metrics.updateDepth(currentDepth);

            int pivotIndex = partition(arr, low, high, metrics);

            if (pivotIndex - low < high - pivotIndex) {
                sort(arr, low, pivotIndex - 1, currentDepth + 1, metrics);
                low = pivotIndex + 1;
            } else {
                sort(arr, pivotIndex + 1, high, currentDepth + 1, metrics);
                high = pivotIndex - 1;
            }
        }
    }

    private static int partition(int[] arr, int low, int high, MetricsTracker metrics) {
        int randomPivotIndex = low + RAND.nextInt(high - low + 1);
        swap(arr, randomPivotIndex, high);

        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            metrics.incrementComparisons();
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}