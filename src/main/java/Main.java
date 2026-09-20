public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Divide-and-Conquer Experiments...");
        try {
            Experiment.run();
            System.out.println("Experiments finished. Results saved to results/results.csv");
        } catch (Exception e) {
            System.err.println("Experiment failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}