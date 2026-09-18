import java.util.Arrays;

public class MergeSorter {
    private static final int CUTOFF = 15;

    public static void sort(int[] arr, MetricsTracker metrics) {
        if (arr == null || arr.length <= 1) return;

        int[] aux = Arrays.copyOf(arr, arr.length);

        metrics.start();
        sort(aux, arr, 0, arr.length - 1, 1, metrics);
        metrics.stop();
    }

    private static void sort(int[] src, int[] dest, int low, int high, int depth, MetricsTracker metrics) {
        metrics.updateDepth(depth);

        if (high - low <= CUTOFF) {
            insertionSort(dest, low, high, metrics);
            return;
        }

        int mid = low + (high - low) / 2;

        sort(dest, src, low, mid, depth + 1, metrics);
        sort(dest, src, mid + 1, high, depth + 1, metrics);

        merge(src, dest, low, mid, high, metrics);
    }

    private static void merge(int[] src, int[] dest, int low, int mid, int high, MetricsTracker metrics) {
        int i = low;
        int j = mid + 1;

        for (int k = low; k <= high; k++) {
            if (i > mid) {
                dest[k] = src[j++];
            } else if (j > high) {
                dest[k] = src[i++];
            } else {
                metrics.incrementComparisons();
                if (src[j] < src[i]) {
                    dest[k] = src[j++];
                } else {
                    dest[k] = src[i++];
                }
            }
        }
    }

    private static void insertionSort(int[] arr, int low, int high, MetricsTracker metrics) {
        for (int i = low + 1; i <= high; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= low) {
                metrics.incrementComparisons();
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j--;
                } else {
                    break;
                }
            }
            arr[j + 1] = key;
        }
    }
}