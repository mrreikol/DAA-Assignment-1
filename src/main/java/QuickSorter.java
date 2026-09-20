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

            int[] p = partition3Way(arr, low, high, metrics);

            int leftSize = p[0] - low;
            int rightSize = high - p[1];

            if (leftSize < rightSize) {
                sort(arr, low, p[0] - 1, currentDepth + 1, metrics);
                low = p[1] + 1;
            } else {
                sort(arr, p[1] + 1, high, currentDepth + 1, metrics);
                high = p[0] - 1;
            }
        }
    }

    private static int[] partition3Way(int[] arr, int low, int high, MetricsTracker metrics) {
        int randomPivotIndex = low + RAND.nextInt(high - low + 1);
        int pivot = arr[randomPivotIndex];

        int lt = low;
        int gt = high;
        int i = low;

        while (i <= gt) {
            metrics.incrementComparisons();
            if (arr[i] < pivot) {
                swap(arr, lt++, i++);
            } else if (arr[i] > pivot) {
                swap(arr, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}