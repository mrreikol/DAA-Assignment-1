public class DeterministicSelector {

    public static int select(int[] arr, int k, MetricsTracker metrics) {
        if (arr == null || arr.length == 0 || k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("Invalid array or k");
        }
        metrics.start();
        int result = select(arr, 0, arr.length - 1, k, 1, metrics);
        metrics.stop();
        return result;
    }

    private static int select(int[] arr, int low, int high, int k, int depth, MetricsTracker metrics) {
        metrics.updateDepth(depth);
        if (low == high) return arr[low];

        int pivot = medianOfMedians(arr, low, high, depth, metrics);
        int[] p = partition3Way(arr, low, high, pivot, metrics);

        if (k >= p[0] && k <= p[1]) {
            return arr[k];
        } else if (k < p[0]) {
            return select(arr, low, p[0] - 1, k, depth + 1, metrics);
        } else {
            return select(arr, p[1] + 1, high, k, depth + 1, metrics);
        }
    }

    private static int medianOfMedians(int[] arr, int low, int high, int depth, MetricsTracker metrics) {
        int n = high - low + 1;
        if (n <= 5) return findMedian(arr, low, high, metrics);

        int mediansCount = 0;
        for (int i = low; i <= high; i += 5) {
            int subRight = Math.min(i + 4, high);
            findMedian(arr, i, subRight, metrics);
            int medianIndex = i + (subRight - i) / 2;
            swap(arr, low + mediansCount, medianIndex);
            mediansCount++;
        }

        int mediansHigh = low + mediansCount - 1;
        int midTarget = low + mediansCount / 2;
        return select(arr, low, mediansHigh, midTarget, depth + 1, metrics);
    }

    private static int findMedian(int[] arr, int low, int high, MetricsTracker metrics) {
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
        return arr[low + (high - low) / 2];
    }

    private static int[] partition3Way(int[] arr, int low, int high, int pivot, MetricsTracker metrics) {
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