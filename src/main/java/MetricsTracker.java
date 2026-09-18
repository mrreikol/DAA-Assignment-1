public class MetricsTracker {
    private long startTime;
    private long endTime;
    private int maxDepth;
    private long comparisons;

    public void start() {
        reset();
        this.startTime = System.nanoTime();
    }

    public void stop() {
        this.endTime = System.nanoTime();
    }

    public void updateDepth(int depth) {
        if (depth > this.maxDepth) {
            this.maxDepth = depth;
        }
    }

    public void incrementComparisons() {
        this.comparisons++;
    }

    public void addComparisons(long count) {
        this.comparisons += count;
    }

    public void reset() {
        this.startTime = 0;
        this.endTime = 0;
        this.maxDepth = 0;
        this.comparisons = 0;
    }

    public long getExecutionTime() {
        return this.endTime - this.startTime;
    }

    public int getMaxDepth() {
        return this.maxDepth;
    }

    public long getComparisons() {
        return this.comparisons;
    }
}